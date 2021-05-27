package com.example.lms.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author 王子昂
 * @date 5/21/21
 * @description Redisson配置
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port)
        .setRetryInterval(1000)
        .setTimeout(1000)
        .setConnectTimeout(1000)
        .setIdleConnectionTimeout(1000)
        .setRetryAttempts(3);
        return Redisson.create(config);
    }

}
