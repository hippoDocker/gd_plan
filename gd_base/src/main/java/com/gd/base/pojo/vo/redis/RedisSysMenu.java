package com.gd.base.pojo.vo.redis;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "redis菜单信息")
public class RedisSysMenu implements Serializable {
    @ApiModelProperty("菜单ID")
    private Long menuId;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单路径")
    private String menuUrl;

    @ApiModelProperty("菜单图标")
    private String menuIcon;

    @ApiModelProperty("菜单描述")
    private String menuType;

    @ApiModelProperty("菜单描述")
    private String menuDescription;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("菜单创建人")
    private String createBy;

    @ApiModelProperty("最后修改时间")
    private LocalDateTime lastModifyTime;

    @ApiModelProperty("最后修改人")
    private String lastModifyBy;

    @ApiModelProperty("状态")
    private Long state;

    @ApiModelProperty("菜单父级id")
    private Long parentId;
}
