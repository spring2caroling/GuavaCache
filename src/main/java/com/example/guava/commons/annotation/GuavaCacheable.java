package com.example.guava.commons.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import com.example.guava.commons.enums.Operation;

/**
 * @Author wangyingchun07
 * @Date 2021/2/2 14:29
 * @Version V1.0
 * @Desc
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GuavaCacheable {

    /**
     * loadingCache 名称
     * @return
     */
    String cacheName() default "";

    /**
     * key
     */
    String key() default "";

    /**
     * 操作类型
     */
    Operation operation();
}
