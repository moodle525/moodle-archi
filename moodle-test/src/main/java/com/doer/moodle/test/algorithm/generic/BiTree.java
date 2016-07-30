package com.doer.moodle.test.algorithm.generic;

import java.util.Stack;

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
	 * 
	 * @param data
	 */
	public boolean delete(Integer data) {
		Node current = root;
		Node parent = root;
		boolean isLeft = true;// 节点在树的左边
		while (current.data != data) {// 找到要删除的节点
			parent = current;
			if (data < current.data) {
				current = current.left;
				isLeft = true;
			} else {
				current = current.right;
				isLeft = false;
			}
			if (current == null)// 没有找到要删除的节点
				return false;
		}
		// 找到了要删除的节点
		// 1.删除的是叶子节点
		if (parent.left == null && parent.right == null) {
			if (current == root) {// 删除根节点
				root = null;
			} else if (isLeft) {// 删除节点在父节点的左边
				parent.left = null;
			} else {// 删除节点在父节点的右边
				parent.right = null;
			}
		} else if (current.right == null) {// 2.删除的节点只有左子节点
			if (current == root) {// 删除根节点
				root = current.left;
			} else if (isLeft) {// 删除节点在父节点的左边
				parent.left = current.left;
			} else {// 删除节点在父节点的右边
				parent.right = current.left;
			}
		} else if (current.left == null) {// 3.删除节点只有右子节点
			if (current == root) {// 删除根节点
				root = current.right;
			} else if (isLeft) {// 删除节点在父节点的左边
				parent.left = current.right;
			} else {// 删除节点在父节点的右边
				parent.right = current.right;
			}
		} else {// 4.删除节点左右子节点都有
			Node successor = getSuccessor(current);
			if (current == root) {
				root = successor;
			} else if (isLeft) {
				parent.left = successor;
			} else {
				parent.right = successor;
			}
			successor.left = current.left;
		}
		return true;
	}

	/**
	 * 向右子节点向下找继承者节点
	 * 
	 * @param current
	 * @return
	 */
	public Node getSuccessor(Node node) {
		Node successorParent = node;
		Node successor = node;
		Node current = node.right;// 从右子节点开始查找
		while (current != null) {
			successorParent = successor;
			successor = current;
			current = current.left;
		}

		if (successor != node.right) {
			successorParent.left = successor.right;
			successor.right = node.right;
		}
		return successor;
	}

	/**
	 * 查找某个节点
	 * 
	 * @param data
	 * @return
	 */
	public Node find(Integer data) {
		Node current = root;
		while (data != current.data) {
			if (data < current.data) {
				current = current.left;
			} else {
				current = current.right;
			}
			if (current == null) {
				return null;
			}
		}
		return current;
	}

	/**
	 * 遍历
	 * 
	 * @param data
	 */
	public void traverse(Integer data) {
		switch (data) {
		case 1:// 从上至下，从左至右
			System.out.println("Preorder traverse:");
			preOrder(root);
			break;
		case 2:// 从下至上，从左至右(从小到大)
			System.out.println("Inorder traverse:");
			inOrder(root);
		case 3:// 从下至上，从右至左
			postOrder(root);
			System.out.println("Postorder traverse:");
		default:
			break;
		}
		System.out.println();
	}

	private void preOrder(Node localRoot) {// 从上至下，从左至右遍历
		if (localRoot != null) {
			System.out.println(localRoot);
			preOrder(localRoot.left);
			preOrder(localRoot.right);
		}
	}

	private void inOrder(Node localRoot) {
		if (localRoot != null) {
			inOrder(localRoot.left);
			System.out.println(localRoot);
			inOrder(localRoot.right);
		}
	}

	private void postOrder(Node localRoot) {
		if (localRoot != null) {
			postOrder(localRoot.left);
			postOrder(localRoot.right);
			System.out.println(localRoot);
		}
	}

	public void display() {
		Stack<Node> globalStack = new Stack<Node>();
		globalStack.push(root);
		int nBlanks = 32;
		boolean isRowEmpty = false;
		System.out.println(".....................................");
		while (isRowEmpty == false) {
			Stack<Node> localStack = new Stack<Node>();
			isRowEmpty = true;
			for (int j = 0; j < nBlanks; j++) {
				System.out.print(" ");
			}
			while (globalStack.isEmpty()==false) {
				Node temp = globalStack.pop();
				if (temp != null) {
					System.out.print(temp.data);
					localStack.push(temp.left);
					localStack.push(temp.right);
					if (temp.left != null || temp.right != null) {
						isRowEmpty = false;
					} else {
						System.out.print("--");
						localStack.push(null);
						localStack.push(null);
					}
					for (int j = 0; j < nBlanks * 2 - 2; j++) {
						System.out.print(" ");
					}
				}
			}
			System.out.println();
			nBlanks /= 2;
			while (localStack.isEmpty()==false) {
				globalStack.push(localStack.pop());
			}
		}
		System.out.println(".......................................................");
	}
}
