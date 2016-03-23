package com.doer.moodle.common.config.zoo.impl;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.doer.moodle.common.config.zoo.ZKClient;

public class ZKClientFactory implements PooledObjectFactory<ZKClient> {
	private final AtomicReference<String> address = new AtomicReference<String>();
	private final int timeout;
	private final AtomicReference<String> authSchema = new AtomicReference<String>();
	private final AtomicReference<String> authInfo = new AtomicReference<String>();

	public ZKClientFactory(final String address, final int timeout,
			final String authSchema, final String authInfo) {
		super();
		this.address.set(address);
		this.timeout = timeout;
		this.setAuthSchema(authSchema);
		this.setAuthInfo(authInfo);
	}

    public ZKClientFactory(final String address, final int timeout) {
        super();
        this.address.set(address);
        this.timeout = timeout;
    }

	public void setAddress(final String address) {
		this.address.set(address);
	}

	public void setAuthSchema(final String authSchema) {
		this.authSchema.set(authSchema);
	}

	public void setAuthInfo(final String authInfo) {
		this.authInfo.set(authInfo);
	}

	public void destroyObject(PooledObject<ZKClient> pooledZKClient)
			throws Exception {
		final ZKClient zkClient = pooledZKClient.getObject();
		if (zkClient.isConnected()) {
			try {
				try {
					zkClient.quit();
				} catch (Exception e) {
				}
			} catch (Exception e) {

			}
		}

	}

	public PooledObject<ZKClient> makeObject() throws Exception {
		final String address = this.address.get();
		final String authSchema = this.authSchema.get();
		final String authInfo = this.authInfo.get();
		final ZKClient zkClient = new ZKClient(address, this.timeout,
				authSchema, authInfo);

		return new DefaultPooledObject<ZKClient>(zkClient);
	}

	public boolean validateObject(PooledObject<ZKClient> pooledZKClient) {
		final ZKClient zkClient = pooledZKClient.getObject();
		try {
			return zkClient.isConnected();
		} catch (final Exception e) {
			return false;
		}
	}

	public void activateObject(PooledObject<ZKClient> pooledZKClient)
			throws Exception {

	}

	public void passivateObject(PooledObject<ZKClient> pooledZKClient)
			throws Exception {

	}
}
