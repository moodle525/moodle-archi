package com.doer.moodle.test.hadoop.hdfs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class HDFSDemo {
	FileSystem fs = null;
	
	@Before
	public void init() throws Exception, Exception{
		//创建FileSystem的实现类（工具类）
		fs = FileSystem.get(new URI("hdfs://moodle01:9000"), new Configuration(),"root");
	}
	
	/**
	 * 文件上传
	 * @throws IOException
	 */
	@Test
	public void upload() throws IOException{
		//读取本地系统文件
		InputStream in = new FileInputStream("/Users/lixiongcheng/Downloads/hadoop.tar.gz");
		//在HDFS上创建一个文件
		OutputStream out = fs.create(new Path("/hadoop"));
		IOUtils.copyBytes(in, out, 4096,true);
	}
	
	/**
	 * 文件下载，从HDFS到本地系统
	 * @throws Exception
	 */
	@Test
	public void download() throws Exception{
		InputStream in = fs.open(new Path("/hadoop"));
		OutputStream out = new FileOutputStream("/Users/lixiongcheng/Downloads/hadoop");
		IOUtils.copyBytes(in, out, 4096,true);
	}
	
	/**
	 * 文件下载，快捷方式
	 * @throws Exception
	 * @throws IOException
	 */
	@Test
	public void download2() throws Exception, IOException{
		fs.copyToLocalFile(new Path("/hadoop"), new Path("/Users/lixiongcheng/Downloads/hadoop"));
	}
	
	/**
	 * 删除HDFS文件
	 * @throws Exception
	 */
	@Test
	public void del() throws Exception{
		boolean flag = fs.delete(new Path("/hadoop"), false);
		System.out.println(flag);
	}
	
	@Test
	public void mkdir() throws Exception{
		boolean flag = fs.mkdirs(new Path("/ha"));
		System.out.println(flag);
	}
}
