package com.gd.gd_service.service.impl;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.GdPlanReportPageDTO;
import com.gd.base.pojo.dto.plan.TeacherDirectionPageDTO;
import com.gd.base.pojo.dto.plan.TeacherThemePageDTO;
import com.gd.base.jdbc.GdPlanInformManageJdbc;
import com.gd.base.jdbc.GdPlanThemeJdbc;
import com.gd.base.pojo.dto.sys.GdPlanProcessReportDTO;
import com.gd.base.pojo.dto.sys.SysInterfaceLogDTO;
import com.gd.base.pojo.vo.plan.GdPlanDetailPageVO;
import com.gd.base.pojo.vo.plan.TeacherDirectionVO;
import com.gd.base.pojo.vo.plan.TeacherThemeVO;
import com.gd.gd_service.service.GdPlanInformManageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计信息管理业务处理
 * @author: tangxl
 * @create: 2022-03-13 16:23
 */
@Service
public class GdPlanInformManageServiceImpl implements GdPlanInformManageService {
	@Autowired
	private GdPlanInformManageJdbc gdPlanInformManageJdbc;
	@Autowired
	private GdPlanThemeJdbc gdPlanThemeJdbc;

	/**
	 * @description: TODO 毕设主题信息汇总分页查询
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	@Override public CommonResult findGdplanDetailPage(String token, GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo page) {
		PageBaseInfo<GdPlanDetailPageVO> gdPlanDetailPageVoPage = gdPlanInformManageJdbc.findGdplanDetailPage(gdPlanReportPageDto,page);
		return CommonResult.success(gdPlanDetailPageVoPage,"查询成功！");
	}

	/**
	 * @description: TODO 毕设主题信息汇总导出
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	@Override public List<GdPlanDetailPageVO> downGdplanDetail(String token, GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo page) {
		PageBaseInfo<GdPlanDetailPageVO> gdPlanDetailPageVoPage = gdPlanInformManageJdbc.findGdplanDetailPage(gdPlanReportPageDto,page);
		return gdPlanDetailPageVoPage.getData();
	}

	/**
	 * @description: TODO 毕设方向汇总excel导出
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	@Override
	public List<TeacherDirectionVO> downTeacherDesDir(String token, TeacherDirectionPageDTO teacherDirectionPageDto, PageBaseInfo page) {
		PageBaseInfo<TeacherDirectionVO> teacherDesignVoPage = gdPlanThemeJdbc.findTeacherDesignDirectionPage(teacherDirectionPageDto,page);
		return teacherDesignVoPage.getData();
	}

	/**
	 * @description: TODO 设计主题Excel导出
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	@Override public List<TeacherThemeVO> downDesignTheme(String token, TeacherThemePageDTO teacherThemePageDto, PageBaseInfo page) {
		PageBaseInfo<TeacherThemeVO> teacherThemeVoPage = gdPlanThemeJdbc.findTeacherDesignThemePage(teacherThemePageDto,page);
		return teacherThemeVoPage.getData();
	}

	/**
	 * @description: TODO 查询毕业设计流程图形化报表数据
	 * @return:
	 * @author: tangxl
	 * @time:  14:42
	 */
	@Override
	public CommonResult findGdPlanProcessRePort(SysInterfaceLogDTO sysInterfaceLogDTO) {
		if(StringUtils.isEmpty(sysInterfaceLogDTO.getCreateTimeStart())){
			return CommonResult.failed("开始时间为空！");
		}
		if(StringUtils.isEmpty(sysInterfaceLogDTO.getCreateTimeEnd())){
			return CommonResult.failed("结束时间为空！");
		}
		List<GdPlanProcessReportDTO> gdPlanProcessReportDTOS = gdPlanThemeJdbc.findGdPlanProcessRePort(sysInterfaceLogDTO);
		return CommonResult.success(gdPlanProcessReportDTOS,"查询成功!");
	}

}
