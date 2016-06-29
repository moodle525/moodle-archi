package com.doer.moodle.test;

import java.util.ArrayList;
import java.util.List;

public class ValueTransTest {
	public static void main(String[] arj){
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("a2");
		list.add("a3");
		list.add("a4");
		list.add("a5");
		int i = 5;
		update(i);
		System.out.println(i);
		
		remove(list);
		for(String s : list){
			System.out.print(s);
		}
		while(true){
			Object obj = new Object();
		}
	}

	private static void update(int i) {
		i = 6;
		System.out.println("update:"+i);
	}

	private static void remove(List<String> list) {
		list.remove(0);
		for(String s : list){
			System.out.print(s);
		}
		System.out.println();
	}
}
