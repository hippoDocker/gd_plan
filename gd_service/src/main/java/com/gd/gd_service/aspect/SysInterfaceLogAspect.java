package com.gd.gd_service.aspect;

import com.alibaba.fastjson.JSON;
import com.gd.gd_service.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @program: gd_plan
 * @description: TODO 系统接口日志切面
 * @author: liu yan
 * @create: 2022-03-01 11:49
 */

@Aspect
@Configuration
@Slf4j
public class SysInterfaceLogAspect {
	private static final String SYS_LOG_POINTCUT_EXPRESSION="execution(* com.gd.gd_service.controller.*.*(..)))";
	@Autowired
	private SysLogService sysLogService;

	@Pointcut(SYS_LOG_POINTCUT_EXPRESSION)
	public void sysLogPointCut() {

	}

	//方法的前置通知--2
	@Before("sysLogPointCut()")
	public void before(JoinPoint joinPoint) throws IOException {
		//获取请求报文头部元数据
		ServletRequestAttributes requestAttributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		//获取请求对象
		HttpServletRequest request=requestAttributes.getRequest();
		//接口信息打印
		log.info("接口请求");
		log.info("请求接口：{}",request.getRequestURI());
		log.info("请求方式：{}",request.getMethod());
		log.info("接口方法：{}",joinPoint.getSignature().getName());
		log.info("接口请求参数：{}", JSON.toJSONString(request.getParameterMap()));
		//保存日志
		sysLogService.addsysInterfaceLog(request,joinPoint.getArgs());

	}

	//后置通知--3
	@AfterReturning("sysLogPointCut()")
	public void afterMethod(){
	}

	//异常通知
	@AfterThrowing("sysLogPointCut()")
	public void afterThrowing()throws Throwable{

	}
	//最终通知--4
	@After("sysLogPointCut()")
	public void finalMethod(){
	}

	//环绕通知--1
	@Around("sysLogPointCut()")
	public Object aroundMethod(ProceedingJoinPoint pjp) throws Throwable{
		return  pjp.proceed();
	}


}
