package com.doer.moodle.test;

public class InterviewTest {
	public static void main(String[] args) {
		String str = "str";
		add(str);
		System.out.println(str);
	}
	
	public static void add(String str){
		str = "b"+str;
	}
}
