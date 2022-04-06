package com.gd.gd_service.service;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.GdplanThemePastPageDTO;

/**
 * @program: gd_plan
 * @description: TODO 审核学生选题
 * @author: tangxl
 * @create: 2022-03-10 17:12
 */
public interface GdPlanThemePastService {
	CommonResult findThemePastPage(String token, GdplanThemePastPageDTO gdPlanReportPageDto, PageBaseInfo pageBaseInfo);

	CommonResult saveThemePast(String token, GdplanThemePastPageDTO gdPlanReportPageDto);

	CommonResult saveThemeReturn(String token, GdplanThemePastPageDTO gdPlanReportPageDto);
}
