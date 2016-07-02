package com.doer.moodle.test.algorithm.generic;

public class BiTree {
	private Node root;

	public BiTree() {
	}

	/**
	 * 插入节点
	 * 
	 * @param data
	 */
	public void insert(Integer data) {
		Node newNode = new Node(data);
		if (root == null) {
			root = newNode;
		} else {
			Node current = root;
			Node parent;
			while (true) {
				parent = current;
				if (data < parent.data) {
					current = current.left;
					if (current == null) {
						parent.left = newNode;
						return;
					}
				} else {
					current = current.right;
					if (current == null) {
						parent.right = newNode;
						return;
					}
				}
			}
		}
	}
	
	/**
	 * 删除节点
	 * @param data
	 */
	public boolean delete(Integer data){
		Node current = root;
		Node parent = root;
		boolean isLeft = true;//节点在树的左边
		while(current.data != data){//找到要删除的节点
			parent = current;
			if(data < current.data){
				current = current.left;
				isLeft = true;
			}else{
				current = current.right;
				isLeft = false;
			}
			if(current == null)//没有找到要删除的节点
				return false;
		}
		//找到了要删除的节点
		//1.删除的是叶子节点
		if(parent.left == null && parent.right == null){
			if(current == root){//删除根节点
				root = null;
			}else if(isLeft){//删除节点在父节点的左边
				parent.left = null;
			}else{//删除节点在父节点的右边
				parent.right = null;
			}
		}else if(current.right == null){//2.删除的节点只有左子节点
			if(current == root){//删除根节点
				root = current.left;
			}else if(isLeft){//删除节点在父节点的左边
				parent.left = current.left;
			}else{//删除节点在父节点的右边
				parent.right = current.left;
			}
		}else if(current.left == null){//3.删除节点只有右子节点
			if(current == root){//删除根节点
				root = current.right;
			}else if(isLeft){//删除节点在父节点的左边
				parent.left = current.right;
			}else{//删除节点在父节点的右边
				parent.right = current.right;
			}
		}else{//4.删除节点左右子节点都有
			Node successor = getSuccessor(current);
			if(current == root){
				root = successor;
			}else if(isLeft){
				parent.left = successor;
			}else{
				parent.right = successor;
			}
			successor.left = current.left;
		}
		return true;
	}

	/**
	 * 向右子节点向下找继承者节点
	 * @param current
	 * @return
	 */
	public Node getSuccessor(Node node) {
		Node successorParent = node;
		Node successor = node;
		Node current = node.right;//从右子节点开始查找
		while(current != null){
			successorParent = successor;
			successor = current;
			current = current.left;
		}
		
		if(successor!=node.right){
			successorParent.left = successor.right;
			successor.right=node.right;
		}
		return successor;
	}
}
