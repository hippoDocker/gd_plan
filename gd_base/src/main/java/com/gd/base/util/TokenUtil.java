package com.gd.base.util;

import com.gd.base.entity.SysUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: tangxl
 * @Date: 2021年11月30日14:47:01
 * @Description: Token工具类
 */
public class TokenUtil {
    private static final long expireTime = 24*60*60*1000;  //失效时间(与redis失效时间相同)--24小时
    private static final String privateKey = "eyJleHAiOj";  //密钥

    /**
     * 签名生成（Token生成）
     * @param sysUser
     * @return
     */
    public static String sign(SysUser sysUser){
        String token = null;
        Map<String,Object> header = new HashMap<>();
        header.put("typ","jwt");
        header.put("alg","HS256");
        Map<String, Object> claims = new HashMap<>();
        //自定义有效载荷部分
        claims.put("userCode",sysUser.getUserCode());
        token = Jwts.builder()
                //发证人
                .setIssuer("auth")
                //Jwt头
                .setHeader(header)
                //有效载荷
                .setClaims(claims)
                //设定签发时间
                .setIssuedAt(new Date())
                //设定过期时间
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                //使用HS256算法签名，PRIVATE_KEY为签名密钥
                .signWith(SignatureAlgorithm.HS256,privateKey)
                .compact();
        return token;
    }

    /**
     * 验证 token信息 是否正确
     * @param token
     * @return
     */
    public static boolean verify(String token){
        //获取签名密钥
        //String key = userEntity.getUserPassword();
        //获取DefaultJwtParser
        try{
            Jwts.parser()
                    //设置 密钥
                    .setSigningKey(privateKey)
                    //设置需要解析的 token
                    .parseClaimsJws(token).getBody();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //测试
    public static void main(String[] args) {
        SysUser sysUser = new SysUser();
        sysUser.setUserCode("2018750336");
        String token = sign(sysUser);
        System.out.println(token);
        System.out.println(verify("eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NDYxMzc3MzEsImlhdCI6MTY0NjEzMDUzMSwidXNlckNvZGUiOiIyMDE4NzUwMzMwIn0.FbLGC7PRWnVJZZhtQ1aPd9zFZMSl6WuUpy8CJZ_0J38"));
        System.out.println(verify("eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NDYxMzc2NDUsImlhdCI6MTY0NjEzMDQ0NSwidXNlckNvZGUiOiIyMDE4NzUwMzMwIn0.TxWIBSOHHfm8I4amTmYm05zxX_SQLDD1PRu0H54Wj6E"));
    }
}
