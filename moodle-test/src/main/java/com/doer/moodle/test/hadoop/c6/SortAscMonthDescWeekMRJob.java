package com.doer.moodle.test.hadoop.c6;

import java.io.IOException;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.doer.moodle.test.hadoop.utils.AirlineDataUtils;


public class SortAscMonthDescWeekMRJob extends Configured implements Tool {

    public static class SortAscMonthDescWeekMapper extends
            Mapper<LongWritable, Text, MonthDoWWritable, DelaysWritable> {

//1987,12,30,3,1438,1315,1715,1612,CO,757,NA,157,177,NA,63,83,EWR,PBI,1024,NA,NA,0,NA,0,NA,NA,NA,NA,NA
//年，月，日，天
        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            if (!AirlineDataUtils.isHeader(value)) {
                String[] contents = value.toString().split(",");
                String month = AirlineDataUtils.getMonth(contents);
                String dow = AirlineDataUtils.getDayOfTheWeek(contents);
                MonthDoWWritable mw = new MonthDoWWritable();
                mw.month = new IntWritable(Integer.parseInt(month));
                mw.dayOfWeek = new IntWritable(Integer.parseInt(dow));
                DelaysWritable dw = AirlineDataUtils.parseDelaysWritable(value
                        .toString());
                context.write(mw, dw);
            }
        }
    }

    public static class SortAscMonthDescWeekReducer extends
            Reducer<MonthDoWWritable, DelaysWritable, NullWritable, Text> {
        public void reduce(MonthDoWWritable key,
                Iterable<DelaysWritable> values, Context context)
                throws IOException, InterruptedException {

            for (DelaysWritable val : values) {
                context.write(
                        NullWritable.get(),
                        new Text(AirlineDataUtils
                                .parseDelaysWritableToText(val)));
            }
        }
    }

    public static class MonthDoWPartitioner extends
            Partitioner<MonthDoWWritable, Text> implements Configurable {
        private Configuration conf = null;
        private int indexRange = 0;

        private int getDefaultRange() {
            int minIndex = 0;
            int maxIndex = 11 * 7 + 6;
            int range = (maxIndex - minIndex) + 1;
            return range;
        }

        @Override
        public void setConf(Configuration conf) {
            this.conf = conf;
            this.indexRange = conf.getInt("key.range", getDefaultRange());
        }

        @Override
        public Configuration getConf() {
            return this.conf;
        }

        @Override
        public int getPartition(MonthDoWWritable key, Text value,
                int numReduceTasks) {
            return AirlineDataUtils.getCustomPartition(key, indexRange,
                    numReduceTasks);
        }
    }

    public int run(String[] allArgs) throws Exception {
        Job job = Job.getInstance(getConf());
        job.setJarByClass(SortAscMonthDescWeekMRJob.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setMapOutputKeyClass(MonthDoWWritable.class);
        job.setMapOutputValueClass(DelaysWritable.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(SortAscMonthDescWeekMapper.class);
        job.setReducerClass(SortAscMonthDescWeekReducer.class);
        job.setPartitionerClass(MonthDoWPartitioner.class);

        String[] args = new GenericOptionsParser(getConf(), allArgs)
                .getRemainingArgs();
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);

        return 0;
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        ToolRunner.run(new SortAscMonthDescWeekMRJob(), args);
    }

}