package com.gd.base.pojo.dto.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: tangxl
 * @Date: 2022年1月28日10:21:37
 * @Description: 登录传入参数接口
 */
@ApiModel(value = "登录DTO实体类")
@Data
public class SysLonginDTO implements Serializable {
    @ApiModelProperty("用户登录账号(手机号/学号)")
    private String userCode;

    @ApiModelProperty("用户登录密码")
    private String userPwd;

    @ApiModelProperty("验证码")
    private String imgCode;

    @ApiModelProperty("同验证码的code")
    private String key;
}
