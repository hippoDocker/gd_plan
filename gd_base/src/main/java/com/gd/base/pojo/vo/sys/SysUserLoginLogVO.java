package com.gd.base.pojo.vo.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: gd_plan
 * @description: TODO 用户登录日志相关接口返回参数
 * @author: tangxl
 * @create: 2022-03-01 16:00
 */
@Data
@ApiModel("用户登录日志相关接口返回参数")
public class SysUserLoginLogVO implements Serializable {
	@ApiModelProperty("主键ID")
	private Long Id;

	@ApiModelProperty("登录用户ID")
	private Long loginUserId;

	@ApiModelProperty("登录用户姓名")
	private String LoginUserName;

	@ApiModelProperty("用户登录时间")
	private LocalDateTime loginDate;
}
