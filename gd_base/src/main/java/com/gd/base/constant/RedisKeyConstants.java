package com.gd.base.constant;
/**
 * @Auther: tangxl
 * @Date: 2021年12月13日16:17:56
 * @Description: TODO redis缓存常数
 */
public class RedisKeyConstants {
    /**
     * 登录验证码key
     */
    public static final String LOGIN_VALIDATE_KEY="sys:login:key";
    /**
     * 登录缓存信息
     */
    public static final String LOGIN_VALIDATE_SYS_USER="sys:login:user";
    /**
     * 登录用户code对应token
     */
    public static final String LOGIN_VALIDATE_SYS_TOKEN="sys:login:token";
}
