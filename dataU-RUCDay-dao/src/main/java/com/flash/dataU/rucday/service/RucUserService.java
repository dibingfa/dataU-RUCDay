package com.flash.dataU.rucday.service;

import com.alibaba.fastjson.JSONObject;
import com.flash.dataU.rucday.entity.RucUserDO;
import com.flash.dataU.rucday.redis.RedisKeyUtil;
import com.flash.dataU.rucday.redis.RedisOpsUtil;
import com.flash.dataU.rucday.repository.RucUserRepository;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/9/23.
 */
@Service
public class RucUserService {

    private final static String TABLE = "user";
    private final static String USER_GUID = "user_guid";
    private final static String IP = "ip";
    private final static String NAME = "name";

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @Autowired
    private RucUserRepository rucUserRepository;

    /**
     * 根据guid查询用户信息
     */
    public RucUserDO findByUserGuid(String userGuid) {
        return findBy(USER_GUID, userGuid);
    }

    /**
     * 根据ip查询用户信息
     */
    public RucUserDO findByIp(String ip) {
        return findBy(IP, ip);
    }

    /**
     * 根据name查询用户信息
     */
    public RucUserDO findByName(String name) {
        return findBy(NAME, name);
    }

    /**
     * 根据ip查询用户信息
     */
    public RucUserDO findByIpOnlyRedis(String ip) {
        String redisKey = RedisKeyUtil.getFullKey(TABLE, IP, ip);
        String userDoStr = redisOpsUtil.get(redisKey);
        if (StringUtils.isNullOrEmpty(userDoStr)) {
            return null;
        }
        return JSONObject.parseObject(userDoStr, RucUserDO.class);
    }

    /**
     * 根据name查询用户信息
     */
    public RucUserDO findByNameOnlyRedis(String name) {
        String redisKey = RedisKeyUtil.getFullKey(TABLE, name, name);
        String userDoStr = redisOpsUtil.get(redisKey);
        if (StringUtils.isNullOrEmpty(userDoStr)) {
            return null;
        }
        return JSONObject.parseObject(userDoStr, RucUserDO.class);
    }

    /**
     * 存储用户
     */
    public RucUserDO save(RucUserDO userDO) {
        // 存储到数据库
        userDO.setCreateTime((int) System.currentTimeMillis());
        userDO = rucUserRepository.save(userDO);
        // 存储redis
        String userDoStr = JSONObject.toJSONString(userDO);
        redisOpsUtil.set(RedisKeyUtil.getFullKey(TABLE, USER_GUID, userDO.getUserGuid()), userDoStr);
        redisOpsUtil.set(RedisKeyUtil.getFullKey(TABLE, IP, userDO.getIp()), userDoStr);
        redisOpsUtil.set(RedisKeyUtil.getFullKey(TABLE, NAME, userDO.getName()), userDoStr);

        return userDO;
    }


    /**
     * findBy通用方法
     */
    private RucUserDO findBy(String byWhat, String key) {
        String redisKey = RedisKeyUtil.getFullKey(TABLE, byWhat, key);
        // 查询redis
        String userDoStr = redisOpsUtil.get(redisKey);
        if (StringUtils.isNullOrEmpty(userDoStr)) {
            // 未命中查询数据库
            RucUserDO userDo = null;
            int byWhatType = parseByWhatToType(byWhat);
            switch (byWhatType) {
                case 1:
                    userDo = rucUserRepository.findByUserGuid(key);
                    break;
                case 2:
                    userDo = rucUserRepository.findByIp(key);
                    break;
                case 3:
                    userDo = rucUserRepository.findByName(key);
                    break;
            }
            if (null != userDo) {
                // 如果查到了，同时将数据库数据存入redis
                userDoStr = JSONObject.toJSONString(userDo);
                redisOpsUtil.set(RedisKeyUtil.getFullKey(TABLE, USER_GUID, userDo.getUserGuid()), userDoStr);
                redisOpsUtil.set(RedisKeyUtil.getFullKey(TABLE, IP, userDo.getIp()), userDoStr);
                redisOpsUtil.set(RedisKeyUtil.getFullKey(TABLE, NAME, userDo.getName()), userDoStr);
            }
            return userDo;
        }
        return JSONObject.parseObject(userDoStr, RucUserDO.class);
    }

    /**
     * 将名称转换为数字，如将“user_guid”转换为1，便于用switch
     */
    private int parseByWhatToType(String byWhat) {
        if (byWhat.equals(USER_GUID))
            return 1;
        if (byWhat.equals(IP))
            return 2;
        if (byWhat.equals(NAME))
            return 3;
        return 0;
    }


}
