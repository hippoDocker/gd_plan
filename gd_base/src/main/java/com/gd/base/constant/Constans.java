package com.gd.base.constant;
/**
 * @Auther: tangxl
 * @Date:2021年12月14日08:55:18
 * @Description: TODO 常用常量
 */
public class Constans {
    /**
     * @Description: TODO 登录校验返回常量
     */
    public static final String SYS_LOGIN_CODE_ONE = "账号为空";
    public static final String SYS_LOGIN_CODE_TWO = "密码为空";
    public static final String SYS_LOGIN_CODE_THREE = "验证码为空";
    public static final String SYS_LOGIN_CODE_FOUR = "验证码失效";
    public static final String SYS_LOGIN_CODE_FIVE = "验证码错误";
    public static final String SYS_LOGIN_CODE_SIX = "账号不唯一,请联系管理员!";
    public static final String SYS_LOGIN_CODE_SEVEN = "账号不存在";
    public static final String SYS_LOGIN_CODE_EIGHT = "密码错误";
    public static final String SYS_LOGIN_CODE_NINE = "用户已锁定,请联系管理员解锁!";
    public static final String SYS_LOGIN_CODE_TEN = "用户注销";
    public static final String SYS_LOGIN_CODE_ELEVEN = "该用户未授权任何角色";
    public static final String SYS_LOGIN_CODE_TWELVE = "登录成功！";
    public static final String SYS_USER_TOKEN_OVER = "用户信息失效，请重新登录！";


    /**
     * 默认日期时间格式
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_TIME_FORMAT2 = "yyyy-MM-dd HH:mm";
    public static final String SMS_DATE_TIME_FORMAT = "yyyy年MM月dd日";
    public static final String SMS_DATE_TIME_FORMAT2 = "yyyy年MM月";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_FORMAT2 = "yyyyMMdd";
    public static final String DEFAULT_Month_FORMAT = "yyyy-MM";
    public static final String DEFAULT_DATE_TIME_FORMAT3 = "yyyyMMddHHmmssSSS";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    public static final String DEFAULT_TIME_FORMAT3 = "yyyy/MM/dd";
    public static final String DEFAULT_TIME_FORMAT4 = "yyyy/MM/dd HH/mm/ss";

    /**
     * FTP常量
     */
    public static final String HEAD_IMG_DEFAULT_NAME="head_img.jpeg";
    public static final String HEAD_IMG_PATH = "/gd_plan_ftp/head_img";
    public static final String THEME_DETAIL_PATH = "/gd_plan_ftp/gd_theme_detail";

    /**
     * 用户头像获取url
     */
    public static final String HEAD_IMG_URL = "http://localhost:8085/api/sys/findHeadImg?phone=";

    /**
     * 角色和用户关联的静态属性编码
     */
    public static final String SYS_ROLE_MENU = "SYS_ROLE_MENU";

    /**
     * 静态值类型编码
     */
    public static final String USER_PWD_STATIC = "USER_PWD";
    public static final String STUDENT_PWD_STATIC = "学生初始密码";
    public static final String TEACHER_PWD_STATIC = "教师初始密码";
    public static final String ADMIN_PWD_STATIC = "管理员初始密码";
    public static final String IS_DEFAULT_PWD = "IS_DEFAULT_PWD";
}
