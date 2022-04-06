package com.gd.base.jdbc;

import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysRoleDTO;
import com.gd.base.entity.SysRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
/**
 * @Auther: tangxl
 * @Date:2022年2月25日14:33:40
 * @Description: 角色数据处理JDBC
 */
@Repository
public class SysRoleJdbc {
	@Autowired
	private BaseJdbcTemplate baseJdbcTemplate;

	public PageBaseInfo<SysRole> findRoleByRoleNamePage(SysRoleDTO sysRoleDto, PageBaseInfo page) {
		String sqlStr = " select * from sys_role sr where sr.state = 1 ";
		StringBuffer sql = new StringBuffer(sqlStr);
		List<Object> params = new ArrayList<>();
		//角色名称条件
		if(StringUtils.isNotBlank(sysRoleDto.getRoleName())){
			sql.append(" and sr.role_name like ? ");
			params.add("%"+sysRoleDto.getRoleName()+"%");
		}
		PageBaseInfo<SysRole> sysRolePage = new PageBaseInfo<>();
		sysRolePage=baseJdbcTemplate.queryForMysqlPageInfo(SysRole.class,sql.toString(),Boolean.FALSE,null,page,params.toArray());
		return sysRolePage;
	}
}