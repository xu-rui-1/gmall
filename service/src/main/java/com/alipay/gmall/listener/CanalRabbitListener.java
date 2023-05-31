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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
    public void receiveCanalMessage(Message message) {
        String msg = new String(message.getBody());
        LOGGER.info("收到来自canal监听到的消息：{}", msg);
        JSONObject jsonObject = JSON.parseObject(msg);
        String table = jsonObject.getString(CANAL_LISTEN_TABLE);
        if (!TABLE_NAME.equalsIgnoreCase(table)) {
            LOGGER.warn("该数据不是来自product_base_info表，无需处理。该数据所在表：{}", table);
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
                jsonArray.forEach(o->{
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
    }
}
