package com.flash.dataU.rucday.controller;

import com.alibaba.fastjson.JSONObject;
import com.flash.dataU.rucday.bo.IndexResponseBO;
import com.flash.dataU.rucday.bo.UserRegisterResponseBO;
import com.flash.dataU.rucday.business.RucUserBusiness;
import com.flash.dataU.rucday.entity.RucUserDO;
import com.flash.dataU.rucday.service.RucUserService;
import com.flash.dataU.rucday.util.CookieUtils;
import java.util.ArrayList;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Administrator on 2017/9/23.
 */

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private RucUserService rucUserService;

    @Autowired
    private RucUserBusiness rucUserBusiness;

    /**
     * 跳转到入侵页面
     */
    @RequestMapping("break")
    public String leadToBreak(HttpServletRequest request) {
        // 检查是否带有用户信息的cookie
        String userDOStr = CookieUtils.getCookie(request, CookieUtils.USER_COOKIE);
        // 未注册用户跳转到注册页面
        if (null == userDOStr) {
            return "register";
        }
        //已注册用户跳转到登录页面
        request.setAttribute("userinfo", JSONObject.parseObject(userDOStr, RucUserDO.class));
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
            String userDOStr = JSONObject.toJSONString(userDO);
            CookieUtils.setCookie(response, CookieUtils.USER_COOKIE, userDOStr);
        } else {
            // 老用户从cookie中获取
            String userDOStr = CookieUtils.getCookie(request, CookieUtils.USER_COOKIE);
            userDO = JSONObject.parseObject(userDOStr, RucUserDO.class);
            indexResponseBOS = rucUserBusiness.login(userDO);
        }
        // 页面信息放入session
        request.getSession().setAttribute("indexGroupInfos", indexResponseBOS);

        return "index";
    }



}
