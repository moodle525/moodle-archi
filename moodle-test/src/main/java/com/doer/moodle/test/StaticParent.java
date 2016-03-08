package com.doer.moodle.test;

public class StaticParent {
	static {
		System.out.println("parent static");
	}

	{
		System.out.println("I am parent class");
	}

	public StaticParent() {
		System.out.println("new Parent");
	}
}
