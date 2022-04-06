package com.gd.gd_service.service;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysSchoolDTO;
import com.gd.base.pojo.vo.sys.SysSchoolVO;

public interface SysSchoolService {
	CommonResult addSchool(String token, SysSchoolDTO sysSchoolDto);

	CommonResult deleteSchool(String token,  SysSchoolDTO sysSchoolDto);

	CommonResult updateSchool(String token, SysSchoolDTO sysSchoolDto);

	PageBaseInfo<SysSchoolVO> findShcoolPage(String token, SysSchoolDTO sysSchoolDto, PageBaseInfo pageBaseInfo);

	CommonResult findSchoolByRoleId(String token, SysSchoolDTO sysSchoolDto);

	CommonResult findCodeDataSchool(String token);
}
