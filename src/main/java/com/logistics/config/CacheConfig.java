package com.logistics.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    @Primary
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(30))
            .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .withCacheConfiguration("inventory", 
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)))
            .withCacheConfiguration("users", 
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(15)))
            .build();
    }

    @Bean
    @ConditionalOnMissingBean(RedisConnectionFactory.class)
    @Primary
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(5))
            .maximumSize(100));
        return cacheManager;
    }
} 