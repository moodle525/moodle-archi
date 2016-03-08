package com.doer.moodle.test;

public class StaticChild extends StaticParent{
	static{
		System.out.println("Static Child");
	}
	
	{
		System.out.println("I am child");
	}
	
	public StaticChild(){
		System.out.println("new Child");
	}
	
	public static void main(String[] args) {
		new StaticChild();
	}
}
