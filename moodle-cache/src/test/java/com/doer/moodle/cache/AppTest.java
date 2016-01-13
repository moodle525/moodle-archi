package com.doer.moodle.cache;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.doer.moodle.cache.RedisClientTemplate;

public class AppTest {
	public static void main(String[] args) {
        ApplicationContext ac =  new ClassPathXmlApplicationContext("classpath:/spring/beans.xml");
        RedisClientTemplate redisClient = (RedisClientTemplate)ac.getBean("redisClientTemplate");
        redisClient.set("a", "abc");
        System.out.println(redisClient.get("a")); 
    }
}
