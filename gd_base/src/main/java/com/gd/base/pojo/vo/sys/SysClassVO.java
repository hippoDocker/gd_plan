package com.gd.base.pojo.vo.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "班级管理相关接口返回参数")
@Data
public class SysClassVO implements Serializable {
	@ApiModelProperty(name="classId",value = "班级ID")
	private Long classId;
	@ApiModelProperty(name = "classCode",value = "班级编码")
	private String classCode;
	@ApiModelProperty(name = "className",value = "班级名称")
	private String className;
	@ApiModelProperty(name = "state",value = "状态")
	private Long state;
}
