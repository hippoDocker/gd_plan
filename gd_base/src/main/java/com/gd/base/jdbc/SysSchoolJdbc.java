package com.gd.base.jdbc;

import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysSchoolDTO;
import com.gd.base.pojo.vo.sys.SysSchoolVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: tangxl
 * @Date: 2022年2月25日14:34:03
 * @Description: 学校数据处理JDBC
 */
@Repository
public class SysSchoolJdbc {
	@Autowired
	private BaseJdbcTemplate jdbcTemplate;

	public PageBaseInfo<SysSchoolVO> findShcoolPage(String token, SysSchoolDTO sysSchoolDto, PageBaseInfo page) {
		String sqlStr = " select * from sys_school ss where 1=1 ";
		StringBuffer sql = new StringBuffer(sqlStr);
		List<Object> params = new ArrayList<>();
		sql.append(" and ss.state=1 ");
		//学校姓名
		if(StringUtils.isNotBlank(sysSchoolDto.getSchoolName())){
			sql.append(" and ss.school_name like ? ");
			params.add("%"+sysSchoolDto.getSchoolName()+"%");
		}
		//分页查询
		PageBaseInfo<SysSchoolVO> sysSchoolPage = new PageBaseInfo<>();
		sysSchoolPage=jdbcTemplate.queryForMysqlPageInfo(SysSchoolVO.class,sql.toString(),Boolean.FALSE,null,page,params.toArray());
		return sysSchoolPage;
	}
}
