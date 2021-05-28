package com.example.data_service.Config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    private RedissonClient redissonClient;
    @Bean
    public RedisTemplate<Object, Object> CreateReidsTemplate(RedisTemplate redisTemplate) {     //更改Redis默认序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    @Bean
    public RedissonClient SetRedissonClient(){
        Config config = new Config();
        config
                .useSentinelServers()
                .addSentinelAddress("redis://127.0.0.1:16379", "redis://127.0.0.1:26379")
                .setMasterName("mymaster")
                .setDatabase(2)
                .setConnectTimeout(30000)
                .setReadMode(ReadMode.SLAVE);
        this.redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
