package com.gd.gd_service.controller;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.GdplanThemePastPageDTO;
import com.gd.base.pojo.vo.plan.GdplanThemePastPageVO;
import com.gd.gd_service.service.GdPlanThemePastService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: gd_plan
 * @description: TODO 审核学生选题
 * @author: tangxl
 * @create: 2022-03-10 17:10
 */
@RestController
@RequestMapping("/gdplan")
@Api(value = "GdPlanThemePastController",tags = "审核学生选题")
public class GdPlanThemePastController {
	@Autowired
	private GdPlanThemePastService gdPlanThemePastService;

	@PostMapping("/doThemePast")
	@ApiOperation(value = "通过",notes = "审核学生选题通过:参数：选择的主题数据ID(id)")
	public CommonResult doThemePast(@RequestHeader String token, @RequestBody GdplanThemePastPageDTO gdPlanReportPageDto){
		return gdPlanThemePastService.saveThemePast(token,gdPlanReportPageDto);
	}

	@PostMapping("/findThemePastPage")
	@ApiOperation(value = "数据分页查询",notes = "审核主题数据分页查询：条件查询：学生姓名(studentName),教师名称(teacherName),设计方向(designDirection),设计主题(themeName),分页大小(pageSize),分页页码(pageNo);教师只能查询它自己当前的主题审核数据，管理员可以查看所有的数据")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = GdplanThemePastPageVO.class)
	})
	public CommonResult findThemePastPage(@RequestHeader String token, @RequestBody GdplanThemePastPageDTO gdPlanReportPageDto){
		return gdPlanThemePastService.findThemePastPage(token,gdPlanReportPageDto,
				new PageBaseInfo(gdPlanReportPageDto.getPageNo(),gdPlanReportPageDto.getPageSize(),Boolean.FALSE));
	}

	@PostMapping("/doThemeReturn")
	@ApiOperation(value = "退回",notes = "审核学生选题退回:参数：选择的主题数据ID(id)")
	public CommonResult doThemeReturn(@RequestHeader String token, @RequestBody GdplanThemePastPageDTO gdPlanReportPageDto){
		return gdPlanThemePastService.saveThemeReturn(token,gdPlanReportPageDto);
	}
}
