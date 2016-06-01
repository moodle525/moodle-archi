package com.doer.moodle.test.hadoop.mapreduce.datacount;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.doer.moodle.test.hadoop.mapreduce.datacount.entity.DataBean;

/**
 * root@moodle01:/# hadoop fs -put /data.dat /data.doc root@moodle01:/# hadoop
 * jar /upload/dc.jar com.doer.moodle.test.hadoop.mapreduce.datacount.DataCount
 * /data.doc /dataOut
 **/
public class DataCount_Partitioner {
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);

		job.setJarByClass(DataCount_Partitioner.class);

		job.setMapperClass(DataCountMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(DataBean.class);
		FileInputFormat.setInputPaths(job, new Path(args[0]));

		job.setReducerClass(DataCountReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DataBean.class);
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setPartitionerClass(ServiceProviderPartitioner.class);
		job.setNumReduceTasks(Integer.parseInt(args[2]));

		job.waitForCompletion(true);
	}

	public static class DataCountMapper extends Mapper<LongWritable, Text, Text, DataBean> {

		@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, DataBean>.Context context)
				throws IOException, InterruptedException {
			// 接受数据
			String line = value.toString();
			// 分割
			String[] fields = line.split("\t");
			String telNo = fields[1];
			long upPayload = Long.parseLong(fields[8]);
			long downPayload = Long.parseLong(fields[9]);
			// 包装数据
			DataBean bean = new DataBean(telNo, upPayload, downPayload);
			// 输出数据
			context.write(new Text(telNo), bean);
		}

	}

	public static class DataCountReducer extends Reducer<Text, DataBean, Text, DataBean> {

		@Override
		protected void reduce(Text key, Iterable<DataBean> values,
				Reducer<Text, DataBean, Text, DataBean>.Context context) throws IOException, InterruptedException {
			long upPayloadCounter = 0;
			long downPayloadCounter = 0;
			for (DataBean bean : values) {
				upPayloadCounter += bean.getUpPayLoad();
				downPayloadCounter += bean.getDownPayLoad();
			}
			DataBean dataBean = new DataBean("", upPayloadCounter, downPayloadCounter);
			context.write(key, dataBean);
		}

	}

	/**
	 * 自定义Partitioner
	 * @author lixiongcheng
	 *
	 */
	public static class ServiceProviderPartitioner extends Partitioner<Text, DataBean> {

		private static Map<String, Integer> providerMap = new HashMap<String, Integer>();

		static {
			providerMap.put("139", 1);
			providerMap.put("138", 2);
			providerMap.put("159", 3);
		}

		/**
		 * 返回分区号，一般有几个reduce对应几个partition 
		 */
		@Override
		public int getPartition(Text key, DataBean value, int number) {
			String telNo = key.toString();
			String pcode = telNo.substring(0, 3);
			Integer p = providerMap.get(pcode);
			if (p == null) {
				p = 0;
			}
			return p;
		}

	}
}
