package com.gd.base.annotation;

import com.alibaba.fastjson.JSON;
import com.gd.base.entity.SysInterfaceLog;
import com.gd.base.jpa.SysInterfaceLogDao;
import com.gd.base.util.DateUtil;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
/**
 * @Auther: tangxl
 * @Date: 2021年12月1日19:31:49
 * @Description: 切面实现系统日志
 */
@Aspect
@Component
@Log4j2
public class SysLogAspect {
    @Autowired
    SysInterfaceLogDao sysInterfaceLogDao;
    @Autowired
    HttpServletRequest httpServletRequest;

    //定义切点@pointcut
    //在注解的位置切入代码
    @Pointcut("@annotation(com.gd.base.annotation.MyLog)")
    public void logPoinCut() {

    }

    //切面 配置通知
    @AfterReturning("logPoinCut()")
    public void savesysInterfaceLog(JoinPoint joinPoint) {
        //保存日志
        SysInterfaceLog sysInterfaceLog = new SysInterfaceLog();
        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取接入点所在方法
        Method method = signature.getMethod();

        //获取操作
        MyLog myLog = method.getAnnotation(MyLog.class);

        //获取请求类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = method.getName();
        sysInterfaceLog.setMethod(className + "." + methodName);

        //请求参数
        Object[] args = joinPoint.getArgs();
        //将参数所在的数组转为json
        String params = JSON.toJSONString(args);
        sysInterfaceLog.setParam(params);
        //设置调用时间
        sysInterfaceLog.setCreateTime(DateUtil.dateToLocalDateTime(new Date()));
        //获取用户名
        sysInterfaceLog.setUserCode("txl");
        //获取用户ip地址
        String ip = httpServletRequest.getRemoteAddr();
        System.out.println(ip);
        sysInterfaceLog.setIp(httpServletRequest.getRemoteAddr());

        //保存sysInterfaceLog实体类到数据库
        sysInterfaceLog = sysInterfaceLogDao.save(sysInterfaceLog);
        log.info("记录系统日志:"+sysInterfaceLog);
        Long sysInterfaceLogCount = sysInterfaceLogDao.count();
        log.info("累计记录系统日志"+sysInterfaceLogCount+"条");
    }

    //通过IP地址获取MAC地址

}
