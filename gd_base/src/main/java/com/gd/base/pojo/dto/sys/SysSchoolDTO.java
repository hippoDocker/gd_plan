package com.gd.base.pojo.dto.sys;

import com.gd.base.pojo.dto.PageBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "学校管理相关接口接受参数")
@Data
public class SysSchoolDTO extends PageBaseInfo implements Serializable {

	@ApiModelProperty(name="schoolId",value = "学校ID")
	private Long schoolId;
	@ApiModelProperty(name = "schoolCode",value = "学校编码")
	private String schoolCode;
	@ApiModelProperty(name = "schoolName",value = "学校名称")
	private String schoolName;
	@ApiModelProperty(name = "state",value = "状态")
	private Long state;
	@ApiModelProperty(name = "parentId",value = "父级ID")
	private Long parentId;
	@ApiModelProperty(name = "schoolId",value = "学校ID集合")
	private List<Long> schoolIds;
}
