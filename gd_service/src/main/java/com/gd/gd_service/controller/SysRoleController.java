package com.gd.gd_service.controller;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.DataCodeDTO;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysRoleDTO;
import com.gd.base.entity.SysRole;
import com.gd.base.pojo.vo.sys.SysRoleVO;
import com.gd.gd_service.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: tangxl
 * @Date: 2021年12月13日11:38:10
 * @Description: TODO 系统菜单操作
 */
@RestController
@RequestMapping("/role")
@Api(value = "角色管理",tags = "角色管理")
@Log4j2
public class SysRoleController {
    @Autowired
    SysRoleService sysRoleService;
    /**
     * @Description: TODO 创建角色
     * @return
     */
    @PostMapping("/create")
    @ApiOperation(value = "新增角色",notes = "根据角色名称（roleName），角色编码（roleCode）")
    public CommonResult createRole(@RequestHeader String token ,@RequestBody SysRoleDTO sysRoleDto){
        return sysRoleService.createRole(sysRoleDto,token);
    }

    /**
     * @Description: TODO 删除角色
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除角色", notes = "根据角色id集合（roleIds）删除菜单信息")
    public CommonResult deleteRole(@RequestHeader String token,@RequestBody SysRoleDTO sysRoleDto){
        return sysRoleService.deleteRole(token,sysRoleDto);
    }

    /**
     * @Description: TODO 更新角色
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value="更新角色",notes = "根据角色ID（roleId）更新角色编码（roleCode），角色名称（roleName）")
    public CommonResult updateRole(@RequestHeader String token,@RequestBody SysRoleDTO sysRoleDto){
        CommonResult commonResult = sysRoleService.updateRole(token,sysRoleDto);
        return commonResult;
    }

    /**
     * @Description: TODO 分页查询角色
     * @return
     */
    @PostMapping("/find")
    @ApiResponses({
            @ApiResponse(code = 200,message = "查询成功！",response = SysRole.class)
    })
    @ApiOperation(value="分页查询角色",notes = "角色分页查询，也可通过角色名称（roleNme)条件查询,分页大小(pageSize),分页页码(pageNo)")
    public CommonResult findRoleByRoleName(@RequestHeader String token,@RequestBody SysRoleDTO sysRoleDto){
        PageBaseInfo<SysRole> sysRolePage = sysRoleService.findRoleByRoleNamePage(token,sysRoleDto,new PageBaseInfo(sysRoleDto.getPageNo(),sysRoleDto.getPageSize(),Boolean.FALSE));
        return CommonResult.success(sysRolePage,"查询成功！");
    }
    /**
     * @Description: TODO 根据ID查询角色
     * @return
     */
    @PostMapping("/findbyId")
    @ApiResponses({
            @ApiResponse(code = 200,message = "查询成功！",response = SysRoleVO.class)
    })
    @ApiOperation(value="根据ID查询角色",notes = "根据角色ID（roleId）查询角色信息")
    public CommonResult findRoleByRoleId(@RequestHeader String token,@RequestBody SysRoleDTO sysRoleDto){
        CommonResult commonResult = sysRoleService.findRoleByRoleId(token,sysRoleDto);
        return commonResult;
    }

    /**
     * @Description: TODO 查询所有角色
     * @return
     */
    @PostMapping("/findCodeDataRole")
    @ApiResponses({
            @ApiResponse(code = 200,message = "查询成功！",response = DataCodeDTO.class)
    })
    @ApiOperation(value="查询Code-data形式的角色信息",notes = "查询所有角色信息，以code-data的数据形式返回，code对应角色id，date对应角色名称")
    public CommonResult findCodeDataRole(@RequestHeader String token){
        CommonResult commonResult = sysRoleService.findCodeDataRole(token);
        return commonResult;
    }
}
