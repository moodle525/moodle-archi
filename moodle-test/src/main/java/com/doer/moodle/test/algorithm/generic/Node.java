package com.doer.moodle.test.algorithm.generic;

public class Node {
	public Integer data;
	public Node left;
	public Node right;

	public Node(Integer data, Node left, Node right) {
		this.data = data;
		this.left = left;
		this.right = right;
	}

	public Node(Integer data) {
		this(data, null, null);
	}

	public Integer getData() {
		return data;
	}

	@Override
	public String toString() {
		Integer leftData = null;
		Integer rightData = null;
		if (left != null) {
			leftData = left.data;
		}
		if (right != null) {
			rightData = right.data;
		}
		return "{" + data + "," + leftData + "," + rightData + "}";
	}

}
