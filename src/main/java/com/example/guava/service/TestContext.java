package com.example.guava.service;

import java.util.concurrent.ExecutionException;

import com.example.guava.pojo.User;


/**
 * @Author wangyingchun07
 * @Date 2020/12/29 20:46
 * @Version V1.0
 * @Desc
 **/

public interface TestContext {


    User add(User user);

    String getCacheVisual();

    User query(String name);

    User getFromCache1(String userName) throws ExecutionException;

    void delete(String name);

    User update(User user);

    void changeCacheStats();

    void dynamicChangeCacheStats();


}
