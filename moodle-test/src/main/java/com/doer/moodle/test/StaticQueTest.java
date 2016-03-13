package com.doer.moodle.test;

public class StaticQueTest {
	static int i = 0;
	static{
		i+=1;
	}
	
	static{
		//i=-1;
	}
	
	public static void main(String[] args) {
		System.out.println(i);
	}
}
