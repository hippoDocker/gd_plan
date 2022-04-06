package com.gd.base.pojo.dto.plan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 教师设计主题新增删除接口入参Dto
 * @author: tangxl
 * @create: 2022-03-05 15:58
 */
@ApiModel(value = "教师设计主题新增删除接口入参Dto")
@Data
public class TeacherThemeDTO implements Serializable {
	@ApiModelProperty(name="Ids",value = "设计主题集合ID集合")
	private List<Long> Ids;
	@ApiModelProperty(name="Id",value = "设计主题集合ID")
	private Long Id;
	@ApiModelProperty(name = "gdDesignDirectionId",value = "关联设计方向ID")
	private Long gdDesignDirectionId;
	@ApiModelProperty(name = "themeName",value = "主题名称")
	private String themeName;
	@ApiModelProperty(name = "overTime",value = "选题截止时间")
	private String overTime;
	@ApiModelProperty(name = "themeSource",value = "主题来源")
	private String themeSource;
	@ApiModelProperty(name = "remark",value = "备注")
	private String remark;
}
