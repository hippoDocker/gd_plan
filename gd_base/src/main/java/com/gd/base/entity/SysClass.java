package com.gd.base.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ApiModel(value = "班级表")
@Table(name="sys_class")
@Entity
public class SysClass implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    @ApiModelProperty(name="classgeId",value = "班级ID")
    private Long classId;
    @ApiModelProperty(name = "classCode",value = "班级编码")
    @Column(name = "class_code")
    private String classCode;
    @ApiModelProperty(name = "className",value = "班级名称")
    @Column(name = "class_name")
    private String className;
    @ApiModelProperty(name = "state",value = "状态")
    @Column(name = "state")
    private Long state;
    @ApiModelProperty(name = "parentId",value = "父级ID")
    @Column(name = "parent_id")
    private Long parentId;
}
