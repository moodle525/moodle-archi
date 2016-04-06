package com.doer.moodle.test.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.doer.moodle.cache.RedisClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/beans.xml" })
public class RedisTest {
	@Autowired
	private RedisClient redis;

	@Test
	public void test01(){
		redis.set("zhangsan", "是");
		System.out.println(redis.get("zhangsan"));
	}
	
	@Test
	public void ttl(){
		System.out.println(redis.expire("zhangsan", 50));
		System.out.println(redis.ttl("zhangsan"));
	}
	
	@Test
	public void update(){
		redis.update("zhangsana", "hee");
		System.out.println(redis.ttl("zhangsan"));
	}
	
}
