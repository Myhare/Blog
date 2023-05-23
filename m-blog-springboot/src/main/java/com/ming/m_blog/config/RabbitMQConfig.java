package com.ming.m_blog.config;


import com.ming.m_blog.constant.MQPrefixConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /**
     * maxwell通知队列
     */
    @Bean
    public Queue maxwellQueue(){
        return new Queue(MQPrefixConst.MAXWELL_QUEUE,true);
    }

    /**
     * maxwell交换机
     */
    @Bean
    public FanoutExchange maxwellExchange(){
        return new FanoutExchange(MQPrefixConst.MAXWELL_EXCHANGE,true,false);
    }

    /**
     * 绑定maxwell交换机和队列
     */
    @Bean
    public Binding bindingMaxwellDirect(){
        return BindingBuilder.bind(maxwellQueue()).to(maxwellExchange());
    }

    /**
     * 发送邮件队列
     */
    @Bean
    public Queue emailQueue() {
        return new Queue(MQPrefixConst.EMAIL_QUEUE, true);
    }

    /**
     * 发送邮件交换机
     */
    @Bean
    public FanoutExchange emailExchange() {
        return new FanoutExchange(MQPrefixConst.EMAIL_EXCHANGE, true, false);
    }

    /**
     * 绑定交换机和队列
     */
    @Bean
    public Binding bindingEmailDirect() {
        return BindingBuilder.bind(emailQueue()).to(emailExchange());
    }

}
