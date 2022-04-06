package com.gd.base.pojo.vo.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: tangxl
 * @Date: 2022年3月30日19:29:17
 * @Description: 设计主题词频统计请求返回VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "设计主题词频统计请求返回VO")
public class ParticipleCountVO {
    @ApiModelProperty(name = "name", value = "词语")
    private String name;

    @ApiModelProperty(name = "value", value = "次数")
    private Long value;
}
