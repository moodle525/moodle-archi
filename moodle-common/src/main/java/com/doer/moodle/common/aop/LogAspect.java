package com.doer.moodle.common.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.doer.moodle.common.web.R;

/**
 * 
 * @author lixiongcheng
 *
 */
@Aspect
@Component
public class LogAspect {

	@Autowired
	HttpServletRequest request;

	/**
	 * 定义一个方法，用于声明切入点表达式。一般的，该方法中再不需要添加其他的代码
	 */
	@Pointcut(value = "@annotation(com.doer.moodle.common.annotation.SystemLog)")
	public void controllerAspect() {
	}

	@Before(value = "controllerAspect()")
	public void beafore(JoinPoint joinPoint) {
		System.out.println("before.............");
	}

	/**
	 * 无论该方法是否出现异常,都执行
	 * 
	 * @param joinPoint
	 */
	@After(value = "controllerAspect()")
	public void after(JoinPoint joinPoint) {
		System.out.println("after.............");
	}

	/**
	 * 方法正常结束后执行的代码 返回通知是可以访问到方法的返回值的
	 */
	@AfterReturning(value = "controllerAspect()", returning = "result")
	public void afterReturning(JoinPoint joinPoint, Object result) {
		if (result instanceof R) {
			R<?> r = (R<?>) result;
			System.out.println(r.getData());
		}
	}

	/**
	 * 环绕通知需要携带ProceedingJoinPoint类型的参数
	 * 环绕通知类似于动态代理的全过程：ProceedingJoinPoint类型的参数可以决定是否执行目标方法。
	 * 而且环绕通知必须有返回值，返回值即为目标方法的返回值
	 * @throws Throwable 
	 * @throws ClassNotFoundException 
	 */
//	@Around(value = "controllerAspect()")
//	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//		System.out.println("around");
//		return joinPoint.proceed();
//	}

	@AfterThrowing(value = "controllerAspect()")
	public void afterThrowing() {
		System.out.println("afterThrowing..............");
	}
}
