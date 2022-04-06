package com.gd.gd_service.aspect;

import com.gd.base.util.BeanUtil;
import com.gd.base.pojo.vo.redis.RedisSysUser;
import com.gd.gd_service.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: gd_plan
 * @description: TODO token校验切面
 * @author: tangxl
 * @create: 2022-03-01 18:35
 */
@Aspect
@Slf4j
@Configuration
public class CheckTokenAspect {
	private static final String TOKEN_POINTCUT_EXPRESSION="execution(* com.gd.gd_service.controller.*.*(..)))";
	@Autowired
	private RedisService redisService;
	@Pointcut(TOKEN_POINTCUT_EXPRESSION)
	public void sysLogPointCut() {

	}

	//方法的前置通知--2
	@Before("sysLogPointCut()")
	public void before(JoinPoint joinPoint){
		//获取请求报文头部元数据
		ServletRequestAttributes requestAttributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		//获取请求对象
		HttpServletRequest request=requestAttributes.getRequest();
		HttpServletResponse response = requestAttributes.getResponse();
		//token校验
		String token = request.getHeader("token");
		if(StringUtils.isBlank(token)){
			return;
		}else {
			RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
			if(BeanUtil.isEmpty(redisSysUser)){
				throw new RuntimeException("用户信息失效，请重新登录！");
			}
		}


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
