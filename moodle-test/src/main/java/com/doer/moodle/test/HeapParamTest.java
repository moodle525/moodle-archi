package com.doer.moodle.test;

public class HeapParamTest {
	public static void main(String[] args) {
		p("Xmx=" + byte2m(Runtime.getRuntime().maxMemory()));
		p("free mem="+ byte2m(Runtime.getRuntime().freeMemory()));
		p("total mem="+byte2m(Runtime.getRuntime().totalMemory()));
		byte[] b = new byte[20*1024*1024];
		p("Xmx=" + byte2m(Runtime.getRuntime().maxMemory()));
		p("free mem="+ byte2m(Runtime.getRuntime().freeMemory()));
		p("total mem="+byte2m(Runtime.getRuntime().totalMemory()));
	}
	
	public static String byte2m(long b){
		return b/1024/1024+"M";
	}
	
	public static void p(String str){
		System.out.println(str);
	}
}
