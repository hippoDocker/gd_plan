package com.gd.base.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "设计方向表")
@Table(name="gd_design_direction")
@Entity
public class GdDesignDirection implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(name="id",value = "主键ID")
    private Long id;
    @ApiModelProperty(name = "designDirection",value = "设计方向")
    @Column(name = "design_direction")
    private String designDirection;
    @ApiModelProperty(name = "sysUserId",value = "关联教师ID")
    @Column(name = "sys_user_id")
    private Long sysUserId;
    @ApiModelProperty(name = "themeNum",value = "关联主题个数")
    @Column(name = "theme_num")
    private Long themeNum;
    @ApiModelProperty(name = "themeMargin",value = "关联主题空余量个数")
    @Column(name = "theme_margin")
    private Long themeMargin;
    @ApiModelProperty(name = "remark",value = "备注")
    @Column(name = "remark")
    private String remark;
    @ApiModelProperty(name = "createTime",value = "创建时间")
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @ApiModelProperty(name = "state",value = "状态")
    @Column(name = "state")
    private Long state;

}
