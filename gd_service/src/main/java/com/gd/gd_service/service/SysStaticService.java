package com.gd.gd_service.service;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysStaticTypeDTO;
import com.gd.base.pojo.dto.sys.SysStaticTypePageDTO;
import com.gd.base.pojo.dto.sys.SysStaticValueDTO;
import com.gd.base.pojo.dto.sys.SysStaticValuePageDTO;

public interface SysStaticService {
    public CommonResult addSysStaticType(String token, SysStaticTypeDTO sysStaticTypeDto);

	CommonResult findStaticTypePage(String token, SysStaticTypePageDTO sysStaticTypePageDto, PageBaseInfo objectPageBaseInfo);

	CommonResult findStaticValuePage(String token, SysStaticValuePageDTO sysStaticValuePageDto, PageBaseInfo objectPageBaseInfo);

	CommonResult addStaticType(String token, SysStaticTypeDTO sysStaticTypeDto);

	CommonResult addStaticValue(String token, SysStaticValueDTO sysStaticValueDto);

	CommonResult updateStaticType(String token, SysStaticTypeDTO sysStaticTypeDto);

	CommonResult updateStaticValue(String token, SysStaticValueDTO sysStaticValueDto);
}
