package com.gd.base.pojo.vo.redis;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.gd.base.pojo.vo.sys.SysMenuVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Auther: tangxl
 * @Date:2021年12月13日10:00:12
 * @Description: TODO redis存放的用户信息
 */
@Data
@ApiModel(value = "redis用户信息")
public class RedisSysUser implements Serializable {
    @ApiModelProperty("用户ID")
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

    @ApiModelProperty("用户出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime birthday;

    @ApiModelProperty("用户关联学校ID")
    private Long schoolId;

    @ApiModelProperty("用户学校")
    private String schoolName;

    @ApiModelProperty("用户关联学院ID")
    private Long collegeId;

    @ApiModelProperty("用户学院")
    private String collegeName;

    @ApiModelProperty("用户关联专业ID")
    private Long majorId;

    @ApiModelProperty("用户专业")
    private String majorName;

    @ApiModelProperty("用户关联班级ID")
    private Long classId;

    @ApiModelProperty("用户班级")
    private String className;

    @ApiModelProperty("用户学历")
    private String education;

    @ApiModelProperty("关联角色ID")
    private Long roleId;

    @ApiModelProperty("角色")
    private String roleName;

    @ApiModelProperty("用户创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    @ApiModelProperty("用户信息更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;

    @ApiModelProperty("状态（0用户理论删除；1状态正常；2用户锁定）")
    private Long state;

    @ApiModelProperty("用户登录密码错误次数")
    private Long pwdErrTime;

    @ApiModelProperty("用户头像")
    private String headImg;

    @ApiModelProperty("用户最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastLoginTime;

    @ApiModelProperty("用户登录令牌")
    private String token;

    @ApiModelProperty("用户关联菜单")
    private List<SysMenuVO> sysMenuVOList;

}
