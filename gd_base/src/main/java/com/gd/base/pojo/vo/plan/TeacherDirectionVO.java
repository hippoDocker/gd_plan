package com.gd.base.pojo.vo.plan;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gd_plan
 * @description: TODO 教师关联主题分页查询Vo
 * @author: tangxl
 * @create: 2022-03-02 20:19
 */
@ApiModel(value = "教师关联设计方向分页查询Vo")
@Data
public class TeacherDirectionVO implements Serializable {
	@ApiModelProperty(name="Id",value = "序号")
	@ExcelProperty(value = "序号",index =0 )
	private Long Id;

	@ApiModelProperty(name = "teacherName",value = "教师姓名")
	@ExcelProperty(value = "教师姓名",index =1 )
	private String teacherName;

	@ApiModelProperty(name = "phone",value = "教师电话")
	@ExcelProperty(value = "联系方式",index =3 )
	private String phone;

	@ApiModelProperty(name = "email",value = "邮箱")
	@ExcelIgnore
	private String email;

	@ApiModelProperty(name = "designDirection",value = "设计方向")
	@ExcelProperty(value = "设计方向",index =2 )
	private String designDirection;

	@ApiModelProperty(name = "themeNum",value = "主题个数")
	@ExcelProperty(value = "主题个数",index = 4)
	private Long themeNum;

	@ApiModelProperty(name = "themeMargin",value = "主题余量(除去已审核)")
	@ExcelProperty(value = "余量",index =5 )
	private Long themeMargin;


}
