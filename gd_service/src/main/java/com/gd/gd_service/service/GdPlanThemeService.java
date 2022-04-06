package com.gd.gd_service.service;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.TeacherDesDirDTO;
import com.gd.base.pojo.dto.plan.TeacherDirectionPageDTO;
import com.gd.base.pojo.dto.plan.TeacherThemeDTO;
import com.gd.base.pojo.dto.plan.TeacherThemePageDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @program: gd_plan
 * @description: TODO 毕设主题选择业务层接口
 * @author: tangxl
 * @create: 2022-03-02 17:05
 */
public interface GdPlanThemeService {
	CommonResult findTeacherDesignDirectionPage(String token, TeacherDirectionPageDTO teacherDirectionPageDto, PageBaseInfo pageBaseInfo);

	CommonResult findTeacherDesignThemePage(String token, TeacherThemePageDTO teacherThemePageDto, PageBaseInfo pageBaseInfo);

	CommonResult addTeacherDesDir(String token, TeacherDesDirDTO teacherDesDirDto);

	CommonResult deleteTeacherDesDir(String token, TeacherDesDirDTO teacherDesDirDto);

	CommonResult findThemeSource(String token);

	CommonResult addTeacherTheme(String token, TeacherThemeDTO teacherTheme);

	CommonResult uploadGdThemeDetail(String token, MultipartFile[] files, TeacherThemeDTO teacherTheme);

	CommonResult findGdThemeDetailName(String token, TeacherThemeDTO teacherThemeDto);

	void downloadGdThemeDetail(String token, TeacherThemeDTO teacherThemeDto, HttpServletResponse response);

	CommonResult addUserGdPlan(String token, TeacherThemeDTO teacherThemeDto);

	CommonResult updateTeacherTheme(String token, TeacherThemeDTO teacherTheme);

	CommonResult updateTeacherDesDir(String token, TeacherDesDirDTO teacherDesDirDto);

	CommonResult deleteTeacherTheme(String token, TeacherThemeDTO teacherTheme);

	CommonResult findDataCodeGdDerection(String token);
}
