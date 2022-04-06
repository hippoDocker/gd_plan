package com.gd.gd_service.controller;

/**
 * @Auther: tangxl
 * @Date: 2021年11月30日14:38:07
 * @Description: 登录相关接口
 */

import com.gd.base.constant.RedisKeyConstants;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.sys.SysLonginDTO;
import com.gd.base.util.RedisUtil;
import com.gd.base.util.VerifyCodeUtil;
import com.gd.base.pojo.vo.redis.RedisSysUser;
import com.gd.gd_service.service.SysLoginService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Log4j2
@Api(value = "系统登录",tags = "系统登录")
@RestController
@RequestMapping("/sys")
public class SysLoginController {
    @Autowired
    private SysLoginService sysLoginService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private VerifyCodeUtil verifyCodeUtil;

    @ApiOperation(value = "登录接口",notes = "用户2小时失效,学生--账号：手机号/学号，密码：默认学号后六位(如果学号不够6位，默认123456)；  教师--账号：手机号，密码：默认123456;   管理员--账号：手机号，密码：默认superAdmin")
    @PostMapping("/login")
    @ApiResponses({
            @ApiResponse(code = 200,message = "调用成功！",response = RedisSysUser.class)
    })
    public CommonResult sysLogin(@RequestBody SysLonginDTO sysLonginDto){
        CommonResult loginResult = sysLoginService.saveSysLogin(sysLonginDto.getUserCode().trim(),sysLonginDto.getUserPwd().trim(),sysLonginDto.getImgCode().trim(),sysLonginDto.getKey());
        return loginResult;
    }
    @GetMapping("/verificationcode")
    @ApiOperation(value = "生成验证码",notes = "验证码30秒失效")
    public ResponseEntity<byte[]> VerificationCode(@RequestParam String key) throws IOException {
        //初始化header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        /**
         * 生成随机验证码及图片
         * Object[0]：验证码字符串；
         * Object[1]：验证码图片 data-uri 格式。
         */
        //随机生成验证码
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage verifyImg=new BufferedImage(70,30,BufferedImage.TYPE_INT_RGB);
        Object[] verifyCode = verifyCodeUtil.createImage(outputStream);
        log.info("=====>>验证码为："+verifyCode[0]);
        redisUtil.set(RedisKeyConstants.LOGIN_VALIDATE_KEY + key, verifyCode[0], 2L, TimeUnit.MINUTES);
        byte[] bs = outputStream.toByteArray();
        try{
            outputStream.close();
        }catch (Exception e){
            log.info("验证码生成失败：{}",e);
            throw new RuntimeException("验证码生成失败：{}",e);
        }
        return new ResponseEntity<>(bs,headers, HttpStatus.OK);
    }

    @GetMapping("/cleanUserCache")
    @ApiOperation(value = "用户注销登录", notes = "用户注销登录,token令牌失效")
    public CommonResult cleanUserCache(@RequestHeader String token){
        return sysLoginService.cleanUserCache(token);
    }
}
