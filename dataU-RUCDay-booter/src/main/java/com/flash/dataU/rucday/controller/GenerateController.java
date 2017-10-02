package com.flash.dataU.rucday.controller;

import com.alibaba.fastjson.JSONObject;
import com.flash.dataU.rucday.entity.RucGroupDO;
import com.flash.dataU.rucday.entity.RucGroupMessageDO;
import com.flash.dataU.rucday.entity.RucUserDO;
import com.flash.dataU.rucday.mock.MockUtils;
import com.flash.dataU.rucday.redis.RedisKeyUtil;
import com.flash.dataU.rucday.redis.RedisOpsUtil;
import com.flash.dataU.rucday.repository.RucGroupMessageRepository;
import com.flash.dataU.rucday.repository.RucGroupRepository;
import com.flash.dataU.rucday.repository.RucUserRepository;
import com.flash.dataU.rucday.service.RucGroupMessageService;
import com.flash.dataU.rucday.service.RucGroupService;
import com.flash.dataU.rucday.service.RucUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * .
 *
 * @author sunyiming (sunyiming170619@credithc.com)
 * @version 0.0.1-SNAPSHOT
 * @since 2017年09月26日 14时44分
 */
@RestController
@RequestMapping("generate")
public class GenerateController {

    @Autowired
    private RucUserService rucUserService;

    @Autowired
    private RucGroupService rucGroupService;

    @Autowired
    private RucGroupMessageService rucGroupMessageService;

    @Autowired
    private RucUserRepository rucUserRepository;

    @Autowired
    private RucGroupRepository rucGroupRepository;

    @Autowired
    private RucGroupMessageRepository rucGroupMessageRepository;

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    private Jedis jedis = new Jedis("localhost", 6379);

    private String[] groupMsgs = {"我是小庆，先给大家开个头哦，头！"};

    private List<RucGroupDO> groupDOS = initGroup();
    private List<RucUserDO> userDOS = initUser();
    private List<RucGroupMessageDO> groupMessageDOS;

    private List<RucGroupDO> initGroup() {
        List<RucGroupDO> groupDOS = new ArrayList<RucGroupDO>();
        //groupDOS.add(MockUtils.mockGroup("刘伟", "101"));
        groupDOS.add(MockUtils.mockGroup("一勺池群", "102"));
        groupDOS.add(MockUtils.mockGroup("明德楼群", "103"));
        groupDOS.add(MockUtils.mockGroup("求是石群", "104"));
        groupDOS.add(MockUtils.mockGroup("保研路群", "105"));
        groupDOS.add(MockUtils.mockGroup("教二草坪群", "106"));
        groupDOS.add(MockUtils.mockGroup("本应用意见反馈", "201"));
        return groupDOS;
    }

    private List<RucUserDO> initUser() {
        List<RucUserDO> userDOS = new ArrayList<RucUserDO>();
        userDOS.add(MockUtils.mockUser("RUC小庆 ", 1, "101"));
        //userDOS.add(MockUtils.mockUser("张三男 ", 1, "1"));
        //userDOS.add(MockUtils.mockUser("李四女 ", 0, "0"));
        //userDOS.add(MockUtils.mockUser("王五变态 ", 2, "2"));
        return userDOS;
    }

    /**
     * 添加群
     */
    @RequestMapping("group")
    public String group() {
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (RucGroupDO groupDO : groupDOS) {
            groupDO = rucGroupService.save(groupDO);
            sb.append("第" + count++ + "个群创建：" + groupDO.getName() + "<br/>");
        }
        return sb.toString();
    }

    /**
     * 添加用户
     */
    @RequestMapping("user")
    public String user() {
        StringBuilder sb = new StringBuilder();
        int count = 1;

        for (RucUserDO userDO:userDOS) {
            // 初始化最后一条未读消息记录
            List<RucGroupDO> rucGroupDOS = rucGroupService.findAll();
            String[] lastReadMsgIndexs = new String[rucGroupDOS.size()];
            for (int i = 0; i < lastReadMsgIndexs.length; i++) {
                lastReadMsgIndexs[i] = rucGroupDOS.get(i).getGroupGuid() + ":" + "-1";
            }
            String lastReadMsgIndexStr = String.join(",", lastReadMsgIndexs);
            userDO.setLastReadMsgIndex(lastReadMsgIndexStr);

            userDO = rucUserService.save(userDO);
            sb.append("第" + count++ + "个新用户创建：" + userDO.getName() + "<br/>");
        }

        return sb.toString();
    }

    /**
     * 添加群聊消息
     */
    @RequestMapping("groupMsg")
    public String groupMsg() {
        List<RucGroupDO> rucGroupDOS = rucGroupService.findAll();
        List<RucUserDO> rucUserDOs = rucUserService.findAll();
        StringBuilder sb = new StringBuilder();
        int count = 1;
        Random random = new Random();
        int randomRange = groupMsgs.length;

        RucUserDO userDO = rucUserDOs.get(0);
        for (RucGroupDO groupDO : rucGroupDOS) {
            RucGroupMessageDO groupMessageDO = new RucGroupMessageDO();
            String content = groupMsgs[random.nextInt(randomRange)];
            groupMessageDO.setContent(content);
            groupMessageDO.setFromGuid(userDO.getUserGuid());
            groupMessageDO.setToGuid(groupDO.getGroupGuid());
            RucGroupMessageDO rucGroupMessageDO = rucGroupMessageService.save(groupMessageDO);
            sb.append("第" + count++ + "条信息创建：from " + userDO.getName() +
                    "to " + groupDO.getName() + "--> " + content + "<br/>");
        }

        return sb.toString();
    }

    /**
     * 清除全部消息
     */
    @RequestMapping("flushAll")
    public String flushAll() {
        rucUserService.deleteAll();
        rucGroupService.deleteAll();
        rucGroupMessageService.deleteAll();

        List<RucUserDO> userDOS = rucUserRepository.findAll();
        List<RucGroupDO> groupDOS = rucGroupRepository.findAll();
        List<RucGroupMessageDO> groupMessageDOS = rucGroupMessageRepository.findAll();

        if (userDOS != null && userDOS.size() != 0) {
            return "数据库 userDO未删除干净：" + userDOS;
        }
        if (groupDOS != null && groupDOS.size() != 0) {
            return "数据库 groupDOS未删除干净：" + groupDOS;
        }
        if (groupMessageDOS != null && groupMessageDOS.size() != 0) {
            return "数据库 groupMessageDOS未删除干净：" + groupMessageDOS;
        }

        jedis.flushAll();

        List<String> keys = redisOpsUtil.getKeys("*");
        if (keys == null || keys.size() == 0) {
            return "success";
        }
        for (String key : keys) {
            redisOpsUtil.delete(key);
        }

        keys = redisOpsUtil.getKeys("*");
        if (keys != null && keys.size() != 0) {
            return "redis未删除干净" + Arrays.toString(keys.toArray());
        }

        return "success";
    }

    /**
     * 清除redis消息
     */
    @RequestMapping("flushRedis")
    public String flushRedis() {

        jedis.flushAll();

        List<String> keys = redisOpsUtil.getKeys("*");
        if (keys == null || keys.size() == 0) {
            return "success";
        }
        for (String key : keys) {
            redisOpsUtil.delete(key);
        }

        keys = redisOpsUtil.getKeys("*");
        if (keys != null && keys.size() != 0) {
            return "redis未删除干净" + Arrays.toString(keys.toArray());
        }

        return "success";
    }

    /**
     * 同步数据库和redis消息
     */
    @RequestMapping("fillRedis")
    public String fillRedis() {

        jedis.flushAll();

        List<RucUserDO> userDOS = rucUserRepository.findAll();
        List<RucGroupDO> groupDOS = rucGroupRepository.findAll();
        List<RucGroupMessageDO> groupMessageDOS = rucGroupMessageRepository.findAll();

        for (RucUserDO userDO : userDOS) {
            // 存储redis
            String userDOStr = JSONObject.toJSONString(userDO);
            redisOpsUtil.set(RedisKeyUtil.getFullKey("user", "user_guid", userDO.getUserGuid()), userDOStr);
            redisOpsUtil.set(RedisKeyUtil.getFullKey("user", "ip", userDO.getIp()), userDOStr);
            redisOpsUtil.set(RedisKeyUtil.getFullKey("user", "name", userDO.getName()), userDOStr);
        }

        for (RucGroupDO groupDO : groupDOS) {
            // 存储redis
            String groupDOStr = JSONObject.toJSONString(groupDO);
            redisOpsUtil.set(RedisKeyUtil.getFullKey("group", "group_guid", groupDO.getGroupGuid()), groupDOStr);
        }

        for (RucGroupMessageDO groupMessageDO : groupMessageDOS) {
            // 存储redis
            String groupMessageDOStr = JSONObject.toJSONString(groupMessageDO);
            String redisKey = RedisKeyUtil.getFullKey("groupMessage", "groupGuid", groupMessageDO.getToGuid());
            redisOpsUtil.rpush(redisKey, groupMessageDOStr);
        }

        return "success";
    }


    /**
     * 同步数据库和redis消息
     */
    @RequestMapping("checkData")
    public String checkData() {

        StringBuilder sb = new StringBuilder();

        List<RucUserDO> userDOS = rucUserRepository.findAll();
        List<RucGroupDO> groupDOS = rucGroupRepository.findAll();
        List<RucGroupMessageDO> groupMessageDOS = rucGroupMessageRepository.findAll();

        sb.append("用户的同步---------------");
        for (RucUserDO userDO : userDOS) {
            // 存储redis
            String userDOStr = JSONObject.toJSONString(userDO);
            String s1 = redisOpsUtil.get(RedisKeyUtil.getFullKey("user", "user_guid", userDO.getUserGuid()));
            String s2 = redisOpsUtil.get(RedisKeyUtil.getFullKey("user", "ip", userDO.getIp()));
            String s3 = redisOpsUtil.get(RedisKeyUtil.getFullKey("user", "name", userDO.getName()));
            if (!userDOStr.equals(s1)) {
                sb.append("<br>redis同步fail(user_guid):" + userDO.getUserGuid() + "-" + userDO.getName());
            }
            if (!userDOStr.equals(s2)) {
                sb.append("<br>redis同步fail(ip):" + userDO.getUserGuid() + "-" + userDO.getName());
            }
            if (!userDOStr.equals(s3)) {
                sb.append("<br>redis同步fail(name):" + userDO.getUserGuid() + "-" + userDO.getName());
            }
        }

        sb.append("群的同步---------------");
        for (RucGroupDO groupDO : groupDOS) {
            // 存储redis
            String groupDOStr = JSONObject.toJSONString(groupDO);
            String s = redisOpsUtil.get(RedisKeyUtil.getFullKey("group", "group_guid", groupDO.getGroupGuid()));
            if (!groupDOStr.equals(s)) {
                sb.append("<br>redis同步fail(group_guid):" + groupDO.getGroupGuid() + "-" + groupDO.getName());
            }
        }

        sb.append("群消息的同步---------------");
        for (RucGroupMessageDO groupMessageDO : groupMessageDOS) {

        }

        return sb.toString();
    }


}
