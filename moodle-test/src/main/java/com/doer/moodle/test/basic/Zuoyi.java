package com.doer.moodle.test.basic;

public class Zuoyi {
		public static void main(String[] args) {
			 int capacity = 1;
		        while (capacity < 1<<4){
		            capacity <<= 1;
		            System.out.println("for:"+capacity);
		        }
		}
}
