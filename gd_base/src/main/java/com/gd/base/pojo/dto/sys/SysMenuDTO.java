package com.gd.base.pojo.dto.sys;

import com.gd.base.pojo.dto.PageBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: tangxl
 * @Date:2021年12月21日15:32:45
 * @Description: TODO 菜单接受参数对象
 */
@ApiModel(value = "菜单DTO实体类")
@Data
public class SysMenuDTO extends PageBaseInfo  implements Serializable {
    @ApiModelProperty("菜单Id")
    private Long menuId;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单路径")
    private String menuUrl;

    @ApiModelProperty("菜单图标")
    private String menuIcon;

    @ApiModelProperty("菜单类型")
    private String menuType;

    @ApiModelProperty("菜单描述")
    private String menuDescription;

    @ApiModelProperty("菜单父级id")
    private Long parentId;

    @ApiModelProperty("开始创建时间")
    private String createTimeBegin;

    @ApiModelProperty("结束创建时间")
    private String createTimeEnd;

    @ApiModelProperty("菜单创建人")
    private String createBy;

    @ApiModelProperty("学号")
    private String userCode;

    @ApiModelProperty("菜单集合")
    private List<Long> menuIds;

    @ApiModelProperty("手机号")
    private String phone;
}
