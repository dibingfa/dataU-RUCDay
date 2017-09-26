package com.flash.dataU.test.service.impl;

import com.flash.dataU.rucday.entity.RucUserDO;
import com.flash.dataU.rucday.service.RucUserService;
import com.flash.dataU.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service Test.
 *
 * @author Flash (18811311416@sina.cn)
 * @since 2017-07-06 14:27
 */
@Service
public class TestServiceImpl implements TestService{

    @Autowired
    private RucUserService rucUserService;

    /**
     * this method is only for Controller-Service test
     * @return
     */
    public String testService() {
        return "a Service class visit succeed, " +
            "connect 'browser-->Controller-->Service' build success";
    }

    /**
     * this method is only for Controller-Service-Dao test
     * @return
     */
    public String testDao() {
        RucUserDO userDO = rucUserService.findByUserGuid("12345");
        if (null == userDO) {
            return "未查到数据";
        }
        return userDO.toString();
    }

}
