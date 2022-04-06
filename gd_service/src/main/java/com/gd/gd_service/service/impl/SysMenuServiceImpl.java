package com.gd.gd_service.service.impl;

import com.gd.base.constant.Constans;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.DataCodeDTO;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysMenuDTO;
import com.gd.base.entity.*;
import com.gd.base.enums.ResultCodeEnums;
import com.gd.base.jdbc.SysMenuJdbc;
import com.gd.base.jpa.*;
import com.gd.base.util.BeanUtil;
import com.gd.base.util.DateUtil;
import com.gd.base.pojo.vo.redis.RedisSysUser;
import com.gd.base.pojo.vo.sys.SysMenuVO;
import com.gd.gd_service.service.RedisService;
import com.gd.gd_service.service.SysmenuService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @Auther: tangxl
 * @Date:2021年12月13日11:55:02
 * @Description: 菜单处理
 */
@Service
@Log4j2
public class SysMenuServiceImpl implements SysmenuService {
    @Autowired
    private SysMenuJdbc sysMenuJdbc;
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;
    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysMenuRoleDao sysMenuRoleDao;
    @Autowired
    private SysStaticTypeDao sysStaticTypeDao;
    @Autowired
    private SysStaticValueDao sysStaticValueDao;

    /**
     * @Description:TODO 创建菜单
     * @return
     */
    @Override
    @Transactional
    public CommonResult addMenu(SysMenuDTO sysMenuDto, String token) {
        //获取所有菜单
        List<SysMenu> sysMenuList = sysMenuDao.findAllByStateAndParentId(1L,0L);
        if(StringUtils.isEmpty(sysMenuDto.getMenuName())){
            return CommonResult.failed("菜单名称不能为空！");
        }
        //菜单校验
        for (SysMenu sysMenu : sysMenuList) {
            if (sysMenuDto.getParentId().equals(0L) && sysMenu.getMenuName().equals(sysMenuDto.getMenuName())){
                return CommonResult.failed("此名称菜单已存在！");
            }
        }

        if(StringUtils.isEmpty(sysMenuDto.getMenuUrl())){
            return CommonResult.failed("菜单路径不能为空！");
        }
        //获取用户信息
        RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
//        if(BeanUtil.isEmpty(redisSysUser)){
//            return CommonResult.unLogin(null);
//        }
        //不同相同属性值拷贝
        SysMenu sysMenu =BeanUtil.copyObject(sysMenuDto,SysMenu.class);
        //菜单初始化赋值
        //创建时间
        sysMenu.setCreateTime(DateUtil.dateToLocalDateTime(new Date()));
        //菜单创建人
        sysMenu.setCreateBy(redisSysUser.getUserName());
        //最后修改时间
        sysMenu.setLastModifyTime(DateUtil.dateToLocalDateTime(new Date()));
        //父级ID
        if(null==sysMenuDto.getParentId()||"".equals(sysMenuDto.getParentId())){
            return CommonResult.failed("菜单父级ID不能为空！一级菜单父级ID默认为0");
        }
        sysMenu.setParentId(sysMenuDto.getParentId());
        //状态
        sysMenu.setState(1L);
        CommonResult commonResult = new CommonResult();
        try {
            sysMenuDao.save(sysMenu);
        }catch (Exception e){
            log.info("====>创建菜单错误：{}",e);
            commonResult.setCode(ResultCodeEnums.FAILED.getCode());
            commonResult.setMessage(ResultCodeEnums.FAILED.getMessage());
            return commonResult;
        }
        commonResult.setCode(ResultCodeEnums.SUCCESS.getCode());
        commonResult.setMessage(ResultCodeEnums.SUCCESS.getMessage());
        return commonResult;
    }
    /**
     * @Description:TODO 根据ID集合删除菜单
     * @return
     */
    @Override
    @Transactional
    public CommonResult deleteMenu(String token, SysMenuDTO sysMenuDto) {
        //获取用户信息
        RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
//        //获取菜单id列表
//        List<Long> idList = Arrays.asList (ids.split(",")).stream().map(o->Long.valueOf(o)).collect(Collectors.toList());
        List<SysMenu> sysMenuList = sysMenuDao.findAllByMenuIdInAndState(sysMenuDto.getMenuIds(),1L);
        //删除角色绑定的菜单信息以及菜单信息
        List<Long> menuIds = sysMenuList.stream().map(o->o.getMenuId()).collect(Collectors.toList());
        List<SysMenuRole> sysMenuRoles = sysMenuRoleDao.findAllByRoleIdInAndState(menuIds,1L);
        sysMenuRoles.stream().forEach(o->o.setState(0L));
        sysMenuList.stream().forEach(o->o.setState(0L));
        try {
            sysMenuDao.saveAll(sysMenuList);
            sysMenuRoleDao.saveAll(sysMenuRoles);
        }catch (Exception e){
            log.info("====>删除菜单错误：{}",e);
            return CommonResult.failed("删除菜单错误!");
        }
        return CommonResult.success("删除菜单成功！");
    }
    /**
     * @Description:TODO 更新菜单
     * @return
     */
    @Override
    public CommonResult updateMenu(String token, SysMenuDTO sysMenuDto) {
        //获取用户信息
        RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
        if(BeanUtil.isEmpty(sysMenuDto.getMenuId())){
            return CommonResult.failed("修改菜单ID不能为空！");
        }
        //获取需要修改的菜单信息
        SysMenu sysMenu = sysMenuDao.findByMenuIdAndState(sysMenuDto.getMenuId(),1L);
        //菜单信息校验--菜单名称（menuName），菜单url（menuUrl）,菜单父级ID（parentID）三个字段不能为空
        if (StringUtils.isEmpty(sysMenu.getMenuName())) {
            return CommonResult.failed("修改的菜单名称不能为空！");
        }
        if (StringUtils.isEmpty(sysMenuDto.getMenuUrl())) {
            return CommonResult.failed("修改的菜单路径不能为空！");
        }
        if (BeanUtil.isEmpty(sysMenuDto.getParentId())) {
            return CommonResult.failed("修改菜单的父级ID不能为空！一级菜单父级菜单为0");
        }
        //获取所以菜单--校验菜单名称重复
        List<SysMenu> sysMenuList = sysMenuDao.findAllByState(1L);
        Long count = sysMenuList.stream().filter(o->{
            if(o.getMenuName().equals(sysMenuDto.getMenuName())){
                //都为一级菜单时
                if((o.getParentId().equals(0L)&&sysMenuDto.getParentId().equals(0L))&& !o.getMenuId().equals(sysMenuDto.getMenuId())){
                    return true;
                }
            }
            return false;
        }).count();
        if(!count.equals(0L)){
            return CommonResult.failed("已存在此名称一级菜单！");
        }
        //菜单信息修改
        sysMenu.setMenuName(sysMenuDto.getMenuName());
        sysMenu.setMenuUrl(sysMenuDto.getMenuUrl());
        sysMenu.setMenuIcon(sysMenuDto.getMenuIcon());
        sysMenu.setMenuType(sysMenuDto.getMenuType());
        sysMenu.setLastModifyTime(DateUtil.dateToLocalDateTime(new Date()));
        sysMenu.setLastModifyBy(redisSysUser.getUserName());
        sysMenu.setMenuDescription(sysMenuDto.getMenuDescription());
        sysMenu.setParentId(sysMenuDto.getParentId());
        try{
            sysMenuDao.save(sysMenu);
        }catch (Exception e){
            log.info("菜单修改出错：{}",e);
            e.printStackTrace();
            return CommonResult.failed("修改菜单出错！");
        }
        return CommonResult.success("修改菜单成功！");
    }

    /**
     * @Description TODO 菜单树查询
     * @param token
     * @param sysMenuDto
     * @return
     */
    @Override
    public CommonResult findMenuTree(String token, SysMenuDTO sysMenuDto) {
        CommonResult commonResult = new CommonResult();
        try{
            List<SysMenu> sysMenus = new ArrayList<>();
            //获取用户关联菜单
            if(StringUtils.isNotBlank(sysMenuDto.getPhone())){
                SysUser sysUser = sysUserDao.findByPhoneAndState(sysMenuDto.getPhone(),1L);
                //获取用户关联所有角色
                List<SysUserRole> sysUserRoles = sysUserRoleDao.findAllByUserId(sysUser.getUserId());
                List<Long> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
                //获取角色关联的菜单
                List<SysMenuRole> sysMenuRoles = sysMenuRoleDao.findAllByRoleIdInAndState(roleIds,1L);
                //获取关联菜单所有ID
                List<Long> menuIds = sysMenuRoles.stream().map(SysMenuRole::getMenuId).collect(Collectors.toList());
                //获取用户对应角色关联的所有菜单
                sysMenus = sysMenuDao.findAllByMenuIdInAndState(menuIds,1L);
            }else {//获取所有菜单
                sysMenus = sysMenuDao.findAllByState(1L);
            }
            List<SysMenuVO> sysMenuVOList = BeanUtil.menuSerialize(sysMenus,0L);
            return CommonResult.success(sysMenuVOList,ResultCodeEnums.SUCCESS.getMessage());
        }catch (Exception e){
            log.info("菜单查询出错：{}",e);
            return CommonResult.failed("菜单查询出错！");
        }

    }

    /**
     * @Description TODO 根据菜单ID查询菜单信息
     * @param token
     * @param id
     * @return
     */
    @Override
    public CommonResult findMenuByMenuId(String token, Long id) {
        try{
            //根据id查询菜单
            SysMenu sysMenu = sysMenuDao.findByMenuIdAndState(id,1L);
            SysMenuVO sysMenuVo = BeanUtil.copyObject(sysMenu, SysMenuVO.class);
            return CommonResult.success(sysMenuVo,"菜单查询成功！");
        }catch (Exception e){
            log.info("根据菜单ID查询菜单出错：{}",e);
            return CommonResult.failed("菜单查询出错！");
        }
    }

    /**
     * 条件分页查询菜单
     * @param token 令牌
     * @param sysMenuDto 条件查询参数
     * @param pageBaseInfo 分页信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageBaseInfo<SysMenu> findMenuList(String token, SysMenuDTO sysMenuDto, PageBaseInfo pageBaseInfo) {
        //更新静态值配置的角色与菜单
        SysStaticType sysStaticType = sysStaticTypeDao.findByStaticCodeAndState(Constans.SYS_ROLE_MENU,1L);
        //静态值
        List<SysStaticValue> sysStaticValueList = sysStaticValueDao.findAllByStaticTypeIdAndState(sysStaticType.getStaticId(),1L);
        //所有菜单
        List<SysMenu> sysMenuList = sysMenuDao.findAllByState(1L);
        //所有角色
        List<SysRole> sysRoleList = sysRoleDao.findAllByState(1L);
        //所有角色菜单关联表信息
        List<SysMenuRole> allSysMenuRoles = sysMenuRoleDao.findAllByState(1L);
        //符合条件的角色菜单关联表
        List<SysMenuRole> sysMenuRoles = new ArrayList<>();
        //符合条件的静态值
        List<SysStaticValue> fitSysStaticValue = sysStaticValueList.stream().filter(o->
             sysRoleList.stream().anyMatch(a -> {
                if(o.getValueName().equals(a.getRoleName())){
                    o.setRemarkOne(a.getRoleId().toString());
                    return true;
                }
                return false;
            })
        ).collect(Collectors.toList());
        //再次筛选菜单存在的添加到关联表
        fitSysStaticValue.stream().forEach(o -> {
            log.info("菜单ID:{}",o.getValue().split(","));
            Arrays.stream(o.getValue().split(",")).forEach(a->{
                if(StringUtils.isNotEmpty(a)){
                    sysMenuList.stream().forEach(b->{
                        if(Long.valueOf(a).equals(b.getMenuId())){
                            sysMenuRoles.add(new SysMenuRole(Long.valueOf(a),Long.valueOf(o.getRemarkOne()),1L));
                        }
                    });
                }

            });
        });
        List<SysMenuRole> fitSysMenuRoles = new ArrayList<>();
        //筛选未添加到关联表的数据
        AtomicBoolean flag = new AtomicBoolean(true);
        sysMenuRoles.stream().forEach(o->{
            flag.set(true);
            for (SysMenuRole sysMenuRole : allSysMenuRoles) {
                if(o.getMenuId().equals(sysMenuRole.getMenuId())&&o.getRoleId().equals(sysMenuRole.getRoleId())){
                    flag.set(false);
                    break;
                }
            }
            if (flag.get()) {
                fitSysMenuRoles.add(o);
            }

        });
        log.info("添加角色关联菜单数据：{}",fitSysMenuRoles);
        sysMenuRoleDao.saveAll(fitSysMenuRoles);
        PageBaseInfo<SysMenu> sysMenuPage= sysMenuJdbc.findMenuList(sysMenuDto,pageBaseInfo);
        return sysMenuPage;
    }

    /**
     * @Description TODO 查询code-data形式的菜单信息
     * @param token
     * @return
     */
    @Override
    public CommonResult findCodeDataMenu(String token) {
        List<SysMenu> sysMenuList = sysMenuDao.findAllByState(1L);
        List<DataCodeDTO> sysMenuDataCodeList = sysMenuList.stream().map(o->{
            return new DataCodeDTO(o.getMenuId().toString(), o.getMenuName());
        }).collect(Collectors.toList());
        return CommonResult.success(sysMenuDataCodeList,"查询成功！");
    }

}
