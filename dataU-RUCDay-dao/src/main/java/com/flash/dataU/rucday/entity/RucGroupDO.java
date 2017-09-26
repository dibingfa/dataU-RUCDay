package com.flash.dataU.rucday.entity;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/9/23.
 */
@Entity
@Table(name = "ruc_group", schema = "ruc_day", catalog = "")
public class RucGroupDO extends BaseDO{
    private long groupId;
    private String groupGuid;
    private String name;
    private String icon;
    private String intro;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "group_id")
    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    @Basic
    @Column(name = "group_guid")
    public String getGroupGuid() {
        return groupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        this.groupGuid = groupGuid;
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
    @Column(name = "icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Basic
    @Column(name = "intro")
    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        RucGroupDO aDo = (RucGroupDO)o;

        if (groupId != aDo.groupId)
            return false;
        if (groupGuid != null ? !groupGuid.equals(aDo.groupGuid) : aDo.groupGuid != null)
            return false;
        if (name != null ? !name.equals(aDo.name) : aDo.name != null)
            return false;
        if (icon != null ? !icon.equals(aDo.icon) : aDo.icon != null)
            return false;
        return intro != null ? intro.equals(aDo.intro) : aDo.intro == null;
    }

    @Override
    public int hashCode() {
        int result = (int)(groupId ^ (groupId >>> 32));
        result = 31 * result + (groupGuid != null ? groupGuid.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (intro != null ? intro.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RucGroupDO{" + "groupId=" + groupId + ", groupGuid='" + groupGuid + '\'' + ", name='" + name + '\'' + ", icon='" + icon + '\'' + ", intro='" + intro + '\'' + '}';
    }
}
