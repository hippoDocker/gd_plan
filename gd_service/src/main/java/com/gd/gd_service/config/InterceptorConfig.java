package com.gd.gd_service.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * @Description 添加放行路径，白名单
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TokenInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new TokenInterceptor());
        // 所有路径都被拦截
        registration.addPathPatterns("/**");
        //设置白名单，放行连接
        registration.excludePathPatterns(
                "/doc.html",    //放行swagger地址
                "/swagger-resources",      //swagger静态资源
                "/v2/api-docs",      //swagger静态资源
                "/webjars/**",      //swagger静态资源
                "/**/*.html",   //放行所有html静态资源
                "/**/*.js",     //放行所有js静态资源
                "/**/*.css",     //放行所有js静态资源
                "/sys/verificationcode",   //验证码接口
                "/sys/findHeadImg",       //头像接口
                "/sys/login",        //登录接口
                "/error",   //swagger错误接口
                "/sys/test/**", //测试接口
                "/*.html" ,     //静态文件
                "/**"  //测试放行所有接口
        );
    }
}
