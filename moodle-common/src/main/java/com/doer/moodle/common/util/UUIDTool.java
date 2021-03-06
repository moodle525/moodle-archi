package com.doer.moodle.common.util;

import java.util.UUID;

public class UUIDTool {
	private UUIDTool() {

	}

	public static String genId32() {
		return UUID.randomUUID().toString().replaceAll("\\-", "").toLowerCase();
	}
	
	public static int genShortId() {
		return genId32().hashCode();
	}
	public static void main(String[] args) {
		System.out.println(UUIDTool.genId32());
	}
}
