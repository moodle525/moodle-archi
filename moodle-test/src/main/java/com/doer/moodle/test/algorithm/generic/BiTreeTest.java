package com.doer.moodle.test.algorithm.generic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

public class BiTreeTest {
	public String getString() throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		return br.readLine();
	}

	public char getChar() throws IOException {
		return getString().charAt(0);
	}

	public int getInt() throws IOException {
		return Integer.parseInt(getString());
	}

	BiTree tree = new BiTree();

	@Before
	public void init() {
		tree.insert(50);
		tree.insert(25);
		tree.insert(75);
		tree.insert(12);
		tree.insert(37);
		tree.insert(43);
		tree.insert(30);
		tree.insert(33);
		tree.insert(87);
		tree.insert(93);
		tree.insert(97);
	}

	@Test
	public void main() throws IOException {
		Integer data = null;
		while (true) {
			System.out.print("输入操作类型:show(s),insert(i),find(f),delete(d),traverse(t):");
			char choice = getChar();
			switch (choice) {
			case 's':// 显示
				tree.display();
				break;
			case 'i':// 插入
				System.out.print("输入插入值:");
				data = getInt();
				tree.insert(data);
				break;
			case 'f':// 查找
				System.out.print("输入查找值:");
				data = getInt();
				Node res = tree.find(data);
				if (res != null) {
					System.out.println("结果：" + res);
				} else {
					System.out.println("没找到数据:" + data);
				}
				break;
			case 'd':// 删除
				System.out.print("输入删除值:");
				data = getInt();
				boolean del = tree.delete(data);
				if (del) {
					System.out.println("删除成功:" + data);
				} else {
					System.out.println("删除失败:" + data);
				}
				break;
			case 't':// 遍历
				System.out.print("输入遍历方式1，2，3:");
				data = getInt();
				tree.traverse(data);
				break;
			default:
				System.out.println("无效操作");
				break;
			}
		}
	}
}
