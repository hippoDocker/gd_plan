package com.gd.base.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户角色关联表")
@Table(name="sys_menu_role")
@Entity
public class SysMenuRole implements Serializable {
    @Id
    @ApiModelProperty("主键ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty("菜单ID")
    @Column(name = "menu_id")
    private Long menuId;

    @ApiModelProperty("角色ID")
    @Column(name = "role_id")
    private Long roleId;

    @ApiModelProperty("状态")
    @Column(name = "state")
    private Long state;

    public SysMenuRole(Long menuId, Long roleId, Long state) {
        this.menuId = menuId;
        this.roleId = roleId;
        this.state = state;
    }
}
