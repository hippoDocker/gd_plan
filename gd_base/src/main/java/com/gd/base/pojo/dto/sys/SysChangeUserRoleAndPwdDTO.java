package com.gd.base.pojo.dto.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "修改用户角色权限和密码的DTO")
@Data
public class SysChangeUserRoleAndPwdDTO {
    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户新密码")
    private String userPwd;

    @ApiModelProperty("角色ID")
    private Long roleId;
}
