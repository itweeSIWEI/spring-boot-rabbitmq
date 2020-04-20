package com.mq.mssage.msg;

import com.mq.mssage.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 消息生产者
 *
 * @author wusw
 * @date 2020/4/17 10:32
 */
@Component
public class MsgProducer implements RabbitTemplate.ConfirmCallback {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public MsgProducer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
        //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
        rabbitTemplate.setConfirmCallback(this);
    }
    /**
     *  实现类 回调
     * @param correlationData 回调 id
     * @param b 是否成功
     * @param s 失败消息
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        logger.info("回调 id:{}",correlationData);
        if (b){
            logger.info("消费成功");
        }else {
            logger.info("消费消息失败:{}",s);
        }
    }

    /**
     * 要发送的消息 A
     * @param content 消息内容
     */
    public void sendMsg(String content){
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A,RabbitConfig.ROUTINGKEY_A,content,correlationData);
    }

    /**
     * 要发送的消息 B
     * @param content 消息内容
     */
    public void sendMsgB(String content){
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_B,RabbitConfig.ROUTINGKEY_B,content,correlationData);
    }

    /**
     * 要发送的消息 C
     * @param content 消息内容
     */
    public void sendMsgC(String content){
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_C,RabbitConfig.ROUTINGKEY_C,content,correlationData);
    }
}
