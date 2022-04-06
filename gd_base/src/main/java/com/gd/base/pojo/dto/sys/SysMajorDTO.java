package com.gd.base.pojo.dto.sys;

import com.gd.base.pojo.dto.PageBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "专业管理相关接口接受参数")
@Data
public class SysMajorDTO extends PageBaseInfo implements Serializable {
	@ApiModelProperty(name="majorId",value = "专业ID")
	private Long majorId;
	@ApiModelProperty(name = "majorCode",value = "专业编码")
	private String majorCode;
	@ApiModelProperty(name = "majorName",value = "专业名称")
	private String majorName;
	@ApiModelProperty(name = "state",value = "状态")
	private Long state;
	@ApiModelProperty(name = "parentId",value = "父级ID")
	private Long parentId;
	@ApiModelProperty(name = "majorId",value = "专业ID集合")
	private List<Long> majorIds;
}
