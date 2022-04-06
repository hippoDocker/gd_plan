package com.gd.base.pojo.vo.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "学院管理相关接口返回参数")
@Data
public class SysCollegeVO implements Serializable {
	@ApiModelProperty(name="collegeId",value = "学院ID")
	private Long collegeId;
	@ApiModelProperty(name = "collegeCode",value = "学院编码")
	private String collegeCode;
	@ApiModelProperty(name = "collegeName",value = "学院名称")
	private String collegeName;
	@ApiModelProperty(name = "state",value = "状态")
	private Long state;
}
