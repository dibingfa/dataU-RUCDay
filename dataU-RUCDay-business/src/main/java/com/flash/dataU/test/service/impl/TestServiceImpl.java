package com.flash.dataU.test.service.impl;

import com.flash.dataU.test.entity.CityEntity;
import com.flash.dataU.test.repository.TestRepository;
import com.flash.dataU.test.service.TestRedis;
import com.flash.dataU.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.util.resources.cldr.en.TimeZoneNames_en_PK;

/**
 * Service Test.
 *
 * @author Flash (18811311416@sina.cn)
 * @since 2017-07-06 14:27
 */
@Service
public class TestServiceImpl implements TestService{

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestRedis testRedis;

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
        return timePK(testRepository);
    }

    /**
     * this method is only for Controller-Service-Dao test
     * @return
     */
    public String testRedis() {
        return timePK(testRedis);
    }

    private String timePK(Object testDao){
        String name = "Chitungwiza";
        CityEntity cityEntity = null;
        long start = System.currentTimeMillis();
        for (int i = 0; i<1000;i++) {
            if (testDao instanceof TestRedis) {
                cityEntity = testRedis.findByName(name);
            } else {
                cityEntity = testRepository.findByName(name);
            }
        }
        long end = System.currentTimeMillis();
        long time = end - start;
        return name + " --- " + cityEntity.getPopulation() + " people, usetime:"+ time;
    }
}
