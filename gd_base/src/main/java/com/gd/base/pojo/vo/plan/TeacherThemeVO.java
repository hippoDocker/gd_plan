package com.gd.base.pojo.vo.plan;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.gd.base.enums.plan.IsChooseEnum;
import com.gd.base.enums.plan.ThemeSourceEunm;
import com.gd.base.enums.plan.UploadStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: gd_plan
 * @description: TODO 教师关联主题信息Vo
 * @author: tangxl
 * @create: 2022-03-02 20:29
 */
@ApiModel(value = "教师关联主题信息Vo")
@Data
public class TeacherThemeVO implements Serializable {
	@ApiModelProperty(name="Id",value = "序号")
	@ExcelProperty(value = "序号",index =0 )
	private Long Id;

	@ApiModelProperty(name = "teacherName",value = "教师姓名")
	@ExcelProperty(value = "教师姓名",index =1 )
	private String teacherName;

	@ApiModelProperty(name = "phone",value = "教师电话")
	@ExcelProperty(value = "教师电话",index =2 )
	private String phone;

	@ApiModelProperty(name = "email",value = "邮箱")
	@ExcelIgnore
	private String email;

	@ApiModelProperty(name = "designDirection",value = "设计方向")
	@ExcelProperty(value = "设计方向",index =3 )
	private String designDirection;

	@ApiModelProperty(name = "themeName",value = "主题名称")
	@ExcelProperty(value = "设计地址",index =4 )
	private String themeName;

	@ApiModelProperty(name = "themeSource",value = "主题来源")
	@ExcelProperty(value = "主题来源",index =5 )
	private String themeSource;

	public void setThemeSource(String themeSource) {
		this.themeSource = ThemeSourceEunm.getTypeNameByType(Long.valueOf(themeSource));
	}

	@ApiModelProperty(name = "remark",value = "备注")
	@ExcelProperty(value = "主题来源",index =6 )
	private String remark;

	@ApiModelProperty(name = "themeUploadState",value = "主题详情上传状态--（1：已上传；0：未上传）")
	@ExcelProperty(value = "主题详情上传状态",index =7 )
	private String themeUploadState;

	public void setThemeUploadState(String themeUploadState) {
		this.themeUploadState = UploadStateEnum.getTypeNameByType(Long.valueOf(themeUploadState));
	}

	@ApiModelProperty(name = "isChoose",value = "是否被选择且审核已通过--（0：未被选中；1：已被选择）")
	@ExcelIgnore
	private String isChoose;

	public void setIsChoose(String isChoose) {
		this.isChoose = IsChooseEnum.getTypeNameByType(StringUtils.isEmpty(isChoose)?3L: Long.valueOf(isChoose));
	}

	@ApiModelProperty(name = "overTime",value = "选题截止时间")
	@ExcelIgnore
	private LocalDateTime overTime;


}
