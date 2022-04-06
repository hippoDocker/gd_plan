package com.gd.gd_service.service.impl;

import com.gd.base.constant.Constans;
import com.gd.base.constant.RedisKeyConstants;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.entity.*;
import com.gd.base.jdbc.UserLoginJdbc;
import com.gd.base.jpa.*;
import com.gd.base.util.*;
import com.gd.base.pojo.vo.redis.RedisSysUser;
import com.gd.base.pojo.vo.sys.SysMenuVO;
import com.gd.gd_service.service.RedisService;
import com.gd.gd_service.service.SysLoginService;
import com.gd.gd_service.service.SysmenuService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Auther: tangxl
 * @Date: 2021年12月13日17:18:43
 * @Description: TODO 系统登录
 */
@Service
@Log4j2
public class SysLoginServiceImpl implements SysLoginService {
    @Autowired
    private UserLoginJdbc userLoginJdbc;
    @Autowired
    private VerifyCodeUtil verifyCodeUtil;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;
    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysmenuService sysmenuService;
    @Autowired
    private SysMenuRoleDao sysMenuRoleDao;
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysUserLoginLogDao sysUserLoginLogDao;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SysSchoolDao sysSchoolDao;
    @Autowired
    private SysMajorDao sysMajorDao;
    @Autowired
    private SysCollegeDao sysCollegeDao;
    @Autowired
    private SysClassDao sysClassDao;

    /**
     * @Description TODO 系统登录1
     * @return
     * @param userCode
     * @param userPwd
     * @param imgCode
     * @param key
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,timeout=50)
    public CommonResult saveSysLogin(String userCode, String userPwd, String imgCode, String key) {
        //登录账号 密码 验证码 是否为空校验
        //账号
        if(StringUtils.isEmpty(userCode)){
            return CommonResult.failed(Constans.SYS_LOGIN_CODE_ONE);
        }
        //密码
        if(StringUtils.isEmpty(userPwd)){
            return CommonResult.failed(Constans.SYS_LOGIN_CODE_TWO);
        }
        //验证码
        if(StringUtils.isEmpty(imgCode)){
            return CommonResult.failed(Constans.SYS_LOGIN_CODE_THREE);
        }
        //校验验证码
        String imgCodeRe = verifyCodeUtil.checkImgCode(imgCode, key);
        if(imgCodeRe.equals("error1")){
            return CommonResult.failed(Constans.SYS_LOGIN_CODE_FOUR);
        }
        if(imgCodeRe.equals("error2")){
            return CommonResult.failed(Constans.SYS_LOGIN_CODE_FIVE);
        }
        List<SysUser> sysUserList = new ArrayList<SysUser>();
        //如果是手机号登录
        if(userCode.matches("^1(3|4|5|6|7|8|9)\\d{9}$")){
            //通过手机号码获取符合条件的所有用户
            sysUserList = sysUserDao.findAllByPhoneAndStateOrState(userCode,1L,2L);
        }else {
            //通过学号获取符合条件的所有用户
            sysUserList = sysUserDao.findAllByUserCodeAndStateOrState(userCode,1L,2L);
        }
        //校验账号是否存在
        if(sysUserList.isEmpty() || sysUserList.size()==0){
            return CommonResult.failed(Constans.SYS_LOGIN_CODE_SEVEN);
        }
        //校验账号个数
        if(sysUserList.size()>1){
            return CommonResult.failed(Constans.SYS_LOGIN_CODE_SIX);
        }
        //校验账号状态 state=2锁定state=0注销
//        if(sysUserList.get(0).getState().equals(2L)){
//            return CommonResult.failed(Constans.SYS_LOGIN_CODE_NINE);
//        }
//        if(sysUserList.get(0).getState().equals(0L)){
//            return CommonResult.failed(Constans.SYS_LOGIN_CODE_TEN);
//        }
        //校验密码--密码错误添加错误次数
        if(!SecurityUtils.checkPwd(userPwd,sysUserList.get(0).getUserPwd())){
            //如果密码错误次数超过6次，锁定账号，需要管理员解锁
            sysUserList.get(0).setPwdErrTime(sysUserList.get(0).getPwdErrTime()+1);
            if(sysUserList.get(0).getPwdErrTime() >= 6){
                sysUserList.get(0).setState(2L);
                sysUserDao.saveAll(sysUserList);
                return CommonResult.failed(Constans.SYS_LOGIN_CODE_EIGHT+",账号已锁定，请联系管理员解锁！");
            }
            sysUserDao.saveAll(sysUserList);
            return CommonResult.failed(Constans.SYS_LOGIN_CODE_EIGHT+",剩余"+(6-sysUserList.get(0).getPwdErrTime())+"次机会！");
        }
        //更新用户登录密码错误次数
        sysUserList.get(0).setPwdErrTime(0L);
        //更新最后登录时间
        sysUserList.get(0).setLastLoginTime(DateUtil.dateToLocalDateTime(new Date()));
        sysUserDao.save(sysUserList.get(0));

        //加入缓存
        RedisSysUser redisSysUser = BeanUtil.copyObject(sysUserList.get(0),RedisSysUser.class);
        //获取用户关联所有角色
        SysUserRole sysUserRole = sysUserRoleDao.findByUserIdAndState(sysUserList.get(0).getUserId(),1L);
        //获取角色
        if(BeanUtil.isEmpty(sysUserRole)){
            return CommonResult.failed(Constans.SYS_LOGIN_CODE_ELEVEN);
        }
        //获取用户关联的角色信息
        SysRole sysRole = sysRoleDao.findByRoleIdAndState(sysUserRole.getRoleId(),1L);
        //初始化角色缓存
        redisSysUser.setRoleId(sysRole.getRoleId());
        redisSysUser.setRoleName(sysRole.getRoleName());
        //生成token令牌
        String token = TokenUtil.sign(sysUserList.get(0));
        redisSysUser.setToken(token);
        //获取角色关联的菜单
        List<SysMenuRole> sysMenuRoles = sysMenuRoleDao.findAllByRoleIdAndState(sysRole.getRoleId(),1L);
        //获取关联菜单所有ID
        List<Long> menuIds = sysMenuRoles.stream().map(SysMenuRole::getMenuId).collect(Collectors.toList());
        //获取用户对应角色关联的所有菜单
        List<SysMenu> sysMenus = sysMenuDao.findAllByMenuIdInAndState(menuIds,1L);
        //菜单序列化--生成子集菜单
        List<SysMenuVO> sysMenuVOList = BeanUtil.menuSerialize(sysMenus,0L);
        //初始化菜单缓存
        redisSysUser.setSysMenuVOList(sysMenuVOList);
        //获取学校、学院、专业、班级信息
        SysSchool sysSchool = sysSchoolDao.findBySchoolIdAndState(sysUserList.get(0).getSchoolId(),1L);
        SysMajor sysMajor = sysMajorDao.findByMajorIdAndState(sysUserList.get(0).getMajorId(),1L);
        SysCollege sysCollege = sysCollegeDao.findBycollegeIdAndState(sysUserList.get(0).getCollegeId(),1L);
        SysClass sysClass = sysClassDao.findByClassIdAndState(sysUserList.get(0).getClassId(),1L);
        if(("Student").equals(sysRole.getRoleCode())){
            if(BeanUtil.isEmpty(sysSchool)){
                return CommonResult.failed("学校信息错误，请联系管理员！");
            }
            if(BeanUtil.isEmpty(sysMajor)){
                return CommonResult.failed("专业信息错误，请联系管理员！");
            }
            if(BeanUtil.isEmpty(sysCollege)){
                return CommonResult.failed("学院信息错误，请联系管理员！");
            }
            if(BeanUtil.isEmpty(sysClass)){
                return CommonResult.failed("班级信息错误，请联系管理员！");
            }
        }
        if(BeanUtil.isNotEmpty(sysSchool)){
            redisSysUser.setSchoolId(sysSchool.getSchoolId());
            redisSysUser.setSchoolName(sysSchool.getSchoolName());
        }
        if(BeanUtil.isNotEmpty(sysCollege)){
            redisSysUser.setCollegeId(sysCollege.getCollegeId());
            redisSysUser.setCollegeName(sysCollege.getCollegeName());
        }
        if(BeanUtil.isNotEmpty(sysMajor)){
            redisSysUser.setMajorId(sysMajor.getMajorId());
            redisSysUser.setMajorName(sysMajor.getMajorName());

        }
        if(BeanUtil.isNotEmpty(sysClass)){
            redisSysUser.setCollegeId(sysClass.getClassId());
            redisSysUser.setCollegeName(sysClass.getClassName());
        }
        //生成登录日志
        SysUserLoginLog sysUserLoginLog = new SysUserLoginLog();
        sysUserLoginLog.setLoginUserId(redisSysUser.getUserId());
        sysUserLoginLog.setLoginUserName(redisSysUser.getUserName());
        sysUserLoginLog.setLoginDate(DateUtil.dateToLocalDateTime(new Date()));
        sysUserLoginLogDao.save(sysUserLoginLog);
        //清除原缓存,实现一个账户只允许一处登录
        String oldtoken = redisService.getTokenByUserCode(redisSysUser.getUserCode());
        if(!StringUtils.isEmpty(oldtoken)){
            redisService.deleteTokenByUserCode(redisSysUser.getUserCode());
            redisService.deleteSysUserByToken(oldtoken);
        }
        //保存登录缓存
        redisUtil.set(RedisKeyConstants.LOGIN_VALIDATE_SYS_USER+redisSysUser.getToken(),redisSysUser,4,TimeUnit.HOURS);
        redisUtil.set(RedisKeyConstants.LOGIN_VALIDATE_SYS_TOKEN+redisSysUser.getPhone(),token,4,TimeUnit.HOURS);
        log.info("====>登录成功：{}",redisSysUser);
        return CommonResult.success(redisSysUser,Constans.SYS_LOGIN_CODE_TWELVE);
    }

    /**
     * @description: TODO 用户注销登录
     * @Param: token 令牌
     * @return: 2022年3月1日11:18:05
     * @author: tangxl
     * @date: CommonResult
     */
    @Override public CommonResult cleanUserCache(String token) {
        RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
        //清理当前用户redis缓存
        redisService.deleteSysUserByToken(token);
        //清理token信息
        redisService.deleteTokenByUserCode(redisSysUser.getUserCode());
        //处理用户最后登录时间
        return CommonResult.success("注销成功！");
    }
}
