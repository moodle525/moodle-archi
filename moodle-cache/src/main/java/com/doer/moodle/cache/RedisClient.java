package com.doer.moodle.cache;

import org.springframework.beans.factory.annotation.Autowired;

import com.doer.moodle.cache.core.RedisClientTemplate;

public class RedisClient {
	@Autowired
	private RedisClientTemplate clientTemplate;

	/*********************************************************
	 * redis对key的操作
	 *********************************************************/
	/**
	 * redis key所对应值得类型
	 * @param key
	 * @return
	 */
	public String type(String key) {
		return clientTemplate.type(key);
	}
	
	/**
	 * 删除某个key
	 * @param key
	 * @return
	 */
	public Long del(String key){
		return clientTemplate.del(key);
	}
	
	public String get(String key){
		return clientTemplate.get(key);
	}
}
