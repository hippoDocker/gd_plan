package com.gd.base.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ApiModel(value = "学校表")
@Table(name="sys_school")
@Entity
public class SysSchool implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_id")
    @ApiModelProperty(name="schoolId",value = "学校ID")
    private Long schoolId;
    @ApiModelProperty(name = "schoolCode",value = "学校编码")
    @Column(name = "school_code")
    private String schoolCode;
    @ApiModelProperty(name = "schoolName",value = "学校名称")
    @Column(name = "school_name")
    private String schoolName;
    @ApiModelProperty(name = "state",value = "状态")
    @Column(name = "state")
    private Long state;
    @ApiModelProperty(name = "parentId",value = "父级ID")
    @Column(name = "parent_id")
    private Long parentId;
}
