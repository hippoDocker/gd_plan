package com.gd.base.pojo.vo.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "学校管理相关接口返回参数")
@Data
public class SysSchoolVO implements Serializable {
	@ApiModelProperty(name="schoolId",value = "学校ID")
	private Long schoolId;
	@ApiModelProperty(name = "schoolCode",value = "学校编码")
	private String schoolCode;
	@ApiModelProperty(name = "schoolName",value = "学校名称")
	private String schoolName;
	@ApiModelProperty(name = "state",value = "状态")
	private Long state;
}
