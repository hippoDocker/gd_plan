package com.gd.base.jdbc;

import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysCollegeDTO;
import com.gd.base.pojo.vo.sys.SysCollegeVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: tangxl
 * @Date:2022年2月25日14:33:11
 * @Description: 学院数据处理JDBC
 */
@Repository
public class SysCollegeJdbc {
	@Autowired
	private BaseJdbcTemplate jdbcTemplate;

	/**
	 * @Description TODO 学院分页查询
	 * @param token
	 * @param sysCollegeDto
	 * @param page
	 * @return
	 */
	public PageBaseInfo<SysCollegeVO> findCollegePage(String token, SysCollegeDTO sysCollegeDto, PageBaseInfo page) {
		String sqlStr = " select * from sys_college ss where 1=1 ";
		StringBuffer sql = new StringBuffer(sqlStr);
		List<Object> params = new ArrayList<>();
		sql.append(" and ss.state=1 ");
		//学校姓名
		if(StringUtils.isNotBlank(sysCollegeDto.getCollegeName())){
			sql.append(" and ss.college_name like ? ");
			params.add("%"+sysCollegeDto.getCollegeName()+"%");
		}
		//分页查询
		PageBaseInfo<SysCollegeVO> SysCollegeVoPage = null;
		SysCollegeVoPage=jdbcTemplate.queryForMysqlPageInfo(SysCollegeVO.class,sql.toString(),Boolean.FALSE,null,page,params.toArray());
		return SysCollegeVoPage;
	}
}
