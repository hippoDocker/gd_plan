package com.gd.gd_service.service;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysChangeUserRoleAndPwdDTO;
import com.gd.base.pojo.dto.sys.SysUserAddDTO;
import com.gd.base.entity.SysUser;
import com.gd.base.pojo.vo.sys.SysUserExportVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface SysUserService {
    CommonResult addOneUser(String token, SysUserAddDTO sysUserAddDto);

    void findHeadImg(String token, String phone, ByteArrayOutputStream out);

    PageBaseInfo<SysUserExportVO> findUserPage(String token, SysUserAddDTO sysUserAddDto, PageBaseInfo pageBaseInfo);

    List<SysUserExportVO> findUserExport(String token, SysUserAddDTO sysUserAddDto, PageBaseInfo page);

    CommonResult saveAllUser(String token,List<SysUserAddDTO> sysUserAddDTOList);
    SysUser addtestTrans(SysUser sysUser);

	CommonResult addUserHead(String token, MultipartFile file);

    CommonResult updateUserPwd(String token, SysChangeUserRoleAndPwdDTO userDto);

    CommonResult updateUserRole(String token, SysChangeUserRoleAndPwdDTO userDto);

    CommonResult updateUserDetail(String token, SysUserAddDTO userDto);

    CommonResult updateUserState(String token, SysChangeUserRoleAndPwdDTO userDto);

    CommonResult deleteUser(String token, SysChangeUserRoleAndPwdDTO userDto);
}
