package com.gd.gd_service.service;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysMajorDTO;
import com.gd.base.pojo.vo.sys.SysMajorVO;

public interface SysMajorService {
	CommonResult addMajor(String token, SysMajorDTO sysMajorDto);

	CommonResult deleteMajor(String token, SysMajorDTO sysMajorDto);

	CommonResult updateMajor(String token, SysMajorDTO sysMajorDto);

	PageBaseInfo<SysMajorVO> findMajorPage(String token, SysMajorDTO sysMajorDto, PageBaseInfo pageBaseInfo);

	CommonResult findMajorById(String token, SysMajorDTO sysMajorDto);

	CommonResult findCodeDataMajor(String token);
}
