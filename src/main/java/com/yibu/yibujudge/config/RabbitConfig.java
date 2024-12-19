package com.yibu.yibujudge.config;


import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${judge.judge-queue}")
    private String judgeQueue;

    @Value("${judge.result-queue}")
    private String resultQueue;

    @Bean
    public Queue queue() {
        return new Queue(judgeQueue, true);
    }

    @Bean
    public Queue resultQueue() {
        return new Queue(resultQueue, true);
    }
}
