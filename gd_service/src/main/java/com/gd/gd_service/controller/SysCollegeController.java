package com.gd.gd_service.controller;

import com.gd.base.entity.SysCollege;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.DataCodeDTO;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysCollegeDTO;
import com.gd.base.pojo.vo.sys.SysCollegeVO;
import com.gd.gd_service.service.SysCollegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value ="/college")
@Api(value = "学院管理",tags = "学院管理")
@Log4j2
public class SysCollegeController {
	@Autowired
	private SysCollegeService sysCollegeService;
	/**
	 * @Description: TODO 添加学院
	 * @return
	 */
	@PostMapping("/create")
	@ApiOperation(value = "添加学院",notes = "根据学院名称（collegeName）--必填，学院编码（collegeCode）--不必填")
	public CommonResult addcollege(@RequestHeader String token , @RequestBody SysCollegeDTO sysCollegeDto){
		return sysCollegeService.addCollege(token,sysCollegeDto);
	}

	/**
	 * @Description: TODO 删除学院
	 * @return
	 */
	@PostMapping("/delete")
	@ApiOperation(value = "删除学院", notes = "根据学院id集合（collegeIds）删除学院信息")
	public CommonResult deleteRole(@RequestHeader String token,@RequestBody SysCollegeDTO sysCollegeDto){
		return sysCollegeService.deleteCollege(token,sysCollegeDto);
	}

	/**
	 * @Description: TODO 更新学院信息
	 * @return
	 */
	@PostMapping("/update")
	@ApiOperation(value="更新学院",notes = "根据学院ID（collegeId）更新学院编码（collegeCode），学院名称（collegeName）")
	public CommonResult updatecollege(@RequestHeader String token,@RequestBody SysCollegeDTO sysCollegeDto){
		CommonResult commonResult = sysCollegeService.updateCollege(token,sysCollegeDto);
		return commonResult;
	}

	/**
	 * @Description: TODO 分页查询角色
	 * @return
	 */
	@PostMapping("/findPage")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = SysCollegeVO.class)
	})
	@ApiOperation(value="分页查询学院",notes = "学院分页查询，也可通过学院名称（collegeNme)条件查询,分页大小(pageSize),分页页码(pageNo)")
	public CommonResult findcollegePage(@RequestHeader String token,@RequestBody SysCollegeDTO sysCollegeDto){
		PageBaseInfo<SysCollegeVO> syscollegePage = sysCollegeService.findCollegePage(token,sysCollegeDto,new PageBaseInfo(sysCollegeDto.getPageNo(),sysCollegeDto.getPageSize(),Boolean.FALSE));
		return CommonResult.success(syscollegePage,"查询成功！");
	}
	/**
	 * @Description: TODO 根据ID查询学院信息
	 * @return
	 */
	@PostMapping("/findCollegebyId")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = SysCollege.class)
	})
	@ApiOperation(value="根据ID查询学院",notes = "根据学院ID（collegeId）查询角色信息")
	public CommonResult findCollegebyId(@RequestHeader String token,@RequestBody SysCollegeDTO sysCollegeDto){
		CommonResult commonResult = sysCollegeService.findCollegeById(token,sysCollegeDto);
		return commonResult;
	}

	/**
	 * @Description: TODO 查询所有学院
	 * @return
	 */
	@PostMapping("/findCodeDatacollege")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = DataCodeDTO.class)
	})
	@ApiOperation(value="查询Code-data形式的学院信息",notes = "查询所有学院信息，以code-data的数据形式返回，code对应学院id，date对应学院名称,下拉框数据可以使用")
	public CommonResult findCodeDatacollege(@RequestHeader String token){
		CommonResult commonResult = sysCollegeService.findCodeDataCollege(token);
		return commonResult;
	}
}
