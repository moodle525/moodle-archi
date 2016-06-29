package com.doer.moodle.test;

public class CircularRefTest {
	private CircularRefTest instance = null;  
    private byte[] buffer = new byte[1024 * 1024];  
      
    public static void main(String[] args) {  
        CircularRefTest a = new CircularRefTest();  
        CircularRefTest b = new CircularRefTest();  
        a.instance = b;  
        b.instance = a;  
        a = null;  
        b = null;  
        System.gc();  
    }  
}
