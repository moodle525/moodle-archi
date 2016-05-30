package com.doer.moodle.common.session;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;

public class JedisClusterConnectionFactory implements InitializingBean, DisposableBean, RedisConnectionFactory{

	@Override
	public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RedisConnection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getConvertPipelineAndTxResults() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RedisSentinelConnection getSentinelConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
