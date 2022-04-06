package com.gd.base.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ApiModel(value = "静态值表")
@Table(name="sys_static_value")
@Entity
public class SysStaticValue implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("静态值ID")
    @Column(name = "static_value_id")
    private Long staticValueId;

    @ApiModelProperty("静态类型ID")
    @Column(name = "static_type_id")
    private Long staticTypeId;

    @ApiModelProperty("静态名称")
    @Column(name = "value_name")
    private String valueName;

    @ApiModelProperty("静态值")
    @Column(name = "value")
    private String value;

    @ApiModelProperty("备用字段一")
    @Column(name = "remark_one")
    private String remarkOne;

    @ApiModelProperty("备用字段二")
    @Column(name = "remark_two")
    private String remarkTwo;

    @ApiModelProperty("状态")
    @Column(name = "state")
    private Long state;
}
