package com.gd.base.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("用户表")
@Table(name="sys_user")
@Entity
public class SysUser implements Serializable {
    @Id
    @ApiModelProperty("用户ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @ApiModelProperty("用户学号")
    @Column(name = "user_code")
    private String userCode;

    @ApiModelProperty("用户姓名")
    @Column(name = "user_name")
    private String userName;

    @ApiModelProperty("用户手机号码")
    @Column(name = "phone")
    private String phone;

    @ApiModelProperty("用户邮箱")
    @Column(name = "email")
    private String email;

    @ApiModelProperty("用户登录密码")
    @Column(name = "user_pwd")
    private String userPwd;

    @ApiModelProperty("用户性别")
    @Column(name = "sex")
    private String sex;

    @ApiModelProperty("用户出生日期")
    @Column(name = "birthday")
    private LocalDateTime birthday;

    @ApiModelProperty("年级")
    @Column(name = "grade")
    private String grade;

    @ApiModelProperty("用户关联专业ID")
    @Column(name = "major_id")
    private Long majorId;

    @ApiModelProperty("用户关联学院ID")
    @Column(name = "college_id")
    private Long collegeId;

    @ApiModelProperty("用户关联班级ID")
    @Column(name = "class_id")
    private Long classId;


    @ApiModelProperty("用户学历")
    @Column(name = "education")
    private String education;

    @ApiModelProperty("用户创建时间")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @ApiModelProperty("用户信息更新时间")
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @ApiModelProperty("状态（0用户理论删除；1状态正常；2用户锁定）")
    @Column(name = "state")
    private Long state;

    @ApiModelProperty("用户登录密码错误次数")
    @Column(name = "pwd_err_time")
    private Long pwdErrTime;

    @ApiModelProperty("用户最后登录时间")
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty("用户头像")
    @Column(name = "head_img")
    private String headImg;

    @ApiModelProperty("学院")
    @Column(name = "college")
    private String college;

    @ApiModelProperty("学校ID")
    @Column(name = "school_id")
    private Long schoolId;

    @ApiModelProperty("创建人ID")
    @Column(name = "create_user_id")
    private Long createUserId;
}
