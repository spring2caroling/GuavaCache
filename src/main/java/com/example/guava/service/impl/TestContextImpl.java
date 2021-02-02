package com.example.guava.service.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.example.guava.commons.annotation.GuavaCacheable;
import com.example.guava.commons.enums.Operation;
import com.example.guava.pojo.User;
import com.example.guava.service.TestContext;
import com.google.common.cache.AbstractCache.StatsCounter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;

import lombok.extern.slf4j.Slf4j;


/**
 * @Author wangyingchun07
 * @Date 2020/12/29 20:47
 * @Version V1.0
 * @Desc
 **/
@Slf4j
@Service
public class TestContextImpl implements TestContext {

    @Autowired
    private LoadingCache cacheTest1;

    @Autowired
    private CacheBuilder cacheBuilder;


    @GuavaCacheable(cacheName = "cacheTest1", key = "#user.name", operation = Operation.UPDATE)
    public User add(User user) {
        System.out.println("走方法了。。。。。");
        return user;
    }

    @Override
    public String getCacheVisual() {
                CacheStats cacheStats = cacheTest1.stats();
                log.error("testCache1:{}", JSONObject.toJSONString(cacheTest1.asMap()));
                log.error("命中次数:{}", JSONObject.toJSONString(cacheStats.hitCount()));
                // 不记录手动删除的
                log.error("删除的条数:{}", JSONObject.toJSONString(cacheStats.evictionCount()));
                log.error("未命中次数:{}", JSONObject.toJSONString(cacheStats.missCount()));
                log.error("失败加载新值的次数:{}", JSONObject.toJSONString(cacheStats.loadExceptionCount()));
                log.error("请求次数:{}", JSONObject.toJSONString(cacheStats.requestCount()));
                log.error("加载新值花费时间:{}", JSONObject.toJSONString(cacheStats.averageLoadPenalty()));
                log.error("命中率:{}", JSONObject.toJSONString(cacheStats.hitRate()));
                log.error("总加载次数:{}", JSONObject.toJSONString(cacheStats.loadCount()));
                log.error("加载成功次数:{}", JSONObject.toJSONString(cacheStats.loadSuccessCount()));
                log.error("未命中率:{}", JSONObject.toJSONString(cacheStats.missRate()));
                log.error("总加载时长:{}", JSONObject.toJSONString(cacheStats.totalLoadTime()));
        return "ok";
    }

    @GuavaCacheable(cacheName = "cacheTest1", key = "#name", operation = Operation.QUERY)
    @Override
    public User query(String name) {
        System.out.println("query 没有走缓存。。。");
        return new User("111", 12);
    }

    @Override
    //    @Cacheable(value = "testCache1",key = "#userName")
    public User getFromCache1(String userName) throws ExecutionException {
        //        Object o = cacheTest1.get(userName);
        //        System.out.println(JSONObject.toJSONString(o));
        //        User user = (User) o;
        //        return user;
        return null;
    }

    @Override
    @GuavaCacheable(cacheName = "cacheTest1", key = "#name", operation = Operation.DELETE)
    public void delete(String name) {
        System.out.println("缓存删除了。。");
    }

    @Override
    @GuavaCacheable(cacheName = "cacheTest1", key = "#user.name", operation = Operation.UPDATE)
    public User update(User user) {
        System.out.println("缓存更新了");
        return user;
    }

    @Override
    public void changeCacheStats() {

    }

    @Override
    public void dynamicChangeCacheStats() {
        //        Class clazz = cacheBuilder.getClass();
        //
        //        try {
        //
        //            Field[] declaredFields = clazz.getDeclaredFields();
        //            for (Field declaredField : declaredFields) {
        //                declaredField.setAccessible(true);
        //                log.info(declaredField.getName() + "====" + JSONObject.toJSONString(declaredField.get
        // (cacheBuilder)));
        //            }
        //
        //            Field statsCounterSupplier = clazz.getDeclaredField("statsCounterSupplier");
        //            //            Field nullStatsCounter = clazz.getDeclaredField("NULL_STATS_COUNTER");
        //            statsCounterSupplier.setAccessible(true);
        //            statsCounterSupplier.set(cacheBuilder, Suppliers.ofInstance(
        //                    new StatsCounter() {
        //                        @Override
        //                        public void recordHits(int count) {
        //                        }
        //
        //                        @Override
        //                        public void recordMisses(int count) {
        //                        }
        //
        //                        @Override
        //                        public void recordLoadSuccess(long loadTime) {
        //                        }
        //
        //                        @Override
        //                        public void recordLoadException(long loadTime) {
        //                        }
        //
        //                        @Override
        //                        public void recordEviction() {
        //                        }
        //
        //                        @Override
        //                        public CacheStats snapshot() {
        //                            return new CacheStats(0, 0, 0, 0, 0, 0);
        //                        }
        //                    }));
        //
        //            Field capacity = clazz.getDeclaredField("strictParsing");
        //            capacity.setAccessible(true);
        //            capacity.set(cacheBuilder, false);
        //
        //            log.info("===================================");
        //
        //            for (Field declaredField : declaredFields) {
        //                declaredField.setAccessible(true);
        //                log.info(declaredField.getName() + "====" + JSONObject.toJSONString(declaredField.get
        // (cacheBuilder)));
        //            }
        //
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        //        System.out.println("反射设置了。。。。");

        try {
            Class localcacheClazz = Class.forName("com.google.common.cache.LocalCache");
            Constructor localcacheDeclaredConstructor =
                    localcacheClazz.getDeclaredConstructor(cacheBuilder.getClass(), CacheLoader.class);
            localcacheDeclaredConstructor.setAccessible(true);
            Object localcacheObj = localcacheDeclaredConstructor.newInstance(cacheBuilder, null);

            StatsCounter nullStatsCounter = new StatsCounter() {
                @Override
                public void recordHits(int count) {

                }

                @Override
                public void recordMisses(int count) {

                }

                @Override
                public void recordLoadSuccess(long loadTime) {

                }

                @Override
                public void recordLoadException(long loadTime) {

                }

                @Override
                public void recordEviction() {

                }

                @Override
                public CacheStats snapshot() {
                    return new CacheStats(0, 0, 0, 0, 0, 0);
                }
            };

            Class[] declaredClasses = localcacheClazz.getDeclaredClasses();
            for (Class segmentClazz : declaredClasses) {
                if ("Segment".equals(segmentClazz.getSimpleName())) {
                    for (Field field : segmentClazz.getDeclaredFields()) {
                        //                        System.out.println(field.getName()+"=="+field.get());
                    }


                    Constructor segmentClazzDeclaredConstructor = segmentClazz
                            .getDeclaredConstructor(localcacheClazz, int.class, long.class, StatsCounter.class);
                    segmentClazzDeclaredConstructor.setAccessible(true);
                    Object obj = segmentClazzDeclaredConstructor.newInstance(localcacheObj, 0, 0, nullStatsCounter);
                    Field statsCounter = segmentClazz.getDeclaredField("statsCounter");
                    statsCounter.setAccessible(true);
                    statsCounter.set(obj, nullStatsCounter);

                    //                    cacheTest1.invalidateAll();
                }
            }
        } catch (Exception e) {
            log.error("dynamic change record stats failed, msg:{}", e);
        }

    }


    public static void main(String[] args) {
        SpelExpressionParser parser = new SpelExpressionParser();
        User user = new User("a", 1);
        String value = "#user.name";
        Expression expression = parser.parseExpression(value);
        String expressionString = expression.getExpressionString();
        System.out.println(expressionString);
    }

}
