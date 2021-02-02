package com.example.guava.controller;

import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.example.guava.pojo.User;
import com.example.guava.service.TestContext;


/**
 * @Author wangyingchun07
 * @Date 2020/12/27 21:37
 * @Version V1.0
 * @Desc
 **/
@RestController
public class TestController {

    @Resource
    private TestContext testContext;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void add(@RequestBody User user) {
        testContext.add(user);
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public void query(String name) {
        User query = testContext.query(name);
        System.out.println("查询结果：" + JSONObject.toJSONString(query));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public void delete(String name) {
        testContext.delete(name);
    }

    @RequestMapping(value = "/cache/visual", method = RequestMethod.GET)
    public void cacheVisual() {
        testContext.getCacheVisual();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void update(@RequestBody User user) throws ExecutionException {
        testContext.update(user);
    }

    @RequestMapping(value = "/getFromCache2", method = RequestMethod.GET)
    public void getFromCache2() throws ExecutionException {
        testContext.getFromCache1("cache2");
    }

    @RequestMapping(value = "/changeCacheStats", method = RequestMethod.GET)
    public void changeCacheStats() {
        testContext.changeCacheStats();
    }

    @RequestMapping(value = "/dynamicChangeCacheStats", method = RequestMethod.GET)
    public void dynamicChangeCacheStats() {
        testContext.dynamicChangeCacheStats();
    }
}
