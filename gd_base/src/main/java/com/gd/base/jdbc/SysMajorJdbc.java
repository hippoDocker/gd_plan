package com.gd.base.jdbc;

import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysMajorDTO;
import com.gd.base.pojo.vo.sys.SysMajorVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: tangxl
 * @Date: 2022年2月25日14:32:19
 * @Description: 专业处理JDBC
 */
@Repository
public class SysMajorJdbc {
	@Autowired
	private BaseJdbcTemplate jdbcTemplate;

	/**
	 * @Description TODO 专业分页查询
	 * @param token
	 * @param sysMajorDto
	 * @param page
	 * @return
	 */
	public PageBaseInfo<SysMajorVO> findMajorPage(String token, SysMajorDTO sysMajorDto, PageBaseInfo page) {
		String sqlStr = " select * from sys_major ss where 1=1 ";
		StringBuffer sql = new StringBuffer(sqlStr);
		List<Object> params = new ArrayList<>();
		sql.append(" and ss.state=1 ");
		//学校姓名
		if(StringUtils.isNotBlank(sysMajorDto.getMajorName())){
			sql.append(" and ss.major_name like ? ");
			params.add("%"+sysMajorDto.getMajorName()+"%");
		}
		//分页查询
		PageBaseInfo<SysMajorVO> SysMajorVoPage = null;
		SysMajorVoPage=jdbcTemplate.queryForMysqlPageInfo(SysMajorVO.class,sql.toString(),Boolean.FALSE,null,page,params.toArray());
		return SysMajorVoPage;
	}
}
