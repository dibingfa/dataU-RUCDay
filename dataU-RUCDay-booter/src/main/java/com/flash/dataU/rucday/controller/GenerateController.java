package com.flash.dataU.rucday.controller;

import com.flash.dataU.rucday.entity.RucGroupDO;
import com.flash.dataU.rucday.entity.RucGroupMessageDO;
import com.flash.dataU.rucday.entity.RucUserDO;
import com.flash.dataU.rucday.redis.RedisOpsUtil;
import com.flash.dataU.rucday.repository.RucGroupMessageRepository;
import com.flash.dataU.rucday.repository.RucGroupRepository;
import com.flash.dataU.rucday.repository.RucUserRepository;
import com.flash.dataU.rucday.service.RucGroupMessageService;
import com.flash.dataU.rucday.service.RucGroupService;
import com.flash.dataU.rucday.service.RucUserService;
import com.flash.dataU.rucday.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

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

    private Jedis jedis = new Jedis("localhost",6379);

    private String[] users = {"张三", "李四", "王二麻子"};
    private String[] groups = {"人大教师群", "人大建筑群", "人大学生组织群"};
    private String[] groupMsgs = {"哈哈又到我发言了", "恩恩", "楼上的什么意思", "我觉得他们不怎么样", "哪有瞎扯",
            "我就来看看", "我就问还有谁！", "楼上的一二级的吧？"};


    /**
     * 添加群
     */
    @RequestMapping("group")
    public String group() {
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (String groupName : groups) {
            RucGroupDO groupDO = new RucGroupDO();
            groupDO.setGroupGuid(RandomUtils.generateUUID());
            groupDO.setName(groupName);
            groupDO.setIcon("12");
            groupDO.setIntro("anything");
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
        for (String userName : users) {
            RucUserDO userDO = new RucUserDO();
            userDO.setName(userName);
            userDO.setIp(RandomUtils.generateUUID());
            userDO.setUserGuid(RandomUtils.generateUUID());
            userDO.setIcon("3");
            userDO.setSex(1);
            // 初始化最后一条未读消息记录
            List<RucGroupDO> rucGroupDOS = rucGroupService.findAll();
            String[] lastReadMsgIndexs = new String[rucGroupDOS.size()];
            for (int i = 0; i < lastReadMsgIndexs.length; i++) {
                lastReadMsgIndexs[i] = rucGroupDOS.get(i).getGroupGuid() + ":" + "-1";
            }
            String lastReadMsgIndexStr = String.join(",", lastReadMsgIndexs);
            userDO.setLastReadMsgIndex(lastReadMsgIndexStr);
            // 存储操作
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

        while (count<13){

            for (RucUserDO userDO : rucUserDOs) {
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
            }
        }

        return sb.toString();
    }

    /**
     * 添加群聊消息
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
        if (keys == null || keys.size()==0) {
            return "success";
        }
        for (String key:keys) {
            redisOpsUtil.delete(key);
        }

        keys = redisOpsUtil.getKeys("*");
        if (keys != null && keys.size() != 0) {
            return "redis未删除干净" + Arrays.toString(keys.toArray());
        }

        return "success";
    }


}
