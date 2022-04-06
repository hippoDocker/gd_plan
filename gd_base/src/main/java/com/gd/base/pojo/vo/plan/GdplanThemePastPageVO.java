package com.gd.base.pojo.vo.plan;

import com.gd.base.enums.plan.ExamineStateEunm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gd_plan
 * @description: TODO 主题审核数据分页查询返回参数VO
 * @author: tangxl
 * @create: 2022-03-10 18:39
 */
@Data
@ApiModel(value = "主题审核数据分页查询返回参数VO")
public class GdplanThemePastPageVO implements Serializable {
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

	@ApiModelProperty(name = "phone",value = "学生电话")
	private String phone;

	@ApiModelProperty(name = "email",value = "学生邮箱")
	private String email;

	@ApiModelProperty(name = "teacherName",value = "教师")
	private String teacherName;

	@ApiModelProperty(name = "designDirection",value = "论文方向")
	private String designDirection;

	@ApiModelProperty(name = "themeName",value = "论文主题")
	private String themeName;

	@ApiModelProperty(name = "examineState",value = "主题审核状态（0-未审核；1--审核通过；2--审核失败)")
	private String  examineState;

	public void setExamineState(String examineState) {
		this.examineState = ExamineStateEunm.getTypeNameByType(Long.valueOf(examineState));
	}

}
