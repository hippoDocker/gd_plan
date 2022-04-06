package com.gd.base.pojo.dto.sys;

import com.gd.base.pojo.dto.PageBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gd_plan
 * @description: TODO 静态类型分页查询Dto
 * @author: tangxl
 * @create: 2022-03-02 14:31
 */
@Data
@ApiModel(value = "静态类型分页查询Dto")
public class SysStaticTypePageDTO extends PageBaseInfo  implements Serializable {
	@ApiModelProperty("静态类型名称")
	private String staticName;
}
