package com.flash.dataU.rucday.redis;

/**
 * Created by Administrator on 2017/9/23.
 */
public class RedisKeyUtil {

    private static final String REDIS_KEY_PREFIX = "ruc_day:";

    public static String getFullKey(String key) {
        return REDIS_KEY_PREFIX + key;
    }

    public static String getFullKey(String tableName, String findColumn, String key) {
        return REDIS_KEY_PREFIX + tableName + ":" + findColumn + ":" + key;
    }

    public static String getKey(String tableName, String findColumn, String key) {
        return tableName + ":" + findColumn + ":" + key;
    }

}
