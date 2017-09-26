package com.flash.dataU.rucday.controller;

import com.flash.dataU.rucday.business.RucGroupBusiness;
import com.flash.dataU.rucday.entity.RucGroupDO;
import com.flash.dataU.rucday.entity.RucGroupMessageDO;
import com.flash.dataU.rucday.service.RucGroupService;
import com.flash.dataU.rucday.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
        List<RucGroupMessageDO> rucGroupMessageDOS = rucGroupBusiness.enterGroup(userGuid, groupGuid);
        // 封装进request域对象
        request.setAttribute("groupMessages", rucGroupMessageDOS);
        RucGroupDO groupDO = rucGroupService.findByGroupGuid(groupGuid);
        request.setAttribute("groupName", groupDO == null ? "未知群" : groupDO.getName());
        return "group";
    }

}
