package com.gd.gd_service.controller;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.GdPlanReportPageDTO;
import com.gd.base.pojo.vo.plan.GdPlanScorePageVO;
import com.gd.gd_service.service.GdPlanResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计最终结果相关接口
 * @author: tangxl
 * @create: 2022-03-09 15:48
 */
@RestController
@RequestMapping("/gdplan")
@Api(value = "GdPlanProcessController",tags = "毕设信息")
public class GdPlanResultController {
	@Autowired
	private GdPlanResultService gdPlanResultService;
	@PostMapping("/findGdPlanScorePage")
	@ApiOperation(value = "得分信息--分页查询",notes = "获取毕设信息得分情况分页查询：条件查询：学生姓名(studentName),教师姓名(teacherName),设计方向(designDirection),设计主题(themeName),分页大小(pageSize),分页页码(pageNo);"
			+ "注：学生默认查询的是已审核的主题数据")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = GdPlanScorePageVO.class)
	})
	public CommonResult findGdPlanScorePage(@RequestHeader String token, @RequestBody GdPlanReportPageDTO gdPlanReportPageDto){
		return gdPlanResultService.findGdPlanScorePage(token,gdPlanReportPageDto,
				new PageBaseInfo(gdPlanReportPageDto.getPageNo(),gdPlanReportPageDto.getPageSize(),Boolean.FALSE));
	}
}
