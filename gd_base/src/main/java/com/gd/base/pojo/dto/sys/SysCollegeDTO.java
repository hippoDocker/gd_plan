package com.gd.base.pojo.dto.sys;

import com.gd.base.pojo.dto.PageBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "学院管理相关接口接受参数")
@Data
public class SysCollegeDTO extends PageBaseInfo implements Serializable {
	@ApiModelProperty(name="collegeId",value = "学院ID")
	private Long collegeId;
	@ApiModelProperty(name = "collegeCode",value = "学院编码")
	private String collegeCode;
	@ApiModelProperty(name = "collegeName",value = "学院名称")
	private String collegeName;
	@ApiModelProperty(name = "state",value = "状态")
	private Long state;
	@ApiModelProperty(name = "parentId",value = "父级ID")
	private Long parentId;
	@ApiModelProperty(name = "collegeId",value = "学院ID集合")
	private List<Long> collegeIds;
}
