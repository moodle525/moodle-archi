package com.doer.moodle.test;

public class StringDemo {
	public static void main(String[] args) {
		String a = "hello";
		String b = "he" + new String("llo");
		System.out.println(a == b);
		System.out.println(a.equals(b));
	}
}
