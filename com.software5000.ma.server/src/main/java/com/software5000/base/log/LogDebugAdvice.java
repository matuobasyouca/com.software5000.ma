package com.software5000.base.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Spring 统一日志处理实现类
 * 
 */
@Aspect
@Component
public class LogDebugAdvice {

	private static Log log = LogFactory.getLog("MethodLogs");

	/**
	 * Pointcut 定义Pointcut，Pointcut名称为aspectjMethod,必须无参，无返回值 只是一个标识，并不进行调用
	 */
	@Pointcut("(execution(* com.zscp..*Controller.*(..))) || (execution(* com.zscp..*Task.*(..)))")
	private void aspectJMethod() {
	};

	@Around("aspectJMethod()")
	public Object doAround(ProceedingJoinPoint pjPoint) throws Throwable {
        long t1 = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug(" ->  BEGIN <- " + pjPoint.toLongString());// 方法前的操作
			for(int i = 0 ; i< pjPoint.getArgs().length ; i++)
				log.debug( " 	->  ARGS  <- " + pjPoint.toShortString() + " # args : " + pjPoint.getArgs()[i]);// 方法前的操作
		}
		Object retval = pjPoint.proceed();// 执行需要Log的方法
		if (log.isDebugEnabled()) {
			log.debug(" ->  END <- " + pjPoint.toLongString() +" . cost ["+(System.currentTimeMillis() - t1)+"] ms . ");// 方法后的操作
		}

		return retval;
	}
}