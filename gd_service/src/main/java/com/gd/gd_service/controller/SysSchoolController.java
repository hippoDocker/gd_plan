package com.gd.gd_service.controller;

import com.gd.base.entity.SysSchool;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.DataCodeDTO;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysSchoolDTO;
import com.gd.base.pojo.vo.sys.SysSchoolVO;
import com.gd.gd_service.service.SysSchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value ="/school")
@Api(value = "学校管理",tags = "学校管理")
@Log4j2
public class SysSchoolController {
	@Autowired
	private SysSchoolService sysSchoolService;
	/**
	 * @Description: TODO 添加学校
	 * @return
	 */
	@PostMapping("/create")
	@ApiOperation(value = "添加学校",notes = "根据学校名称（schoolName）--必填，学校编码（schoolCode）--不必填")
	public CommonResult addSchool(@RequestHeader String token , @RequestBody SysSchoolDTO sysSchoolDto){
		return sysSchoolService.addSchool(token,sysSchoolDto);
	}

	/**
	 * @Description: TODO 删除学校
	 * @return
	 */
	@PostMapping("/delete")
	@ApiOperation(value = "删除学校", notes = "根据学校id集合（schoolIds）删除学校信息")
	public CommonResult deleteSchool(@RequestHeader String token,@RequestBody SysSchoolDTO sysSchoolDto){
		return sysSchoolService.deleteSchool(token,sysSchoolDto);
	}

	/**
	 * @Description: TODO 更新学校信息
	 * @return
	 */
	@PostMapping("/update")
	@ApiOperation(value="更新学校",notes = "根据学校ID（schoolId）更新学校编码（schoolCode），学校名称（schoolName）")
	public CommonResult updateSchool(@RequestHeader String token,@RequestBody SysSchoolDTO sysSchoolDto){
		CommonResult commonResult = sysSchoolService.updateSchool(token,sysSchoolDto);
		return commonResult;
	}

	/**
	 * @Description: TODO 分页查询角色
	 * @return
	 */
	@PostMapping("/findPage")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = SysSchoolVO.class)
	})
	@ApiOperation(value="分页查询学校",notes = "学校分页查询，也可通过学校名称（schoolNme)条件查询,分页大小(pageSize),分页页码(pageNo)")
	public CommonResult findSchoolPage(@RequestHeader String token,@RequestBody SysSchoolDTO sysSchoolDto){
		PageBaseInfo<SysSchoolVO> sysSchoolPage = sysSchoolService.findShcoolPage(token,sysSchoolDto,new PageBaseInfo(sysSchoolDto.getPageNo(),sysSchoolDto.getPageSize(),Boolean.FALSE));
		return CommonResult.success(sysSchoolPage,"查询成功！");
	}
	/**
	 * @Description: TODO 根据ID查询学校信息
	 * @return
	 */
	@PostMapping("/findbyid")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = SysSchool.class)
	})
	@ApiOperation(value="根据ID查询学校",notes = "根据学校ID（schoolId）查询角色信息")
	public CommonResult findRoleByRoleId(@RequestHeader String token,@RequestBody SysSchoolDTO sysSchoolDto){
		CommonResult commonResult = sysSchoolService.findSchoolByRoleId(token,sysSchoolDto);
		return commonResult;
	}

	/**
	 * @Description: TODO 查询所有学校
	 * @return
	 */
	@PostMapping("/findCodeDataSchool")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = DataCodeDTO.class)
	})
	@ApiOperation(value="查询Code-data形式的学校信息",notes = "查询所有学校信息，以code-data的数据形式返回，code对应学校id，date对应学校名称,下拉框数据可以使用")
	public CommonResult findCodeDataSchool(@RequestHeader String token){
		CommonResult commonResult = sysSchoolService.findCodeDataSchool(token);
		return commonResult;
	}
}
