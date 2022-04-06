package com.gd.base.util;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: tangxl
 * @Date: 2022年2月13日12:37:06
 * @Description: 字符串工具类
 */
public class StringUtil {
    /**
     * TODO 邮箱校验
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)){
            return false;
        }
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(email);
        if(m.matches()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * TODO 手机号校验
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone){
        if (null==phone || "".equals(phone)){
            return false;
        }
        if (phone.matches("^1(3|4|5|6|7|8|9)\\d{9}$")){
            return true;
        }
        return false;
    }

    /**
     * TODO 生成一个编码格式为UTF-8的字符串
     * @param str
     * @return
     */
    public static String creatNewString(String str){
        return new String(str.getBytes(StandardCharsets.UTF_8));
    }
}
