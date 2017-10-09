package com.flash.dataU.rucday.business;

import com.flash.dataU.rucday.bo.GroupDetailResponseBO;
import com.flash.dataU.rucday.bo.GroupMessageDetailResponseBO;
import com.flash.dataU.rucday.entity.RucGroupMessageDO;
import com.flash.dataU.rucday.entity.RucUserDO;
import com.flash.dataU.rucday.service.RucGroupMessageService;
import com.flash.dataU.rucday.service.RucGroupService;
import com.flash.dataU.rucday.service.RucUserService;
import com.flash.dataU.rucday.utils.UserIndexTransferUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/26.
 */
@Service
public class RucGroupBusiness {

    @Autowired private RucUserService rucUserService;

    @Autowired private RucGroupService rucGroupService;

    @Autowired private RucGroupMessageService rucGroupMessageService;

    private static final int MAX_MGS_NUM = 500;

    /**
     * 进入群聊
     */
    public GroupDetailResponseBO enterGroup(String userGuid, String groupGuid) {
        // 查询出所有属于该群的消息
        List<RucGroupMessageDO> groupMessageDOS = rucGroupMessageService.findAll(groupGuid, MAX_MGS_NUM);
        int index = groupMessageDOS.size() - 1;
        // 封装进返回对象
        GroupDetailResponseBO groupDetailResponseBO = new GroupDetailResponseBO();
        List<GroupMessageDetailResponseBO> groupMessageDetailResponseBOS = new ArrayList<GroupMessageDetailResponseBO>(groupMessageDOS.size());
        for (RucGroupMessageDO groupMessageDO : groupMessageDOS) {
            GroupMessageDetailResponseBO groupMessageDetailResponseBO = new GroupMessageDetailResponseBO();
            groupMessageDetailResponseBO.setGroupMessageDO(groupMessageDO);
            groupMessageDetailResponseBO.setUserDO(rucUserService.findByUserGuid(groupMessageDO.getFromGuid()));
            groupMessageDetailResponseBOS.add(groupMessageDetailResponseBO);
        }
        groupDetailResponseBO.setGroupMessageDetailResponseBOS(groupMessageDetailResponseBOS);
        groupDetailResponseBO.setIndex(index);
        // 更新用户最后阅读消息索引
        RucUserDO userDO = rucUserService.findByUserGuid(userGuid);
        refreshLastReadMsgIndex(userDO, groupGuid);

        return groupDetailResponseBO;
    }

    /**
     * 发送信息
     */
    public RucUserDO sendMsg(String content, String userGuid, String groupGuid) {
        // 存储消息
        RucGroupMessageDO groupMessageDO = createMsg(content, userGuid, groupGuid);
        rucGroupMessageService.save(groupMessageDO);
        // 更新用户阅读记录
        RucUserDO userDO = rucUserService.findByUserGuid(userGuid);
        addLastReadMsgIndex(userDO, groupGuid);
        return userDO;
    }

    /**
     * 刷新消息
     */
    public GroupDetailResponseBO refreshGroupMsg(String groupGuid, String userGuid, int index) {
        GroupDetailResponseBO groupDetailResponseBO = new GroupDetailResponseBO();
        groupDetailResponseBO.setIndex(-1);
        // 比较当前索引和数据库中最后一条的索引大小
        long dataIndex = rucGroupMessageService.countAll(groupGuid)-1;
        if (index >= dataIndex) {
            // 当前索引和数据库相等，没有数据更新
            return groupDetailResponseBO;
        }
        // 有数据更新，判断是否都是自己发的数据
        List<RucGroupMessageDO> groupMessageDOS = rucGroupMessageService.findAll(groupGuid, (int)(dataIndex - index));
        List<GroupMessageDetailResponseBO> groupMessageDetailResponseBOS = new ArrayList<GroupMessageDetailResponseBO>();
        for (RucGroupMessageDO groupMessageDO:groupMessageDOS) {
            String fromGuid = groupMessageDO.getFromGuid();
            RucUserDO fromUserDO = rucUserService.findByUserGuid(fromGuid);
            if (!fromGuid.equals(userGuid)) {
                GroupMessageDetailResponseBO groupMessageDetailResponseBO = new GroupMessageDetailResponseBO();
                groupMessageDetailResponseBO.setUserDO(fromUserDO);
                groupMessageDetailResponseBO.setGroupMessageDO(groupMessageDO);
                groupMessageDetailResponseBOS.add(groupMessageDetailResponseBO);
            }
        }
        // 若都是自己发的数据，则仍然认为无数据更新
        if (groupMessageDetailResponseBOS.size() == 0) {
            return groupDetailResponseBO;
        }

        // 发现数据更新
        groupDetailResponseBO.setGroupMessageDetailResponseBOS(groupMessageDetailResponseBOS);
        groupDetailResponseBO.setIndex((int)dataIndex);
        // 同时更新用户未读消息记录字段
        RucUserDO userDO = rucUserService.findByUserGuid(userGuid);
        refreshLastReadMsgIndex(userDO, groupGuid, (int)dataIndex);

        return groupDetailResponseBO;
    }

    /**
     * 更新用户最后阅读消息索引
     */
    private void refreshLastReadMsgIndex(RucUserDO userDO, String groupGuid) {
        int index = (int)rucGroupMessageService.countAll(groupGuid) - 1;
        refreshLastReadMsgIndex(userDO, groupGuid, index);
    }

    /**
     * 更新用户最后阅读消息索引
     */
    private void refreshLastReadMsgIndex(RucUserDO userDO, String groupGuid, int index) {
        String lastReadMsgIndexStr = userDO.getLastReadMsgIndex();
        Map<String, Integer> lastReadMsgIndexMap = UserIndexTransferUtils.parseUserLastReadMsgIndex(lastReadMsgIndexStr);
        lastReadMsgIndexMap.put(groupGuid, index);
        lastReadMsgIndexStr = UserIndexTransferUtils.generateUserLastReadMsgIndex(lastReadMsgIndexMap);
        userDO.setLastReadMsgIndex(lastReadMsgIndexStr);
        rucUserService.save(userDO);
    }

    /**
     * 更新用户最后阅读消息索引+1
     * 由于用户只发送一条信息，一定是该索引+1，不用完全查库重置
     */
    private void addLastReadMsgIndex(RucUserDO userDO, String groupGuid) {
        String lastReadMsgIndexStr = userDO.getLastReadMsgIndex();
        Map<String, Integer> lastReadMsgIndexMap = UserIndexTransferUtils.parseUserLastReadMsgIndex(lastReadMsgIndexStr);
        lastReadMsgIndexMap.put(groupGuid, lastReadMsgIndexMap.get(groupGuid) + 1);
        lastReadMsgIndexStr = UserIndexTransferUtils.generateUserLastReadMsgIndex(lastReadMsgIndexMap);
        userDO.setLastReadMsgIndex(lastReadMsgIndexStr);
        rucUserService.save(userDO);
    }

    private static String formateMsgTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String formateTime = sdf.format(date);
        return formateTime;
    }

    /**
     * 新消息记录生成
     */
    private RucGroupMessageDO createMsg(String content, String userGuid, String groupGuid) {
        RucGroupMessageDO groupMessageDO = new RucGroupMessageDO();
        groupMessageDO.setContent(content);
        groupMessageDO.setFromGuid(userGuid);
        groupMessageDO.setToGuid(groupGuid);
        return groupMessageDO;
    }

}
