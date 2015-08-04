package com.doer.moodle.dubbo;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DubboProvider {

	private static Logger logger = Logger.getLogger(DubboProvider.class);

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext(new String[] {
					"/spring/beans.xml", "/dubbo/provider.xml" });
			context.start();
            logger.info("dubbo provider started ... ");

			Thread.sleep(Long.MAX_VALUE);
		} catch (Throwable e) {
			logger.error(e);
			System.exit(1);
		} finally {
			if (context != null)
				context.close();
		}
	}

}
