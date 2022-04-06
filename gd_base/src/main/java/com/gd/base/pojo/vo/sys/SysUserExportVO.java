package com.gd.base.pojo.vo.sys;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Auther: tangxl
 * @Date: 2022年2月17日15:49:07
 * @Description: 用户信息分页查询导出之类Vo
 */
@ApiModel("用户分页查询以及导入导出Vo")
@Data
public class SysUserExportVO implements Serializable {
    @ExcelProperty(value = "序号",index = 0)
    @ApiModelProperty("用户ID")
    private Long userId;

    @ExcelProperty(value = "学号",index = 1)
    @ApiModelProperty("用户学号")
    private String userCode;

    @ExcelProperty(value = "姓名",index = 2)
    @ApiModelProperty("用户姓名")
    private String userName;

    @ExcelProperty(value = "手机号码",index = 3)
    @ApiModelProperty("用户手机号码")
    private String phone;

    @ExcelProperty(value = "邮箱",index = 4)
    @ApiModelProperty("用户邮箱")
    private String email;

    @ExcelProperty(value = "性别",index = 5)
    @ApiModelProperty("用户性别")
    private String sex;

    @ExcelProperty(value = "出生日期",index = 6)
    @ApiModelProperty("用户出生日期")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime birthday;

    @ExcelProperty(value = "专业",index = 7)
    @ApiModelProperty("用户专业")
    private String majorName;

    @ExcelProperty(value = "学院",index = 8)
    @ApiModelProperty("用户学院")
    private String collegeName;

    @ExcelProperty(value = "班级",index = 9)
    @ApiModelProperty("用户班级")
    private String className;

    @ExcelProperty(value = "学历",index = 10)
    @ApiModelProperty("用户学历")
    private String education;

    @ExcelProperty(value = "角色",index = 11)
    @ApiModelProperty("角色")
    private String roleName;

    @ExcelProperty(value = "创建时间",index = 12)
    @ApiModelProperty("创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("状态（0用户理论删除；1状态正常；2用户锁定）")
    @ExcelIgnore
    private Long state;

    @ApiModelProperty("用户登录密码错误次数")
    @ExcelIgnore
    private Long pwdErrTime;

    @ApiModelProperty("用户头像")
    @ExcelIgnore
    private String headImg;


}
