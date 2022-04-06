package com.gd.base.pojo.vo.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: gd_plan
 * @description: TODO 系统接口日志相关接口返回参数
 * @author: tangxl
 * @create: 2022-03-01 16:01
 */
@Data
@ApiModel("系统接口日志相关接口返回参数")
public class SysInterfaceLogVO implements Serializable {
	@ApiModelProperty(name="id",value = "日志ID")
	private Long id;
	@ApiModelProperty(name = "userCode",value = "用户学号")
	private String userCode;
	@ApiModelProperty(name = "userCode",value = "用户姓名")
	private String userName;
	@ApiModelProperty(name = "interfaceUrl",value = "请求接口")
	private String interfaceUrl;
	@ApiModelProperty(name = "method",value = "请求方式")
	private String method;
	@ApiModelProperty(name = "param",value = "参数")
	private String param;
	@ApiModelProperty(name = "ip",value = "IP地址")
	private String ip;
	@ApiModelProperty(name = "createTime",value = "操作时间")
	private LocalDateTime createTime;
}
