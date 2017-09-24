package com.flash.dataU.rucday.entity;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/9/23.
 */
@Entity
@Table(name = "ruc_user", schema = "ruc_day", catalog = "")
public class RucUserDO {
    private long userId;
    private String userGuid;
    private String ip;
    private String name;
    private Integer sex;
    private String icon;
    private String lastReadMsgIndex;
    private Integer createTime;

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

    @Basic
    @Column(name = "create_time")
    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "RucUserDO{" +
                "userId=" + userId +
                ", userGuid='" + userGuid + '\'' +
                ", ip='" + ip + '\'' +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", icon='" + icon + '\'' +
                ", lastReadMsgIndex='" + lastReadMsgIndex + '\'' +
                ", createTime=" + createTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RucUserDO userDO = (RucUserDO) o;

        if (userId != userDO.userId) return false;
        if (userGuid != null ? !userGuid.equals(userDO.userGuid) : userDO.userGuid != null) return false;
        if (ip != null ? !ip.equals(userDO.ip) : userDO.ip != null) return false;
        if (name != null ? !name.equals(userDO.name) : userDO.name != null) return false;
        if (sex != null ? !sex.equals(userDO.sex) : userDO.sex != null) return false;
        if (icon != null ? !icon.equals(userDO.icon) : userDO.icon != null) return false;
        if (lastReadMsgIndex != null ? !lastReadMsgIndex.equals(userDO.lastReadMsgIndex) : userDO.lastReadMsgIndex != null)
            return false;
        return createTime != null ? createTime.equals(userDO.createTime) : userDO.createTime == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (userGuid != null ? userGuid.hashCode() : 0);
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (lastReadMsgIndex != null ? lastReadMsgIndex.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        return result;
    }
}
