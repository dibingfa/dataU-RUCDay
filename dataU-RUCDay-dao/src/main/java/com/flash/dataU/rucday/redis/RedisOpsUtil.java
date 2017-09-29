package com.flash.dataU.rucday.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RedisOpsUtil {

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Resource(name = "stringRedisTemplate")
	ValueOperations<String, String> valOpsStr;

	/**
	 * 查询值
	 */
	public String get(String key) {
		String value = valOpsStr.get(key);
		return value;
	}

	/**
	 * 存入值
	 */
	public void set(String key, String value) {
		valOpsStr.set(key, value);
	}

	/**
	 * 更新值
	 */
	public void update(String key, String value) {
		valOpsStr.setIfAbsent(key, value);
	}

	/**
	 * 删除值
	 */
	public void delete(String key) {
		valOpsStr.getOperations().delete(key);
	}

	/**
	 * 查询全部
	 * @param formatKey 通配符的key
	 */
	public List<String> getKeys(String formatKey){
		Set<String> keys = valOpsStr.getOperations().keys(formatKey);
		List<String> list = new ArrayList<String>(keys.size());
		for (String certainKey : keys) {
			String value = valOpsStr.get(certainKey);
			list.add(value);
		}
		return list;
	}

	/**
	 * 队列查
	 */
	public String index(String key, long index){
		return valOpsStr.getOperations().boundListOps(key).index(index);
	}

	/**
	 * 队列存
	 */
	public void rpush(String key, String value){
		valOpsStr.getOperations().boundListOps(key).rightPush(value);
	}

	/**
	 * 队列批量查询（全部）
	 */
	public List<String> listAll(String key){
		BoundListOperations<String, String> listOps = valOpsStr.getOperations().boundListOps(key);
		return listOps.range(0, -1);
	}

	/**
	 * 队列批量查询（范围查询）
	 * @param count 后多少条的意思
	 */
	public List<String> listAll(String key, long count){
		BoundListOperations<String, String> listOps = valOpsStr.getOperations().boundListOps(key);
		Long size = listOps.size();
		if (size > count) {
			return listOps.range(size - count, -1);
		} else {
			return listOps.range(0, -1);
		}
	}

	/**
	 * 队列大小
	 */
	public Long countAll(String key){
		return valOpsStr.getOperations().boundListOps(key).size();
	}

	/**
	 * 清空
	 */
	public void unwatch(){
		valOpsStr.getOperations().unwatch();
	}

}
