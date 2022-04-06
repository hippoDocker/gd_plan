package com.gd.base.pojo.dto.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计流程图形化报表返回参数DTO
 * @author: tangxl
 * @create: 2022-03-01 15:51
 */
@ApiModel(value = "毕业设计流程图形化报表返回参数DTO")
@Data
public class GdPlanProcessReportDTO {
    @ApiModelProperty(name = "day",value = "天")
    private String day;

    @ApiModelProperty(name = "themeNum",value = "主题审核数量")
    private String themeNum;

    @ApiModelProperty(name = "openNum",value = "开题报告审核数量")
    private String openNum;

    @ApiModelProperty(name = "midNum",value = "中期检测审核数量")
    private String midNum;

    @ApiModelProperty(name = "scoreNum",value = "已评分数量")
    private String scoreNum;

    @ApiModelProperty(name = "defenseNum",value = "免答辩通过数量")
    private String defenseNum;
}
