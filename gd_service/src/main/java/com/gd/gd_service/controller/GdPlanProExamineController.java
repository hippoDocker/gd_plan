package com.gd.gd_service.controller;

import com.alibaba.fastjson.JSON;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.plan.GdPlanPaperScoreDTO;
import com.gd.base.pojo.dto.plan.GdPlanUploadFileDTO;
import com.gd.gd_service.service.GdPlanProExamineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @program: gd_plan
 * @description: TODO 毕业流程审核
 * @author: tangxl
 * @create: 2022-03-11 09:14
 */
@RestController
@RequestMapping("/gdplan")
@Api(value = "GdPlanProExamineControoler",tags = "毕业流程审核")
public class GdPlanProExamineController {
	@Autowired
	GdPlanProExamineService gdPlanProExamineService;

	@PostMapping("/uploadRequireFile")
	@ApiOperation(value = "开题报告/中期检测/论文打分--上传",notes = "开题报告/中期检测/论文打分的教师要求文件上传：files文件数组,数据ID(id),截止时间(overTime),类型(type)开题报告要求文件上传type=1,中期检测报告要求文件上传type=2,论文要求文件上传type=3")
	public CommonResult uploadRequireFile(@RequestHeader String token, @RequestParam(value = "files", required = false) MultipartFile[] files,@RequestParam String gdPlanUploadFileDto){
		GdPlanUploadFileDTO gdPlanUploadFileDTO1 = JSON.parseObject(gdPlanUploadFileDto, GdPlanUploadFileDTO.class);
		return gdPlanProExamineService.uploadRequireFile(token,files, gdPlanUploadFileDTO1);
	}


	@PostMapping("/downReportFile")
	@ApiOperation(value = "开题报告/中期检测/毕业论文--下载",notes = "开题报告/中期检测学生上传的的文件下载：数据ID(id)类型(type)开题报告学生上传文件下载type=1,中期检测学生上传文件下载type=2,毕业论文下载type=3")
	public void downReportFile(@RequestHeader String token, @RequestBody GdPlanUploadFileDTO gdPlanUploadFileDto, HttpServletResponse response){
		gdPlanProExamineService.downReportFile(token,gdPlanUploadFileDto,response);
	}

	@PostMapping("/updateReportReturn")
	@ApiOperation(value = "开题报告/中期检测/免答辩审核--退回",notes = "开题报告/中期检测/免答辩审核 学生上传的的文件退回：数据ID(id)类型(type)开题报告退回type=1,中期检测退回type=2,毕业论文打分退回type=3,免答辩申请退回type-4")
	public CommonResult updateReportReturn(@RequestHeader String token, @RequestBody GdPlanUploadFileDTO gdPlanUploadFileDto){
		return gdPlanProExamineService.updateReportReturn(token,gdPlanUploadFileDto);
	}

	@PostMapping("/updateReportScore")
	@ApiOperation(value = "毕业论文打分--打分",notes = "毕业论文打分：数据ID(id),指导老师分数(defenseGroupScore),答辩组分数(defenseGroupScore);注总分为100")
	public CommonResult updateReportScore(@RequestHeader String token, @RequestBody GdPlanPaperScoreDTO gdPlanPaperScoreDto){
		return gdPlanProExamineService.updateReportScore(token,gdPlanPaperScoreDto);
	}

	@PostMapping("/updateReportPass")
	@ApiOperation(value = "开题报告/中期检测/免答辩审核--通过",notes = "开题报告/中期检测/免答辩申请 学生上传的文件通过：数据ID(id)类型(type)开题报告通过type=1,中期检测通过type=2,免答辩申请通过type-4")
	public CommonResult updateReportPass(@RequestHeader String token, @RequestBody GdPlanUploadFileDTO gdPlanUploadFileDto){
		return gdPlanProExamineService.updateReportPass(token,gdPlanUploadFileDto);
	}



}
