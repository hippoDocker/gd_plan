package com.gd.gd_service.controller;

import com.alibaba.fastjson.JSON;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.GdPlanReportPageDTO;
import com.gd.base.pojo.dto.plan.GdPlanUploadFileDTO;
import com.gd.base.pojo.vo.plan.GdPlanExemptDefensePageVO;
import com.gd.base.pojo.vo.plan.GdPlanReportPageVO;
import com.gd.gd_service.service.GdPlanProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计流程管理控制
 * @author: tangxl
 * @create: 2022-03-09 08:47
 */
@RestController
@RequestMapping("/gdplan")
@Api(value = "GdPlanProMgController",tags = "毕设流程管理")
public class GdPlanProMgController {
	@Autowired
	private GdPlanProcessService gdPlanProcessService;

	@PostMapping("/findOpenReportPage")
	@ApiOperation(value = "开题报告--数据分页查询",notes = "开题报告提交分页查询：条件查询：学生姓名(studentName),教师名称(teacherName),设计方向(designDirection),设计主题(themeName),分页大小(pageSize),分页页码(pageNo);"
			+ "注：学生默认查询的是已审核的主题数据")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = GdPlanReportPageVO.class)
	})
	public CommonResult findOpenReportPage(@RequestHeader String token, @RequestBody GdPlanReportPageDTO gdPlanReportPageDto){
		return gdPlanProcessService.findOpenReportPage(token,gdPlanReportPageDto,
				new PageBaseInfo(gdPlanReportPageDto.getPageNo(),gdPlanReportPageDto.getPageSize(),Boolean.FALSE));
	}

	@PostMapping("/findMidReportPage")
	@ApiOperation(value = "中期检测--数据分页查询",notes = "中期检测提交分页查询：条件查询：学生姓名(studentName),教师名称(teacherName),设计方向(designDirection),设计主题(themeName),分页大小(pageSize),分页页码(pageNo);"
			+ "注：学生默认查询的是已审核的主题数据")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = GdPlanReportPageVO.class)
	})
	public CommonResult findMidReportPage(@RequestHeader String token, @RequestBody GdPlanReportPageDTO gdPlanReportPageDto){
		return gdPlanProcessService.findMidReportPage(token,gdPlanReportPageDto,
				new PageBaseInfo(gdPlanReportPageDto.getPageNo(),gdPlanReportPageDto.getPageSize(),Boolean.FALSE));
	}

	@PostMapping("/findEndReportPage")
	@ApiOperation(value = "毕业论文提交--数据分页查询",notes = "毕业论文提交分页查询：条件查询：学生姓名(studentName),教师名称(teacherName),设计方向(designDirection),设计主题(themeName),分页大小(pageSize),分页页码(pageNo);"
			+ "注：学生默认查询的是已审核的主题数据")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = GdPlanReportPageVO.class)
	})
	public CommonResult findEndReportPage(@RequestHeader String token, @RequestBody GdPlanReportPageDTO gdPlanReportPageDto){
		return gdPlanProcessService.findEndReportPage(token,gdPlanReportPageDto,
				new PageBaseInfo(gdPlanReportPageDto.getPageNo(),gdPlanReportPageDto.getPageSize(),Boolean.FALSE));
	}

	@PostMapping("/findExemptReportPage")
	@ApiOperation(value = "免答辩审核申请--数据分页查询",notes = "毕业免答辩数据分页查询：条件查询：学生姓名(studentName),教师名称(teacherName),设计方向(designDirection),设计主题(themeName),分页大小(pageSize),分页页码(pageNo);"
			+ "注：学生默认查询的是已审核的主题数据")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = GdPlanExemptDefensePageVO.class)
	})
	public CommonResult findExemptReportPage(@RequestHeader String token, @RequestBody GdPlanReportPageDTO gdPlanReportPageDto){
		return gdPlanProcessService.findExemptReportPage(token,gdPlanReportPageDto,
				new PageBaseInfo(gdPlanReportPageDto.getPageNo(),gdPlanReportPageDto.getPageSize(),Boolean.FALSE));
	}

	@PostMapping("/uploadReport")
	@ApiOperation(value = "开题报告/中期检测/毕业论文/免答辩申请--上传",notes = "开题报告上传：可传多个文件(files)，数据ID(id),"
			+ "类型(type)开题报告文件上传type=1,中期检测报告文件上传type=2,毕业论文上传type=3,免答辩申请文件上传type=4"
			+ "对于已上传过的，再次上传会将原来上传的文件删除")
	public CommonResult uploadReport(@RequestHeader String token, @RequestParam(value = "files", required = false) MultipartFile[] files
			, @RequestParam String gdPlanUploadFileDto){
		GdPlanUploadFileDTO gdPlanUploadFileDTO1 = JSON.parseObject(gdPlanUploadFileDto, GdPlanUploadFileDTO.class);
		if(files.length == 0){
			return CommonResult.failed("请选择文件上传！");
		}
		return gdPlanProcessService.uploadReport(token,files, gdPlanUploadFileDTO1);
	}

	@PostMapping("/downRequire")
	@ApiOperation(value = "开题报告/中期检测/毕业论文--下载",notes = "开题报告/中期检测/毕业论文 要求下载：数据ID(id),"
			+ "类型(type)开题报告要求下载type=1,中期检测要求下载type=2,毕业论文要求上传type=3,"
			+ "对于已上传过的，再次上传会将原来上传的文件删除")
	public void downRequire(@RequestHeader String token, @RequestBody GdPlanUploadFileDTO gdPlanUploadFileDto, HttpServletResponse response){
		gdPlanProcessService.downRequire(token,gdPlanUploadFileDto,response);
	}
}
