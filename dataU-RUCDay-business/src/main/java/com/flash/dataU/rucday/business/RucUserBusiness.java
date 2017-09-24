package com.flash.dataU.rucday.business;

import com.flash.dataU.rucday.bo.IndexResponseBO;
import com.flash.dataU.rucday.entity.RucGroupDO;
import com.flash.dataU.rucday.entity.RucUserDO;
import com.flash.dataU.rucday.service.RucGroupService;
import com.flash.dataU.rucday.service.RucUserService;
import com.flash.dataU.rucday.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/9/24.
 */
@Service
public class RucUserBusiness {

    @Autowired
    private RucUserService rucUserService;

    @Autowired
    private RucGroupService rucGroupService;

    /**
     * 注册（新老用户统一）
     */
    public List<IndexResponseBO> register(String ip, String name, int sex, String icon, int type){
        RucUserDO userDO;
        if (type == 1) {
            // 新用户存储
            userDO = createUser(ip, name, sex, icon);
            userDO = rucUserService.save(userDO);
        } else {
            // 老用户查询
            userDO = rucUserService.findByIp(ip);
        }

        return null;
    }

    /**
     * 新用户生成
     */
    private RucUserDO createUser(String ip, String name, int sex, String icon){
        RucUserDO userDO = new RucUserDO();
        // 创建用户注册传入的信息
        userDO.setIp(ip);
        userDO.setName(name);
        userDO.setSex(sex);
        userDO.setIcon(icon);
        // 生成唯一索引
        userDO.setUserGuid(RandomUtils.generateUUID());
        // 初始化最后一条未读消息记录
        List<RucGroupDO> rucGroupDOS = rucGroupService.findAll();
        String[] lastReadMsgIndexs = new String[rucGroupDOS.size()];
        for (int i = 0; i < lastReadMsgIndexs.length; i++) {
            lastReadMsgIndexs[i] = rucGroupDOS.get(i).getGroupGuid() + ":" + 0;
        }
        String lastReadMsgIndexStr = String.join(",", lastReadMsgIndexs);
        userDO.setLastReadMsgIndex(lastReadMsgIndexStr);

        return userDO;
    }

}
