package com.gd.gd_service.service;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.GdPlanReportPageDTO;
import com.gd.base.pojo.dto.plan.TeacherDirectionPageDTO;
import com.gd.base.pojo.dto.plan.TeacherThemePageDTO;
import com.gd.base.pojo.dto.sys.SysInterfaceLogDTO;
import com.gd.base.pojo.vo.plan.GdPlanDetailPageVO;
import com.gd.base.pojo.vo.plan.TeacherDirectionVO;
import com.gd.base.pojo.vo.plan.TeacherThemeVO;

import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计信息管理
 * @author: tangxl
 * @create: 2022-03-13 16:24
 */
public interface GdPlanInformManageService {
	CommonResult findGdplanDetailPage(String token, GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo pageBaseInfo);

	List<GdPlanDetailPageVO> downGdplanDetail(String token, GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo pageBaseInfo);

	List<TeacherDirectionVO> downTeacherDesDir(String token, TeacherDirectionPageDTO teacherDirectionPageDto, PageBaseInfo pageBaseInfo);

	List<TeacherThemeVO> downDesignTheme(String token, TeacherThemePageDTO teacherThemePageDto, PageBaseInfo pageBaseInfo);

    CommonResult findGdPlanProcessRePort(SysInterfaceLogDTO sysInterfaceLogDTO);
}
