package com.flash.dataU.test.controller;

import com.flash.dataU.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller test.
 *
 * @author Flash (18811311416@sina.cn)
 * @since 2017-07-06 14:14
 */

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    /**
     * this method is only for Controller test, make sure that a Controller you write visit successfully
     * visit "http://localhost:8080/testController" after running, you can get a message
     * @return
     */
    @RequestMapping("testController")
    public String testController(){
        return "a Controller class visit succeed, " +
            "it means a connect 'browser-->Controller' build success";
    }

    /**
     * this method is only for Controller-Service test
     * visit "http://localhost:8080/testService" after running, you can get a message
     * @return
     */
    @RequestMapping("testService")
    public String testService(){
        return testService.testService();
    }

    /**
     * this method is only for Controller-Service-Dao test
     * visit "http://localhost:8080/testService" after running, you can get a message
     * @return
     */
    @RequestMapping("testDao")
    public String testDao(){
        return testService.testDao();
    }

    /**
     * this method is only for Controller-Service-Dao test
     * visit "http://localhost:8080/testRedis" after running, you can get a message
     * @return
     */
    @RequestMapping("testRedis")
    public String testRedis(){
        return testService.testRedis();
    }
}
