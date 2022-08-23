package com.imooc.malldevv1.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

/**
 * 缓存的配置类
 * 利用Redis缓存加速响应-S6：
 * 增加两个注解@Configuration和@EnableCaching
 * 来自视频5-11 利用Redis缓存加速响应
 * 2022-08-23 创建
 */
@Configuration
@EnableCaching
public class CachingConfig {

    //返回RedisCacheManager的对象，Spring就知道已对Redis进行了配置；找到对应的Bean进行注入
    /**
     * RedisCacheManager：
     * This cache manager creates caches by default upon first write. Empty caches are not visible on Redis due to how Redis represents empty data structures.
     *
     * RedisConnectionFactory：
     * Provides a suitable connection for interacting with Redis.
     * @return connection for interacting with Redis.
     *
     * RedisCacheWriter：
     * provides low level access to Redis commands ({@code SET, SETNX, GET, EXPIRE,...}) used for caching.
     *
     * RedisCacheConfiguration：
     * Immutable RedisCacheConfiguration helps customizing RedisCache behaviour such as caching
     * null values, cache key prefixes and binary serialization.
     */

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        //下面的配置也是模板化的
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(connectionFactory);
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();

        //定义超时时间，此处设置30s
        cacheConfiguration = cacheConfiguration.entryTtl(Duration.ofSeconds(30));
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisCacheWriter, cacheConfiguration);
        return redisCacheManager;
    }
}
