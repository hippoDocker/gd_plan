package com.gd.base.pojo.vo.plan;

import com.gd.base.enums.plan.ExamineStateEunm;
import com.gd.base.enums.plan.ThemeSourceEunm;
import com.gd.base.enums.plan.UploadStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计审批详情分页查询结果--学生VO
 * @author: tangxl
 * @create: 2022-03-08 19:20
 */
@Data
@ApiModel(value = "毕业设计审批详情分页查询结果--学生VO")
public class GdPlanDetailStuPageVO implements Serializable {
	@ApiModelProperty(name="Id",value = "序号")
	private Long Id;

	@ApiModelProperty(name = "teacherName",value = "教师姓名")
	private String teacherName;

	@ApiModelProperty(name = "studentName",value = "选题学生")
	private String studentName;

	@ApiModelProperty(name = "designDirection",value = "设计方向")
	private String designDirection;

	@ApiModelProperty(name = "themeName",value = "设计主题")
	private String themeName;

	@ApiModelProperty(name = "themeSource",value = "主题来源")
	private String themeSource;

	public void setThemeSource(String themeSource) {
		this.themeSource = ThemeSourceEunm.getTypeNameByType(Long.valueOf(themeSource));
	}

	@ApiModelProperty(name = "phone",value = "教师电话")
	private String phone;

	@ApiModelProperty(name = "examineState",value = "主题审核状态（0-未审核；1--审核通过；2--审核失败)")
	private String  examineState;

	public void setExamineState(String examineState) {
		this.examineState = ExamineStateEunm.getTypeNameByType(Long.valueOf(examineState));
	}

	@ApiModelProperty(name = "themeUploadState",value = "主题详情上传状态（1：已上传；0：未上传）")
	private String themeUploadState;

	public void setThemeUploadState(String themeUploadState) {
		this.themeUploadState = UploadStateEnum.getTypeNameByType(Long.valueOf(themeUploadState));
	}

	@ApiModelProperty(name = "remark",value = "备注")
	private String remark;

}
