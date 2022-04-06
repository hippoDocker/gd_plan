package com.gd.gd_service.service.impl;

import com.gd.base.constant.Constans;
import com.gd.base.entity.*;
import com.gd.base.jdbc.SysUserJdbc;
import com.gd.base.jpa.*;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysChangeUserRoleAndPwdDTO;
import com.gd.base.pojo.dto.sys.SysStaticValueDTO;
import com.gd.base.pojo.dto.sys.SysUserAddDTO;
import com.gd.base.pojo.vo.redis.RedisSysUser;
import com.gd.base.pojo.vo.sys.SysUserExportVO;
import com.gd.base.util.*;
import com.gd.gd_service.service.RedisService;
import com.gd.gd_service.service.SysUserService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: tangxl
 * @Date: 2022年2月11日16:04:03
 * @Description: 用户管理
 */
@Service
@Log4j2
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;
    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysMajorDao sysMajorDao;
    @Autowired
    private SysCollegeDao sysCollegeDao;
    @Autowired
    private SysSchoolDao sysSchoolDao;
    @Autowired
    private SysClassDao sysClassDao;
    @Autowired
    private SysFtpFileDao sysFtpFileDao;
    @Autowired
    FtpUtil ftpUtil;
    @Autowired
    private SysUserJdbc sysUserJdbc;


    /**
     * @Description 添加单个用户
     * @param token
     * @param sysUserAddDto
     * @return
     */
    @Override
    public CommonResult addOneUser(String token, SysUserAddDTO sysUserAddDto) {
        //只有管理员才能添加用户
        RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
        if(BeanUtil.isEmpty(redisSysUser)){
            return CommonResult.failed("用户已失效，请重新登录！");
        }
        SysUserRole sysUserRole = sysUserRoleDao.findByUserIdAndState(redisSysUser.getUserId(),1L);
        SysRole sysRole = sysRoleDao.findByRoleIdAndState(sysUserRole.getRoleId(),1L);
        if(!("Admin").equals(sysRole.getRoleCode())){
            return CommonResult.failed("只有超级管理可以添加用户！");
        }
        //初始化新用户信息
        SysUser newSysUser = new SysUser();
        String msg ="";
        //信息校验
        //获取所以有效用户
        List<SysUser> sysUserList = sysUserDao.findAllByState(1L);
        //用户姓名校验
        if(StringUtils.isEmpty(sysUserAddDto.getUserName())){
            msg = "用户姓名为空！";
            throw new RuntimeException(msg);
        }
        newSysUser.setUserName(sysUserAddDto.getUserName());
        //手机号校验
        if(!Pattern.matches("^1[3-9]\\d{9}$", sysUserAddDto.getPhone())){
            msg="手机号码格式错误！";
            throw new RuntimeException(msg);
        }
        if(!StringUtil.isPhone(sysUserAddDto.getPhone())){
            msg = "手机号码错误！";
            throw new RuntimeException(msg);
        }
        if((sysUserList.stream().filter(sysUser->sysUser.getPhone().equals(sysUserAddDto.getPhone())).count())!=0L){
            msg ="此手机号码已注册！";
            throw new RuntimeException(msg);
        }
        newSysUser.setPhone(sysUserAddDto.getPhone());
        //邮箱校验
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)？\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(check);
        Matcher m = p.matcher(sysUserAddDto.getEmail());
        if(!StringUtil.isEmail(sysUserAddDto.getEmail())||m.matches()){
            msg="邮箱格式错误！";
            throw new RuntimeException(msg);
        }
        newSysUser.setEmail(sysUserAddDto.getEmail());
        //用户性别
        if(StringUtils.isEmpty(sysUserAddDto.getSex())){
            msg="性别为空！";
            throw new RuntimeException(msg);
        }
        if(!(sysUserAddDto.getSex().equals("男")||sysUserAddDto.getSex().equals("女"))){
            msg="性别格式错误！";
            throw new RuntimeException(msg);
        }
        newSysUser.setSex(sysUserAddDto.getSex());
        //用户出生日期
        if(StringUtils.isEmpty(sysUserAddDto.getBirthday())){
            throw new RuntimeException("用户出生日期为空！");
        }
        if(DateUtil.validDateStr(sysUserAddDto.getBirthday(),Constans.DEFAULT_DATE_FORMAT) ||
                DateUtil.validDateStr(sysUserAddDto.getBirthday(),Constans.DEFAULT_TIME_FORMAT3)){
            newSysUser.setBirthday(DateUtil.stringToLocalDateTime(sysUserAddDto.getBirthday(),Constans.DEFAULT_DATE_TIME_FORMAT));
        }else {
            throw new RuntimeException("出生日期格式错误！");
        }

        //用户角色
        if(StringUtils.isBlank(sysUserAddDto.getRoleName())){
            msg="角色不能为空！";
            throw new RuntimeException(msg);
        }
        SysRole associatedRole = sysRoleDao.findByRoleNameAndState(sysUserAddDto.getRoleName(),1L);
        if(BeanUtil.isEmpty(associatedRole)){
            msg="角色不存在！";
            throw new RuntimeException(msg);
        }
        //用户关联专业ID、用户关联学院ID、用户关联学校、用户关联班级
        SysMajor sysMajor = sysMajorDao.findByMajorIdAndState(sysUserAddDto.getMajorId(),1L);
        SysCollege sysCollege = sysCollegeDao.findBycollegeIdAndState(sysUserAddDto.getCollegeId(),1L);
        SysSchool sysSchool = sysSchoolDao.findBySchoolIdAndState(sysUserAddDto.getSchoolId(),1L);
        SysClass sysClass = sysClassDao.findByClassIdAndState(sysUserAddDto.getClassId(),1L);
        if("Student".equals(associatedRole.getRoleCode())){
            if(BeanUtil.isEmpty(sysSchool)){
                throw new RuntimeException("学校不存在！");
            }
            if(BeanUtil.isEmpty(sysCollege)){
                throw new RuntimeException("学院不存在！");
            }
            if(BeanUtil.isEmpty(sysMajor)){
                throw new RuntimeException("专业不存在！");
            }
            if(BeanUtil.isEmpty(sysClass)){
                throw new RuntimeException("班级不存在！");
            }
        }
        if(BeanUtil.isNotEmpty(sysSchool)){
            newSysUser.setSchoolId(sysSchool.getSchoolId());
        }
        if(BeanUtil.isNotEmpty(sysCollege)){
            newSysUser.setCollegeId(sysCollege.getCollegeId());
            newSysUser.setCollege(sysCollege.getCollegeName());
        }
        if(BeanUtil.isNotEmpty(sysMajor)){
            newSysUser.setMajorId(sysMajor.getMajorId());
        }
        if(BeanUtil.isNotEmpty(sysClass)){
            newSysUser.setClassId(sysClass.getClassId());
        }

        //用户学历
        newSysUser.setEducation("本科");

        //判断是否需要使用静态值配置的密码并初始化所有密码
        List<SysStaticValueDTO> isDefaultPwdStaticDTOS = sysUserJdbc.findIsDefaultPwdStatic(Constans.IS_DEFAULT_PWD,null);
        String studentPwd = "";
        String teacherPwd = "";
        String adminPwd = "";
        if(!CollectionUtils.isEmpty(isDefaultPwdStaticDTOS) && isDefaultPwdStaticDTOS.get(0).getValue().equals("1")){
            //获取静态值配置密码信息
            List<SysStaticValueDTO> userPwdStaticDtos = sysUserJdbc.findIsDefaultPwdStatic(Constans.USER_PWD_STATIC,null);
            switch (associatedRole.getRoleCode()){
                case "Student":studentPwd = userPwdStaticDtos.stream().filter(o->o.getValueName().equals(Constans.STUDENT_PWD_STATIC)).findFirst().get().getValue();
                    break;
                case "Teacher":teacherPwd = userPwdStaticDtos.stream().filter(o->o.getValueName().equals(Constans.TEACHER_PWD_STATIC)).findFirst().get().getValue();
                    break;
                case "Admin":adminPwd = userPwdStaticDtos.stream().filter(o->o.getValueName().equals(Constans.ADMIN_PWD_STATIC)).findFirst().get().getValue();
                    break;
                default: return CommonResult.failed("角色信息错误,请联系管理员！");
            }
        }else {
            switch (associatedRole.getRoleCode()){
                case "Student":
                    if(sysUserAddDto.getUserCode().length() > 6){
                       studentPwd = sysUserAddDto.getUserCode().substring(sysUserAddDto.getUserCode().length()-6);
                    }else {
                        studentPwd = "123456";
                    }
                    break;
                case "Teacher":teacherPwd =  "123456";
                    break;
                case "Admin":adminPwd = "superAdmin";
                    break;
                default: return CommonResult.failed("角色信息错误,请联系管理员！");
            }
        }
        //只有学生才有学号，学生密码默认学号后六位(如果学号不够6位，默认123456)，教师密码默认123456且教师没有学号,管理员默认superAdmin且管理员可以以学号为随意账号
        if(("Student").equals(associatedRole.getRoleCode())){
            if(StringUtils.isEmpty(sysUserAddDto.getUserCode())){
                msg="用户学号为空！";
                throw new RuntimeException(msg);
            }
            if(!BeanUtil.isEmpty(sysUserDao.findByUserCodeAndState(sysUserAddDto.getUserCode(),1L))){
                msg="此学号已注册！";
                throw new RuntimeException(msg);
            }
            //如果没有选择年级默认学号欠位
            if(StringUtils.isEmpty(sysUserAddDto.getGrade())){
               sysUserAddDto.setGrade(sysUserAddDto.getUserCode().substring(0,4));
            }
            newSysUser.setGrade(sysUserAddDto.getGrade());
            log.info(">>>>>新用户"+sysUserAddDto.getUserName()+"初始密码："+studentPwd);
            newSysUser.setUserPwd(SecurityUtils.encryptionPwd(studentPwd));
        }
        if(associatedRole.getRoleCode().equals("Teacher")){
            log.info(">>>>>新用户"+sysUserAddDto.getUserName()+"初始密码："+teacherPwd);
            newSysUser.setUserPwd(SecurityUtils.encryptionPwd(teacherPwd));
        }
        if(associatedRole.getRoleCode().equals("Admin")){
            log.info(">>>>>新用户"+sysUserAddDto.getUserName()+"初始密码："+adminPwd);
            newSysUser.setUserPwd(SecurityUtils.encryptionPwd(adminPwd));
        }
        //教师和管理员不能有学号
        if(("Teacher").equals(associatedRole.getRoleCode()) || ("Admin").equals(associatedRole.getRoleCode())){
            if(StringUtils.isNotEmpty(sysUserAddDto.getUserCode())){
                log.info("姓名::"+sysUserAddDto.getUserName()+";教师和管理人员不能有学号！已自动去除学号");
            }
        }else {
            newSysUser.setUserCode(sysUserAddDto.getUserCode());
        }
        //用户创建时间更新时间
        newSysUser.setCreateTime(DateUtil.dateToLocalDateTime(new Date()));
        newSysUser.setUpdateTime(DateUtil.dateToLocalDateTime(new Date()));
        //用户其他信息初始化
        newSysUser.setState(1L);
        newSysUser.setPwdErrTime(0L);
        newSysUser.setLastLoginTime(DateUtil.dateToLocalDateTime(new Date()));
        newSysUser.setCreateUserId(sysUserDao.findByPhoneAndState(redisSysUser.getPhone(),1L).getUserId());
        //初始化用户头像
        newSysUser.setHeadImg(Constans.HEAD_IMG_URL+sysUserAddDto.getPhone());
        ftpUtil.addNewUserHead(Constans.HEAD_IMG_PATH,sysUserAddDto.getPhone()+".jpeg");
        //保存用户
        newSysUser = sysUserDao.save(newSysUser);
        //保存关联角色信息
        SysUserRole associatedUserRole= new SysUserRole();
        associatedUserRole.setUserId(newSysUser.getUserId());
        associatedUserRole.setRoleId(associatedRole.getRoleId());
        associatedUserRole.setState(1L);
        sysUserRoleDao.save(associatedUserRole);
        return CommonResult.success("用户保存成功！");
    }

    /**
     * @Description 获取单个用户头像
     * @param token
     * @param phone 用户手机号
     * @return
     */
    @Override
    public void findHeadImg(String token, String phone,ByteArrayOutputStream out) {
        ftpUtil.getUserHeadStream(Constans.HEAD_IMG_PATH,phone,out);
    }

    /**
     * 用户信息分页查询
     * @param token 令牌
     * @param sysUserAddDto 查询条件
     * @param page 分页信息
     * @return
     */
    @Override
    public PageBaseInfo<SysUserExportVO> findUserPage(String token, SysUserAddDTO sysUserAddDto, PageBaseInfo page) {
        PageBaseInfo<SysUserExportVO> sysUserExportVoPage = sysUserJdbc.findUserPage(sysUserAddDto,page);
        return sysUserExportVoPage;
    }

    /**
     * 导出用户信息条件查询
     * @param token 令牌
     * @param sysUserAddDto 查询条件
     * @param page 分页信息
     * @return
     */
    @Override
    public List<SysUserExportVO> findUserExport(String token, SysUserAddDTO sysUserAddDto, PageBaseInfo page) {
        PageBaseInfo<SysUserExportVO> sysUserVoPage = sysUserJdbc.findUserPage(sysUserAddDto,page);
        return sysUserVoPage.getData();
    }

    /**
     * 用户信息批量导入
     * @param sysUserAddDTOList
     */
    @Override
//    @Transactional(rollbackOn = Exception.class)
    public CommonResult saveAllUser(String token,List<SysUserAddDTO> sysUserAddDTOList) {
        CommonResult commonResult = null;
        int num = 0;
        String msg = "";
        //添加数据

        //只有管理员才能添加用户
        RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
        if(BeanUtil.isEmpty(redisSysUser)){
            return CommonResult.failed("用户已失效，请重新登录！");
        }
        SysUserRole sysUserRole = sysUserRoleDao.findByUserIdAndState(redisSysUser.getUserId(),1L);
        SysRole sysRole = sysRoleDao.findByRoleIdAndState(sysUserRole.getRoleId(),1L);
        if(!("Admin").equals(sysRole.getRoleCode())){
            return CommonResult.failed("只有超级管理可以添加用户！");
        }
        try {
            for (SysUserAddDTO sysUserAddDto : sysUserAddDTOList) {
                num++;
                //初始化新用户信息
                SysUser newSysUser = new SysUser();
                //信息校验
                //获取所以有效用户
                List<SysUser> sysUserList = sysUserDao.findAllByState(1L);
                //用户姓名校验
                if(StringUtils.isEmpty(sysUserAddDto.getUserName())){
                    msg = "第"+num+"行数据有误：用户姓名为空！";
                    throw new RuntimeException(msg);
                }
                newSysUser.setUserName(sysUserAddDto.getUserName());
                //手机号校验
                if(!Pattern.matches("^1[3-9]\\d{9}$", sysUserAddDto.getPhone())){
                    msg="第"+num+"行数据有误：手机号码格式错误！";
                    throw new RuntimeException(msg);
                }
                if(!StringUtil.isPhone(sysUserAddDto.getPhone())){
                    msg = "第"+num+"行数据有误：手机号码错误！";
                    throw new RuntimeException(msg);
                }
                if((sysUserList.stream().filter(sysUser->sysUser.getPhone().equals(sysUserAddDto.getPhone())).count())!=0L){
                    msg ="第"+num+"行数据有误：此手机号码已注册！";
                    throw new RuntimeException(msg);
                }
                newSysUser.setPhone(sysUserAddDto.getPhone());
                //邮箱校验
                String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)？\\.)+[a-zA-Z]{2,}$";
                Pattern p = Pattern.compile(check);
                Matcher m = p.matcher(sysUserAddDto.getEmail());
                if(!StringUtil.isEmail(sysUserAddDto.getEmail())||m.matches()){
                    msg="第"+num+"行数据有误：邮箱格式错误！";
                    throw new RuntimeException(msg);
                }
                newSysUser.setEmail(sysUserAddDto.getEmail());
                //用户性别
                if(StringUtils.isEmpty(sysUserAddDto.getSex())){
                    msg="第"+num+"行数据有误：性别为空！";
                    throw new RuntimeException(msg);
                }
                if(!(sysUserAddDto.getSex().equals("男")||sysUserAddDto.getSex().equals("女"))){
                    msg="第"+num+"行数据有误：性别格式错误！";
                    throw new RuntimeException(msg);
                }
                newSysUser.setSex(sysUserAddDto.getSex());
                //用户出生日期
                if(StringUtils.isEmpty(sysUserAddDto.getBirthday())){
                    throw new RuntimeException("第"+num+"行数据有误：用户出生日期为空！");
                }
                try {
                    if(DateUtil.validDateStr(sysUserAddDto.getBirthday(),Constans.DEFAULT_DATE_TIME_FORMAT) ||
                            DateUtil.validDateStr(sysUserAddDto.getBirthday(),Constans.DEFAULT_DATE_FORMAT)){
                        newSysUser.setBirthday(DateUtil.stringToLocalDateTime(sysUserAddDto.getBirthday(),Constans.DEFAULT_DATE_TIME_FORMAT));
                    }else if(DateUtil.validDateStr(sysUserAddDto.getBirthday(),Constans.DEFAULT_TIME_FORMAT3)||
                            DateUtil.validDateStr(sysUserAddDto.getBirthday(),Constans.DEFAULT_TIME_FORMAT3)){
                        sysUserAddDto.setBirthday(sysUserAddDto.getBirthday().replaceAll("/","-"));
                        newSysUser.setBirthday(DateUtil.stringToLocalDateTime(sysUserAddDto.getBirthday(),Constans.DEFAULT_DATE_TIME_FORMAT));
                    }else{
                        throw new RuntimeException("第"+num+"行数据有误：出生日期格式错误！");
                    }
                }catch (Exception e) {
                    throw new RuntimeException("第"+num+"行数据有误：出生日期格式错误！");
                }

                //用户角色
                if(StringUtils.isBlank(sysUserAddDto.getRoleName())){
                    msg="第"+num+"行数据有误：角色不能为空！";
                    throw new RuntimeException(msg);
                }
                SysRole associatedRole = sysRoleDao.findByRoleNameAndState(sysUserAddDto.getRoleName(),1L);
                if(BeanUtil.isEmpty(associatedRole)){
                    msg="第"+num+"行数据有误：角色不存在！";
                    throw new RuntimeException(msg);
                }
                //用户关联专业ID、用户关联学院ID、用户关联学校、用户关联班级
                SysMajor sysMajor = sysMajorDao.findByMajorIdAndState(sysUserAddDto.getMajorId(),1L);
                SysCollege sysCollege = sysCollegeDao.findBycollegeIdAndState(sysUserAddDto.getCollegeId(),1L);
                SysSchool sysSchool = sysSchoolDao.findBySchoolIdAndState(sysUserAddDto.getSchoolId(),1L);
                SysClass sysClass = sysClassDao.findByClassIdAndState(sysUserAddDto.getClassId(),1L);
                if(("Student").equals(associatedRole.getRoleCode())){
                    if(BeanUtil.isEmpty(sysSchool)){
                        throw new RuntimeException("第"+num+"条数据有误,学校不存在！");
                    }
                    if(BeanUtil.isEmpty(sysCollege)){
                        throw new RuntimeException("第"+num+"条数据有误,学院不存在！");
                    }
                    if(BeanUtil.isEmpty(sysMajor)){
                        throw new RuntimeException("第"+num+"条数据有误,专业不存在！");
                    }
                    if(BeanUtil.isEmpty(sysClass)){
                        throw new RuntimeException("第"+num+"条数据有误,班级不存在！");
                    }
                }
                if(BeanUtil.isNotEmpty(sysSchool)){
                    newSysUser.setSchoolId(sysSchool.getSchoolId());
                }
                if(BeanUtil.isNotEmpty(sysCollege)){
                    newSysUser.setCollegeId(sysCollege.getCollegeId());
                    newSysUser.setCollege(sysCollege.getCollegeName());
                }
                if(BeanUtil.isNotEmpty(sysMajor)){
                    newSysUser.setMajorId(sysMajor.getMajorId());
                }
                if(BeanUtil.isNotEmpty(sysClass)){
                    newSysUser.setClassId(sysClass.getClassId());
                }

                //用户学历
                newSysUser.setEducation(sysUserAddDto.getEducation());

                //判断是否需要使用静态值配置的密码并初始化所有密码
                List<SysStaticValueDTO> isDefaultPwdStaticDTOS = sysUserJdbc.findIsDefaultPwdStatic(Constans.IS_DEFAULT_PWD,null);
                String studentPwd = "";
                String teacherPwd = "";
                String adminPwd = "";
                //判断是否采取静态值配置初始密码
                if(!CollectionUtils.isEmpty(isDefaultPwdStaticDTOS) && isDefaultPwdStaticDTOS.get(0).getValue().equals("1")){
                    //获取静态值配置密码信息
                    List<SysStaticValueDTO> userPwdStaticDtos = sysUserJdbc.findIsDefaultPwdStatic(Constans.USER_PWD_STATIC,null);
                    switch (associatedRole.getRoleCode()){
                        case "Student":studentPwd = userPwdStaticDtos.stream().filter(o->o.getValueName().equals(Constans.STUDENT_PWD_STATIC)).findFirst().get().getValue();
                            break;
                        case "Teacher":teacherPwd = userPwdStaticDtos.stream().filter(o->o.getValueName().equals(Constans.TEACHER_PWD_STATIC)).findFirst().get().getValue();
                            break;
                        case "Admin":adminPwd = userPwdStaticDtos.stream().filter(o->o.getValueName().equals(Constans.ADMIN_PWD_STATIC)).findFirst().get().getValue();
                            break;
                        default: throw new RuntimeException("第"+num+"条数据有误：角色信息错误,请联系管理员！");
                    }
                }else {
                    switch (associatedRole.getRoleCode()){
                        case "Student":
                            if(sysUserAddDto.getUserCode().length() > 6){
                                studentPwd = sysUserAddDto.getUserCode().substring(sysUserAddDto.getUserCode().length()-6);
                            }else {
                                studentPwd = "123456";
                            }
                            break;
                        case "Teacher":teacherPwd =  "123456";
                            break;
                        case "Admin":adminPwd = "superAdmin";
                            break;
                        default: throw new RuntimeException("第"+num+"条数据有误：角色信息错误,请联系管理员！");
                    }
                }
                //只有学生才有学号，学生密码默认学号后六位(如果学号不够6位，默认123456)，教师密码默认123456且教师没有学号,管理员默认superAdmin且管理员可以以学号为随意账号
                if(("Student").equals(associatedRole.getRoleCode())){
                    if(StringUtils.isEmpty(sysUserAddDto.getUserCode())){
                        msg="第"+num+"条数据有误：用户学号为空！";
                        throw new RuntimeException(msg);
                    }
                    if(!BeanUtil.isEmpty(sysUserDao.findByUserCodeAndState(sysUserAddDto.getUserCode(),1L))){
                        msg="第"+num+"条数据有误：此学号已注册！";
                        throw new RuntimeException(msg);
                    }
                    if(StringUtils.isEmpty(sysUserAddDto.getGrade())){
                        sysUserAddDto.setGrade(sysUserAddDto.getUserCode().substring(0,4));
                    }
                    newSysUser.setGrade(sysUserAddDto.getGrade());
                    log.info(">>>>>新用户"+sysUserAddDto.getUserName()+"初始密码："+studentPwd);
                    newSysUser.setUserPwd(SecurityUtils.encryptionPwd(studentPwd));
                }
                if(associatedRole.getRoleCode().equals("Teacher")){
                    log.info(">>>>>新用户"+sysUserAddDto.getUserName()+"初始密码："+teacherPwd);
                    newSysUser.setUserPwd(SecurityUtils.encryptionPwd(teacherPwd));
                }
                if(associatedRole.getRoleCode().equals("Admin")){
                    log.info(">>>>>新用户"+sysUserAddDto.getUserName()+"初始密码："+adminPwd);
                    newSysUser.setUserPwd(SecurityUtils.encryptionPwd(adminPwd));
                }
                //教师和管理员不能有学号
                if(("Teacher").equals(associatedRole.getRoleCode()) || ("Admin").equals(associatedRole.getRoleCode())){
                    if(StringUtils.isNotEmpty(sysUserAddDto.getUserCode())){
                        log.info("姓名::"+sysUserAddDto.getUserName()+";教师和管理人员不能有学号！已自动去除学号");
                    }
                }else {
                    newSysUser.setUserCode(sysUserAddDto.getUserCode());
                }

                //用户创建时间更新时间
                newSysUser.setCreateTime(DateUtil.dateToLocalDateTime(new Date()));
                newSysUser.setUpdateTime(DateUtil.dateToLocalDateTime(new Date()));
                //用户其他信息初始化
                newSysUser.setUserCode(sysUserAddDto.getUserCode());
                newSysUser.setState(1L);
                newSysUser.setPwdErrTime(0L);
                newSysUser.setLastLoginTime(DateUtil.dateToLocalDateTime(new Date()));
                newSysUser.setCreateUserId(sysUserDao.findByPhoneAndState(redisSysUser.getPhone(),1L).getUserId());
                //初始化用户头像
                newSysUser.setHeadImg(Constans.HEAD_IMG_URL+sysUserAddDto.getPhone());
                ftpUtil.addNewUserHead(Constans.HEAD_IMG_PATH,sysUserAddDto.getPhone()+".jpeg");
                //保存用户
                newSysUser = sysUserDao.save(newSysUser);
                //保存关联角色信息
                SysUserRole associatedUserRole= new SysUserRole();
                associatedUserRole.setUserId(newSysUser.getUserId());
                associatedUserRole.setRoleId(associatedRole.getRoleId());
                associatedUserRole.setState(1L);
                sysUserRoleDao.save(associatedUserRole);
            }
        }finally {
            num = 0;
        }

        msg="用户保存成功！";
        return CommonResult.success(msg);

    }

    @Override
    public SysUser addtestTrans(SysUser sysUser){
        return sysUserDao.save(sysUser);
    }

    /**
     * @description: TODO 用户头像更换
     * @Param: token 用户令牌，file 新头像文件
     * @author: tangxl
     * @date: 2022年3月8日11:49:33
     * @return: CommonResult
     */
    @Override public CommonResult addUserHead(String token, MultipartFile file) {
        RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
        //删除用户原头像
        ftpUtil.deleteUserHead(Constans.HEAD_IMG_PATH,redisSysUser.getPhone());
        //修改用户头像
        try {
            String fileName = file.getOriginalFilename();
            ftpUtil.uploadFile(Constans.HEAD_IMG_PATH,redisSysUser.getPhone() + fileName.substring(fileName.lastIndexOf(".")),file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("修改用户头像失败{}",e.getMessage());
            return CommonResult.failed("修改失败！");
        }

        return CommonResult.success("修改成功！");
    }

    /**
     * @description: TODO 修改用户密码
     * @return:
     * @author: tangxl
     * @time:  15:07
     */ 
    @Override
    public CommonResult updateUserPwd(String token, SysChangeUserRoleAndPwdDTO userDto) {
        RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
        //密码校验
        if(StringUtils.isEmpty(userDto.getUserPwd())){
            return CommonResult.failed("密码为空");
        }
        // 6-20 位，字母、数字、字符
        String regStr = "^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*()——+|{}【】‘；：”“'。，、？]){6,20}$";

        if(!userDto.getUserPwd().matches(regStr)){
            return CommonResult.failed("请输入6~15位由数字、字母、字符组成的密码");
        }
        //用户修改密码和用户信息更新时间
        SysUser sysUser = sysUserDao.findByStateAndUserId(1L,userDto.getUserId());
        if(BeanUtil.isEmpty(sysUser)){
            return CommonResult.failed("未查询到用户信息！");
        }
        String newPwd = SecurityUtils.encryptionPwd(userDto.getUserPwd());
        sysUser.setUserPwd(newPwd);
        sysUser.setUpdateTime(DateUtil.dateToLocalDateTime(new Date()));
        sysUserDao.save(sysUser);
        //清理当前用户缓存信息
        redisService.cleanUser(sysUser.getPhone());
        return CommonResult.success("修改成功！");
    }

    /**
     * @description: TODO 用户授权角色信息
     * @return:
     * @author: tangxl
     * @time:  16:00
     */ 
    @Override
    public CommonResult updateUserRole(String token, SysChangeUserRoleAndPwdDTO userDto) {
        SysUser sysUser = sysUserDao.findByStateAndUserId(1L,userDto.getUserId());
        if(BeanUtil.isEmpty(sysUser)){
            return CommonResult.failed("未查询到用户信息！");
        }
        if(null == userDto.getRoleId()){
            return CommonResult.failed("请选择用户赋权角色");
        }
        SysRole sysRole = sysRoleDao.findByRoleIdAndState(1L, userDto.getRoleId());
        if(BeanUtil.isEmpty(sysRole)){
            return CommonResult.failed("角色不存在！");
        }
        //用户赋权角色
        List<SysUserRole> sysUserRoleList = sysUserRoleDao.findAllByUserIdAndState(userDto.getUserId(), 1L);
        sysUserRoleList.stream().forEach(o->o.setState(0L));
        sysUserRoleDao.saveAll(sysUserRoleList);
        SysUserRole sysUserRole = new SysUserRole(null,userDto.getUserId(), userDto.getRoleId(), 1L);
        sysUserRoleDao.save(sysUserRole);
        sysUser.setUpdateTime(DateUtil.dateToLocalDateTime(new Date()));
        sysUserDao.save(sysUser);
        return CommonResult.success("赋权成功!");
    }

    /**
     * @description: TODO 修改用户信息，包含用户姓名、手机号码、邮箱、学历、性别、专业、学院、班级
     * @return:
     * @author: tangxl
     * @time:  16:27
     */
    @Override
    public CommonResult updateUserDetail(String token, SysUserAddDTO userDto) {
        RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
        SysUser sysUser = sysUserDao.findByStateAndUserId(1L,userDto.getUserId());
        if(BeanUtil.isEmpty(sysUser)){
            return CommonResult.failed("用户已不存在！");
        }
        //学号
        if(StringUtils.isNotEmpty(sysUser.getUserCode())){
            sysUser.setUserCode(userDto.getUserCode());
        }
        //姓名
        if(StringUtils.isNotEmpty(sysUser.getUserName())){
            sysUser.setUserName(userDto.getUserName());
        }
        //手机号码
        if(StringUtils.isNotEmpty(userDto.getPhone())){
            //手机号格式校验
            if(!StringUtil.isPhone(userDto.getPhone())){
                return CommonResult.failed("手机号格式错误！");
            }
            sysUser.setPhone(userDto.getPhone());
        }
        //邮箱
        if(StringUtils.isNotEmpty(userDto.getEmail())){
            if(!StringUtil.isEmail(userDto.getEmail())){
                return CommonResult.failed("邮箱格式错误！");
            }
            sysUser.setEmail(userDto.getEmail());
        }
        //学历
        if(StringUtils.isNotEmpty(userDto.getEducation())){
            sysUser.setEducation(userDto.getEducation());
        }
        //性别
        if(null != userDto.getSex()){
            sysUser.setSex(userDto.getSex());
        }
        //专业
        if(null != userDto.getMajorId()){
            if(BeanUtil.isEmpty(sysMajorDao.findByMajorIdAndState(userDto.getMajorId(), 1L))){
                return CommonResult.failed("专业不存在！");
            }
            sysUser.setMajorId(userDto.getMajorId());
        }
        //学院
        if(null != userDto.getCollegeId()){
            SysCollege sysCollege = sysCollegeDao.findByCollegeIdAndState(userDto.getCollegeId(),1L);
            if(BeanUtil.isEmpty(sysCollege)){
                return CommonResult.failed("学院不存在！");
            }
            sysUser.setCollegeId(sysCollege.getCollegeId());
            sysUser.setCollege(sysCollege.getCollegeName());
        }
        //班级
        if(null != userDto.getClassId()){
            if (BeanUtil.isEmpty(sysClassDao.findByClassIdAndState(userDto.getClassId(), 1L))){
                return CommonResult.failed("班级不存在！");
            }
            sysUser.setClassId(userDto.getClassId());
        }
        //设计更新时间
        sysUser.setUpdateTime(DateUtil.dateToLocalDateTime(new Date()));
        sysUserDao.save(sysUser);
        return CommonResult.success("操作成功！");
    }

    /**
     * @description: TODO
      * @param token 令牌，userDto 用户ID信息
     * @author: tangxl
     * @return: CommonResult
     */
    @Override
    public CommonResult updateUserState(String token, SysChangeUserRoleAndPwdDTO userDto) {
        SysUser sysUser = sysUserDao.findByUserId(userDto.getUserId());
        if(BeanUtil.isEmpty(sysUser)){
            return CommonResult.failed("用户信息不存在！");
        }
        //修改用户状态
        sysUser.setState(1L);
        sysUser.setPwdErrTime(0L);
        sysUser.setUpdateTime(DateUtil.dateToLocalDateTime(new Date()));
        sysUserDao.save(sysUser);
        return CommonResult.success("操作成功！");
    }

    /**
     * @description: TODO 删除用户，将用户改为无效，清理关联关系
      * @param null
     * @author: tangxl
     * @return:
     */
    @Override
    public CommonResult deleteUser(String token, SysChangeUserRoleAndPwdDTO userDto) {
        SysUser sysUser = sysUserDao.findByUserId(userDto.getUserId());
        if(BeanUtil.isEmpty(sysUser)){
            return CommonResult.failed("用户不存在！");
        }
        //修改用户状态
        sysUser.setState(0L);
        //删除用户关联角色信息
        List<SysUserRole> sysUserRoleList = sysUserRoleDao.findAllByUserIdAndState(sysUser.getUserId(),1L);
        sysUserRoleList.stream().forEach(o->o.setState(0L));
        //删除用户头像信息--成功
        if(ftpUtil.deleteUserHead(Constans.HEAD_IMG_PATH,sysUser.getPhone())){
            sysUserDao.save(sysUser);
            sysUserRoleDao.saveAll(sysUserRoleList);
        }
        return CommonResult.success("删除成功！");
    }


}
