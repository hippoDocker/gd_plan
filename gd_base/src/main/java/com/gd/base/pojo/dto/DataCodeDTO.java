package com.gd.base.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther: tangxl
 * @Date: 2022年2月16日15:36:37
 * @Description: 编码数据封装
 */
@ApiModel(value = "code:data数据通用类")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataCodeDTO implements Serializable {
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("值")
    private String data;
}
