package com.doer.moodle.test.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.doer.moodle.cache.RedisClientTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/beans.xml" })
public class RedisTest {
	@Autowired
	private RedisClientTemplate redis;

	@Test
	public void test01(){
		redis.set("zhangsan", "是");
		System.out.println(redis.get("zhangsan"));
	}
}
