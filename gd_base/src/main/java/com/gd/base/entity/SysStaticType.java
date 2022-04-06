package com.gd.base.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ApiModel(value = "静态类型表")
@Table(name="sys_static_type")
@Entity
public class SysStaticType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("静态类型ID")
    @Column(name = "static_id")
    private Long staticId;

    @ApiModelProperty("静态编码")
    @Column(name = "static_code")
    private String staticCode;

    @ApiModelProperty("静态类型名称")
    @Column(name = "static_name")
    private String staticName;

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
