package com.gd.base.pojo.dto.sys;

import com.gd.base.pojo.dto.PageBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "系统用户新增DTO")
@Data
public class SysUserAddDTO extends PageBaseInfo   implements Serializable {
    @ApiModelProperty("用户学号")
    private Long userId;

    @ApiModelProperty("用户学号")
    private String userCode;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("用户手机号码")
    private String phone;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("用户登录密码")
    private String userPwd;

    @ApiModelProperty("用户性别")
    private String sex;

    @ApiModelProperty("用户出生日期开始时间")
    private String birthdayStart;

    @ApiModelProperty("用户出生日期结束时间")
    private String birthdayEnd;

    @ApiModelProperty("用户出生日期")
    private String birthday;

    @ApiModelProperty("用户关联专业ID")
    private Long majorId;

    @ApiModelProperty("用户关联学院ID")
    private Long collegeId;

    @ApiModelProperty("用户关联班级ID")
    private Long classId;

    @ApiModelProperty("用户关联角色ID")
    private Long roleId;

    @ApiModelProperty("用户关联学校ID")
    private Long schoolId;

    @ApiModelProperty("专业")
    private String majorName;

    @ApiModelProperty("学院")
    private String collegeName;

    @ApiModelProperty("班级")
    private String className;

    @ApiModelProperty("角色")
    private String roleName;

    @ApiModelProperty("用户学历")
    private String education;

    @ApiModelProperty("年级")
    private String grade;

}
