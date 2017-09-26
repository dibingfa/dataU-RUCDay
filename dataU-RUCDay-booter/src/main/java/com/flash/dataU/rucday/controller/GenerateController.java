package com.flash.dataU.rucday.controller;

import com.flash.dataU.rucday.entity.RucGroupDO;
import com.flash.dataU.rucday.entity.RucGroupMessageDO;
import com.flash.dataU.rucday.entity.RucUserDO;
import com.flash.dataU.rucday.service.RucGroupMessageService;
import com.flash.dataU.rucday.service.RucGroupService;
import com.flash.dataU.rucday.service.RucUserService;
import com.flash.dataU.rucday.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /**
     * 添加群
     */
    @RequestMapping("group")
    public String group(String name) {
        RucGroupDO groupDO = new RucGroupDO();
        groupDO.setGroupGuid(RandomUtils.generateUUID());
        groupDO.setName(name);
        groupDO.setIcon("12");
        groupDO.setIntro("anything");
        rucGroupService.save(groupDO);
        return "success";
    }

    /**
     * 添加群聊消息
     */
    @RequestMapping("groupMsg")
    public String groupMsg(String from, String to) {
        List<RucGroupDO> rucGroupDOS = rucGroupService.findAll();
        List<RucUserDO> rucUserDOs = rucUserService.findAll();
        for (RucUserDO userDO:rucUserDOs) {
            for (RucGroupDO groupDO:rucGroupDOS) {
                RucGroupMessageDO groupMessageDO = new RucGroupMessageDO();
                groupMessageDO.setContent("你好啊小屁孩"+RandomUtils.generateUUID().substring(1,5));
                groupMessageDO.setFromGuid(userDO.getUserGuid());
                groupMessageDO.setToGuid(groupDO.getGroupGuid());
                rucGroupMessageService.save(groupMessageDO);
            }
        }
        return "success";
    }

}
