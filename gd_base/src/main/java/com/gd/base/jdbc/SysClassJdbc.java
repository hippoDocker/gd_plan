package com.gd.base.jdbc;

import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysClassDTO;
import com.gd.base.pojo.vo.sys.SysClassVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: tangxl
 * @Date:2022年2月25日14:32:49
 * @Description: 班级处理JDBC
 */
@Repository
public class SysClassJdbc {
	@Autowired
	private BaseJdbcTemplate jdbcTemplate;
	/**
	 * @Description TODO 班级分页查询
	 * @param token
	 * @param sysClassDto
	 * @param page
	 * @return
	 */
	public PageBaseInfo<SysClassVO> findClassPage(String token, SysClassDTO sysClassDto, PageBaseInfo page) {
		String sqlStr = " select * from sys_class ss where 1=1 ";
		StringBuffer sql = new StringBuffer(sqlStr);
		List<Object> params = new ArrayList<>();
		sql.append(" and ss.state=1 ");
		//学校姓名
		if(StringUtils.isNotBlank(sysClassDto.getClassName())){
			sql.append(" and ss.school_name = ? ");
			params.add(sysClassDto.getClassName());
		}
		//分页查询
		PageBaseInfo<SysClassVO> SysClassVoPage = null;
		SysClassVoPage=jdbcTemplate.queryForMysqlPageInfo(SysClassVO.class,sql.toString(),Boolean.FALSE,null,page,params.toArray());
		return SysClassVoPage;
	}
}
