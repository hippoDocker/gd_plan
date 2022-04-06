package com.gd.base.pojo.dto.plan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gd_plan
 * @description: TODO 上传相关接口入参参数DTO
 * @author: tangxl
 * @create: 2022-03-11 10:00
 */
@Data
@ApiModel(value = "上传相关接口入参参数DTO")
public class GdPlanUploadFileDTO implements Serializable {
	@ApiModelProperty(name = "id",value = "数据ID")
	private Long id;

	@ApiModelProperty(name = "type",value = "来源编码 1--开题报告,2--中期检测,3--毕业论文,4--免答辩申请")
	private Long type;

	@ApiModelProperty(name = "overTime",value = "截止时间")
	private String overTime;
}
