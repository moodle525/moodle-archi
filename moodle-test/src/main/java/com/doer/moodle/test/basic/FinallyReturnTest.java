package com.doer.moodle.test.basic;

//http://cmsblogs.com/?p=1574
public class FinallyReturnTest {
	public static class Test{
		public int call(){
			int i = 0;
			try {
				return ++i;
			} catch (Exception e) {
			}finally {
				System.out.println("finally before ++:"+i);
				i++;
				System.out.println("finally after ++:"+i);
			}
			return i;
		}
	}
	
	public static void main(String[] args){
		Test t = new Test();
		int i = t.call();
		System.out.println(i);
	}
	

}

