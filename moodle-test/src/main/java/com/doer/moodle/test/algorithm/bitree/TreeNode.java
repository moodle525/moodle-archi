package com.doer.moodle.test.algorithm.bitree;

public class TreeNode {
	private int key;
	private TreeNode leftChild;
	private TreeNode rightChild;
	private TreeNode parent;

	public TreeNode(int key, TreeNode leftChild, TreeNode rightChild, TreeNode parent) {
		super();
		this.key = key;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		this.parent = parent;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public TreeNode getLeftChild() {
		return leftChild;
	}

	public void setLeftChild(TreeNode leftChild) {
		this.leftChild = leftChild;
	}

	public TreeNode getRightChild() {
		return rightChild;
	}

	public void setRightChild(TreeNode rightChild) {
		this.rightChild = rightChild;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public String toString() {
		String leftkey = (leftChild == null ? "" : String.valueOf(leftChild.key));
		String rightkey = (rightChild == null ? "" : String.valueOf(rightChild.key));
		return "(" + leftkey + " , " + key + " , " + rightkey + ")";
	}

}
