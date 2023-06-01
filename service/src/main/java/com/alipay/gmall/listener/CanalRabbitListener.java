/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.gmall.common.client.RedisClient;
import com.alipay.gmall.common.constant.RedisConstants;
import com.alipay.gmall.common.enums.CanalEventTypeEnum;
import com.alipay.gmall.common.enums.ResultCodeEnum;
import com.alipay.gmall.common.exception.ProductBizException;
import com.alipay.gmall.config.RabbitMqConfig;
import com.alipay.gmall.dal.domain.ProductBaseInfo;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author ruitu.xr
 * @version CanalRabbitListener.java, v 0.1 2023年05月25日 19:48 ruitu.xr Exp $
 */
@Component
public class CanalRabbitListener {

    public static final Logger LOGGER = LoggerFactory.getLogger(CanalRabbitListener.class);

    @Resource
    private RedisClient redisClient;

    private static final String CANAL_LISTEN_TABLE = "table";
    private static final String CANAL_LISTEN_DATA = "data";
    private static final String CANAL_LISTEN_TYPE = "type";
    private static final String TABLE_NAME = "product_base_info";

    @RabbitListener(queues = {RabbitMqConfig.CANAL_QUEUE})
    public void receiveCanalMessage(Message message, Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String msg = new String(message.getBody());
            LOGGER.info("收到来自canal监听到的消息：{}", msg);
            JSONObject jsonObject = JSON.parseObject(msg);
            String table = jsonObject.getString(CANAL_LISTEN_TABLE);
            if (!TABLE_NAME.equalsIgnoreCase(table)) {
                LOGGER.warn("该数据不是来自product_base_info表，无需处理。该数据所在表：{}", table);
            } else {
                throw new ProductBizException(ResultCodeEnum.QUERY_ERROR, "error");
            }
            /**
             * 将数据同步至redis
             */
            String type = jsonObject.getString(CANAL_LISTEN_TYPE);
            JSONArray jsonArray = jsonObject.getJSONArray(CANAL_LISTEN_DATA);
            CanalEventTypeEnum eventType = CanalEventTypeEnum.getEnumByCode(type);
            switch (eventType) {
                case UPDATE:
                case INSERT:
                    jsonArray.forEach(o -> {
                        ProductBaseInfo productBaseInfo = JSON.parseObject(o.toString(), ProductBaseInfo.class);
                        String key = RedisConstants.PRODUCT_KEY_PREFIX + productBaseInfo.getId();
                        redisClient.add(key, productBaseInfo);
                        LOGGER.info("更新redis缓存数据成功，数据值为：{}", productBaseInfo);
                    });
                    break;
                case DELETE:
                    jsonArray.forEach(o -> {
                        ProductBaseInfo productBaseInfo = JSON.parseObject(o.toString(), ProductBaseInfo.class);
                        String key = RedisConstants.PRODUCT_KEY_PREFIX + productBaseInfo.getId();
                        redisClient.delete(key);
                        LOGGER.info("删除redis缓存数据成功，删除的数据值为：{}", productBaseInfo);
                    });
                    break;
                default:
                    String errorMsg = String.format("不支持的消息监听类型:%s", eventType.getCode());
                    throw new ProductBizException(ResultCodeEnum.ILLEGAL_PARAMETER, errorMsg);
            }
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            LOGGER.error("消息签收失败，将消息转发至备用队列进行处理....", e);
            try {
                /**
                 * 当发生异常后，如何使用channel.basicNack(deliveryTag, false, true)的话，失败的消息会回退至对应消息队列的头部，消费者接着消费，接着抛异常，如此反复，导致消费阻塞，消息积压。
                 * 解决方案1：catch异常后，手动发送到指定队列，然后使用channel给rabbitmq确认消息已消费
                 * 解决方案2：给Queue绑定死信队列，使用nack（requque为false）确认消息消费失败
                 */
                //channel.basicPublish(RabbitMqConfig.DIRECT_EXCHANGE, RabbitMqConfig.DIRECT_ROUTING_KEY, MessageProperties.PERSISTENT_BASIC, message.getBody());
                //channel.basicAck(deliveryTag, false);
                channel.basicNack(deliveryTag, false, false);
            } catch (IOException ex) {
                LOGGER.error("消息转发至备用队列失败，请联系技术同学...", ex);
            }
        }
    }
}
