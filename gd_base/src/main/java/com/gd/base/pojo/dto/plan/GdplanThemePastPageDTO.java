package com.gd.base.pojo.dto.plan;

import com.gd.base.pojo.dto.PageBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gd_plan
 * @description: TODO 主题审核数据Dto
 * @author: tangxl
 * @create: 2022-03-10 18:38
 */
@Data
@ApiModel(value = "主题审核数据Dto")
public class GdplanThemePastPageDTO extends PageBaseInfo implements Serializable {
	@ApiModelProperty(name = "id",value = "主题数据ID")
	private Long id;

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

	@ApiModelProperty(name = "userId",value = "用户ID")
	private Long userId;
}
