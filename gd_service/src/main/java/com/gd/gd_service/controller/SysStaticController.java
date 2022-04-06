package com.gd.gd_service.controller;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysStaticTypeDTO;
import com.gd.base.pojo.dto.sys.SysStaticTypePageDTO;
import com.gd.base.pojo.dto.sys.SysStaticValueDTO;
import com.gd.base.pojo.dto.sys.SysStaticValuePageDTO;
import com.gd.base.pojo.vo.sys.SysStaticTypeVO;
import com.gd.base.pojo.vo.sys.SysStaticValueVO;
import com.gd.gd_service.service.SysStaticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: tangxl
 * @Date: 2021年12月13日11:38:10
 * @Description: TODO 系统静态状态操作
 */
@RestController
@RequestMapping("/static")
@Api(value = "静态值管理",tags = "静态值管理")
@Slf4j
public class SysStaticController {
	@Autowired
	private SysStaticService sysStaticService;

	@ApiOperation(value = "静态类型分页查询",notes = "静态类型分页查询--条件模糊查询：属性名称（staticName）,分页大小(pageSize),分页页码(pageNo)")
	@PostMapping("/findStaticTypePage")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = SysStaticTypeVO.class)
	})
	public CommonResult findStaticTypePage(@RequestHeader String token, @RequestBody SysStaticTypePageDTO sysStaticTypePageDto){
		return sysStaticService.findStaticTypePage(token,sysStaticTypePageDto,new PageBaseInfo<>(sysStaticTypePageDto.getPageNo(),sysStaticTypePageDto.getPageSize(),Boolean.FALSE));
	}

	@ApiOperation(value = "静态值分页查询",notes = "静态值分页查询--传参：静态属性id(staticId),分页大小(pageSize),分页页码(pageNo)")
	@PostMapping("/findStaticValuePage")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = SysStaticValueVO.class)
	})
	public CommonResult findStaticValuePage(@RequestHeader String token, @RequestBody SysStaticValuePageDTO sysStaticValuePageDto){
		return sysStaticService.findStaticValuePage(token,sysStaticValuePageDto,new PageBaseInfo<>(sysStaticValuePageDto.getPageNo(),sysStaticValuePageDto.getPageSize(),Boolean.FALSE));
	}

	@ApiOperation(value = "添加静态类型",notes = "添加静态类型--字段：静态编码(staticCode),静态类型名称(staticName),备用字段一(remarkOne),备用字段二(remarkTwo)，状态(state);"
			+ "备用字段可以为空,编码、名称和状态不能为空，状态可以做成下拉框写死：0-有效,1-无效,传0或1")
	@PostMapping("/addStaticType")
	public CommonResult addStaticType(@RequestHeader String token, @RequestBody SysStaticTypeDTO sysStaticTypeDto){
		return sysStaticService.addStaticType(token,sysStaticTypeDto);
	}

	@ApiOperation(value = "添加静态值",notes = "添加静态值--字段：静态类型ID(staticId),静态名称(valueName),静态值(value),备用字段一(remarkOne),备用字段二(remarkTwo)，状态(state);"
			+ "备用字段可以为空，静态类型ID不能为空，静态值名称和状态不能为空，状态可以做成下拉框写死：0-有效,1-无效,传0或1")
	@PostMapping("/addStaticValue")
	public CommonResult addStaticValue(@RequestHeader String token, @RequestBody SysStaticValueDTO sysStaticValueDto){
		return sysStaticService.addStaticValue(token,sysStaticValueDto);
	}

	@ApiOperation(value = "修改静态类型",notes = "修改静态类型--字段：静态类型ID(staticId),静态编码(staticCode),静态类型名称(staticName),备用字段一(remarkOne),备用字段二(remarkTwo),状态(state);"
			+ "备用字段可以为空,静态类型ID、编码和名称不能为空,状态传int类型 0-无效，1-有效")
	@PostMapping("/updateStaticType")
	public CommonResult updateStaticType(@RequestHeader String token, @RequestBody SysStaticTypeDTO sysStaticTypeDto){
		return sysStaticService.updateStaticType(token,sysStaticTypeDto);
	}

	@ApiOperation(value = "修改静态值",notes = "添加静态值--字段：静态值ID(staticValueId),静态名称(valueName),静态值(value),备用字段一(remarkOne),备用字段二(remarkTwo),状态(state);"
			+ "备用字段可以为空，静态值ID不能为空，静态值名称不能为空,状态传int类型 0-无效，1-有效")
	@PostMapping("/updateStaticValue")
	public CommonResult updateStaticValue(@RequestHeader String token, @RequestBody SysStaticValueDTO sysStaticValueDto){
		return sysStaticService.updateStaticValue(token,sysStaticValueDto);
	}
}
