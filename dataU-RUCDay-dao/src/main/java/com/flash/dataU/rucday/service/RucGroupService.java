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
        List<String> groupDoStrs = redisOpsUtil.getKeys(redisFormatKey);
        // 未查到任何数据，则继续查数据库
        if (null == groupDoStrs || groupDoStrs.size() == 0) {
            List<RucGroupDO> groupDos = rucGroupRepository.findAll();
            // 存入redis
            for (RucGroupDO groupDo:groupDos) {
                String redisKey = RedisKeyUtil.getFullKey(TABLE, GROUP_GUID, groupDo.getGroupGuid());
                redisOpsUtil.set(redisKey, JSONObject.toJSONString(groupDo));
            }
            return groupDos;
        }
        // 查到了则封装成对象list
        List<RucGroupDO> groupDos = new ArrayList<RucGroupDO>(groupDoStrs.size());
        for (String groupDoStr:groupDoStrs) {
            RucGroupDO rucGroupDO = JSONObject.parseObject(groupDoStr, RucGroupDO.class);
            groupDos.add(rucGroupDO);
        }
        return groupDos;
    }

    /**
     * 存储群聊框
     */
    public RucGroupDO save(RucGroupDO groupDO) {
        // 存储到数据库
        groupDO.setCreateTime((int) System.currentTimeMillis());
        groupDO = rucGroupRepository.save(groupDO);
        // 存储redis
        String groupDoStr = JSONObject.toJSONString(groupDO);
        redisOpsUtil.set(RedisKeyUtil.getFullKey(TABLE, GROUP_GUID, groupDO.getGroupGuid()), groupDoStr);

        return rucGroupRepository.save(groupDO);
    }

    /**
     * findBy通用方法
     */
    private RucGroupDO findBy(String byWhat, String key) {
        String redisKey = RedisKeyUtil.getFullKey(TABLE, byWhat, key);
        // 查询redis
        String groupDoStr = redisOpsUtil.get(redisKey);
        if (StringUtils.isNullOrEmpty(groupDoStr)) {
            // 未命中查询数据库
            RucGroupDO groupDo = null;
            int byWhatType = parseByWhatToType(byWhat);
            switch (byWhatType) {
                case 1:
                    groupDo = rucGroupRepository.findByGroupGuid(key);
                    break;
            }
            if (null != groupDo) {
                // 如果查到了，同时将数据库数据存入redis
                groupDoStr = JSONObject.toJSONString(groupDo);
                redisOpsUtil.set(redisKey, groupDoStr);
            }
            return groupDo;
        }
        return JSONObject.parseObject(groupDoStr, RucGroupDO.class);
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
