package com.gd.base.pojo.vo.plan;

import com.gd.base.enums.plan.ExamineStateEunm;
import com.gd.base.enums.plan.UploadStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: gd_plan
 * @description: TODO 报告\检测\论文分页查询返回数据VO-学生VO
 * @author: tangxl
 * @create: 2022-03-09 10:12
 */
@Data
@ApiModel(value = "报告\\检测\\论文分页查询返回数据VO")
public class GdPlanReportPageVO implements Serializable {
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

	@ApiModelProperty(name = "examineState",value = "主题审核状态（0-未审核；1--审核通过；2--审核失败)")
	private String  examineState;

	public void setExamineState(String examineState) {
		this.examineState = ExamineStateEunm.getTypeNameByType(Long.valueOf(examineState));
	}

	@ApiModelProperty(name = "themeUploadState",value = "要求文件上传状态（1：已上传；0：未上传）")
	private String themeUploadState;
	@ApiModelProperty(name = "requireUploadState",value = "要求上传状态")
	private String requireState;

	public void setRequireState(String requireState) {
		this.requireState = UploadStateEnum.getTypeNameByType(Long.valueOf(requireState));
	}

	@ApiModelProperty(name = "reportState",value = "报告文件上传状态")
	private String reportState;
	public void setReportState(String reportState) {
		this.reportState = UploadStateEnum.getTypeNameByType(Long.valueOf(reportState));
	}

	@ApiModelProperty(name = "overTime",value = "上传截止时间")
	private LocalDateTime overTime;

	@ApiModelProperty(name = "remark",value = "备注")
	private String remark;
}
