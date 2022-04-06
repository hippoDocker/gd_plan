package com.gd.gd_service.service;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysClassDTO;
import com.gd.base.pojo.vo.sys.SysClassVO;

public interface SysClassService {
	CommonResult addClass(String token, SysClassDTO sysClassDto);

	CommonResult deleteClass(String token, SysClassDTO sysClassDto);

	CommonResult updateClass(String token, SysClassDTO sysClassDto);

	PageBaseInfo<SysClassVO> findClassPage(String token, SysClassDTO sysClassDto, PageBaseInfo pageBaseInfo);

	CommonResult findClassById(String token, SysClassDTO sysClassDto);

	CommonResult findCodeDataClass(String token);
}
