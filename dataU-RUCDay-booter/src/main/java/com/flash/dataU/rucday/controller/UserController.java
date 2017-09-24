package com.flash.dataU.rucday.controller;

import com.flash.dataU.rucday.bo.IndexResponseBO;
import com.flash.dataU.rucday.business.RucUserBusiness;
import com.flash.dataU.rucday.entity.RucUserDO;
import com.flash.dataU.rucday.service.RucUserService;
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
        // 获取用户ip
        String ip = request.getRemoteAddr();
        RucUserDO userDo = rucUserService.findByIp(ip);
        // 未注册用户跳转到注册页面
        if (null == userDo) {
            return "register";
        }
        //已注册用户跳转到登录页面
        request.setAttribute("user", userDo);
        return "login";
    }

    /**
     * 用户注册并登陆
     */
    @RequestMapping("register")
    public String register(HttpServletRequest request, String name, Integer sex, String icon, int type) {
        String ip = request.getRemoteAddr();
        List<IndexResponseBO> indexResponseBOS = rucUserBusiness.register(ip, name, sex, icon, type);
        request.getSession().setAttribute("indexGroupInfo", indexResponseBOS);
        return "index";
    }


}
