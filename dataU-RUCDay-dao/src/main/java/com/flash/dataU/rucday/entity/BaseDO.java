package com.flash.dataU.rucday.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * .
 *
 * @author flash (18811311416@sina.cn)
 * @version 0.0.1-SNAPSHOT
 * @since 2017年09月26日 11时20分
 */
@MappedSuperclass
public class BaseDO {

    protected Long createTime;

    @Basic
    @Column(name = "createTime")
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
