package com.gd.base.pojo.dto.sys;

import com.gd.base.pojo.dto.PageBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gd_plan
 * @description: TODO 系统接口日志相关接口接受参数
 * @author: tangxl
 * @create: 2022-03-01 15:51
 */
@ApiModel(value = "系统接口日志相关接口入参接受参数")
@Data
public class SysInterfaceLogDTO extends PageBaseInfo implements Serializable {
	@ApiModelProperty(name = "userCode",value = "用户学号")
	private String userCode;
	@ApiModelProperty(name = "userName",value = "用户姓名")
	private String userName;
	@ApiModelProperty(name = "interfaceUrl",value = "请求接口")
	private String interfaceUrl;
	@ApiModelProperty(name = "method",value = "请求方式")
	private String method;
	@ApiModelProperty(name = "ip",value = "IP地址")
	private String ip;
	@ApiModelProperty(name = "createTimeStart",value = "开始操作时间")
	private String createTimeStart;
	@ApiModelProperty(name = "createTimeEnd",value = "结束操作时间")
	private String createTimeEnd;
}
