package com.mq.mssage.msg;

import com.mq.mssage.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者
 * 这一步也是最复杂的，因为可以编写出很多不同的需求出来，写法也有很多的不同
 * @author wusw
 * @date 2020/4/17 10:54
 */
@Component
@RabbitListener(queues = {RabbitConfig.QUEUE_A,RabbitConfig.QUEUE_B,RabbitConfig.QUEUE_C})
public class MsgReceiver {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 监听消费
     * @param content 获取的消息
     */
    @RabbitHandler
    public void process(String content){
        logger.info("接口处理A 队列消息：{}",content);
    }
}
