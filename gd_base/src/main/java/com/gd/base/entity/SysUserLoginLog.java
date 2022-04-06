package com.gd.base.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("用户表")
@Table(name="sys_user_login_log")
@Entity
public class SysUserLoginLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键ID")
    @Column(name = "id")
    private Long Id;

    @ApiModelProperty("登录用户ID")
    @Column(name = "login_user_id")
    private Long loginUserId;

    @ApiModelProperty("登录用户姓名")
    @Column(name = "login_user_name")
    private String LoginUserName;

    @ApiModelProperty("用户登录时间")
    @Column(name = "login_date")
    private LocalDateTime loginDate;
}
