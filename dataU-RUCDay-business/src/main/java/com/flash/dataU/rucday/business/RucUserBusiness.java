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
import com.flash.dataU.rucday.utils.UserIndexTransferUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        List<IndexResponseBO> indexResponseBOS = login(userDO.getUserGuid());
        // 封装信息
        userRegisterResponseBO.setIndexResponseBOS(indexResponseBOS);
        userRegisterResponseBO.setUserDO(userDO);
        return userRegisterResponseBO;
    }

    /**
     * 老用户登录
     */
    public List<IndexResponseBO> login(String userGuid){
        RucUserDO userDO = rucUserService.findByUserGuid(userGuid);
        List<IndexResponseBO> indexResponseBOs = new ArrayList<IndexResponseBO>();
        // 查询出所有群聊框
        List<RucGroupDO> groupDOS = rucGroupService.findAll();
        // 排个序
        Collections.sort(groupDOS, new Comparator<RucGroupDO>() {
            public int compare(RucGroupDO o1, RucGroupDO o2) {
                return (int) (o1.getGroupId() - o2.getGroupId());
            }
        });
        if (groupDOS == null || groupDOS.size() == 0) {
            return indexResponseBOs;
        }

        packageIndexResponseBOs(indexResponseBOs, userDO, groupDOS);

        return indexResponseBOs;
    }


    /**
     * 刷新主页
     */
    public List<IndexResponseBO> refreshIndex(RucUserDO userDO, List<IndexResponseBO> indexResponseBOs){

        RucUserDO dataUserDO = rucUserService.findByUserGuid(userDO.getUserGuid());
        userDO = dataUserDO;

        // 查询从数据库获取的首页数据
        List<IndexResponseBO> dataIndexResponseBOs = new ArrayList<IndexResponseBO>();
        List<RucGroupDO> groupDOS = rucGroupService.findAll();
        if (groupDOS == null || groupDOS.size() == 0) {
            return dataIndexResponseBOs;
        }
        packageIndexResponseBOs(dataIndexResponseBOs, userDO, groupDOS);

        // 数据库中的和现有的进行比较
        if (!indexResponseBOs.equals(dataIndexResponseBOs)) {
            //不相等说明有变动
            indexResponseBOs = dataIndexResponseBOs;
            return indexResponseBOs;
        }

        return new ArrayList<IndexResponseBO>();

    }

    /**
     * 封装首页参数
     */
    private void packageIndexResponseBOs(List<IndexResponseBO> indexResponseBOs, RucUserDO userDO, List<RucGroupDO> groupDOs) {

        // 取出用户最后一条阅读消息的索引
        String lastReadMsgIndexStr = userDO.getLastReadMsgIndex();
        Map<String, Integer> lastReadMsgIndexMap = UserIndexTransferUtils.parseUserLastReadMsgIndex(lastReadMsgIndexStr);

        // 遍历群聊框
        for (RucGroupDO groupDO:groupDOs) {
            IndexResponseBO indexResponseBO = new IndexResponseBO();
            // 封装group对象
            indexResponseBO.setGroupDO(groupDO);
            // 封装总条数信息
            long totalMsg = rucGroupMessageService.countAll(groupDO.getGroupGuid());
            indexResponseBO.setTotalMsg((int)totalMsg);
            // 封装未读条数信息
            int lastReadMsgIndex = lastReadMsgIndexMap.get(groupDO.getGroupGuid());
            int readMsg = lastReadMsgIndex + 1;
            indexResponseBO.setUnreadMsg((int)(totalMsg - readMsg));
            // 封装最后一条消息信息
            long lastMsgIndex = totalMsg - 1;
            RucGroupMessageDO lastGroupMessageDO = rucGroupMessageService.findByIndex(groupDO.getGroupGuid(), lastMsgIndex);
            indexResponseBO.setLastGroupMessageDO(lastGroupMessageDO);

            //添加至
            indexResponseBOs.add(indexResponseBO);
        }
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
            lastReadMsgIndexs[i] = rucGroupDOS.get(i).getGroupGuid() + ":" + "-1";
        }
        String lastReadMsgIndexStr = String.join(",", lastReadMsgIndexs);
        userDO.setLastReadMsgIndex(lastReadMsgIndexStr);

        return userDO;
    }

}
