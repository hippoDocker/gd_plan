package com.gd.base.pojo.vo.plan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/**
 * @program: gd_plan
 * @description: TODO 历年设计主题推荐VO
 * @author: tangxl
 * @create: 2022年4月1日21:23:26
 */
@Data
@ApiModel(value = "历年设计主题推荐VO")
public class HistoryDesignThemeVO implements Serializable {
    @ApiModelProperty(name="id",value = "序号")
    private Long id;

    @ApiModelProperty(name = "type",value = "专业类别")
    private String  type;

    @ApiModelProperty(name = "name",value = "主题名称")
    private String  name;

    @ApiModelProperty(name = "detailUrl",value = "主题详情地址")
    private String  detailUrl;

    @ApiModelProperty(name = "detailText",value = "主题详情介绍")
    private String  detailText;

    @ApiModelProperty(name = "imgUrl",value = "图片地址")
    private String  imgUrl;
}
