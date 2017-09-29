package com.flash.dataU.rucday.service;

import com.alibaba.fastjson.JSONObject;
import com.flash.dataU.rucday.entity.RucGroupDO;
import com.flash.dataU.rucday.redis.RedisKeyUtil;
import com.flash.dataU.rucday.redis.RedisOpsUtil;
import com.flash.dataU.rucday.repository.RucGroupRepository;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/23.
 */
@Service
public class RucGroupService {

    private final static String TABLE = "group";
    private final static String GROUP_GUID = "group_guid";

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @Autowired
    private RucGroupRepository rucGroupRepository;

    /**
     * 根据guid查询群聊框
     */
    public RucGroupDO findByGroupGuid(String groupGuid) {
        return findBy(GROUP_GUID, groupGuid);
    }

    /**
     * 查询全部群聊框
     */
    public List<RucGroupDO> findAll() {
        String redisFormatKey = RedisKeyUtil.getFullKey(TABLE, GROUP_GUID, "*");
        // 查询redis
        List<String> groupDOStrs = redisOpsUtil.getKeys(redisFormatKey);
        // 未查到任何数据，则继续查数据库
        if (null == groupDOStrs || groupDOStrs.size() == 0) {
            List<RucGroupDO> groupDOs = rucGroupRepository.findAll();
            // 存入redis
            for (RucGroupDO groupDO:groupDOs) {
                String redisKey = RedisKeyUtil.getFullKey(TABLE, GROUP_GUID, groupDO.getGroupGuid());
                redisOpsUtil.set(redisKey, JSONObject.toJSONString(groupDO));
            }
            return groupDOs;
        }
        // 查到了则封装成对象list
        List<RucGroupDO> groupDOs = new ArrayList<RucGroupDO>(groupDOStrs.size());
        for (String groupDOStr:groupDOStrs) {
            RucGroupDO rucGroupDO = JSONObject.parseObject(groupDOStr, RucGroupDO.class);
            groupDOs.add(rucGroupDO);
        }
        return groupDOs;
    }

    /**
     * 存储群聊框
     */
    public RucGroupDO save(RucGroupDO groupDO) {
        // 存储到数据库
        groupDO.setCreateTime(System.currentTimeMillis());
        groupDO = rucGroupRepository.save(groupDO);
        // 存储redis
        String groupDOStr = JSONObject.toJSONString(groupDO);
        redisOpsUtil.set(RedisKeyUtil.getFullKey(TABLE, GROUP_GUID, groupDO.getGroupGuid()), groupDOStr);

        return rucGroupRepository.save(groupDO);
    }

    /**
     * 清空数据库
     */
    public void deleteAll() {
        rucGroupRepository.deleteAll();
    }

    /**
     * findBy通用方法
     */
    private RucGroupDO findBy(String byWhat, String key) {
        String redisKey = RedisKeyUtil.getFullKey(TABLE, byWhat, key);
        // 查询redis
        String groupDOStr = redisOpsUtil.get(redisKey);
        if (StringUtils.isNullOrEmpty(groupDOStr)) {
            // 未命中查询数据库
            RucGroupDO groupDO = null;
            int byWhatType = parseByWhatToType(byWhat);
            switch (byWhatType) {
                case 1:
                    groupDO = rucGroupRepository.findByGroupGuid(key);
                    break;
            }
            if (null != groupDO) {
                // 如果查到了，同时将数据库数据存入redis
                groupDOStr = JSONObject.toJSONString(groupDO);
                redisOpsUtil.set(redisKey, groupDOStr);
            }
            return groupDO;
        }
        return JSONObject.parseObject(groupDOStr, RucGroupDO.class);
    }

    /**
     * 将名称转换为数字，如将“user_guid”转换为1，便于用switch
     */
    private int parseByWhatToType(String byWhat) {
        if (byWhat.equals(GROUP_GUID))
            return 1;
        return 0;
    }

}
