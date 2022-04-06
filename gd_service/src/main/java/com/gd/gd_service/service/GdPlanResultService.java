package com.gd.gd_service.service;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.GdPlanReportPageDTO;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计最终结果相关业务处理接口
 * @author: tangxl
 * @create: 2022-03-09 15:49
 */
public interface GdPlanResultService {
	CommonResult findGdPlanScorePage(String token, GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo pageBaseInfo);
}
