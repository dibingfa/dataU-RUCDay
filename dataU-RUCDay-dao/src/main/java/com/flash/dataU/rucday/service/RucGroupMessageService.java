package com.flash.dataU.rucday.service;

import com.alibaba.fastjson.JSONObject;
import com.flash.dataU.rucday.entity.RucGroupMessageDO;
import com.flash.dataU.rucday.redis.RedisKeyUtil;
import com.flash.dataU.rucday.redis.RedisOpsUtil;
import com.flash.dataU.rucday.repository.RucGroupMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/23.
 */
@Service
public class RucGroupMessageService {

    private final static String TABLE = "groupMessage";
    private final static String GROUP_GUID = "groupGuid";

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @Autowired
    private RucGroupMessageRepository RucGroupMessageRepository;

    /**
     * 根据索引查询群聊消息
     */
    public RucGroupMessageDO findByIndex(String groupGuid, Long index) {
        String redisKey = RedisKeyUtil.getFullKey(TABLE, GROUP_GUID, groupGuid);
        String groupMessageDoStr = redisOpsUtil.index(redisKey, index);
        return JSONObject.parseObject(groupMessageDoStr, RucGroupMessageDO.class);
    }

    /**
     * 查询全部群聊消息（最后多少条）
     */
    public List<RucGroupMessageDO> findAll(String groupGuid, int count) {
        String redisKey = RedisKeyUtil.getFullKey(TABLE, GROUP_GUID, groupGuid);
        List<String> groupMessageDoStrs = redisOpsUtil.listAll(redisKey, count);
        // 转换list里的字符串为object
        List<RucGroupMessageDO> groupMessageDos = new ArrayList<RucGroupMessageDO>(groupMessageDoStrs.size());
        for (String groupMessageDoStr:groupMessageDoStrs) {
            RucGroupMessageDO rucGroupMessageDO = JSONObject.parseObject(groupMessageDoStr, RucGroupMessageDO.class);
            groupMessageDos.add(rucGroupMessageDO);
        }
        return groupMessageDos;
    }

    /**
     * 查询全部群聊消息
     */
    public List<RucGroupMessageDO> findAll(String groupGuid) {
        String redisKey = RedisKeyUtil.getFullKey(TABLE, GROUP_GUID, groupGuid);
        List<String> groupMessageDoStrs = redisOpsUtil.listAll(redisKey);
        // 转换list里的字符串为object
        List<RucGroupMessageDO> groupMessageDos = new ArrayList<RucGroupMessageDO>(groupMessageDoStrs.size());
        for (String groupMessageDoStr:groupMessageDoStrs) {
            RucGroupMessageDO rucGroupMessageDO = JSONObject.parseObject(groupMessageDoStr, RucGroupMessageDO.class);
            groupMessageDos.add(rucGroupMessageDO);
        }
        return groupMessageDos;
    }

    /**
     * 存储群聊消息
     */
    public RucGroupMessageDO save(RucGroupMessageDO groupMessageDO) {
        // 存储到数据库
        groupMessageDO.setCreateTime((int) System.currentTimeMillis());
        groupMessageDO = RucGroupMessageRepository.save(groupMessageDO);
        // 存储redis
        String groupMessageDoStr = JSONObject.toJSONString(groupMessageDO);
        String redisKey = RedisKeyUtil.getFullKey(TABLE, GROUP_GUID, groupMessageDO.getToGuid());
        redisOpsUtil.rpush(redisKey, groupMessageDoStr);

        return groupMessageDO;
    }


}
