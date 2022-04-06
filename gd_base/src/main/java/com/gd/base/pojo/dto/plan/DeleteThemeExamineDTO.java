package com.gd.base.pojo.dto.plan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 取消主题审核接口入参参数DTO
 * @author: tangxl
 * @create: 2022-03-08 21:40
 */
@Data
@ApiModel(value = "取消主题审核接口入参参数DTO")
public class DeleteThemeExamineDTO implements Serializable {
	@ApiModelProperty(name = "ids",value = "主题详情ID集合")
	private List<Long> ids;
}
