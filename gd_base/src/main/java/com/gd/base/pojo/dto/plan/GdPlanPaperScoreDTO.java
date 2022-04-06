package com.gd.base.pojo.dto.plan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计论文打分入参参数DTO
 * @author: tangxl
 * @create: 2022-03-11 10:42
 */
@Data
@ApiModel(value = "毕业设计论文打分入参参数DTO")
public class GdPlanPaperScoreDTO implements Serializable {
	@ApiModelProperty(name = "id",value = "数据ID")
	private Long id;

	@ApiModelProperty(name = "instructorScore",value = "指导老师分数")
	private String instructorScore;

	@ApiModelProperty(name = "defenseGroupScore",value = "答辩组分数")
	private String  defenseGroupScore;
}
