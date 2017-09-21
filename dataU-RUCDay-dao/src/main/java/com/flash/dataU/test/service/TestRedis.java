package com.flash.dataU.test.service;

import com.alibaba.fastjson.JSONObject;
import com.flash.dataU.rucday.redis.RedisOpsUtil;
import com.flash.dataU.test.entity.CityEntity;
import com.flash.dataU.test.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

/**
 * .
 *
 * @author sunyiming (sunyiming170619@credithc.com)
 * @version 0.0.1-SNAPSHOT
 * @since 2017年09月19日 16时19分
 */
@Service
public class TestRedis {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    public CityEntity findByName(String name){
        CityEntity cityEntity;
        String city = redisOpsUtil.get(name);
        if (StringUtils.isEmpty(city)) {
            cityEntity = testRepository.findByName(name);
            redisOpsUtil.set(name, JSONObject.toJSONString(cityEntity));
        } else {
            cityEntity = JSONObject.parseObject(city, CityEntity.class);
        }
        return cityEntity;
    }

}
