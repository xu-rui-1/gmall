/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ruitu.xr
 * @version RabbitMqConfig.java, v 0.1 2023年05月25日 19:13 ruitu.xr Exp $
 */
@Configuration
public class RabbitMqConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqConfig.class);

    @Resource
    private CachingConnectionFactory connectionFactory;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                String msgId = correlationData.getId();
                if (ack) {
                    LOGGER.info("消息成功发送到Exchange, msgId: {}", msgId);
                } else {
                    LOGGER.error("消息发送到Exchange失败，{}, case: {}", correlationData, cause);
                }
            }
        });
        //触发setReturnCallback回调必须设置mandatory=true,否则Exchange没有找到Queue就会丢弃掉消息,而不会触发回调
        rabbitTemplate.setMandatory(true);
        //消息是否从Exchange路由到Queue,注意:这是一个失败回调,只有消息从Exchange路由到Queue失败才会回调这个方法
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                LOGGER.error("消息从Exchange路由到Queue失败:exchange:{},route key:{},replyCode:{},replyText:{},message:{}", returned.getExchange(),
                        returned.getRoutingKey(), returned.getReplyCode(), returned.getReplyText(), returned.getMessage());
            }
        });

        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }


    public static final String DIRECT_EXCHANGE = "direct_exchange";
    public static final String DIRECT_QUEUE = "direct_queue";
    public static final String DIRECT_ROUTING_KEY = "direct_routing_key";

    @Bean
    public DirectExchange directExchange() {
        return ExchangeBuilder.directExchange(DIRECT_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue directQueue() {
        return new Queue(DIRECT_QUEUE, true, false, false);
    }
    @Bean
    public Binding directBinding() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with(DIRECT_ROUTING_KEY);
    }

    /**
     * canal + rabbitmq 配置信息
     */
    public static final String CANAL_EXCHANGE = "canal_exchange";
    public static final String CANAL_QUEUE = "canal_queue";
    public static final String CANAL_ROUTING_KEY = "canal_routing_key";

    //死信交换机
    public static final String DEAD_LETTER_EXCHANGE = "dead_exchange";
    //死信队列
    public static final String DEAD_QUEUE = "dead_queue";
    //死信队列的路由key
    public static final String DEAD_ROUTING_KEY = "dead_routing_key";

    //声明死信交换机
    @Bean
    public DirectExchange deadExchange() {
        return ExchangeBuilder.directExchange(DEAD_LETTER_EXCHANGE).durable(true).build();
    }

    //声明死信队列
    @Bean
    public Queue deadQueue() {
        return QueueBuilder.durable(DEAD_QUEUE).build();
    }

    @Bean
    public Binding deadQueueBinding() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with(DEAD_ROUTING_KEY);
    }

    @Bean
    public DirectExchange canalDirectExchange() {
        return ExchangeBuilder.directExchange(CANAL_EXCHANGE).durable(true).build();
    }

    /**
     * 将该队列绑定指定的死信队列
     * @return
     */
    @Bean
    public Queue canalDirectQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        return new Queue(CANAL_QUEUE, true, false, false, args);
    }
    @Bean
    public Binding canalDirectBinding() {
        return BindingBuilder.bind(canalDirectQueue()).to(canalDirectExchange()).with(CANAL_ROUTING_KEY);
    }

    /**
     * 定义延时交换机、延时队列、延时路由key
     */
    public static final String DELAYED_EXCHANGE = "delayed_exchange";
    public static final String DELAYED_QUEUE = "delayed_queue";
    public static final String DELAYED_ROUTING_KEY = "delayed_routing_key";

    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE, "x-delayed-message", true, false, args);
    }
    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable(DELAYED_QUEUE).build();
    }
    @Bean
    public Binding delayedQueueBinding() {
        return BindingBuilder.bind(deadQueue()).to(delayedExchange()).with(DEAD_ROUTING_KEY).noargs();
    }

}
