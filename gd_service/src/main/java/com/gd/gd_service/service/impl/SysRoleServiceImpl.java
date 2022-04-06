package com.gd.gd_service.service.impl;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.DataCodeDTO;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysRoleDTO;
import com.gd.base.entity.SysMenuRole;
import com.gd.base.entity.SysRole;
import com.gd.base.jdbc.SysRoleJdbc;
import com.gd.base.jpa.SysMenuRoleDao;
import com.gd.base.jpa.SysRoleDao;
import com.gd.base.util.BeanUtil;
import com.gd.base.util.DateUtil;
import com.gd.base.pojo.vo.redis.RedisSysUser;
import com.gd.base.pojo.vo.sys.SysRoleVO;
import com.gd.gd_service.service.RedisService;
import com.gd.gd_service.service.SysRoleService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @Auther: tangxl
 * @Date: 2022年2月25日14:10:42
 * @Description: 系统角色管理
 */
@Service
@Log4j2
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysMenuRoleDao sysMenuRoleDao;
    @Autowired
    private SysRoleJdbc sysRoleJdbc;
    /**
     * @Description:TODO 创建角色
     * @return
     */
    @Override
    @Transactional
    public CommonResult createRole(SysRoleDTO sysRoleDto, String token) {
        //获取用户信息
        RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
        //校验基础信息
        if(StringUtils.isEmpty(redisSysUser.getRoleName())){
            return CommonResult.failed("角色名称不能为空！");
        }
        if (StringUtils.isEmpty(sysRoleDto.getRoleCode())){
            return CommonResult.failed("角色编码不能为空！");
        }
        //角色是否存在---角色名称
        List<SysRole> allSysRole = sysRoleDao.findAllByState(1L);
       if(allSysRole.stream().anyMatch(o->o.getRoleName().equals(sysRoleDto.getRoleName()))){
           return CommonResult.failed("角色名称已存在！");
       }
        if(allSysRole.stream().anyMatch(o -> o.getRoleCode().equals(sysRoleDto.getRoleCode()))){
            return CommonResult.failed("该角色编码已存在！");
        }
        //不同相同属性值拷贝
        SysRole sysRole = BeanUtil.copyObject(sysRoleDto,SysRole.class);
        //角色创建时间
        sysRole.setCreateTime(DateUtil.dateToLocalDateTime(new Date()));
        //状态
        sysRole.setState(1L);
        //创建人
        sysRole.setCreateBy(redisSysUser.getUserName());
        sysRoleDao.save(sysRole);
        return CommonResult.success("添加角色成功！");
    }
    /**
     * @Description:TODO 根据ID集合删除角色
     * @return
     */
    @Override
    public CommonResult deleteRole(String token, SysRoleDTO sysRoleDto) {
        if(CollectionUtils.isEmpty(sysRoleDto.getRoleIds())){
            return CommonResult.failed("请至少选择一个角色删除！");
        }
        //获取菜单id列表
        List<SysRole> sysRoleList = sysRoleDao.findAllByRoleIdInAndState(sysRoleDto.getRoleIds(),1L);
        if(BeanUtil.isEmpty(sysRoleList)){
            return CommonResult.failed("获取角色信息失败！");
        }
        //删除绑定菜单的关联表信息
        List<SysMenuRole> sysMenuRoleList = sysMenuRoleDao.findAllByRoleIdInAndState(sysRoleDto.getRoleIds(), 1L);
        sysMenuRoleList.stream().forEach(o->o.setState(0L));
        sysRoleList.stream().forEach(o->o.setState(0L));
        sysMenuRoleDao.saveAll(sysMenuRoleList);
        sysRoleDao.saveAll(sysRoleList);
        return CommonResult.success("删除成功！");
    }
    /**
     * @Description:TODO 更新角色
     * @return
     */
    @Override
    @Transactional
    public CommonResult updateRole(String token, SysRoleDTO sysRoleDto) {
        //获取需要修改的角色信息
        SysRole sysRole = sysRoleDao.findByRoleIdAndState(sysRoleDto.getRoleId(),1L);
        //菜单信息修改
        sysRole.setRoleCode(sysRoleDto.getRoleCode());
        sysRole.setRoleName(sysRoleDto.getRoleName());
        sysRoleDao.save(sysRole);
        return CommonResult.success("角色信息更新成功！");
    }
    /**
     * @Description TODO 角色分页查询
     * @param token
     * @param sysRoleDto
     * @return
     */
    @Override
    public PageBaseInfo<SysRole> findRoleByRoleNamePage(String token, SysRoleDTO sysRoleDto, PageBaseInfo page) {
        PageBaseInfo<SysRole> sysRolePage = sysRoleJdbc.findRoleByRoleNamePage(sysRoleDto,page);
        return sysRolePage;
    }
    /**
     * @Description TODO 根据菜单ID查询角色信息
     * @param sysRoleDto
     * @return
     */
    @Override
    public CommonResult findRoleByRoleId(String token, SysRoleDTO sysRoleDto) {
        //根据id查询角色
        SysRole sysRole = sysRoleDao.findByRoleIdAndState(sysRoleDto.getRoleId(),1L);
        if(BeanUtil.isEmpty(sysRole)){
            return CommonResult.failed("未查询到角色信息！");
        }
        SysRoleVO sysRoleVo = BeanUtil.copyObject(sysRole, SysRoleVO.class);
        return CommonResult.success(sysRoleVo);
    }

    /**
     * @Description TODO 查询code-data形式角色信息
     * @param token
     * @return
     */
    @Override
    public CommonResult findCodeDataRole(String token) {
        List<SysRole> sysRoleList = sysRoleDao.findAllByState(1L);
        List<DataCodeDTO> sysRoleDataCodeDTOS = sysRoleList.stream().map(o->{
            return new DataCodeDTO(o.getRoleId().toString(),o.getRoleName());
        }).collect(Collectors.toList());
        return CommonResult.success(sysRoleDataCodeDTOS,"查询成功！");
    }
}
