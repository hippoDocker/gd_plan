package com.gd.base.pojo.vo.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "专业管理相关接口返回参数")
@Data
public class SysMajorVO implements Serializable {
	@ApiModelProperty(name="majorId",value = "专业ID")
	private Long majorId;
	@ApiModelProperty(name = "majorCode",value = "专业编码")
	private String majorCode;
	@ApiModelProperty(name = "majorName",value = "专业名称")
	private String majorName;
	@ApiModelProperty(name = "state",value = "状态")
	private Long state;
}
