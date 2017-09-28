package com.flash.dataU.rucday.bo;

import com.flash.dataU.rucday.entity.RucGroupMessageDO;
import com.flash.dataU.rucday.entity.RucUserDO;

/**
 * .
 *
 * @author sunyiming (sunyiming170619@credithc.com)
 * @version 0.0.1-SNAPSHOT
 * @since 2017年09月27日 10时33分
 */
public class GroupMessageDetailResponseBO {

    private RucGroupMessageDO groupMessageDO;
    private RucUserDO userDO;

    @Override
    public String toString() {
        return "GroupMessageDetailResponseBO{" + "groupMessageDO=" + groupMessageDO + ", userDO=" + userDO + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        GroupMessageDetailResponseBO that = (GroupMessageDetailResponseBO)o;

        if (groupMessageDO != null ? !groupMessageDO.equals(that.groupMessageDO) : that.groupMessageDO != null)
            return false;
        return userDO != null ? userDO.equals(that.userDO) : that.userDO == null;
    }

    @Override
    public int hashCode() {
        int result = groupMessageDO != null ? groupMessageDO.hashCode() : 0;
        result = 31 * result + (userDO != null ? userDO.hashCode() : 0);
        return result;
    }

    public RucGroupMessageDO getGroupMessageDO() {

        return groupMessageDO;
    }

    public void setGroupMessageDO(RucGroupMessageDO groupMessageDO) {
        this.groupMessageDO = groupMessageDO;
    }

    public RucUserDO getUserDO() {
        return userDO;
    }

    public void setUserDO(RucUserDO userDO) {
        this.userDO = userDO;
    }
}
