package com.gd.base.pojo.dto.sys;

import com.gd.base.pojo.dto.PageBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "角色DTO实体类")
@Data
public class SysRoleDTO extends PageBaseInfo  implements Serializable {
    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("删除角色ID集合")
    private List<Long> roleIds;

    @ApiModelProperty("角色编码")
    private String roleCode;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("关联菜单Id集合")
    private List<Long> menuIds;
}
