package com.flash.dataU.rucday.entity;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/9/23.
 */
@Entity
@Table(name = "ruc_user", schema = "ruc_day", catalog = "")
public class RucUserDO extends BaseDO{
    private long userId;
    private String userGuid;
    private String ip;
    private String name;
    private Integer sex;
    private String icon;
    private String lastReadMsgIndex;

    @Id
    @Column(name = "user_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "user_guid")
    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    @Basic
    @Column(name = "ip")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "sex")
    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Basic
    @Column(name = "last_read_msg_index")
    public String getLastReadMsgIndex() {
        return lastReadMsgIndex;
    }

    public void setLastReadMsgIndex(String lastReadMsg) {
        this.lastReadMsgIndex = lastReadMsg;
    }

    @Override
    public String toString() {
        return "RucUserDO{" + "userId=" + userId + ", userGuid='" + userGuid + '\'' + ", ip='" + ip + '\'' + ", name='" + name + '\'' + ", sex=" + sex + ", icon='" + icon + '\'' + ", lastReadMsgIndex='" + lastReadMsgIndex + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        RucUserDO rucUserDO = (RucUserDO)o;

        if (userId != rucUserDO.userId)
            return false;
        if (userGuid != null ? !userGuid.equals(rucUserDO.userGuid) : rucUserDO.userGuid != null)
            return false;
        if (ip != null ? !ip.equals(rucUserDO.ip) : rucUserDO.ip != null)
            return false;
        if (name != null ? !name.equals(rucUserDO.name) : rucUserDO.name != null)
            return false;
        if (sex != null ? !sex.equals(rucUserDO.sex) : rucUserDO.sex != null)
            return false;
        if (icon != null ? !icon.equals(rucUserDO.icon) : rucUserDO.icon != null)
            return false;
        return lastReadMsgIndex != null ? lastReadMsgIndex.equals(rucUserDO.lastReadMsgIndex) : rucUserDO.lastReadMsgIndex == null;
    }

    @Override
    public int hashCode() {
        int result = (int)(userId ^ (userId >>> 32));
        result = 31 * result + (userGuid != null ? userGuid.hashCode() : 0);
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (lastReadMsgIndex != null ? lastReadMsgIndex.hashCode() : 0);
        return result;
    }
}
