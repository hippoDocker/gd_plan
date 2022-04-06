package com.gd.gd_service.service;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysCollegeDTO;
import com.gd.base.pojo.vo.sys.SysCollegeVO;

public interface SysCollegeService {
	CommonResult addCollege(String token, SysCollegeDTO sysCollegeDto);

	CommonResult deleteCollege(String token, SysCollegeDTO sysCollegeDto);

	CommonResult updateCollege(String token, SysCollegeDTO sysCollegeDto);

	PageBaseInfo<SysCollegeVO> findCollegePage(String token, SysCollegeDTO sysCollegeDto, PageBaseInfo pageBaseInfo);

	CommonResult findCollegeById(String token, SysCollegeDTO sysCollegeDto);

	CommonResult findCodeDataCollege(String token);
}
