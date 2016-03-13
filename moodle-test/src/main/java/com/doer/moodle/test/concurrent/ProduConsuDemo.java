package com.doer.moodle.test.concurrent;

public class ProduConsuDemo {
	public static void main(String[] args) {
		Res res = new Res();
		Input in = new Input(res);
		Output out = new Output(res);
		new Thread(in).start();
		new Thread(out).start();
	}
}

class Res {
	private String name;
	private String sex;
	private boolean flag;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}

class Input implements Runnable {
	private Res res;

	public Input(Res res) {
		this.res = res;
	}

	@Override
	public void run() {
		int i = 0;
		while (true) {
			try {
				synchronized (res) {
					Thread.sleep(100);
					if (res.isFlag()) {
						if (i == 0) {
							res.setName("张三");
							res.setSex("男");
						} else {
							res.setName("lisi");
							res.setSex("female");
						}
					} else {
						wait();
					}
					res.setFlag(true);
					notify();
					i = (i + 1) % 2;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

class Output implements Runnable {
	private Res res;

	public Output(Res res) {
		this.res = res;
	}

	@Override
	public void run() {
		while (true) {
			try {
				synchronized (res) {
					Thread.sleep(100);
					if (!res.isFlag()) {
						System.out.println(res.getName() + "....."
								+ res.getSex());
					} else {
						wait();
					}
					res.setFlag(false);
					notify();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
