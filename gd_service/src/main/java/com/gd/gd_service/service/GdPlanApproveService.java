package com.gd.gd_service.service;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.DeleteThemeExamineDTO;
import com.gd.base.pojo.dto.plan.GdPlanDetailStuPageDTO;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计主题审批相关业务处理接口
 * @author: tangxl
 * @create: 2022-03-08 19:12
 */
public interface GdPlanApproveService {
	CommonResult findGdPlanDetailPage(String token, GdPlanDetailStuPageDTO gdPlanDetailStuPageDto, PageBaseInfo page);

	CommonResult deleteThemeExamine(String token, DeleteThemeExamineDTO deleteThemeExamineDto);
}
