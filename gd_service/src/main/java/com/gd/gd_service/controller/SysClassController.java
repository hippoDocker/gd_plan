package com.gd.gd_service.controller;

import com.gd.base.entity.SysClass;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.DataCodeDTO;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysClassDTO;
import com.gd.base.pojo.vo.sys.SysClassVO;
import com.gd.gd_service.service.SysClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value ="/class")
@Api(value = "班级管理",tags = "班级管理")
@Log4j2
public class SysClassController {
	@Autowired
	private SysClassService sysClassService;
	/**
	 * @Description: TODO 添加班级
	 * @return
	 */
	@PostMapping("/create")
	@ApiOperation(value = "添加班级",notes = "根据班级名称（className）--必填，班级编码（classCode）--不必填")
	public CommonResult addClass(@RequestHeader String token , @RequestBody SysClassDTO sysClassDto){
		return sysClassService.addClass(token,sysClassDto);
	}

	/**
	 * @Description: TODO 删除班级
	 * @return
	 */
	@PostMapping("/delete")
	@ApiOperation(value = "删除班级", notes = "根据班级id集合（classIds）删除班级信息")
	public CommonResult deleteRole(@RequestHeader String token,@RequestBody SysClassDTO sysClassDto){
		return sysClassService.deleteClass(token,sysClassDto);
	}

	/**
	 * @Description: TODO 更新班级信息
	 * @return
	 */
	@PostMapping("/update")
	@ApiOperation(value="更新班级",notes = "根据班级ID（classId）更新班级编码（classCode），班级名称（className）")
	public CommonResult updateClass(@RequestHeader String token,@RequestBody SysClassDTO sysClassDto){
		CommonResult commonResult = sysClassService.updateClass(token,sysClassDto);
		return commonResult;
	}

	/**
	 * @Description: TODO 分页查询角色
	 * @return
	 */
	@PostMapping("/findPage")
	@ApiOperation(value="分页查询班级",notes = "班级分页查询，也可通过班级名称（classNme)条件查询,分页大小(pageSize),分页页码(pageNo)")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = SysClassVO.class)
	})
	public CommonResult findClassPage(@RequestHeader String token,@RequestBody SysClassDTO sysClassDto){
		PageBaseInfo<SysClassVO> sysClassPage = sysClassService.findClassPage(token,sysClassDto,new PageBaseInfo(sysClassDto.getPageNo(),sysClassDto.getPageSize(),Boolean.FALSE));
		return CommonResult.success(sysClassPage,"查询成功！");
	}
	/**
	 * @Description: TODO 根据ID查询班级信息
	 * @return
	 */
	@PostMapping("/findbyid")
	@ApiOperation(value="根据ID查询班级",notes = "根据班级ID（classId）查询角色信息")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = SysClass.class)
	})
	public CommonResult findClassById(@RequestHeader String token,@RequestBody SysClassDTO sysClassDto){
		CommonResult commonResult = sysClassService.findClassById(token,sysClassDto);
		return commonResult;
	}

	/**
	 * @Description: TODO 查询所有班级
	 * @return
	 */
	@PostMapping("/findCodeDataClass")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = DataCodeDTO.class)
	})
	@ApiOperation(value="查询Code-data形式的班级信息",notes = "查询所有班级信息，以code-data的数据形式返回，code对应班级id，date对应班级名称,下拉框数据可以使用")
	public CommonResult findCodeDataClass(@RequestHeader String token){
		CommonResult commonResult = sysClassService.findCodeDataClass(token);
		return commonResult;
	}
}
