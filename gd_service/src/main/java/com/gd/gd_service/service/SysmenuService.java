package com.gd.gd_service.service;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysMenuDTO;
import com.gd.base.entity.SysMenu;

public interface SysmenuService {
    CommonResult addMenu(SysMenuDTO sysMenuDto, String token);
    CommonResult deleteMenu(String token, SysMenuDTO sysMenuDto);
    CommonResult updateMenu(String token, SysMenuDTO sysMenuDto);
    CommonResult findMenuTree(String token, SysMenuDTO sysMenuDto);
    CommonResult findMenuByMenuId(String token, Long id);

    PageBaseInfo<SysMenu> findMenuList(String token, SysMenuDTO sysMenuDto, PageBaseInfo pageBaseInfo);

	CommonResult findCodeDataMenu(String token);
}
