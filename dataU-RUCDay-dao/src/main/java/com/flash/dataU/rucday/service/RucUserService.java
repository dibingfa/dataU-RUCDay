package com.flash.dataU.rucday.service;

import com.alibaba.fastjson.JSONObject;
import com.flash.dataU.rucday.entity.RucGroupDO;
import com.flash.dataU.rucday.entity.RucUserDO;
import com.flash.dataU.rucday.redis.RedisKeyUtil;
import com.flash.dataU.rucday.redis.RedisOpsUtil;
import com.flash.dataU.rucday.repository.RucUserRepository;
import com.mysql.jdbc.StringUtils;
import java.util.ArrayList;
import java.util.List;
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
        String userDOStr = redisOpsUtil.get(redisKey);
        if (StringUtils.isNullOrEmpty(userDOStr)) {
            return null;
        }
        return JSONObject.parseObject(userDOStr, RucUserDO.class);
    }

    /**
     * 根据name查询用户信息
     */
    public RucUserDO findByNameOnlyRedis(String name) {
        String redisKey = RedisKeyUtil.getFullKey(TABLE, name, name);
        String userDOStr = redisOpsUtil.get(redisKey);
        if (StringUtils.isNullOrEmpty(userDOStr)) {
            return null;
        }
        return JSONObject.parseObject(userDOStr, RucUserDO.class);
    }

    /**
     * 查询全部群聊框
     */
    public List<RucUserDO> findAll() {
        String redisFormatKey = RedisKeyUtil.getFullKey(TABLE, USER_GUID, "*");
        // 查询redis
        List<String> userDOStrs = redisOpsUtil.getKeys(redisFormatKey);
        // 未查到任何数据，则继续查数据库
        if (null == userDOStrs || userDOStrs.size() == 0) {
            List<RucUserDO> userDOS = rucUserRepository.findAll();
            // 存入redis
            for (RucUserDO rucUserDO:userDOS) {
                String redisKey = RedisKeyUtil.getFullKey(TABLE, USER_GUID, rucUserDO.getUserGuid());
                redisOpsUtil.set(redisKey, JSONObject.toJSONString(rucUserDO));
            }
            return userDOS;
        }
        // 查到了则封装成对象list
        List<RucUserDO> userDOS = new ArrayList<RucUserDO>(userDOStrs.size());
        for (String userDOStr:userDOStrs) {
            RucUserDO userDO = JSONObject.parseObject(userDOStr, RucUserDO.class);
            userDOS.add(userDO);
        }
        return userDOS;
    }

    /**
     * 存储用户
     */
    public RucUserDO save(RucUserDO userDO) {
        // 存储到数据库
        userDO.setCreateTime(System.currentTimeMillis());
        userDO = rucUserRepository.save(userDO);
        // 存储redis
        String userDOStr = JSONObject.toJSONString(userDO);
        redisOpsUtil.set(RedisKeyUtil.getFullKey(TABLE, USER_GUID, userDO.getUserGuid()), userDOStr);
        redisOpsUtil.set(RedisKeyUtil.getFullKey(TABLE, IP, userDO.getIp()), userDOStr);
        redisOpsUtil.set(RedisKeyUtil.getFullKey(TABLE, NAME, userDO.getName()), userDOStr);

        return userDO;
    }


    /**
     * findBy通用方法
     */
    private RucUserDO findBy(String byWhat, String key) {
        String redisKey = RedisKeyUtil.getFullKey(TABLE, byWhat, key);
        // 查询redis
        String userDOStr = redisOpsUtil.get(redisKey);
        if (StringUtils.isNullOrEmpty(userDOStr)) {
            // 未命中查询数据库
            RucUserDO userDO = null;
            int byWhatType = parseByWhatToType(byWhat);
            switch (byWhatType) {
                case 1:
                    userDO = rucUserRepository.findByUserGuid(key);
                    break;
                case 2:
                    userDO = rucUserRepository.findByIp(key);
                    break;
                case 3:
                    userDO = rucUserRepository.findByName(key);
                    break;
            }
            if (null != userDO) {
                // 如果查到了，同时将数据库数据存入redis
                userDOStr = JSONObject.toJSONString(userDO);
                redisOpsUtil.set(RedisKeyUtil.getFullKey(TABLE, USER_GUID, userDO.getUserGuid()), userDOStr);
                redisOpsUtil.set(RedisKeyUtil.getFullKey(TABLE, IP, userDO.getIp()), userDOStr);
                redisOpsUtil.set(RedisKeyUtil.getFullKey(TABLE, NAME, userDO.getName()), userDOStr);
            }
            return userDO;
        }
        return JSONObject.parseObject(userDOStr, RucUserDO.class);
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
