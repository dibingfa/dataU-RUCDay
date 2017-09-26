package com.flash.dataU.rucday.business;

import com.flash.dataU.rucday.entity.RucGroupMessageDO;
import com.flash.dataU.rucday.entity.RucUserDO;
import com.flash.dataU.rucday.service.RucGroupMessageService;
import com.flash.dataU.rucday.service.RucGroupService;
import com.flash.dataU.rucday.service.RucUserService;
import com.flash.dataU.rucday.utils.UserIndexTransferUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/26.
 */
@Service
public class RucGroupBusiness {

    @Autowired
    private RucUserService rucUserService;

    @Autowired
    private RucGroupService rucGroupService;

    @Autowired
    private RucGroupMessageService rucGroupMessageService;

    /**
     * 进入群聊
     */
    public List<RucGroupMessageDO> enterGroup(String userGuid, String groupGuid){
        // 查询出所有属于该群的消息
        List<RucGroupMessageDO> groupMessageDOS = rucGroupMessageService.findAll(groupGuid);
        // 更新用户最后阅读消息索引
        RucUserDO userDO = rucUserService.findByUserGuid(userGuid);
        refreshLastReadMsgIndex(userDO, groupGuid);

        return groupMessageDOS;
    }

    /**
     * 更新用户最后阅读消息索引
     */
    private void refreshLastReadMsgIndex(RucUserDO userDO, String groupGuid){
        String lastReadMsgIndexStr = userDO.getLastReadMsgIndex();
        Map<String, Integer> lastReadMsgIndexMap = UserIndexTransferUtils.parseUserLastReadMsgIndex(lastReadMsgIndexStr);
        lastReadMsgIndexMap.put(groupGuid, (int) rucGroupMessageService.countAll(groupGuid) - 1);
        lastReadMsgIndexStr = UserIndexTransferUtils.generateUserLastReadMsgIndex(lastReadMsgIndexMap);
        userDO.setLastReadMsgIndex(lastReadMsgIndexStr);
        rucUserService.save(userDO);
    }

}
