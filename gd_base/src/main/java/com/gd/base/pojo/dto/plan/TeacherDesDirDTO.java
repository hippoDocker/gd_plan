package com.gd.base.pojo.dto.plan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 新增毕业设计方向新增删除接口入参Dto
 * @author: tangxl
 * @create: 2022-03-05 14:05
 */
@ApiModel(value = "新增毕业设计方向新增删除接口入参Dto")
@Data
public class TeacherDesDirDTO implements Serializable {
	@ApiModelProperty(name = "ids",value = "设计方向ID集合")
	private List<Long> ids;

	@ApiModelProperty(name = "id",value = "设计方向ID")
	private Long id;

	@ApiModelProperty(name = "designDirection",value = "设计方向")
	private String designDirection;


}
