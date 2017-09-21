package com.flash.dataU.rucday.redis;

import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisOpsUtil {

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Resource(name = "stringRedisTemplate")
	ValueOperations<String, String> valOpsStr;

	/**
	 * 查询值
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) {
		String v = valOpsStr.get(key);
		return v;
	}

	public void set(String key, String value) {
		valOpsStr.set(key, value);
	}

}
