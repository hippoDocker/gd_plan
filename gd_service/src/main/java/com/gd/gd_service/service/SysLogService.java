package com.gd.gd_service.service;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.HistoryDesignThemeDTO;
import com.gd.base.pojo.dto.sys.SysInterfaceLogDTO;
import com.gd.base.pojo.dto.sys.SysUserLoginLogDTO;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface SysLogService {
    void addsysInterfaceLog(HttpServletRequest request, Object[] args) throws IOException;

    CommonResult findLoginLogPage(String token, SysUserLoginLogDTO sysUserLoginLogDto, PageBaseInfo page);

    CommonResult findInterfaceLogPage(String token, SysInterfaceLogDTO sysInterfaceLogDto, PageBaseInfo page);

    CommonResult findInterfaceReport(String token, SysInterfaceLogDTO sysInterfaceLogDto);

    CommonResult findParticipleCount(Integer amount) ;

    CommonResult findHistoryDesignTheme(HistoryDesignThemeDTO historyDesignThemeDTO,PageBaseInfo page);

    CommonResult findHistoryType();
}
