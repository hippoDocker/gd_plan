package com.gd.gd_service.controller;

import com.gd.base.enums.plan.ExamineStateEunm;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.DataCodeDTO;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.DeleteThemeExamineDTO;
import com.gd.base.pojo.dto.plan.GdPlanDetailStuPageDTO;
import com.gd.base.pojo.vo.plan.GdPlanDetailStuPageVO;
import com.gd.gd_service.service.GdPlanApproveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计选择主题审批相关接口
 * @author: tangxl
 * @create: 2022-03-08 19:11
 */
@RestController
@RequestMapping("/gdplan")
@Api(value = "GdPlanApproveController",tags = "毕设审核情况")
public class GdPlanApproveController {
	@Autowired
	private GdPlanApproveService gdPlanApproveService;

	@PostMapping("/findGdPlanDetailPage")
	@ApiOperation(value = "已选主题审核信息分页查询",notes = "已选主题审核信息分页查询：条件查询：教师名称(teacherName),设计方向(designDirection),设计主题(themeName),审核状态(designDirection),分页大小(pageSize),分页页码(pageNo);"
			+ "注：审核状态可以通过接口获取，传code过来")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = GdPlanDetailStuPageVO.class)
	})
	public CommonResult findGdPlanDetailPage(@RequestHeader String token, @RequestBody GdPlanDetailStuPageDTO gdPlanDetailStuPageDto){
		return gdPlanApproveService.findGdPlanDetailPage(token,gdPlanDetailStuPageDto,
				new PageBaseInfo(gdPlanDetailStuPageDto.getPageNo(),gdPlanDetailStuPageDto.getPageSize(),Boolean.FALSE));
	}

	@GetMapping("/findExamineState")
	@ApiOperation(value = "获取code-data数据类型审核状态",notes = "获取code-data数据类型审核状态")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = DataCodeDTO.class)
	})
	public CommonResult findExamineState(@RequestHeader String token){
		return CommonResult.success(ExamineStateEunm.getAllTypeAndTypeName(),"获取审核状态成功！");
	}

	@PostMapping("/deleteThemeExamine")
	@ApiOperation(value = "取消已选主题",notes = "取消已选主题：取消选择的主题信息ID(ids);"
			+ "注：审核通过的数据取消需要老师通过才行，审核失败和未审核的可以直接取消")
	public CommonResult deleteThemeExamine(@RequestHeader String token, @RequestBody DeleteThemeExamineDTO deleteThemeExamineDto){
		return gdPlanApproveService.deleteThemeExamine(token,deleteThemeExamineDto);
	}



}
