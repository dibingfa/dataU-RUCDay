package com.flash.dataU.rucday.entity;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/9/23.
 */
@Entity
@Table(name = "ruc_group_message", schema = "ruc_day", catalog = "")
public class RucGroupMessageDO {
    private long groupMessageId;
    private String contemt;
    private String fromGuid;
    private String toGuid;
    private Integer createTime;

    @Id
    @Column(name = "group_message_id")
    public long getGroupMessageId() {
        return groupMessageId;
    }

    public void setGroupMessageId(long groupMessageId) {
        this.groupMessageId = groupMessageId;
    }

    @Basic
    @Column(name = "contemt")
    public String getContemt() {
        return contemt;
    }

    public void setContemt(String contemt) {
        this.contemt = contemt;
    }

    @Basic
    @Column(name = "from_guid")
    public String getFromGuid() {
        return fromGuid;
    }

    public void setFromGuid(String fromGuid) {
        this.fromGuid = fromGuid;
    }

    @Basic
    @Column(name = "to_guid")
    public String getToGuid() {
        return toGuid;
    }

    public void setToGuid(String toGuid) {
        this.toGuid = toGuid;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RucGroupMessageDO that = (RucGroupMessageDO) o;

        if (groupMessageId != that.groupMessageId) return false;
        if (contemt != null ? !contemt.equals(that.contemt) : that.contemt != null) return false;
        if (fromGuid != null ? !fromGuid.equals(that.fromGuid) : that.fromGuid != null) return false;
        if (toGuid != null ? !toGuid.equals(that.toGuid) : that.toGuid != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (groupMessageId ^ (groupMessageId >>> 32));
        result = 31 * result + (contemt != null ? contemt.hashCode() : 0);
        result = 31 * result + (fromGuid != null ? fromGuid.hashCode() : 0);
        result = 31 * result + (toGuid != null ? toGuid.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RucGroupMessageDO{" +
                "groupMessageId=" + groupMessageId +
                ", contemt='" + contemt + '\'' +
                ", fromGuid='" + fromGuid + '\'' +
                ", toGuid='" + toGuid + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
