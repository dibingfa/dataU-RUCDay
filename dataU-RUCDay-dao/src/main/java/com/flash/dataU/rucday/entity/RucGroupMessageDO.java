package com.flash.dataU.rucday.entity;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/9/23.
 */
@Entity
@Table(name = "ruc_group_message", schema = "ruc_day", catalog = "")
public class RucGroupMessageDO extends BaseDO{
    private long groupMessageId;
    private String content;
    private String fromGuid;
    private String toGuid;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "group_message_id")
    public long getGroupMessageId() {
        return groupMessageId;
    }

    public void setGroupMessageId(long groupMessageId) {
        this.groupMessageId = groupMessageId;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Override
    public String toString() {
        return "RucGroupMessageDO{" + "groupMessageId=" + groupMessageId + ", content='" + content + '\'' + ", fromGuid='" + fromGuid + '\'' + ", toGuid='" + toGuid + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        RucGroupMessageDO that = (RucGroupMessageDO)o;

        if (groupMessageId != that.groupMessageId)
            return false;
        if (content != null ? !content.equals(that.content) : that.content != null)
            return false;
        if (fromGuid != null ? !fromGuid.equals(that.fromGuid) : that.fromGuid != null)
            return false;
        return toGuid != null ? toGuid.equals(that.toGuid) : that.toGuid == null;
    }

    @Override
    public int hashCode() {
        int result = (int)(groupMessageId ^ (groupMessageId >>> 32));
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (fromGuid != null ? fromGuid.hashCode() : 0);
        result = 31 * result + (toGuid != null ? toGuid.hashCode() : 0);
        return result;
    }
}
