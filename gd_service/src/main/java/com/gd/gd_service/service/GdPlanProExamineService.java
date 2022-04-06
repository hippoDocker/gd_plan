package com.gd.gd_service.service;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.plan.GdPlanPaperScoreDTO;
import com.gd.base.pojo.dto.plan.GdPlanUploadFileDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @program: gd_plan
 * @description: TODO 毕业流程审核
 * @author: tangxl
 * @create: 2022-03-11 09:16
 */
public interface GdPlanProExamineService {
	CommonResult uploadRequireFile(String token, MultipartFile[] files, GdPlanUploadFileDTO gdPlanUploadFileDto);

	void downReportFile(String token, GdPlanUploadFileDTO gdPlanUploadFileDto, HttpServletResponse response);

	CommonResult updateReportReturn(String token, GdPlanUploadFileDTO gdPlanUploadFileDto);

	CommonResult updateReportPass(String token, GdPlanUploadFileDTO gdPlanUploadFileDto);

	CommonResult updateReportScore(String token, GdPlanPaperScoreDTO gdPlanPaperScoreDto);
}
