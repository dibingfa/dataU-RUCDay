package com.flash.dataU.rucday.repository;

import com.flash.dataU.rucday.entity.RucUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *  群聊框
 *
 * @author Flash (18811311416@sina.cn)
 * @since 2017-07-06 14:42
 */
@Mapper
public interface RucUserRepository extends JpaRepository<RucUserDO, Long>{

    /**
     * 根据guid查询用户
     */
    RucUserDO findByUserGuid(String userGuid);

    /**
     * 根据ip查询用户
     */
    RucUserDO findByIp(String ip);

    /**
     * 根据name查询用户
     */
    RucUserDO findByName(String name);

}
