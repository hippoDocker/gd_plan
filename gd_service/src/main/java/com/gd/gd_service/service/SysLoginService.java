package com.gd.gd_service.service;

import com.gd.base.pojo.dto.CommonResult;

public interface SysLoginService {
    CommonResult saveSysLogin(String userCode, String userPwd, String imgCode, String key);

	CommonResult cleanUserCache(String token);

}
