package com.gd.gd_service.controller;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.DataCodeDTO;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysMenuDTO;
import com.gd.base.entity.SysMenu;
import com.gd.base.pojo.vo.sys.SysMenuVO;
import com.gd.gd_service.service.SysmenuService;
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
@RequestMapping("/menu")
@Api(value = "菜单管理",tags = "菜单管理")
@Log4j2
public class SysMenuController {
    @Autowired
    SysmenuService sysmenuService;
    /**
     * @Description: TODO 创建菜单
     * @return
     */
    @PostMapping("/create")
    @ApiOperation(value = "新增菜单",notes = "新增父菜单和子级菜单：*菜单名称(menuName),*菜单路径(menuUrl),菜单类型(menuType),菜单描述(menuDescription),父级ID(parentId);"
            + "备注：如果添加父级菜单就不传父级ID(parentId),如果添加子级菜单就必须传父级ID(parentId),前面有星号的参数必填,菜单类型可以做两个下拉框随便为啥，也可以我写一个接口通过静态值配置")
    public CommonResult createMenu(@RequestHeader String token ,@RequestBody SysMenuDTO sysMenuDto){
        CommonResult commonResult = sysmenuService.addMenu(sysMenuDto,token);

        return commonResult;
    }

    /**
     * @Description: TODO 删除菜单
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除菜单",notes = "根据菜单ID集合（menuIds）删除菜单")
    public CommonResult deleteMenu(@RequestHeader String token,@RequestBody SysMenuDTO sysMenuDto){
        CommonResult commonResult = sysmenuService.deleteMenu(token,sysMenuDto);
        return commonResult;
    }

    /**
     * @Description: TODO 更新菜单
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新菜单",notes = "通过菜单id来修改菜单信息，修改信息时 菜单名称（menuName），菜单url（menuUrl）,菜单父级ID（parentID）三个字段不能为空，其他选填，一级菜单名称不能重复")
    public CommonResult updateMenu(@RequestHeader String token,@RequestBody SysMenuDTO sysMenuDto){
        CommonResult commonResult = sysmenuService.updateMenu(token,sysMenuDto);
        return commonResult;
    }

    /**
     * @Description: TODO 查询菜单树
     * @return
     */
    @PostMapping("/findTree")
    @ApiOperation(value = "查询菜单树",notes = "只传手机号码的时候就查询当前手机号用户（phone）对应的菜单树；什么都不传的就是查询所有的")
    public CommonResult findMenu(@RequestHeader String token,@RequestBody SysMenuDTO sysMenuDto){
        CommonResult commonResult = sysmenuService.findMenuTree(token,sysMenuDto);
        return commonResult;
    }

    /**
     * @Description: TODO 模糊查询菜单集合
     * @return
     */
    @PostMapping("/findList")
    @ApiResponses({
            @ApiResponse(code = 200,message = "查询成功！",response = SysMenu.class)
    })
    @ApiOperation(value = "菜单条件分页查询",notes = "条件查询菜单集合,可添加查询条件:菜单名称(menuName),菜单路径(menuUrl),菜单类型(menuType)")
    public CommonResult findMenuList(@RequestHeader String token,@RequestBody SysMenuDTO sysMenuDto){
        PageBaseInfo<SysMenu>  sysMenuPage= sysmenuService.findMenuList(token,sysMenuDto,new PageBaseInfo(sysMenuDto.getPageNo(),sysMenuDto.getPageSize(),false));
        return CommonResult.success(sysMenuPage);
    }
    /**
     * @Description: TODO 模糊查询菜单
     * @return
     */
    @PostMapping("/findbyid")
    @ApiResponses({
            @ApiResponse(code = 200,message = "查询成功！",response = SysMenuVO.class)
    })
    @ApiOperation(value = "根据ID查询菜单",notes = "根据单个菜单ID（menuId）查询对应菜单信息")
    public CommonResult findMenuByMenuId(@RequestHeader String token,@RequestBody SysMenuDTO sysMenuDto){
        CommonResult commonResult = sysmenuService.findMenuByMenuId(token,sysMenuDto.getMenuId());
        return commonResult;
    }
    /**
     * @Description: TODO 查询code-data形式的菜单信息
     * @return
     */
    @PostMapping("/findCodeDataMenu")
    @ApiResponses({
            @ApiResponse(code = 200,message = "查询成功！",response = DataCodeDTO.class)
    })
    @ApiOperation(value = "查询code-data形式的菜单信息",notes = "查询所有菜单信息，以code-data的数据形式返回，code对应菜单id，date对应菜单名称")
    public CommonResult findCodeDataMenu(@RequestHeader String token){
        CommonResult commonResult = sysmenuService.findCodeDataMenu(token);
        return commonResult;
    }

}

