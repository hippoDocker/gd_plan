package com.gd.base.pojo.dto.plan;

import com.gd.base.pojo.dto.PageBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gd_plan
 * @description: TODO 历年设计主题推荐DTO
 * @author: tangxl
 * @create: 2022年4月1日21:23:26
 */
@Data
@ApiModel(value = "历年设计主题推荐DTO")
public class HistoryDesignThemeDTO  extends PageBaseInfo implements Serializable {
    @ApiModelProperty(name = "type",value = "专业类别")
    private String  type;

}
