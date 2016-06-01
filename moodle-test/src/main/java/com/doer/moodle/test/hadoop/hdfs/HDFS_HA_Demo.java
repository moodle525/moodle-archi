package com.doer.moodle.test.hadoop.hdfs;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

/**
 * Hadoop集群 HDFS
 * @author lixiongcheng
 *
 */
public class HDFS_HA_Demo {
	public static void main(String[] args) throws Exception, InterruptedException, URISyntaxException {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://ns1");
		conf.set("dfs.nameservices", "ns1");
		conf.set("dfs.ha.namenodes.ns1", "nn1,nn2");
		conf.set("dfs.namenode.rpc-address.ns1.nn1", "moodle01:9000");
		conf.set("dfs.namenode.rpc-address.ns1.nn2", "moodle02:9000");
		//conf.setBoolean(name, value);
		conf.set("dfs.client.failover.proxy.provider.ns1", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
		FileSystem fs = FileSystem.get(new URI("hdfs://ns1"), conf, "root");
		InputStream in =new FileInputStream("/Users/lixiongcheng/Downloads/hadoop.tar.gz");
		OutputStream out = fs.create(new Path("/hadoop"));
		IOUtils.copyBytes(in, out, 4096, true);
	}
}
