package com.gd.base.pojo.vo.plan;

import com.gd.base.enums.plan.ExamineStateEunm;
import com.gd.base.enums.plan.UploadStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gd_plan
 * @description: TODO 免答辩数据分页查询返回结果VO
 * @author: tangxl
 * @create: 2022-03-09 15:23
 */
@Data
@ApiModel(value = "免答辩数据分页查询返回结果VO")
public class GdPlanExemptDefensePageVO implements Serializable {
	@ApiModelProperty(name="Id",value = "序号")
	private Long Id;

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

	@ApiModelProperty(name = "defenseState",value = "免答辩申请审核状态（0-未审核；1--审核通过；2--审核失败)")
	private String  defenseState;

	public void setDefenseState(String defenseState) {
		this.defenseState = ExamineStateEunm.getTypeNameByType(Long.valueOf(defenseState));
	}

	@ApiModelProperty(name = "uploadState",value = "免答辩材料文件上传状态")
	private String uploadState;
	public void setUploadState(String uploadState) {
		this.uploadState = UploadStateEnum.getTypeNameByType(Long.valueOf(uploadState));
	}

	@ApiModelProperty(name = "remark",value = "备注")
	private String remark;
}
