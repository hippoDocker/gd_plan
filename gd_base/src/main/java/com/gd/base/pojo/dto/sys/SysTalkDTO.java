package com.gd.base.pojo.dto.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: tangxl
 * @Date: 2022年3月30日19:29:17
 * @Description: 智能机器人交流返回DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "智能机器人交流返回DTO")
public class SysTalkDTO {
    @ApiModelProperty(name = "result", value = "是否是智能回答 0--智能回答,1--连接超时系统回答")
    private Integer result;

    @ApiModelProperty(name = "content", value = "机器人回答")
    private String content;

}
