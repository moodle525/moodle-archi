package com.doer.moodle.test.hadoop.mapreduce.datacount;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.doer.moodle.test.hadoop.mapreduce.datacount.entity.DataBean;

//
//root@moodle01:/# hadoop fs -put /data.dat /data.doc
//root@moodle01:/# hadoop jar /upload/dc.jar com.doer.moodle.test.hadoop.mapreduce.datacount.DataCount /data.doc /dataOut
//
public class DataCount {
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		
		job.setJarByClass(DataCount.class);
		
		job.setMapperClass(DataCountMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(DataBean.class);
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		
		job.setReducerClass(DataCountReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DataBean.class);
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
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
}
