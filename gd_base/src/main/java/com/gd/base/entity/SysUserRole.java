package com.gd.base.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ApiModel("用户角色关联表")
@Table(name="sys_user_role")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRole implements Serializable {
    @Id
    @ApiModelProperty("主键ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty("用户ID")
    @Column(name = "user_id")
    private Long userId;

    @ApiModelProperty("角色ID")
    @Column(name = "role_id")
    private Long roleId;

    @ApiModelProperty("状态")
    @Column(name = "state")
    private Long state;
}
