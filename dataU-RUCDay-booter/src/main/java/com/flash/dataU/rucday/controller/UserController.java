package com.flash.dataU.rucday.controller;

import com.flash.dataU.rucday.bo.IndexResponseBO;
import com.flash.dataU.rucday.bo.UserRegisterResponseBO;
import com.flash.dataU.rucday.business.RucUserBusiness;
import com.flash.dataU.rucday.entity.RucUserDO;
import com.flash.dataU.rucday.service.RucUserService;
import com.flash.dataU.rucday.util.CookieUtils;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.web.bind.annotation.ResponseBody;

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
        request.getSession().setAttribute("userinfo", rucUserService.findByUserGuid(userGuid));
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
    public List<IndexResponseBO> refreshIndexGroup(HttpServletRequest request) throws InterruptedException {
        Thread.sleep(500L);
        long start = System.currentTimeMillis();

        while (true) {
            // 尝试获取最新数据
            RucUserDO userDO = (RucUserDO)request.getSession().getAttribute("userinfo");
            List<IndexResponseBO> indexResponseBOS = (List<IndexResponseBO>)request.getSession().getAttribute("indexGroupInfos");
            indexResponseBOS = rucUserBusiness.refreshIndex(userDO, indexResponseBOS);
            if (indexResponseBOS != null && indexResponseBOS.size()!=0) {
                // 有数据更新
                request.getSession().setAttribute("indexGroupInfos", indexResponseBOS);
                request.getSession().setAttribute("userinfo", userDO);
                return indexResponseBOS;
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
