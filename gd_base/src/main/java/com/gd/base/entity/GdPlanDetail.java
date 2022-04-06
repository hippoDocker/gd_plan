package com.gd.base.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计详情表
 * @author: tangxl
 * @create: 2022-03-08 16:43
 */
@Data
@ApiModel(value = "毕业设计详情表")
@Table(name="gd_plan_detail")
@Entity
public class GdPlanDetail implements Serializable {
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	@ApiModelProperty(name="id",value = "主键ID")
	private Long id;

	@ApiModelProperty(name = "userId",value = "关联学生ID")
	@Column(name = "user_id")
	private Long userId;

	@ApiModelProperty(name = "gdDesignThemeId",value = "关联设计主题ID")
	@Column(name = "gd_design_theme_id")
	private Long gdDesignThemeId;

	@ApiModelProperty(name = "createTime",value = "创建时间")
	@Column(name = "create_time")
	private LocalDateTime createTime;

	@ApiModelProperty(name = "sysUserId",value = "关联教师ID")
	@Column(name = "sys_user_id")
	private Long sysUserId;

	@ApiModelProperty(name = "createBy",value = "创建人")
	@Column(name = "create_by")
	private String  createBy;

	@ApiModelProperty(name = "examineState",value = "主题审核状态（0-未审核；1--审核通过；2--审核失败;3--取消审核")
	@Column(name = "examine_state")
	private Long examineState;

	@ApiModelProperty(name = "openReportState",value = "开题报告上传状态（0--未上传；1--已上传）")
	@Column(name = "open_report_state")
	private Long openReportState;

	@ApiModelProperty(name = "openRequireState",value = "开题报告上传状态（0--未上传；1--已上传）")
	@Column(name = "open_require_state")
	private Long openRequireState;

	@ApiModelProperty(name = "openRequireBatch",value = "开题报告要求文件批次号")
	@Column(name = "open_require_batch")
	private String  openRequireBatch;

	@ApiModelProperty(name = "openReportBatch",value = "开题报告文件批次号")
	@Column(name = "open_report_batch")
	private String openReportBatch;

	@ApiModelProperty(name = "openOverTime",value = "开题报告文件上传截止时间")
	@Column(name = "open_over_time")
	private LocalDateTime openOverTime;

	@ApiModelProperty(name = "openExamineState",value = "开题报告审核状态")
	@Column(name = "open_examine_state")
	private Long openExamineState;

	@ApiModelProperty(name = "midReportState",value = "中期报告文件上传状态（0--未上传；1--已上传）")
	@Column(name = "mid_report_state")
	private Long midReportState;

	@ApiModelProperty(name = "midRequireState",value = "中期报告要求上传状态（0--未上传；1--已上传）")
	@Column(name = "mid_require_state")
	private Long midRequireState;

	@ApiModelProperty(name = "midRequireBatch",value = "中期报告要求文件批次号")
	@Column(name = "mid_require_batch")
	private String midRequireBatch;

	@ApiModelProperty(name = "midReportBatch",value = "中期报告文件批次号")
	@Column(name = "mid_report_batch")
	private String midReportBatch;

	@ApiModelProperty(name = "midExamineState",value = "中期报告审核状态")
	@Column(name = "mid_examine_state")
	private Long midExamineState;

	@ApiModelProperty(name = "midOverTime",value = "中期报告文件上传截止时间")
	@Column(name = "mid_over_time")
	private LocalDateTime midOverTime;

	@ApiModelProperty(name = "endRequireState",value = "毕业论文要求上传状态")
	@Column(name = "end_require_state")
	private Long endRequireState;

	@ApiModelProperty(name = "midRequireBatch",value = "毕业论文文件批次号")
	@Column(name = "end_require_batch")
	private String endRequireBatch;

	@ApiModelProperty(name = "endReportState",value = "毕业论文上传状态")
	@Column(name = "end_report_state")
	private Long endReportState;

	@ApiModelProperty(name = "endReportBatch",value = "毕业论文文件批次号")
	@Column(name = "end_report_batch")
	private String endReportBatch;

	@ApiModelProperty(name = "endOverTime",value = "毕业论文文件上传截止时间")
	@Column(name = "end_over_time")
	private LocalDateTime endOverTime;

	@ApiModelProperty(name = "instructorScore",value = "指导老师打分")
	@Column(name = "instructor_score")
	private String instructorScore;

	@ApiModelProperty(name = "exemptDefenseState",value = "免除答辩状态（0--审核中；1--审核通过；2--审核失败）")
	@Column(name = "exempt_defense_state")
	private Long exemptDefenseState;

	@ApiModelProperty(name = "exemptUploadState",value = "免答辩申请文档上传状态")
	@Column(name = "exempt_upload_state")
	private Long exemptUploadState;

	@ApiModelProperty(name = "exemptUploadBatch",value = "免答辩申请文档批次号")
	@Column(name = "exempt_upload_batch")
	private String exemptUploadBatch;

	@ApiModelProperty(name = "defenseGroupScore",value = "答辩组打分")
	@Column(name = "defense_group_score")
	private String defenseGroupScore;

	@ApiModelProperty(name = "score",value = "答辩总分")
	@Column(name = "score")
	private String score;

	@ApiModelProperty(name = "state",value = "数据状态（0-失效；1-有效）")
	@Column(name = "state")
	private Long state;

}
