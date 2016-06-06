package com.doer.moodle.test;

import java.util.Random;

public class MoTest {
	public static void main(String[] args) {
		int c1 = -1;
		int c2 = -1;
		int c3 = -1;
		int c4 = -1;
		int c5 = -1;
		int i = -1;
		for (int j = 0; j < 100000000; j++) {
			i = new Random().nextInt(10000);
			i = i % 5;
			if (i == 0) {
				c1++;
			} else if (i == 1) {
				c2++;
			} else if (i == 2) {
				c3++;
			} else if (i == 3) {
				c4++;
			} else if (i == 4) {
				c5++;
			}
		}
		System.out.println(c1);
		System.out.println(c2);
		System.out.println(c3);
		System.out.println(c4);
		System.out.println(c5);
	}
}
