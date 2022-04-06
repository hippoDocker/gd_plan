package com.gd.base.pojo.dto.sys;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "用户批量导入接受类")
public class SysUserImportDTO implements Serializable {
    @ExcelProperty(value = "学号",index = 0)
    @ApiModelProperty("用户学号")
    private String userCode;

    @ExcelProperty(value = "姓名",index = 1)
    @ApiModelProperty("用户姓名")
    private String userName;

    @ExcelProperty(value = "手机号码",index = 2)
    @ApiModelProperty("用户手机号码")
    private String phone;

    @ExcelProperty(value = "邮箱",index = 3)
    @ApiModelProperty("用户邮箱")
    private String email;

    @ExcelProperty(value = "性别",index = 4)
    @ApiModelProperty("用户性别")
    private String sex;

//    @ExcelProperty(value = "出生日期",index = 5,converter = LocalDateTimeConverter.class)
    @ExcelProperty(value = "出生日期",index = 5)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("用户出生日期")
    private String birthday;

    @ExcelProperty(value = "学校",index = 6)
    @ApiModelProperty("用户学校")
    private String schoolName;

    @ExcelProperty(value = "学院",index = 7)
    @ApiModelProperty("用户学院")
    private String collegeName;

    @ExcelProperty(value = "专业",index = 8)
    @ApiModelProperty("用户专业")
    private String majorName;

    @ExcelProperty(value = "班级",index = 9)
    @ApiModelProperty("用户班级")
    private String className;

    @ExcelProperty(value = "学历",index = 10)
    @ApiModelProperty("用户学历")
    private String education;

    @ExcelProperty(value = "角色",index = 11)
    @ApiModelProperty("角色")
    private String roleName;

    @ExcelProperty(value = "年级",index = 12)
    @ApiModelProperty("年级")
    private String grade;
}
