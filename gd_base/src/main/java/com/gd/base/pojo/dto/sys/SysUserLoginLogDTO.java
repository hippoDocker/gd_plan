package com.gd.base.pojo.dto.sys;

import com.gd.base.pojo.dto.PageBaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: gd_plan
 * @description: TODO 系统登录日志接口相关参数
 * @author: tangxl
 * @create: 2022-03-01 15:50
 */
@Data
@ApiModel("系统登录日志接口入参相关参数")
public class SysUserLoginLogDTO extends PageBaseInfo  implements Serializable {

	@ApiModelProperty("登录用户姓名")
	private String LoginUserName;

	@ApiModelProperty("用户登录时间开始")
	private String loginDateStart;

	@ApiModelProperty("用户登录时间结束")
	private String loginDateEnd;
}
