package com.gd.base.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
/**
 * @program: gd_plan
 * @description: TODO 历年设计主题推荐
 * @author: tangxl
 * @create: 2022年4月1日21:23:26
 */
@Data
@ApiModel(value = "历年设计主题推荐")
@Table(name="history_design_theme")
@Entity
public class HistoryDesignTheme implements Serializable {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(name="id",value = "主键ID")
    private Long id;

    @ApiModelProperty(name = "type",value = "专业类别")
    @Column(name = "type")
    private String  type;

    @ApiModelProperty(name = "name",value = "主题名称")
    @Column(name = "name")
    private String  name;

    @ApiModelProperty(name = "detailUrl",value = "主题详情地址")
    @Column(name = "detail_url")
    private String  detailUrl;

    @ApiModelProperty(name = "detailText",value = "主题详情介绍")
    @Column(name = "detail_text")
    private String  detailText;

    @ApiModelProperty(name = "imgUrl",value = "图片地址")
    @Column(name = "img_url")
    private String  imgUrl;

}
