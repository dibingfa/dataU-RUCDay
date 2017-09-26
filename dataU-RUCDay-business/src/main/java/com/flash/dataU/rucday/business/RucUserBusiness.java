package com.flash.dataU.rucday.business;

import com.flash.dataU.rucday.bo.IndexResponseBO;
import com.flash.dataU.rucday.bo.UserRegisterResponseBO;
import com.flash.dataU.rucday.entity.RucGroupDO;
import com.flash.dataU.rucday.entity.RucGroupMessageDO;
import com.flash.dataU.rucday.entity.RucUserDO;
import com.flash.dataU.rucday.service.RucGroupMessageService;
import com.flash.dataU.rucday.service.RucGroupService;
import com.flash.dataU.rucday.service.RucUserService;
import com.flash.dataU.rucday.utils.RandomUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.util.StringUtils;

/**
 * Created by Administrator on 2017/9/24.
 */
@Service
public class RucUserBusiness {

    @Autowired
    private RucUserService rucUserService;

    @Autowired
    private RucGroupService rucGroupService;

    @Autowired
    private RucGroupMessageService rucGroupMessageService;

    /**
     * 新用户注册
     */
    public UserRegisterResponseBO register(String ip, String name, int sex, String icon){
        UserRegisterResponseBO userRegisterResponseBO = new UserRegisterResponseBO();
        // 存储用户
        RucUserDO userDO = createUser(ip, name, sex, icon);
        userDO = rucUserService.save(userDO);
        // 注册完毕，直接登录
        List<IndexResponseBO> indexResponseBOS = login(userDO);
        // 封装信息
        userRegisterResponseBO.setIndexResponseBOS(indexResponseBOS);
        userRegisterResponseBO.setUserDO(userDO);
        return userRegisterResponseBO;
    }

    /**
     * 老用户登录
     */
    public List<IndexResponseBO> login(RucUserDO userDO){
        List<IndexResponseBO> indexResponseBOs = new ArrayList<IndexResponseBO>();
        // 查询出所有群聊框
        List<RucGroupDO> groupDOS = rucGroupService.findAll();
        if (groupDOS == null || groupDOS.size() == 0) {
            return indexResponseBOs;
        }

        // 取出用户最后一条阅读消息的索引
        String lastReadMsgIndexStr = userDO.getLastReadMsgIndex();
        Map<String, Integer> lastReadMsgIndexMap = parseUserLastReadMsgIndex(lastReadMsgIndexStr);

        // 遍历群聊框
        for (RucGroupDO groupDO:groupDOS) {
            IndexResponseBO indexResponseBO = new IndexResponseBO();
            // 封装group对象
            indexResponseBO.setGroupDO(groupDO);
            // 封装总条数信息
            long totalMsg = rucGroupMessageService.countAll(groupDO.getGroupGuid());
            indexResponseBO.setTotalMsg((int)totalMsg);
            // 封装未读条数信息
            int index = lastReadMsgIndexMap.get(groupDO.getGroupGuid());
            indexResponseBO.setUnreadMsg((int)(totalMsg - index));
            // 封装最后一条消息信息
            RucGroupMessageDO lastGroupMessageDO = rucGroupMessageService.findByIndex(groupDO.getGroupGuid(), totalMsg);
            indexResponseBO.setLastGroupMessageDO(lastGroupMessageDO);

            //添加至
            indexResponseBOs.add(indexResponseBO);
        }

        return indexResponseBOs;
    }

    /**
     * 新用户生成
     */
    private RucUserDO createUser(String ip, String name, int sex, String icon){
        RucUserDO userDO = new RucUserDO();
        // 创建用户注册传入的信息
        userDO.setIp(RandomUtils.generateUUID());
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

    /**
     * 解析用户最后一条阅读消息的拼接字符串
     */
    private Map<String, Integer> parseUserLastReadMsgIndex(String lastReadMsgIndexStr) {
        if (StringUtils.isEmpty(lastReadMsgIndexStr)) {
            return new HashMap<String, Integer>(0);
        }
        String[] groupAndIndexs = lastReadMsgIndexStr.split(",");
        Map<String, Integer> lastReadMsgIndexMap = new HashMap<String, Integer>(groupAndIndexs.length);
        for (String groupAndIndex:groupAndIndexs) {
            String[] split = groupAndIndex.split(":");
            String groupGuid = split[0];
            int index = Integer.parseInt(split[1]);
            lastReadMsgIndexMap.put(groupGuid, index);
        }
        return lastReadMsgIndexMap;
    }

}
