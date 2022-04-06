package com.gd.base.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ApiModel(value = "专业表")
@Table(name="sys_major")
@Entity
public class SysMajor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "major_id")
    @ApiModelProperty(name="majorId",value = "专业ID")
    private Long majorId;
    @ApiModelProperty(name = "majorCode",value = "专业编码")
    @Column(name = "major_code")
    private String majorCode;
    @ApiModelProperty(name = "majorName",value = "专业名称")
    @Column(name = "major_name")
    private String majorName;
    @ApiModelProperty(name = "state",value = "状态")
    @Column(name = "state")
    private Long state;
    @ApiModelProperty(name = "parentId",value = "父级ID")
    @Column(name = "parent_id")
    private Long parentId;
}
