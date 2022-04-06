package com.gd.base.jdbc;

import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysStaticTypePageDTO;
import com.gd.base.pojo.dto.sys.SysStaticValuePageDTO;
import com.gd.base.util.BeanUtil;
import com.gd.base.pojo.vo.sys.SysStaticTypeVO;
import com.gd.base.pojo.vo.sys.SysStaticValueVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 静态值数据处理
 * @author: tangxl
 * @create: 2022-03-02 11:47
 */
@Repository
public class SysStaticJdbc {
	@Autowired
	BaseJdbcTemplate jdbcTemplate;
	/**
	 * @description: TODO 静态属性分页查询
	 * @Param: SysStaticTypeDto,PageBaseInfo
	 * @author: tangxl
	 * @date: 2022年3月2日11:51:15
	 * @return: PageBaseInfo<SysStaticTypeVo>
	 */
	public PageBaseInfo<SysStaticTypeVO> findStaticTypePage(SysStaticTypePageDTO sysStaticTypePageDto, PageBaseInfo page) {
		String baseSql = " select t.* FROM sys_static_type t where t.state = 1  ";
		StringBuffer querySql = new StringBuffer(baseSql);
		List<Object> params = new ArrayList<>();
		//条件
		if (StringUtils.isNotEmpty(sysStaticTypePageDto.getStaticName())) {
			querySql.append(" and t.static_name like ? ");
			params.add("%"+sysStaticTypePageDto.getStaticName()+"%");
		}
		PageBaseInfo<SysStaticTypeVO> sysStaticTypeVoPage = new PageBaseInfo<>();
		sysStaticTypeVoPage = jdbcTemplate.queryForMysqlPageInfo(SysStaticTypeVO.class,querySql.toString(),Boolean.FALSE,null,page,params.toArray());
		return sysStaticTypeVoPage;
	}
	/**
	 * @description: TODO 静态值分页查询
	 * @Param: SysStaticValueDto,PageBaseInfo
	 * @author: tangxl
	 * @date: 2022年3月2日11:51:15
	 * @return: PageBaseInfo<SysStaticValueVo>
	 */
	public PageBaseInfo<SysStaticValueVO> findStaticValuePage(SysStaticValuePageDTO sysStaticValuePageDto, PageBaseInfo page) {
		String baseSql = " select v.* FROM sys_static_type t "
				+ "left join sys_static_value v on t.static_id = v.static_type_id "
				+ "where t.state = 1 and v.state = 1   ";
		StringBuffer querySql = new StringBuffer(baseSql);
		List<Object> params = new ArrayList<>();
		if(BeanUtil.isNotEmpty(sysStaticValuePageDto.getStaticId())){
			querySql.append(" and t.static_id =? ");
			params.add(sysStaticValuePageDto.getStaticId());
		}
		PageBaseInfo<SysStaticValueVO> sysStaticValueVoPage = new PageBaseInfo<>();
		sysStaticValueVoPage = jdbcTemplate.queryForMysqlPageInfo(SysStaticValueVO.class,querySql.toString(),Boolean.FALSE,null,page,params.toArray());
		return sysStaticValueVoPage;
	}
}
