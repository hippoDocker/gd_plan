package com.gd.base.pojo.vo.plan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gd_plan
 * @description: TODO 毕设信息得分情况分页查询返回参数VO
 * @author: tangxl
 * @create: 2022-03-10 15:23
 */
@Data
@ApiModel(value = "毕设信息得分情况分页查询返回参数VO")
public class GdPlanScorePageVO implements Serializable {
	@ApiModelProperty(name="id",value = "序号")
	private Long id;

	@ApiModelProperty(name = "studentCode",value = "学号")
	private String studentCode;

	@ApiModelProperty(name = "studentName",value = "学生")
	private String studentName;

	@ApiModelProperty(name = "majorName",value = "专业")
	private String majorName;

	@ApiModelProperty(name = "className",value = "班级")
	private String className;

	@ApiModelProperty(name = "teacherName",value = "教师")
	private String teacherName;

	@ApiModelProperty(name = "phone",value = "教师电话")
	private String phone;

	@ApiModelProperty(name = "email",value = "教师邮箱")
	private String email;

	@ApiModelProperty(name = "designDirection",value = "论文方向")
	private String designDirection;

	@ApiModelProperty(name = "themeName",value = "论文主题")
	private String themeName;

	@ApiModelProperty(name = "instrScore",value = "指导老师打分")
	private String instrScore;

	@ApiModelProperty(name = "defGroScore",value = "答辩组打分")
	private String defGroScore;

	@ApiModelProperty(name = "score",value = "总分")
	private String score;

	@ApiModelProperty(name = "remark",value = "备注")
	private String remark;
}
