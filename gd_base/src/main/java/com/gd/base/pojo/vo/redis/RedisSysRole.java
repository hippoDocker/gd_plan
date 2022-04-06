package com.gd.base.pojo.vo.redis;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "redis角色信息")
public class RedisSysRole implements Serializable {

    @ApiModelProperty("角色ID")
    private Long id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("状态")
    private Long state;

    @ApiModelProperty("创建人")
    private String createBy;
}
