package com.gd.gd_service.config;

import com.alibaba.fastjson.JSON;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.gd_service.service.SysLogService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Auther: tangxl
 * @Date: 2021年11月30日16:58:21
 * @Description: 登录拦截器
 */
@Log4j2
@Component
public class TokenInterceptor implements HandlerInterceptor {
    /**
     * 重写 前置拦截方法
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Autowired
    SysLogService sysLogService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        /**
         * @Description: 跨域设置以及OPTIONS预请求
         */
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            log.info("OPTIONS请求，放行");
            return true;
        }
        /**
         * @Description: jwt的token校验
         */
        // 1、从请求头中获取token,解析token
        String token = request.getHeader("token");
        log.info("=====>>token信息："+token);
        // 2、判断 token 是否存在 TODO 去掉 jwt token校验，原因:jwt生成秘钥容易出现失效问题
//        if (StringUtils.isBlank(token) || !TokenUtil.verify(token)) {
        if (StringUtils.isBlank(token) ) {
            log.info("====>>用户未登录");
            setReturn(response,400,"用户信息失效，请重新登录！");
            return false;
        }else {

        }

        return true;
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
         //System.out.println("执行了TestInterceptor的postHandle方法");
    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//        System.out.println("执行了TestInterceptor的afterCompletion方法");
    }

    /**
     * @Description 设置返回消息的方法
     * @param response
     * @param status
     * @param msg
     * @throws IOException
     */
    private static void setReturn(HttpServletResponse response, int status, String msg) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        /* 星号表示所有的域都可以接受， */
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With");
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        httpResponse.setHeader("X-Powered-By","Jetty");
        //response.flushBuffer();
        //UTF-8编码
        httpResponse.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        //response.reset();
        CommonResult result = new CommonResult(false,status,msg);
        String json = JSON.toJSONString(result);
        PrintWriter printWriter = httpResponse.getWriter();
        printWriter.print(json);
        printWriter.flush();
        printWriter.close();
    }

}
