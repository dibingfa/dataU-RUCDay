package com.flash.dataU.rucday.mock;

import com.flash.dataU.rucday.entity.RucGroupDO;
import com.flash.dataU.rucday.entity.RucUserDO;
import com.flash.dataU.rucday.utils.RandomUtils;

/**
 * Created by Administrator on 2017/10/2.
 */
public class MockUtils {

    public static RucUserDO mockUser(String name, Integer sex, String icon) {
        RucUserDO userDO = new RucUserDO();
        userDO.setUserGuid(RandomUtils.generateUUID());
        userDO.setName(name);
        userDO.setSex(sex);
        userDO.setIcon(icon);
        userDO.setIp(RandomUtils.generateUUID());
        return userDO;
    }

    public static RucGroupDO mockGroup(String groupName, String icon) {
        RucGroupDO groupDO = new RucGroupDO();
        groupDO.setGroupGuid(RandomUtils.generateUUID());
        groupDO.setName(groupName);
        groupDO.setIcon(icon);
        groupDO.setIntro("anything");
        return groupDO;
    }

}
