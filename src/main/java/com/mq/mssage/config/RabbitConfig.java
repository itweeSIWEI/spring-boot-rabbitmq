package com.mq.mssage.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 *  Broker:它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输,
 *  Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。
 *  Queue:消息的载体,每个消息都会被投到一个或多个队列。
 *  Binding:绑定，它的作用就是把exchange和queue按照路由规则绑定起来.
 *  Routing Key:路由关键字,exchange根据这个关键字进行消息投递。
 *  vhost:虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。
 *  Producer:消息生产者,就是投递消息的程序.
 *  Consumer:消息消费者,就是接受消息的程序.
 *  Channel:消息通道,在客户端的每个连接里,可建立多个channel.
 * @author wusw
 * @date 2020/4/17 10:15
 */
@Configuration
public class RabbitConfig {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    /**
     * Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。
     */
    public static final String EXCHANGE_A = "my-mq-exchange_A";
    public static final String EXCHANGE_B = "my-mq-exchange_B";
    public static final String EXCHANGE_C = "my-mq-exchange_C";

    /**
     * Queue:消息的载体,每个消息都会被投到一个或多个队列。
     */
    public static final String QUEUE_A = "QUEUE_A";
    public static final String QUEUE_B = "QUEUE_B";
    public static final String QUEUE_C = "QUEUE_C";

    /**
     * Routing Key:路由关键字,exchange根据这个关键字进行消息投递。
     */
    public static final String ROUTINGKEY_A = "spring-boot-routingKey_A";
    public static final String ROUTINGKEY_B = "spring-boot-routingKey_B";
    public static final String ROUTINGKEY_C = "spring-boot-routingKey_C";

    /**
     * 广播模式
     */
    private static final String FANOUT_EXCHANGE = "fanoutExchange";

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory factory = new CachingConnectionFactory(host, port);
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost("/");
        factory.setPublisherConfirms(true);
        return factory;
    }

    /**
     * 这里 spring 的Scope 必须是 prototype 多例模式
     * @return RabbitTemplate
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(){
        return new RabbitTemplate(connectionFactory());
    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
             FanoutExchange: 将消息分发到所有的绑定队列，无 routing key 的概念
             HeadersExchange ：通过添加属性 key-value 匹配
             DirectExchange:按照 routing key 分发到指定队列
             TopicExchange:多关键字匹配
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(RabbitConfig.EXCHANGE_A);
    }

    @Bean
    public DirectExchange directExchangeB(){
        return new DirectExchange(RabbitConfig.EXCHANGE_B);
    }

    @Bean
    public DirectExchange directExchangeC(){
        return new DirectExchange(RabbitConfig.EXCHANGE_C);
    }



    /**
     * 获取 消息队列A
     * @return 消息A
     */
    @Bean
    public Queue queueA(){
        return new Queue(QUEUE_A,true);
    }
    /**
     * 获取 消息队列B
     * @return 消息A
     */
    @Bean
    public Queue queueB(){
        return new Queue(QUEUE_B,true);
    }
    /**
     * 获取 消息队列C
     * @return 消息A
     */
    @Bean
    public Queue queueC(){
        return new Queue(QUEUE_C,true);
    }



    /**
     * Binding:绑定，它的作用就是把 exchange 和 queueA 按照路由规则绑定起来.
     * @return 绑定对象
     */
    @Bean
    public Binding bindingA(){
        return BindingBuilder.bind(queueA()).to(directExchange()).with(RabbitConfig.ROUTINGKEY_A);
    }
    /**
     * BindingB:绑定，它的作用就是把 exchange 和 queueB 按照路由规则绑定起来.
     * @return 绑定对象
     */
    @Bean
    public Binding bindingB(){
        return BindingBuilder.bind(queueB()).to(directExchangeB()).with(RabbitConfig.ROUTINGKEY_B);
    }
    /**
     * BindingC:绑定，它的作用就是把 exchange 和 queueC 按照路由规则绑定起来.
     * @return 绑定对象
     */
    @Bean
    public Binding bindingC(){
        return BindingBuilder.bind(queueC()).to(directExchangeC()).with(RabbitConfig.ROUTINGKEY_C);
    }

//
//    /**
//     * 配置 广播交换机 :无 routing key 的概念  和上面模式 二选一
//     * @return
//     */
//    @Bean
//    public FanoutExchange fanoutExchange(){
//        return new FanoutExchange(RabbitConfig.FANOUT_EXCHANGE);
//    }
//
//    /**
//     * 绑定广播交换机 A
//     */
//    @Bean
//    public Binding bindingExchangeA(Queue queueA,FanoutExchange fanoutExchange){
//        return BindingBuilder.bind(queueA()).to(fanoutExchange);
//    }
//    /**
//     * 绑定广播交换机 A
//     */
//    @Bean
//    public Binding bindingExchangeB(Queue queueB,FanoutExchange fanoutExchange){
//        return BindingBuilder.bind(queueB()).to(fanoutExchange);
//    }
//    /**
//     * 绑定广播交换机 A
//     */
//    @Bean
//    public Binding bindingExchangeC(Queue queueC,FanoutExchange fanoutExchange){
//        return BindingBuilder.bind(queueC()).to(fanoutExchange);
//    }
}
