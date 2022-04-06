package com.gd.base.pojo.vo.plan;

import com.alibaba.excel.annotation.ExcelProperty;
import com.gd.base.enums.plan.ExamineStateEunm;
import com.gd.base.enums.plan.UploadStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计详细信息汇总分页查询返回参数VO
 * @author: tangxl
 * @create: 2022-03-13 16:59
 */
@Data
@ApiModel(value = "毕业设计详细信息汇总分页查询返回参数VO")
public class GdPlanDetailPageVO implements Serializable {

	@ApiModelProperty(name="Id",value = "序号")
	@ExcelProperty(value = "序号",index = 0)
	private Long Id;

	@ApiModelProperty(name = "userCode",value = "学号")
	@ExcelProperty(value = "学号",index = 1)
	private String userCode;

	@ExcelProperty(value = "学生",index = 2)
	@ApiModelProperty(name = "studentName",value = "学生")
	private String studentName;

	@ExcelProperty(value = "学生电话",index = 3)
	@ApiModelProperty(name = "studentPhone",value = "学生电话")
	private String studentPhone;

	@ExcelProperty(value = "教师",index = 4)
	@ApiModelProperty(name = "teacherName",value = "教师")
	private String teacherName;

	@ExcelProperty(value = "教师电话",index = 5)
	@ApiModelProperty(name = "teacherPhone",value = "教师电话")
	private String teacherPhone;

	@ExcelProperty(value = "设计方向",index = 6)
	@ApiModelProperty(name = "designDirection",value = "设计方向")
	private String designDirection;

	@ExcelProperty(value = "设计主题",index = 7)
	@ApiModelProperty(name = "themeName",value = "设计主题")
	private String themeName;

	@ExcelProperty(value = "设计主题要求上传状态",index = 8)
	@ApiModelProperty(name = "themeUpState",value = "设计主题要求上传状态（1：已上传；0：未上传）")
	private String themeUpState;
	public void setThemeUploadState(String themeUpStat){
		this.themeUpState = UploadStateEnum.getTypeNameByType(Long.valueOf(themeUpStat));
	}

	@ExcelProperty(value = "主题审核状态",index = 9)
	@ApiModelProperty(name = "examineState",value = "主题审核状态（0-未审核；1--审核通过；2--审核失败)")
	private String  examineState;
	public void setExamineState(String examineState) {
		this.examineState = ExamineStateEunm.getTypeNameByType(Long.valueOf(examineState));
	}

	@ExcelProperty(value = "开题报告要求上传状态",index = 10)
	@ApiModelProperty(name = "openRequireState",value = "开题报告要求上传状态（1：已上传；0：未上传)")
	private String openRequireState;
	public void setOpenRequireState(String  openRequireState){
		this.openRequireState = UploadStateEnum.getTypeNameByType(Long.valueOf(openRequireState));
	}

	@ExcelProperty(value = "开题报告上传状态",index = 11)
	@ApiModelProperty(name = "openReportState",value = "开题报告上传状态（1：已上传；0：未上传)")
	private String  openReportState;
	public void setOpenReportState(String  openReportState){
		this.openReportState = UploadStateEnum.getTypeNameByType(Long.valueOf(openReportState));
	}


	@ExcelProperty(value = "开题报告审核状态",index = 12)
	@ApiModelProperty(name = "openExamineState",value = "开题报告审核状态（0-未审核；1--审核通过；2--审核失败)")
	private String  openExamineState;
	public void setOpenExamineState(String openExamineState) {
		this.openExamineState = ExamineStateEunm.getTypeNameByType(Long.valueOf(openExamineState));
	}

	@ExcelProperty(value = "期中检测要求上传状态",index = 13)
	@ApiModelProperty(name = "midRequireState",value = "期中检测要求上传状态（1：已上传；0：未上传)")
	private String  midRequireState;
	public void setMidRequireState(String midRequireState) {
		this.midRequireState = UploadStateEnum.getTypeNameByType(Long.valueOf(midRequireState));
	}

	@ExcelProperty(value = "期中检测上传状态",index = 14)
	@ApiModelProperty(name = "midReportState",value = "期中检测上传状态（1：已上传；0：未上传)")
	private String  midReportState;
	public void setMidReportState(String midreportState) {
		this.midReportState = UploadStateEnum.getTypeNameByType(Long.valueOf(midreportState));
	}

	@ExcelProperty(value = "期中检测审核状态",index = 15)
	@ApiModelProperty(name = "midExamineState",value = "期中检测审核状态（0-未审核；1--审核通过；2--审核失败)")
	private String  midExamineState;
	public void setMidExamineState(String midreportState) {
		this.midExamineState = ExamineStateEunm.getTypeNameByType(Long.valueOf(midreportState));
	}

	@ExcelProperty(value = "论文要求上传状态",index = 16)
	@ApiModelProperty(name = "endRequireState",value = "论文要求上传状态（1：已上传；0：未上传)")
	private String  endRequireState;
	public void setEndRequireState(String endRequireState) {
		this.endRequireState = UploadStateEnum.getTypeNameByType(Long.valueOf(endRequireState));
	}

	@ExcelProperty(value = "论文上传状态",index = 17)
	@ApiModelProperty(name = "endReportState",value = "论文上传状态（1：已上传；0：未上传)")
	private String  endReportState;
	public void setEndReportState(String endReportState) {
		this.endReportState = UploadStateEnum.getTypeNameByType(Long.valueOf(endReportState));
	}

	@ExcelProperty(value = "成绩",index = 18)
	@ApiModelProperty(name = "score",value = "成绩")
	private String  score;

	@ExcelProperty(value = "免答辩状态申请上传状态",index = 19)
	@ApiModelProperty(name = "exemptUploadState",value = "免答辩状态申请上传状态（1：已上传；0：未上传)")
	private String  exemptUploadState;
	public void setExemptUploadState(String exemptUploadState) {
		this.exemptUploadState = UploadStateEnum.getTypeNameByType(Long.valueOf(exemptUploadState));
	}

	@ExcelProperty(value = "免答辩状态",index = 20)
	@ApiModelProperty(name = "exemptDefenseState",value = "免答辩状态（0-未审核；1--审核通过；2--审核失败)")
	private String  exemptDefenseState;
	public void setExemptDefenseState(String exemptDefenseState) {
		this.exemptDefenseState = ExamineStateEunm.getTypeNameByType(Long.valueOf(exemptDefenseState));
	}

	@ExcelProperty(value = "备注",index = 21)
	@ApiModelProperty(name = "remark",value = "备注")
	private String remark;
}
