package com.gd.gd_service.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.GdPlanReportPageDTO;
import com.gd.base.pojo.dto.plan.TeacherDirectionPageDTO;
import com.gd.base.pojo.dto.plan.TeacherThemePageDTO;
import com.gd.base.pojo.dto.sys.GdPlanProcessReportDTO;
import com.gd.base.pojo.dto.sys.SysInterfaceLogDTO;
import com.gd.base.pojo.vo.plan.GdPlanDetailPageVO;
import com.gd.base.pojo.vo.plan.TeacherDirectionVO;
import com.gd.base.pojo.vo.plan.TeacherThemeVO;
import com.gd.gd_service.service.GdPlanInformManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计信息管理
 * @author: tangxl
 * @create: 2022-03-13 16:23
 */
@RestController
@RequestMapping("/gdplan")
@Api(value = "GdPlanInformManageController",tags = "毕业设计信息管理")
public class GdPlanInformManageController {
	@Autowired
	private GdPlanInformManageService gdPlanInformManageService;

	@PostMapping("/findGdplanDetailPage")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = GdPlanDetailPageVO.class)
	})
	@ApiOperation(value = "毕设详情汇总--分页查询",notes = "毕设主题信息汇总分页查询：条件查询：学生姓名(studentName),教师名称(teacherName),设计方向(designDirection)"
			+ "设计主题(themeName),主题审核状态(examineState),分页大小(pageSize),分页页码(pageNo);"
			+ "注：学生默认查询的是已审核的主题数据")
	public CommonResult findGdplanDetailPage(@RequestHeader String token, @RequestBody GdPlanReportPageDTO gdPlanReportPageDto){
		return gdPlanInformManageService.findGdplanDetailPage(token,gdPlanReportPageDto,
				new PageBaseInfo(gdPlanReportPageDto.getPageNo(),gdPlanReportPageDto.getPageSize(),Boolean.FALSE));
	}

	@PostMapping("/downGdplanDetail")
	@ApiOperation(value = "毕设详情汇总--excel导出",notes = "毕设主题信息汇总Excel导出：条件查询：学生姓名(studentName),教师名称(teacherName),设计方向(designDirection)"
			+ "设计主题(themeName),主题审核状态(examineState);")
	public void downGdplanDetail(@RequestHeader String token, @RequestBody GdPlanReportPageDTO gdPlanReportPageDto, HttpServletResponse response){
		List<GdPlanDetailPageVO> gdPlanDetailPageVOList = gdPlanInformManageService.downGdplanDetail(token,gdPlanReportPageDto,
						new PageBaseInfo(gdPlanReportPageDto.getPageNo(),gdPlanReportPageDto.getPageSize(),Boolean.TRUE));
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setCharacterEncoding("utf-8");
			// 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
			String fileName = URLEncoder.encode("毕业设计选题详情信息汇总", "UTF-8");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
			//写Excel
			EasyExcel.write(response.getOutputStream(), GdPlanDetailPageVO.class ).sheet().doWrite(gdPlanDetailPageVOList);
		}catch (Exception e) {
			response.reset();
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			Map<String, String> map = new HashMap<String, String>();
			map.put("status", "500");
			map.put("message", "下载文件失败" + e.getMessage());
			try {
				response.getWriter().println(JSON.toJSONString(map));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@PostMapping("/downTeacherDesDir")
	@ApiOperation(value = "毕设方向汇总--excel导出",notes = "毕设方向汇总excel导出：老师设计方向条件导出--条件查询：教师姓名(teacherName),教师电话(phone),设计方向(designDirection)")
	public void downTeacherDesDir(@RequestHeader String token, @RequestBody TeacherDirectionPageDTO teacherDirectionPageDto, HttpServletResponse response){
		List<TeacherDirectionVO> teacherDirectionVoList = gdPlanInformManageService.downTeacherDesDir(token, teacherDirectionPageDto,
				new PageBaseInfo(teacherDirectionPageDto.getPageNo(), teacherDirectionPageDto.getPageSize(), Boolean.TRUE));

		try {
			response.setContentType("application/vnd.ms-excel");
			response.setCharacterEncoding("utf-8");
			// 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
			String fileName = URLEncoder.encode("毕业设计方向汇总", "UTF-8");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
			//写Excel
			EasyExcel.write(response.getOutputStream(), TeacherDirectionVO.class ).sheet().doWrite(teacherDirectionVoList);
		}catch (Exception e) {
			response.reset();
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			Map<String, String> map = new HashMap<String, String>();
			map.put("status", "500");
			map.put("message", "下载文件失败" + e.getMessage());
			try {
				response.getWriter().println(JSON.toJSONString(map));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@PostMapping("/downDesignTheme")
	@ApiOperation(value = "设计主题汇总--Excel导出",notes = "设计主题Excel导出：设计主题导出数据条件--条件查询：教师姓名(teacherName),教师电话(phone),设计方向(designDirection),主题名称(themeName)")
	public void downDesignTheme(@RequestHeader String token, @RequestBody TeacherThemePageDTO teacherThemePageDto, HttpServletResponse response){
		List<TeacherThemeVO> gdPlanDetailPageVoList =  gdPlanInformManageService.downDesignTheme(token, teacherThemePageDto,
				new PageBaseInfo(teacherThemePageDto.getPageNo(), teacherThemePageDto.getPageSize(), Boolean.TRUE));

		try {
			response.setContentType("application/vnd.ms-excel");
			response.setCharacterEncoding("utf-8");
			// 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
			String fileName = URLEncoder.encode("毕业设计主题汇总", "UTF-8");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
			//写Excel
			EasyExcel.write(response.getOutputStream(), TeacherThemeVO.class ).sheet().doWrite(gdPlanDetailPageVoList);
		}catch (Exception e) {
			response.reset();
			response.setContentType("application/json");
			response.setCharacterEncoding("utf-8");
			Map<String, String> map = new HashMap<String, String>();
			map.put("status", "500");
			map.put("message", "下载文件失败" + e.getMessage());
			try {
				response.getWriter().println(JSON.toJSONString(map));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@PostMapping("/findGdPlanProcessRePort")
	@ApiOperation(value = "毕业设计流程图形化报表",notes = "毕业设计流程图形化报表：参数只传开始时间(createTimeStart)和结束时间(createTimeEnd)")
	@ApiResponses({
			@ApiResponse(code = 200, message = "查询成功！",response = GdPlanProcessReportDTO.class)
	})
	public CommonResult findGdPlanProcessRePort(@RequestHeader String token, @RequestBody SysInterfaceLogDTO sysInterfaceLogDTO){
		return gdPlanInformManageService.findGdPlanProcessRePort(sysInterfaceLogDTO);
	}


}
