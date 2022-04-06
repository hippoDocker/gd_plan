package com.gd.base.pojo.dto.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "静态类型Dto实体类")
public class SysStaticTypeDTO implements Serializable {

    @ApiModelProperty("静态类型ID")
    private Long staticId;

    @ApiModelProperty("静态编码")
    private String staticCode;

    @ApiModelProperty("静态类型名称")
    private String staticName;

    @ApiModelProperty("备用字段一")
    private String remarkOne;

    @ApiModelProperty("备用字段二")
    private String remarkTwo;

    @ApiModelProperty("状态")
    private Long state;

}
