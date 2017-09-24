package com.flash.dataU.rucday.repository;

import com.flash.dataU.rucday.entity.RucGroupDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *  群聊框
 *
 * @author Flash (18811311416@sina.cn)
 * @since 2017-07-06 14:42
 */
@Mapper
public interface RucGroupRepository extends JpaRepository<RucGroupDO, Long>{

    /**
     * 根据guid查询群聊框
     */
    RucGroupDO findByGroupGuid(String groupGuid);

}
