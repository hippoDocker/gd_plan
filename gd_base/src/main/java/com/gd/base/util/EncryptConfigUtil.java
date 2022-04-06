package com.gd.base.util;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * @Auther: tangxl
 * @Date: 
 * @Description:jasypt实现yml配置文件加密解密
 */

public class EncryptConfigUtil {
    /**
     * @Description:TODO 密码加密
     * @param password 未加密密码
     * @return
     */
    public static String encryptPwd(String password){
        BasicTextEncryptor encryptor = getEnc();
        return encryptor.encrypt(password);
    }

    /**
     * @Description:TODO  密码解密
     * @param encryptPwd
     * @return
     */
    public static String decryptPwd(String encryptPwd){
        BasicTextEncryptor encryptor = getEnc();
        return encryptor.decrypt(encryptPwd);
    }

    /**
     * @Description:TODO 初始化加密密码
     * @return
     */
    public static BasicTextEncryptor getEnc(){
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加密所需的salt
        textEncryptor.setPassword("zagjdhjkasjkd");
        return  textEncryptor;
    }

    public static void main(String[] args) {
        BasicTextEncryptor textEncryptor = getEnc();
        //要加密的数据（数据库的用户名或密码）
        String username = textEncryptor.encrypt("159230@Txl");
        String password = textEncryptor.encrypt("基于Springboot+Vue的高校毕业设计管理");
        System.out.println("username:"+username);
        System.out.println("password:"+password);
    }
}
