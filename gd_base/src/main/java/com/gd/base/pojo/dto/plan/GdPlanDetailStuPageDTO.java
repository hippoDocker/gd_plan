package com.gd.base.pojo.dto.plan;

import com.gd.base.pojo.dto.PageBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计审核详情分页查询参数--学生DTO
 * @author: tangxl
 * @create: 2022-03-08 19:55
 */
@Data
@ApiModel(value = "毕业设计审核详情分页查询参数--学生DTO")
public class GdPlanDetailStuPageDTO extends PageBaseInfo implements Serializable {
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
}
