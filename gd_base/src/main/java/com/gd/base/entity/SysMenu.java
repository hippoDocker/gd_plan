package com.gd.base.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Table(name = "sys_menu")
@ApiModel(value = "菜单表")
@Entity
public class SysMenu implements Serializable {
    @Id
    @ApiModelProperty("菜单ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long menuId;

    @ApiModelProperty("菜单名称")
    @Column(name = "menu_name")
    private String menuName;

    @ApiModelProperty("菜单路径")
    @Column(name = "menu_url")
    private String menuUrl;

    @ApiModelProperty("菜单图标")
    @Column(name = "menu_icon")
    private String menuIcon;

    @ApiModelProperty("菜单描述")
    @Column(name = "menu_type")
    private String menuType;

    @ApiModelProperty("菜单描述")
    @Column(name = "menu_description")
    private String menuDescription;

    @ApiModelProperty("创建时间")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @ApiModelProperty("菜单创建人")
    @Column(name = "create_by")
    private String createBy;

    @ApiModelProperty("最后修改时间")
    @Column(name = "last_modify_time")
    private LocalDateTime lastModifyTime;

    @ApiModelProperty("最后修改人")
    @Column(name = "last_modify_by")
    private String lastModifyBy;

    @ApiModelProperty("状态")
    @Column(name = "state")
    private Long state;

    @ApiModelProperty("菜单父级id")
    @Column(name = "parent_id")
    private Long parentId;


}
