package com.doer.moodle.config.zookeeper.impl;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.doer.moodle.config.zookeeper.ZKClient;
import com.doer.moodle.config.zookeeper.cons.ConfigCenterConstants;

public class ZKPool extends Pool<ZKClient> {


    public ZKPool(final GenericObjectPoolConfig poolConfig, final String address, int timeout){
        super(poolConfig, new ZKClientFactory(address, timeout));
    }

	public ZKPool(String address, int timeout, final String authSchema,
			final String authInfo) {
		this(new GenericObjectPoolConfig(), address, timeout, authSchema,
				authInfo);
	}

	public ZKPool(final GenericObjectPoolConfig poolConfig,
			final String address, int timeout, final String authSchema,
			final String authInfo) {
		super(poolConfig, new ZKClientFactory(address, timeout, authSchema,
				authInfo));
	}

	public ZKClient getZkClient(final String authSchema,final String authInfo) throws Exception {
		ZKClient zkClient = super.getResource();
		zkClient.setPool(this);
        zkClient.addAuth(authSchema,authInfo);
		return zkClient;
	}

    public ZKClient getZkClient(final String authInfo) throws Exception {
        return getZkClient(ConfigCenterConstants.ZKAuthSchema.DIGEST, authInfo);
    }

	public void returnBrokenResource(final ZKClient resource) throws Exception {
		if (resource != null) {
			returnBrokenResourceObject(resource);
		}
	}

	public void returnResource(final ZKClient resource) throws Exception {
		if (resource != null) {
			try {
				returnResourceObject(resource);
			} catch (Exception e) {
				returnBrokenResource(resource);
				throw new Exception(
						"Could not return the resource to the pool", e);
			}
		}
	}

	public int getNumActive() {
		if (this.internalPool == null || this.internalPool.isClosed()) {
			return -1;
		}

		return this.internalPool.getNumActive();
	}

}
