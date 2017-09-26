package com.flash.dataU.rucday.vo.responsevo;

import com.flash.dataU.rucday.entity.RucGroupDO;
import com.flash.dataU.rucday.entity.RucGroupMessageDO;

/**
 * Created by Administrator on 2017/9/24.
 */
public class IndexResponseVO {

    private RucGroupDO groupDO;
    private Integer totalMsg;
    private Integer unreadMsg;
    private RucGroupMessageDO groupMessageDO;

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

    public RucGroupMessageDO getGroupMessageDO() {
        return groupMessageDO;
    }

    public void setGroupMessageDO(RucGroupMessageDO groupMessageDO) {
        this.groupMessageDO = groupMessageDO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        IndexResponseVO bo = (IndexResponseVO)o;

        if (groupDO != null ? !groupDO.equals(bo.groupDO) : bo.groupDO != null)
            return false;
        if (totalMsg != null ? !totalMsg.equals(bo.totalMsg) : bo.totalMsg != null)
            return false;
        if (unreadMsg != null ? !unreadMsg.equals(bo.unreadMsg) : bo.unreadMsg != null)
            return false;
        return groupMessageDO != null ? groupMessageDO.equals(bo.groupMessageDO) : bo.groupMessageDO == null;
    }

    @Override
    public int hashCode() {
        int result = groupDO != null ? groupDO.hashCode() : 0;
        result = 31 * result + (totalMsg != null ? totalMsg.hashCode() : 0);
        result = 31 * result + (unreadMsg != null ? unreadMsg.hashCode() : 0);
        result = 31 * result + (groupMessageDO != null ? groupMessageDO.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IndexResponseVO{" + "groupDO=" + groupDO + ", totalMsg=" + totalMsg + ", unreadMsg=" + unreadMsg + ", groupMessageDO=" + groupMessageDO + '}';
    }
}
