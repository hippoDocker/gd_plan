package com.gd.base.jdbc;

import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysStaticValueDTO;
import com.gd.base.pojo.dto.sys.SysUserAddDTO;
import com.gd.base.pojo.vo.sys.SysUserExportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: tangxl
 * @Date: 2022年2月16日15:26:36
 * @Description: 用户信息数据操作
 */
@Repository
public class SysUserJdbc {
    @Autowired
    BaseJdbcTemplate baseJdbcTemplate;
    /**
     * 用户信息查询
     * @param sysUserAddDto 查询条件
     * @param page 分页信息
     * @return
     */
    public PageBaseInfo<SysUserExportVO> findUserPage(SysUserAddDTO sysUserAddDto, PageBaseInfo page) {
        StringBuffer sql = new StringBuffer("  " +
                " select su.*,sm.major_name,sco.college_name,scl.class_name,sr.role_name  " +
                " from sys_user su  " +
                " left join sys_major sm on su.major_id = sm.major_id " +
                " left join sys_college sco on su.college_id = sco.college_id " +
                " left join sys_class scl on su.class_id = scl.class_id " +
                " left join sys_user_role sur on su.user_id = sur.user_id " +
                " left join sys_role sr on sur.role_id = sr.role_id " +
                " where 1 = 1 ");
        List<Object> params = new ArrayList<>();
        //添加查询条件
        //默认只查有效账号
        sql.append(" and su.state=1 ");
        //学号
        if (StringUtils.isNotBlank(sysUserAddDto.getUserCode())) {
            sql.append(" and su.user_code like ?  ");
            params.add(" %"+sysUserAddDto.getUserCode()+"% ");
        }
        //用户姓名
        if (StringUtils.isNotBlank(sysUserAddDto.getUserName())) {
            sql.append(" and su.user_name like ?  ");
            params.add(" %"+sysUserAddDto.getUserName()+"% ");
        }
        //手机号
        if (StringUtils.isNotBlank(sysUserAddDto.getPhone())) {
            sql.append(" and su.phone like ?  ");
            params.add(" %"+sysUserAddDto.getPhone()+"% ");
        }
        //邮箱
        if (StringUtils.isNotBlank(sysUserAddDto.getEmail())) {
            sql.append(" and su.email like ?  ");
            params.add(" %"+sysUserAddDto.getEmail()+"% ");
        }
        //出生日期
        if (StringUtils.isNotBlank(sysUserAddDto.getBirthdayStart())&&StringUtils.isNotBlank(sysUserAddDto.getBirthdayStart())) {
            sql.append(" and su.birthday between ? and ?  ");
            params.add(sysUserAddDto.getBirthdayStart());
            params.add(sysUserAddDto.getBirthdayStart());
        }
        //学院
        if(StringUtils.isNotBlank(sysUserAddDto.getCollegeName())){
            sql.append(" sco.college_name like ? ");
            params.add("%"+sysUserAddDto.getCollegeName()+"%");
        }
        //专业
        if(StringUtils.isNotBlank(sysUserAddDto.getMajorName())){
            sql.append(" sm.major_name like ? ");
            params.add("%"+sysUserAddDto.getMajorName()+"%");
        }
        //班级
        if(StringUtils.isNotBlank(sysUserAddDto.getClassName())){
            sql.append(" scl.class_name like ? ");
            params.add("%"+sysUserAddDto.getClassName()+"%");
        }
        PageBaseInfo<SysUserExportVO> sysUserExportVoPage = new PageBaseInfo<>();
        sysUserExportVoPage = baseJdbcTemplate.queryForMysqlPageInfo(SysUserExportVO.class,sql.toString(),false,null,page,params.toArray());
        return sysUserExportVoPage;
    }

    /**
     * 根据类型编码加静态值名称查询静态值
     */
    public List<SysStaticValueDTO> findIsDefaultPwdStatic(String staticCode,String valueName) {
        StringBuffer querySql = new StringBuffer(" select b.value_name,b.value from sys_static_type a " +
                "left join sys_static_value b on b.static_type_id = a.static_id " +
                "where 1 = 1 ");
        List<Object> params = new ArrayList<>();
        if(StringUtils.isNotBlank(staticCode)){
            querySql.append(" and a.static_code = ? ");
            params.add(staticCode);
        }
        if(StringUtils.isNotBlank(valueName)){
            querySql.append(" and b.value_name = ? ");
        }
        List<SysStaticValueDTO> sysStaticValueDTOS = baseJdbcTemplate.queryForMysqlList(SysStaticValueDTO.class, querySql.toString(), Boolean.FALSE,null,params.toArray());
        return sysStaticValueDTOS;
    }
}
