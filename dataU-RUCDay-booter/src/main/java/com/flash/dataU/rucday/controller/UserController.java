package com.flash.dataU.rucday.controller;

import com.alibaba.fastjson.JSONObject;
import com.flash.dataU.rucday.bo.IndexResponseBO;
import com.flash.dataU.rucday.bo.UserRegisterResponseBO;
import com.flash.dataU.rucday.business.RucUserBusiness;
import com.flash.dataU.rucday.entity.RucUserDO;
import com.flash.dataU.rucday.service.RucUserService;
import com.flash.dataU.rucday.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/23.
 */

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private RucUserBusiness rucUserBusiness;

    @Autowired
    private RucUserService rucUserService;

    /**
     * 跳转到入侵页面
     */
    @RequestMapping("break")
    public String leadToBreak(HttpServletRequest request) {
        // 检查是否带有用户信息的cookie
        String userGuid = CookieUtils.getCookie(request, CookieUtils.USER_COOKIE);
        // 未注册用户跳转到注册页面
        if (null == userGuid) {
            return "register";
        }
        //已注册用户跳转到登录页面
        RucUserDO userDO = rucUserService.findByUserGuid(userGuid);
        if (userDO == null) {
            //此时如果cookie的值在数据库中找不到，则请用户重新注册
            return "register";
        }
        request.getSession().setAttribute("userinfo", userDO);
        return "login";
    }

    /**
     * 用户注册并登陆
     */
    @RequestMapping("register")
    public String register(HttpServletRequest request, HttpServletResponse response,
        String name, Integer sex, String icon, int type) {
        // 注册存储用户
        String ip = request.getRemoteHost();
        List<IndexResponseBO> indexResponseBOS = null;
        RucUserDO userDO = null;
        if (type == 1) {
            // 新用户注册
            UserRegisterResponseBO userRegisterResponseBO = rucUserBusiness.register(ip, name, sex, icon);
            userDO = userRegisterResponseBO.getUserDO();
            indexResponseBOS = userRegisterResponseBO.getIndexResponseBOS();
            // 新用户需将信息放入cookie
            CookieUtils.setCookie(response, CookieUtils.USER_COOKIE, userDO.getUserGuid());
            request.getSession().setAttribute("userinfo", userDO);
        } else {
            // 老用户从cookie中获取
            String userGuid = CookieUtils.getCookie(request, CookieUtils.USER_COOKIE);
            indexResponseBOS = rucUserBusiness.login(userGuid);
            request.getSession().setAttribute("userinfo", rucUserService.findByUserGuid(userGuid));
        }
        // 页面信息放入session
        request.getSession().setAttribute("indexGroupInfos", indexResponseBOS);

        return "index";
    }


    /**
     * 刷新首页群聊框
     */
    @ResponseBody
    @RequestMapping("refreshIndexGroup")
    public List<IndexResponseBO> refreshIndexGroup(HttpServletRequest request, String indexGroupInfos) throws InterruptedException {
        long start = System.currentTimeMillis();

        while (true) {
            // 前端的老数据
            List<IndexResponseBO> indexGroupInfosFromWeb = JSONObject.parseArray(indexGroupInfos, IndexResponseBO.class);
            RucUserDO userDO = (RucUserDO)request.getSession().getAttribute("userinfo");

            List<IndexResponseBO> indexResponseBOSFromData = rucUserBusiness.refreshIndex(userDO, indexGroupInfosFromWeb);
            if (indexResponseBOSFromData != null && indexResponseBOSFromData.size()!=0) {
                // 有数据更新
                request.getSession().setAttribute("indexGroupInfos", indexResponseBOSFromData);
                request.getSession().setAttribute("userinfo", userDO);
                return indexResponseBOSFromData;
            }

            Thread.sleep(1000L);
            // 4.5秒就结束掉
            long now = System.currentTimeMillis();

            if (now - start > 7000) {
                return new ArrayList<IndexResponseBO>();
            }
        }
    }



}
