package com.gd.base.pojo.dto.sys;

import com.gd.base.pojo.dto.PageBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "班级管理相关接口接受参数")
@Data
public class SysClassDTO extends PageBaseInfo implements Serializable {
	@ApiModelProperty(name="classId",value = "班级ID")
	private Long classId;
	@ApiModelProperty(name = "classCode",value = "班级编码")
	private String classCode;
	@ApiModelProperty(name = "className",value = "班级名称")
	private String className;
	@ApiModelProperty(name = "state",value = "状态")
	private Long state;
	@ApiModelProperty(name = "parentId",value = "父级ID")
	private Long parentId;
	@ApiModelProperty(name = "classId",value = "班级ID集合")
	private List<Long> classIds;
}
