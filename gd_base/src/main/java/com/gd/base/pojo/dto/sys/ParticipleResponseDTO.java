package com.gd.base.pojo.dto.sys;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther: tangxl
 * @Date: 2022年3月30日19:29:17
 * @Description: 智能分词请求返回DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "智能分词请求返回DTO")
public class ParticipleResponseDTO implements Serializable {
    @ApiModelProperty(name = "t", value = "分词")
    private String t;

    @ApiModelProperty(name = "p", value = "准确概率")
    private String p;

}
