package com.example.guava.commons.aop;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.example.guava.commons.annotation.GuavaCacheable;
import com.example.guava.commons.enums.Operation;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author wangyingchun07
 * @Date 2021/2/2 15:41
 * @Version V1.0
 * @Desc
 **/
@Component
@Slf4j
@Aspect
public class GuavaCacheAspect implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static final Gson gson = new Gson();


    @Pointcut("@annotation(com.example.guava.commons.annotation.GuavaCacheable)")
    public void pointCut() {
    }


    @Around("pointCut()")
    public Object cacheOperator(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            GuavaCacheable cacheAnno = method.getAnnotation(GuavaCacheable.class);
            String cacheName = cacheAnno.cacheName();
            LoadingCache cache = (LoadingCache) applicationContext.getBean(cacheName);
            // 获取本方法所有的参数
            Object[] args = joinPoint.getArgs();
            // 获取key
            String key = parseKey(cacheAnno.key(), method, args);
            Operation cacheOperation = cacheAnno.operation();
            // 获取返回值类型
            Type type = method.getAnnotatedReturnType().getType();
            // 处理查询
            if (Operation.QUERY.equals(cacheOperation)) {
                Object obj = cache.get(key);
                // 缓存有
                if (!key.equals(obj)) {
                    return gson.fromJson((String) obj, type);
                } else {
                    Object obj1 = joinPoint.proceed();
                    cache.put(key, gson.toJson(obj1));
                    return obj1;
                }
            }
            // 处理update
            if (Operation.UPDATE.equals(cacheOperation)) {
                Object updateObj = joinPoint.proceed();
                cache.put(key, gson.toJson(updateObj));
                return updateObj;
            }
            // 处理删除
            if (Operation.DELETE.equals(cacheOperation)) {
                cache.invalidate(key);
            }

        } catch (ExecutionException e) {
            log.error("cacheOperator failed, msg:{}", e);
        }
        return joinPoint.proceed();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    //此方法为了支持spel表达式
    public String parseKey(String key, Method method, Object[] args) {
        //解析方法所有参数名称eg (String name) 解析出来name
        LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        // spel上下文，做参数name和value的映射
        StandardEvaluationContext spelContext = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            spelContext.setVariable(parameterNames[i], args[i]);
        }
        //spel表达式解析器
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        return spelExpressionParser.parseExpression(key).getValue(spelContext, String.class);
    }
}
