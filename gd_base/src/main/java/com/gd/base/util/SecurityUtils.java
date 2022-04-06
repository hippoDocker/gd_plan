package com.gd.base.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * @Auther: tangxl
 * @Date:2021年12月14日11:14:40
 * @Description: TODO  BCryptPasswordEncoder密码加密工具类
 */
@Log4j2
public class SecurityUtils {
    /**
     * @Description:TODO 生成BCryptPasswordEncoder密码--加密中间方法
     * @param password 密码
     * @return 加密字符串
     */
    public static String encodePassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * @Description:TODO 判断密码是否相同--加密中间方法
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * @Description:TODO 登录密码加密
     * @param password 需要加密的密码
     * @return
     */
    public static String encryptionPwd(String password){
        //BCryptPasswordEncoder 加密
        String pwdEncOne = encodePassword(password);
        //jasypt加密
        StringBuffer pwdEncTwo = new StringBuffer(EncryptConfigUtil.encryptPwd(pwdEncOne));
        //第三\六位加下划线
        pwdEncTwo.insert(2,"_");
        pwdEncTwo.insert(5,"_");
        return pwdEncTwo.toString();
    }

    /**
     * @Description:TODO 登录密码判断是否相同
     * @param rawPassword 未加密密码
     * @param encodedPassword 加密后的密码
     * @return
     */
    public static boolean checkPwd(String rawPassword,String encodedPassword){
        StringBuffer pwdEncOne = new StringBuffer(encodedPassword);
        //删除第三\六位的下划线
        pwdEncOne.deleteCharAt(5);
        pwdEncOne.deleteCharAt(2);
        //对加密的密码进行jasypt解密
        String pwdEnc = EncryptConfigUtil.decryptPwd(pwdEncOne.toString());
        // BCryptPasswordEncoder 校验
        return matchesPassword(rawPassword,pwdEnc);
    }

    /**
     * @Description:TODO 测试
     * @param args
     */
    public static void main(String[] args) {
        String pwd = "12dsa_ds@";
        System.out.println("原密码:" + pwd);
        System.out.println("加密后：" + encryptionPwd(pwd));
        System.out.println("密码校验结果" + checkPwd(pwd, encryptionPwd(pwd)));
//
//        String pwd2 = "2+_nB_6rttAsaHlodSwyEOS/N5ORj4uKDWobSjLWuyEd6e2FNFo6ViKmJR2sNS5QLEdkf6JYgNGJar9R/NRdZJi0DZcktDlMtK";
//        System.out.println("密码校验结果" + checkPwd("123456", "Kp_0P_MMWXhwcYzPw25HBur16EsjkFvHADUy0/89OckbtX2cA8gqv1+ev92QwJJfE4HSUAWgz0HO0PdoJ3vTzc3DKyLu9spZ+E"));
    }

}
