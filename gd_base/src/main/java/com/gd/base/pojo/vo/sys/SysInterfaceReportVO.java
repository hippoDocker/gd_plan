package com.gd.base.pojo.vo.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gd_plan
 * @description: TODO 接口数据图形化报表数据接口返回参数VO
 * @author: tangxl
 * @create: 2022-03-22 14:21
 */
@Data
public class SysInterfaceReportVO implements Serializable {
	@ApiModelProperty(name="day",value = "天")
	private String day;
	@ApiModelProperty(name = "male",value = "人次")
	private Integer male;
	@ApiModelProperty(name = "female",value = "人数")
	private Integer female;
	@ApiModelProperty(name = "average",value = "接口调用次数")
	private Integer average;
}
