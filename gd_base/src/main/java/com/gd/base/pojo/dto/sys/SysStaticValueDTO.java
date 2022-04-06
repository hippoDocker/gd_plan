package com.gd.base.pojo.dto.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "静态值DTO实体类")
public class SysStaticValueDTO implements Serializable {

    @ApiModelProperty("静态值ID")
    private Long staticValueId;

    @ApiModelProperty("静态类型ID")
    private Long staticId;

    @ApiModelProperty("静态名称")
    private String valueName;

    @ApiModelProperty("静态值")
    private String value;

    @ApiModelProperty("备用字段一")
    private String remarkOne;

    @ApiModelProperty("备用字段二")
    private String remarkTwo;

    @ApiModelProperty("状态")
    private Long state;
}
