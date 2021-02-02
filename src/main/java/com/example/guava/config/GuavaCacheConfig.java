package com.example.guava.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wangyingchun
 * @version 1.0
 * @desc description
 * @date 2020-08-23 22:06
 */
@Configuration
@Slf4j
public class GuavaCacheConfig {

    @Bean
    public LoadingCache cacheTest1(CacheBuilder cacheBuilder) {
        return cacheBuilder
                .build(new CacheLoader<Object, Object>() {
                    @Override
                    public Object load(Object key)  {
                        return key;
                    }
                });
    }

    @Bean
    public CacheBuilder cacheBuilder() throws IllegalAccessException {
        CacheBuilder cacheBuilder = CacheBuilder.newBuilder()
                .recordStats()
                .expireAfterWrite(210, TimeUnit.MINUTES);
        return cacheBuilder;
    }

}