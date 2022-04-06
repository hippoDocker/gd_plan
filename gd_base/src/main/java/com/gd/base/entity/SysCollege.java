package com.gd.base.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ApiModel(value = "学院表")
@Table(name="sys_college")
@Entity
public class SysCollege implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "college_id")
    @ApiModelProperty(name="collegeId",value = "学院ID")
    private Long collegeId;
    @ApiModelProperty(name = "collegeCode",value = "学院编码")
    @Column(name = "college_code")
    private String collegeCode;
    @ApiModelProperty(name = "collegeName",value = "学院名称")
    @Column(name = "college_name")
    private String collegeName;
    @ApiModelProperty(name = "state",value = "状态")
    @Column(name = "state")
    private Long state;
    @ApiModelProperty(name = "parentId",value = "父级ID")
    @Column(name = "parent_id")
    private Long parentId;

}
