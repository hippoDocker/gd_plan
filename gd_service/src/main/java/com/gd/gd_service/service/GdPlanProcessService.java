package com.gd.gd_service.service;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.GdPlanReportPageDTO;
import com.gd.base.pojo.dto.plan.GdPlanUploadFileDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计流程业务处理接口
 * @author: tangxl
 * @create: 2022-03-09 08:49
 */
public interface GdPlanProcessService {
	CommonResult findOpenReportPage(String token, GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo pageBaseInfo);

	CommonResult findMidReportPage(String token, GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo pageBaseInfo);

	CommonResult findEndReportPage(String token, GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo pageBaseInfo);

	CommonResult findExemptReportPage(String token, GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo pageBaseInfo);

	CommonResult uploadReport(String token, MultipartFile[] files, GdPlanUploadFileDTO gdPlanUploadFileDto);

	void downRequire(String token, GdPlanUploadFileDTO gdPlanUploadFileDto, HttpServletResponse response);
}
