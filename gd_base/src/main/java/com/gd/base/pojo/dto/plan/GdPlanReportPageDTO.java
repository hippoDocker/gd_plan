package com.gd.base.pojo.dto.plan;

import com.gd.base.pojo.dto.PageBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gd_plan
 * @description: TODO 报告\检测\论文相关的分页查询接口参数DTO
 * @author: tangxl
 * @create: 2022-03-09 08:58
 */
@Data
@ApiModel(value = "报告\\检测\\论文相关的分页查询接口参数DTO--学生DTO")
public class GdPlanReportPageDTO extends PageBaseInfo implements Serializable {
	@ApiModelProperty(name = "studentName",value = "学生姓名")
	private String studentName;

	@ApiModelProperty(name = "teacherName",value = "教师名称")
	private String teacherName;

	@ApiModelProperty(name = "designDirection",value = "设计方向")
	private String designDirection;

	@ApiModelProperty(name = "themeName",value = "设计主题")
	private String themeName;

	@ApiModelProperty(name = "examineState",value = "审核状态")
	private String examineState;

	@ApiModelProperty(name = "UserId",value = "用户ID")
	private Long UserId;

	@ApiModelProperty(name = "teacherId",value = "教师ID")
	private Long teacherId;
}
