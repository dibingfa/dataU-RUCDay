package com.flash.dataU.rucday.controller;

import com.flash.dataU.rucday.bo.GroupDetailResponseBO;
import com.flash.dataU.rucday.bo.IndexResponseBO;
import com.flash.dataU.rucday.business.RucGroupBusiness;
import com.flash.dataU.rucday.entity.RucGroupDO;
import com.flash.dataU.rucday.entity.RucUserDO;
import com.flash.dataU.rucday.service.RucGroupService;
import com.flash.dataU.rucday.util.CookieUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/9/26.
 */
@Controller
@RequestMapping("group")
public class GroupController {

    @Autowired
    private RucGroupBusiness rucGroupBusiness;

    @Autowired
    private RucGroupService rucGroupService;

    @RequestMapping("{groupGuid}")
    public String enterGroup(@PathVariable("groupGuid") String groupGuid, HttpServletRequest request, HttpServletResponse response) {
        // 从cookie中获取userdo对象
        String userGuid = CookieUtils.getCookie(request, CookieUtils.USER_COOKIE);
        if (StringUtils.isEmpty(userGuid)) {
            return "register";
        }
        // 调用业务层
        GroupDetailResponseBO groupDetailResponseBO = rucGroupBusiness.enterGroup(userGuid, groupGuid);
        // 封装进request域对象
        request.setAttribute("groupDetailResponseBO", groupDetailResponseBO);
        RucGroupDO groupDO = rucGroupService.findByGroupGuid(groupGuid);
        request.setAttribute("groupDO", groupDO);
        return "group";
    }

    @ResponseBody
    @RequestMapping("sendMsg")
    public String sendMsg(HttpServletRequest request, String content, String userGuid, String groupGuid) {
        RucUserDO userDO = rucGroupBusiness.sendMsg(content, userGuid, groupGuid);
        // 更新session
        request.getSession().setAttribute("userinfo", userDO);
        return "success";
    }

    /**
     * 刷新某一群聊里的消息
     */
    @ResponseBody
    @RequestMapping("refreshGroupMsg")
    public GroupDetailResponseBO refreshGroupMsg(String groupGuid, String userGuid, int index) throws InterruptedException {
        Thread.sleep(500L);
        long start = System.currentTimeMillis();
        while (true) {
            // 尝试获取最新数据
            GroupDetailResponseBO groupDetailResponseBO = rucGroupBusiness.refreshGroupMsg(groupGuid, userGuid, index);
            if (groupDetailResponseBO!=null&&groupDetailResponseBO.getIndex()!=-1) {
                // index返回值不为-1说明有更新消息
                return groupDetailResponseBO;
            }
            Thread.sleep(1000L);
            // 4.5秒就结束掉
            long now = System.currentTimeMillis();
            if (now - start > 7000) {
                GroupDetailResponseBO responseBO = new GroupDetailResponseBO();
                responseBO.setIndex(-1);
                return responseBO;
            }
        }

    }
}
