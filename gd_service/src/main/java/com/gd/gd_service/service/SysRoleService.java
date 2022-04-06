package com.gd.gd_service.service;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysRoleDTO;
import com.gd.base.entity.SysRole;

public interface SysRoleService {
    CommonResult createRole(SysRoleDTO sysRoleDto, String token);

    CommonResult deleteRole(String token, SysRoleDTO sysRoleDto);

    CommonResult updateRole(String token, SysRoleDTO sysRoleDto);

    PageBaseInfo<SysRole> findRoleByRoleNamePage(String token, SysRoleDTO sysRoleDto, PageBaseInfo page);

    CommonResult findRoleByRoleId(String token, SysRoleDTO sysRoleDto);

    CommonResult findCodeDataRole(String token);
}
