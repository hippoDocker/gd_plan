package com.gd.gd_service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.sys.SysUserAddDTO;
import com.gd.base.pojo.dto.sys.SysUserImportDTO;
import com.gd.base.entity.*;
import com.gd.base.jpa.*;
import com.gd.base.util.BeanUtil;
import com.gd.gd_service.service.SysUserService;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: tangxl
 * @Date: 2022年2月17日11:51:57
 * @Description:分批次存储到数据库，然后清空列表，方便内存回收，在导入
 */
@Log4j2
public class ImportUserListener extends AnalysisEventListener<SysUserImportDTO> {

    private static int num = 0;//条数记录
    private static String msg;//错误信息
    private String token;//令牌
    //学校、学院、专业、班级、角色
    private SysSchool sysSchool;
    private SysCollege sysCollege;
    private SysMajor sysMajor;
    private SysClass sysClass;
    private SysRole sysRole;
    /**
     * 缓存的数据
     */
    private SysUserAddDTO sysUserAddDto;

    //用户数据处理
    private SysUserService sysUserService;
    //返回结果
    public CommonResult result;
    private SysSchoolDao sysSchoolDao;
    private SysMajorDao sysMajorDao;
    private SysCollegeDao sysCollegeDao;
    private SysClassDao sysClassDao;
    private SysRoleDao sysRoleDao;

    //数据集合
    List<SysUserAddDTO> sysUserAddDTOList = new ArrayList<SysUserAddDTO>();

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param sysUserService 业务处理层
     */
    public ImportUserListener(SysUserService sysUserService, CommonResult result,String token,
                              SysSchoolDao sysSchoolDao,SysMajorDao sysMajorDao,SysCollegeDao sysCollegeDao,SysClassDao sysClassDao,SysRoleDao sysRoleDao) {
        this.sysUserService = sysUserService;
        this.result = result;
        this.token=token;
        this.sysSchoolDao=sysSchoolDao;
        this.sysCollegeDao = sysCollegeDao;
        this.sysMajorDao=sysMajorDao;
        this.sysClassDao=sysClassDao;
        this.sysRoleDao = sysRoleDao;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data   一条数据. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param analysisContext
     */
//    @Transactional(rollbackFor=Exception.class)
    @Override
    public void invoke(SysUserImportDTO data, AnalysisContext analysisContext) {
        try {
            num++;
            log.info("解析第"+num+"条数据:{}", JSON.toJSONString(data));
            //初始化数据
            initUser(data);
            //校验返回结果
            checkResult();
            sysUserAddDTOList.add(sysUserAddDto);
            //校验保存数据--每1000条
            if(sysUserAddDTOList.size() >= 1000){
                saveData();
                sysUserAddDTOList.clear();
            }
        }finally {
            num=0;
        }
    }

    /**
     * 初始化用户信息
     */
    public void initUser(SysUserImportDTO data) {
        sysUserAddDto = new SysUserAddDTO();
        //初始化部分用户数据
        sysUserAddDto = BeanUtil.copyObject(data, SysUserAddDTO.class);
        //学校、学院、专业、班级校验--教师和超管不需要专业、班级、学历、学校、学院等信息
        sysRole = sysRoleDao.findByRoleNameAndState(data.getRoleName(),1L);
        if(BeanUtil.isEmpty(sysRole)){
            throw new RuntimeException("第"+num+"条数据角色不存在！");
        }
        sysSchool = sysSchoolDao.findBySchoolNameAndState(data.getSchoolName(), 1L);
        sysCollege = sysCollegeDao.findBycollegeNameAndState(data.getCollegeName(), 1L);
        sysMajor = sysMajorDao.findByMajorNameAndState(data.getMajorName(),1L);
        sysClass = sysClassDao.findByClassNameAndState(data.getClassName(), 1L);
        if("Student".equals(sysRole.getRoleCode())){
            if(BeanUtil.isEmpty(sysSchool)){
                throw new RuntimeException("第"+num+"条数据学校不存在！");
            }
            if(BeanUtil.isEmpty(sysCollege)){
                throw new RuntimeException("第"+num+"条数据学院不存在！");
            }
            if(BeanUtil.isEmpty(sysMajor)){
                throw new RuntimeException("第"+num+"条数据专业不存在！");
            }
            if(BeanUtil.isEmpty(sysClass)){
                throw new RuntimeException("第"+num+"条数据班级不存在！");
            }
        }
        if(BeanUtil.isNotEmpty(sysSchool)){
            sysUserAddDto.setSchoolId(sysSchool.getSchoolId());
        }
        if(BeanUtil.isNotEmpty(sysCollege)){
            sysUserAddDto.setCollegeId(sysCollege.getCollegeId());
        }
        if(BeanUtil.isNotEmpty(sysMajor)){
            sysUserAddDto.setMajorId(sysMajor.getMajorId());
        }
        if(BeanUtil.isNotEmpty(sysClass)){
            sysUserAddDto.setClassId(sysClass.getClassId());
        }

    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        num = 0;
        sysUserAddDTOList.clear();
    }

    /**
     * 加上存储数据库
     */
    public void saveData() {
        this.result = sysUserService.saveAllUser(token, sysUserAddDTOList);
        msg = result.getMessage();
    }

    /**
     * 设置失败返回结果
     */
    public void setFailResult(String msg) {
        result.setSuccess(false);
        result.setCode(500);
        result.setMessage(msg);
    }

    /**
     * 校验返回结果
     */
    public void checkResult() {
        if(result.getCode()==500L){
            msg = result.getMessage();
            result.setMessage("第"+num+"行用户数据有误:"+msg);
            num = 0;
            throw new RuntimeException(result.getMessage());
        }
    }
}
