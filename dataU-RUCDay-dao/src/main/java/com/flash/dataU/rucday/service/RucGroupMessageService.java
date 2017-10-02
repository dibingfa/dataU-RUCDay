package com.flash.dataU.rucday.service;

import com.alibaba.fastjson.JSONObject;
import com.flash.dataU.rucday.entity.RucGroupMessageDO;
import com.flash.dataU.rucday.redis.RedisKeyUtil;
import com.flash.dataU.rucday.redis.RedisOpsUtil;
import com.flash.dataU.rucday.repository.RucGroupMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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
    private RucGroupMessageRepository rucGroupMessageRepository;

    /**
     * 根据索引查询群聊消息
     */
    public RucGroupMessageDO findByIndex(String groupGuid, Long index) {
        String redisKey = RedisKeyUtil.getFullKey(TABLE, GROUP_GUID, groupGuid);
        String groupMessageDOStr = redisOpsUtil.index(redisKey, index);
        return JSONObject.parseObject(groupMessageDOStr, RucGroupMessageDO.class);
    }

    /**
     * 查询全部群聊消息（最后多少条）
     */
    public List<RucGroupMessageDO> findAll(String groupGuid, int count) {
        String redisKey = RedisKeyUtil.getFullKey(TABLE, GROUP_GUID, groupGuid);
        List<String> groupMessageDOStrs = redisOpsUtil.listAll(redisKey, count);
        // 转换list里的字符串为object
        List<RucGroupMessageDO> groupMessageDOs = new ArrayList<RucGroupMessageDO>(groupMessageDOStrs.size());
        for (String groupMessageDOStr:groupMessageDOStrs) {
            RucGroupMessageDO rucGroupMessageDO = JSONObject.parseObject(groupMessageDOStr, RucGroupMessageDO.class);
            groupMessageDOs.add(rucGroupMessageDO);
        }
        return groupMessageDOs;
    }

    /**
     * 查询全部群聊消息
     */
    public List<RucGroupMessageDO> findAll(String groupGuid) {
        String redisKey = RedisKeyUtil.getFullKey(TABLE, GROUP_GUID, groupGuid);
        List<String> groupMessageDOStrs = redisOpsUtil.listAll(redisKey);
        // 转换list里的字符串为object
        List<RucGroupMessageDO> groupMessageDOs = new ArrayList<RucGroupMessageDO>(groupMessageDOStrs.size());
        for (String groupMessageDOStr:groupMessageDOStrs) {
            RucGroupMessageDO rucGroupMessageDO = JSONObject.parseObject(groupMessageDOStr, RucGroupMessageDO.class);
            groupMessageDOs.add(rucGroupMessageDO);
        }
        return groupMessageDOs;
    }

    /**
     * 存储群聊消息
     */
    @Transactional
    public RucGroupMessageDO save(RucGroupMessageDO groupMessageDO) {
        // 存储到数据库
        groupMessageDO.setCreateTime(new Timestamp(System.currentTimeMillis()));
        groupMessageDO = rucGroupMessageRepository.save(groupMessageDO);
        // 存储redis
        String groupMessageDOStr = JSONObject.toJSONString(groupMessageDO);
        String redisKey = RedisKeyUtil.getFullKey(TABLE, GROUP_GUID, groupMessageDO.getToGuid());
        redisOpsUtil.rpush(redisKey, groupMessageDOStr);

        return groupMessageDO;
    }

    /**
     * 存储群聊信息
     */
    public List<RucGroupMessageDO> save(List<RucGroupMessageDO> groupMessageDOs) {
        List<RucGroupMessageDO> savedGroupDOs = new ArrayList<RucGroupMessageDO>(groupMessageDOs.size());
        for (RucGroupMessageDO groupMessageDO:groupMessageDOs) {
            RucGroupMessageDO saved = save(groupMessageDO);
            savedGroupDOs.add(saved);
        }
        return savedGroupDOs;
    }

    /**
     * 存储群聊消息
     */
    public long countAll(String groupGuid) {
        String redisKey = RedisKeyUtil.getFullKey(TABLE, GROUP_GUID, groupGuid);
        return redisOpsUtil.countAll(redisKey);
    }

    /**
     * 清空数据库
     */
    public void deleteAll() {
        rucGroupMessageRepository.deleteAll();
    }


}
