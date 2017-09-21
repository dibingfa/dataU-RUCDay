package com.flash.dataU.test.repository;

import com.flash.dataU.test.entity.CityEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *  Dao Test.
 *  其他的所有Dao类都以这个类为模板
 *
 * @author Flash (18811311416@sina.cn)
 * @since 2017-07-06 14:42
 */
@Mapper
public interface TestRepository extends JpaRepository<CityEntity, Long>{

    CityEntity findByName(String name);

}
