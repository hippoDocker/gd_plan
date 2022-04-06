package com.gd.base.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "系统接口日志")
@Table(name="sys_interface_log")
@Entity
public class SysInterfaceLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(name="id",value = "日志ID")
    private Long id;
    @ApiModelProperty(name = "userCode",value = "用户学号")
    @Column(name = "user_code")
    private String userCode;
    @ApiModelProperty(name = "userCode",value = "用户姓名")
    @Column(name = "user_Name")
    private String userName;
    @ApiModelProperty(name = "interfaceUrl",value = "请求接口")
    @Column(name = "interface_url")
    private String interfaceUrl;
    @ApiModelProperty(name = "method",value = "请求方式")
    @Column(name = "method")
    private String method;
    @ApiModelProperty(name = "param",value = "参数")
    @Column(name = "param")
    private String param;
    @ApiModelProperty(name = "ip",value = "IP地址")
    @Column(name = "ip")
    private String ip;
    @ApiModelProperty(name = "createTime",value = "操作时间")
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @ApiModelProperty(name = "phone",value = "电话")
    @Column(name = "phone")
    private String phone;
}
