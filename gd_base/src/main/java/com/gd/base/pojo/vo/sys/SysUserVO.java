package com.gd.base.pojo.vo.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel("返回前端的用户信息")
@Data
public class SysUserVO implements Serializable {
    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户学号")
    private String userCode;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("用户手机号码")
    private String phone;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("用户性别")
    private String sex;

    @ApiModelProperty("用户出生日期")
    private LocalDateTime birthday;

    @ApiModelProperty("用户关联专业ID")
    private Long majorId;



    @ApiModelProperty("用户关联学院ID")
    private Long collegeId;

    @ApiModelProperty("用户关联班级ID")
    private Long classId;

    @ApiModelProperty("用户学历")
    private String education;

    @ApiModelProperty("用户创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("用户信息更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("状态（0用户理论删除；1状态正常；2用户锁定）")
    private Long state;

    @ApiModelProperty("用户登录密码错误次数")
    private Long pwdErrTime;

    @ApiModelProperty("用户最后登录时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty("用户头像")
    private String headImg;
}
