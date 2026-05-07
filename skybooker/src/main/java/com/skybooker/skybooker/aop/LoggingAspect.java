package com.skybooker.skybooker.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
      
	@Before("execution(* com.skybooker.skybooker.service.*.*(..))")
	public void beforeLog(JoinPoint joinPoint) {
		log.debug("Entering {} with args {}",joinPoint.getSignature().getName(),joinPoint.getArgs());
	}
	
	   
	@After("execution(* com.skybooker.skybooker.service.*.*(..))")
     public void afterLog(JoinPoint joinPoint) {
		log.debug("Exiting "+joinPoint.getSignature().getName());
	}
	
}
