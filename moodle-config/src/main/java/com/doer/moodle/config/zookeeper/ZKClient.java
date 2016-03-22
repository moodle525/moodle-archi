package com.doer.moodle.config.zookeeper;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import com.doer.moodle.common.exceptions.PlatformException;
import com.doer.moodle.common.util.StringUtil;
import com.doer.moodle.config.zookeeper.cons.ZkErrorCodeConstants;
import com.doer.moodle.config.zookeeper.impl.ZKPool;

public class ZKClient {
    private static final String DIGEST_SCHEMA = "digest";
    private CuratorFramework client = null;
    private String zkAddr = null;// ip:port
    private int timeOut = 20000;
    private String authSchema = null;
    private String authInfo = null;
    private ZKPool pool = null;

    public ZKClient(String zkAddr, int timeOut, String... auth)
            throws Exception {
        if (StringUtil.isBlank(zkAddr))
            throw new PlatformException(ZkErrorCodeConstants.CONFIG_ZK_ERROR, "");

        this.zkAddr = zkAddr;
        if (timeOut > 0) {
            this.timeOut = timeOut;
        }
        if (null != auth && auth.length >= 2) {
            if (!StringUtil.isBlank(auth[0])) {
                authSchema = auth[0];
            }
            if (!StringUtil.isBlank(auth[1])) {
                authInfo = auth[1];
            }
        }
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory
                .builder().connectString(this.zkAddr)
                .connectionTimeoutMs(this.timeOut)
                .retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 10));
        if (!StringUtil.isBlank(authSchema) && !StringUtil.isBlank(authInfo)) {
            builder.authorization(authSchema, authInfo.getBytes());
        }

        client = builder.build();
        client.start();
       // client.blockUntilConnected();
    }

    public String getNodeData(String nodePath, boolean watch) throws Exception {
        byte[] data;
        if (watch) {
            data = client.getData().watched().forPath(nodePath);
        } else {
            data = client.getData().forPath(nodePath);
        }
        if (null == data || data.length <= 0)
            return null;
        return new String(data, ZkErrorCodeConstants.CHARSET_UTF8);
    }

    public String getNodeData(String nodePath, Watcher watcher) throws Exception {
        byte[] data;
        if (null != watcher)
            data = client.getData().usingWatcher(watcher).forPath(nodePath);
        else
            data = client.getData().forPath(nodePath);
        if (null == data || data.length <= 0)
            return null;
        return new String(data, ZkErrorCodeConstants.CHARSET_UTF8);
    }

    public byte[] getNodeBytes(String nodePath, Watcher watcher) throws Exception {
        byte[] bytes = null;
        if (null != watcher)
            bytes = client.getData().usingWatcher(watcher)
                    .forPath(nodePath);
        else
            bytes = client.getData().forPath(nodePath);

        return bytes;
    }

    public void createNode(String nodePath, String data, CreateMode createMode) throws Exception {
        createNode(nodePath, Ids.OPEN_ACL_UNSAFE, data, createMode);
    }

    public void createNode(String nodePath, List<ACL> acls, String data,
                           CreateMode createMode) throws Exception {
        byte[] bytes = null;
        if (!StringUtil.isBlank(data))
            bytes = data.getBytes(ZkErrorCodeConstants.CHARSET_UTF8);
        if (this.exists(nodePath))
            throw new PlatformException(ZkErrorCodeConstants.CONFIG_ZK_ERROR,
                    "node:" + nodePath + " has existed");
        client.create().creatingParentsIfNeeded().withMode(createMode)
                .withACL(acls).forPath(nodePath, bytes);
    }

    public void createNode(String nodePath, String data) throws Exception {
        createNode(nodePath, data, CreateMode.PERSISTENT);
    }

    public void createNodeWithSuperAndSelfAuth(String schema, String superAuth,
                                               String selfAuth, String path, String data)
            throws Exception {
        List<ACL> acls = new ArrayList<ACL>();
        ACL acl1 = new ACL(ZooDefs.Perms.ALL, new Id(schema,
                DigestAuthenticationProvider.generateDigest(superAuth)));
        ACL acl2 = new ACL(ZooDefs.Perms.READ, new Id(schema,
                DigestAuthenticationProvider.generateDigest(selfAuth)));
        acls.add(acl1);
        acls.add(acl2);
        createNode(path, acls, data, CreateMode.PERSISTENT);
    }

    /**
     * 创建带有链接auth信息的持久节点
     *
     * @param auths    节点auth
     * @param nodePath 节点名
     * @param data     节点数据@throws PlatformException
     */
    public void createNodeWithAuth(String nodePath, String data, String schema, CreateMode createMode, String... auths) throws Exception {
        List<ACL> acls = new ArrayList<ACL>();
        if (auths.length > 0) {
            for (String auth : auths) {
                Id id = new Id(schema,
                        DigestAuthenticationProvider.generateDigest(auth));
                ACL acl = new ACL(ZooDefs.Perms.ALL, id);
                acls.add(acl);
            }
        }
        createNode(nodePath, acls, data, createMode);
    }

    public void setNodeData(String nodePath, String data) throws Exception {
        byte[] bytes = null;
        if (!StringUtil.isBlank(data))
            bytes = data.getBytes(ZkErrorCodeConstants.CHARSET_UTF8);
        client.setData().forPath(nodePath, bytes);
    }

    public List<String> getChildren(String nodePath, Watcher watcher)
            throws Exception {
        return client.getChildren().usingWatcher(watcher).forPath(nodePath);
    }

    /**
     * 在目录下创建顺序节点
     *
     * @param nodePath
     * @throws Exception
     */
    public void createSeqNode(String nodePath) throws Exception {
        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .withACL(Ids.OPEN_ACL_UNSAFE).forPath(nodePath);
    }

    public boolean exists(String path) throws Exception {
        return null == client.checkExists().forPath(path) ? false : true;
    }

    public boolean isConnected() {
        if (null == client
                || CuratorFrameworkState.STARTED != client.getState()) {
            return false;
        }
        return true;
    }

    public void retryConnection() {
        client.start();
    }

    public List<String> getChildren(String path) throws Exception {
        return client.getChildren().forPath(path);
    }

    public List<String> getChildren(String path, boolean watcher) throws Exception {
        if (watcher) {
            return client.getChildren().watched().forPath(path);
        } else {
            return client.getChildren().forPath(path);
        }
    }

    public void deleteNode(String path) throws Exception {
        client.delete().guaranteed().deletingChildrenIfNeeded()
                .forPath(path);
    }

    public void deleteNode(String path, Watcher watcher) throws Exception {
        client.delete().guaranteed().deletingChildrenIfNeeded()
                .forPath(path);
    }

    public void quit() {
        if (null != client
                && CuratorFrameworkState.STARTED != client.getState()) {
            client.close();
        }
    }

    public ZKPool getPool() {
        return pool;
    }

    public void setPool(ZKPool pool) {
        this.pool = pool;
    }

    public ZKClient addAuth(final String authSchema, final String authInfo)
            throws Exception {
        this.client.getZookeeperClient().getZooKeeper()
                .addAuthInfo(DIGEST_SCHEMA, authInfo.getBytes());
        return this;
    }

    public InterProcessLock createInterProcessLock(String lockPath) {
        return new InterProcessMutex(client, lockPath);
    }
}
