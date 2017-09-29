package com.flash.dataU.rucday.bo;

import com.alibaba.fastjson.JSONObject;
import com.flash.dataU.rucday.entity.RucGroupDO;
import com.flash.dataU.rucday.entity.RucGroupMessageDO;

/**
 * Created by Administrator on 2017/9/24.
 */
public class IndexResponseBO {

    private RucGroupDO groupDO;
    private Integer totalMsg;
    private Integer unreadMsg;
    private RucGroupMessageDO lastGroupMessageDO;

    public RucGroupDO getGroupDO() {
        return groupDO;
    }

    public void setGroupDO(RucGroupDO groupDO) {
        this.groupDO = groupDO;
    }

    public Integer getTotalMsg() {
        return totalMsg;
    }

    public void setTotalMsg(Integer totalMsg) {
        this.totalMsg = totalMsg;
    }

    public Integer getUnreadMsg() {
        return unreadMsg;
    }

    public void setUnreadMsg(Integer unreadMsg) {
        this.unreadMsg = unreadMsg;
    }

    public RucGroupMessageDO getLastGroupMessageDO() {
        return lastGroupMessageDO;
    }

    public void setLastGroupMessageDO(RucGroupMessageDO lastGroupMessageDO) {
        this.lastGroupMessageDO = lastGroupMessageDO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        IndexResponseBO bo = (IndexResponseBO)o;

        if (groupDO != null ? !groupDO.equals(bo.groupDO) : bo.groupDO != null)
            return false;
        if (totalMsg != null ? !totalMsg.equals(bo.totalMsg) : bo.totalMsg != null)
            return false;
        if (unreadMsg != null ? !unreadMsg.equals(bo.unreadMsg) : bo.unreadMsg != null)
            return false;
        return lastGroupMessageDO != null ? lastGroupMessageDO.equals(bo.lastGroupMessageDO) : bo.lastGroupMessageDO == null;
    }

    @Override
    public int hashCode() {
        int result = groupDO != null ? groupDO.hashCode() : 0;
        result = 31 * result + (totalMsg != null ? totalMsg.hashCode() : 0);
        result = 31 * result + (unreadMsg != null ? unreadMsg.hashCode() : 0);
        result = 31 * result + (lastGroupMessageDO != null ? lastGroupMessageDO.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
