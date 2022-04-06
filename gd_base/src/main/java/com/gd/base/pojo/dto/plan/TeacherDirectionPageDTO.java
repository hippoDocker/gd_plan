package com.gd.base.pojo.dto.plan;

import com.gd.base.pojo.dto.PageBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gd_plan
 * @description: TODO 教师关联设计方向分页查询入参Dto
 * @author: tangxl
 * @create: 2022-03-02 20:38
 */
@ApiModel(value = "教师关联设计方向分页查询入参Dto")
@Data
public class TeacherDirectionPageDTO extends PageBaseInfo  implements Serializable {
	@ApiModelProperty(name = "teacherName",value = "教师姓名")
	private String teacherName;

	@ApiModelProperty(name = "phone",value = "教师电话")
	private String phone;

	@ApiModelProperty(name = "designDirection",value = "设计方向")
	private String designDirection;
}
