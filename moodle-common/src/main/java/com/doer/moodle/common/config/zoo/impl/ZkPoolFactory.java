package com.doer.moodle.common.config.zoo.impl;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.doer.moodle.common.config.zoo.cons.ZkErrorCodeConstants;
import com.doer.moodle.common.exceptions.PlatformException;

public class ZkPoolFactory {

	/**
	 * 默认不初始化和ZK的连接
	 */
	private static ConcurrentHashMap<String, ZKPool> pools = new ConcurrentHashMap<String, ZKPool>();
	private static final String LOCK = "true";
	private static int total = 500;
	private static String zkTimeout = null;

	private ZkPoolFactory() {
		// 禁止实例化,怎么销毁池子，过一段自动断了
	}

	public static ZKPool getZkPool(String zkAddr) throws PlatformException {
		if (pools.get(zkAddr) == null) {
			synchronized (LOCK) {
				if (pools.get(zkAddr) == null) {
					GenericObjectPoolConfig config = new GenericObjectPoolConfig();
					config.setMaxTotal(total);
					config.setTestOnBorrow(false);
					config.setMinIdle(10);
					int timeOut = 2000;
					if (StringUtils.isBlank(zkAddr)) {
						throw new PlatformException(
								ZkErrorCodeConstants.NO_PARSE_ZK_INFO,
								"zkAddr can not be null,and its format is ip:port");
					}

					if (!StringUtils.isBlank(zkTimeout)) {
						timeOut = Integer.parseInt(zkTimeout);
					}

					ZKPool zkPool = new ZKPool(config, zkAddr, timeOut);
					pools.put(zkAddr, zkPool);
				}
			}
		}

		return pools.get(zkAddr);
	}

	public static ZKPool getZkPool(String zkAddr, Properties poolConfig)
			throws PlatformException {
		synchronized (LOCK) {
			if (pools.get(zkAddr) == null) {
				GenericObjectPoolConfig config = new GenericObjectPoolConfig();
				try {
					//ConfigUtil.propertiesToProperty(poolConfig, config);
				} catch (Exception e) {
					throw new PlatformException(
							ZkErrorCodeConstants.CONFIG_ZK_ERROR, "传入的配置错误", e);
				}
				int timeOut = 2000;
				if (StringUtils.isBlank(zkAddr)) {
					throw new PlatformException(
							ZkErrorCodeConstants.NO_PARSE_ZK_INFO,
							"zkAddr can not be null,and its format is ip:port");
				}

				if (!StringUtils.isBlank(zkTimeout)) {
					timeOut = Integer.parseInt(zkTimeout);
				}

				ZKPool zkPool = new ZKPool(config, zkAddr, timeOut);
				pools.put(zkAddr, zkPool);
			}
		}

		return pools.get(zkAddr);
	}
}
