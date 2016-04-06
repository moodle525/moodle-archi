package com.doer.moodle.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.doer.moodle.cache.core.RedisClientTemplate;

@Repository
public class RedisClient {
	//private static final Logger log = Logger.getLogger(RedisClient.class);
	
	@Autowired
	private RedisClientTemplate clientTemplate;

	/*********************************************************
	 * redis对key的操作
	 *********************************************************/
	/**
	 * redis key所对应值得类型
	 * 
	 * @param key
	 * @return
	 */
	public String type(String key) {
		return clientTemplate.type(key);
	}

	/**
	 * 删除某个key
	 * 
	 * @param key
	 * @return
	 */
	public long del(String key) {
		return clientTemplate.del(key);
	}
	
	/**
	 * key是否存在
	 * @param key
	 * @return
	 */
	public boolean exists(String key){
		return clientTemplate.exists(key);
	}

	/**
	 * 查询key的生命周期</br>
	 * 不存在的key,返回-2,已过期的key/不过期的key,都返回-1</br>
	 * 对于设置过过期时间的key返回有效期的秒数
	 * 
	 * @param key
	 * @return
	 */
	public long ttl(String key) {
		return clientTemplate.ttl(key);
	}

	/**
	 * key多少秒后过期
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public long expire(String key, int seconds) {
		return clientTemplate.expire(key, seconds);
	}

	public long expireAt(String key, long seconds) {
		return clientTemplate.expireAt(key, seconds);
	}

	/***************************************************************************
	 * redis 对字符串操作
	 ***********************************************************************/
	/**
	 * 存储字符串，失败抛异常
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		clientTemplate.set(key, value);
	}
	
	/**
	 * 更新key，保持key的有效期
	 * @param key
	 * @param value
	 */
	public void update(String key,String value){
		if(!exists(key)){
			throw new RuntimeException("no key "+key+" exists,please cheak it");
		}
		clientTemplate.setex(key, (int)ttl(key), value);
	}

	public String get(String key) {
		return clientTemplate.get(key);
	}
}
