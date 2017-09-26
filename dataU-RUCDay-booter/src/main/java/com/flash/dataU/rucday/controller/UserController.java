package com.flash.dataU.rucday.controller;

import com.flash.dataU.rucday.bo.IndexResponseBO;
import com.flash.dataU.rucday.bo.UserRegisterResponseBO;
import com.flash.dataU.rucday.business.RucUserBusiness;
import com.flash.dataU.rucday.entity.RucUserDO;
import com.flash.dataU.rucday.service.RucUserService;
import com.flash.dataU.rucday.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        request.setAttribute("userinfo", rucUserService.findByUserGuid(userGuid));
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
        } else {
            // 老用户从cookie中获取
            String userGuid = CookieUtils.getCookie(request, CookieUtils.USER_COOKIE);
            indexResponseBOS = rucUserBusiness.login(userGuid);
        }
        // 页面信息放入session
        request.getSession().setAttribute("indexGroupInfos", indexResponseBOS);

        return "index";
    }



}
