package com.gd.base.annotation;

import java.lang.annotation.*;
/**
 * @Auther: tangxl
 * @Date: 2021年12月1日19:36:44
 * @Description: 系统日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyLog {
    String value() default  "";
}