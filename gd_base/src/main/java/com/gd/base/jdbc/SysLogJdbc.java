package com.gd.base.jdbc;

import com.gd.base.pojo.dto.DataCodeDTO;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.HistoryDesignThemeDTO;
import com.gd.base.pojo.dto.sys.SysInterfaceLogDTO;
import com.gd.base.pojo.dto.sys.SysUserLoginLogDTO;
import com.gd.base.pojo.vo.plan.HistoryDesignThemeVO;
import com.gd.base.pojo.vo.sys.SysInterfaceLogVO;
import com.gd.base.pojo.vo.sys.SysInterfaceReportVO;
import com.gd.base.pojo.vo.sys.SysUserLoginLogVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 系统日志数据处理JDBC
 * @author: tangxl
 * @create: 2022-03-01 16:21
 */
@Repository
public class SysLogJdbc {
	@Autowired
	private BaseJdbcTemplate jdbcTemplate;

	/**
	 * @description: TODO 用户登录日志分页查询
	 * @Param: sysUserLoginLogDto,PageBaseInfo
	 * @author: tangxl
	 * @date: 2022年3月1日16:31:02
	 * @return: PageBaseInfo<SysUserLoginLogVo>
	 */
	public PageBaseInfo<SysUserLoginLogVO> findLoginLogPage(SysUserLoginLogDTO sysUserLoginLogDto, PageBaseInfo page) {
		String baseSql = " select * from sys_user_login_log su where 1=1 ";
		StringBuffer querysql = new StringBuffer(baseSql);
		List<Object> params = new ArrayList<>();
		//用户名
		if(StringUtils.isNotBlank(sysUserLoginLogDto.getLoginUserName())){
			querysql.append(" and su.login_user_name = ? ");
			params.add(sysUserLoginLogDto.getLoginUserName());
		}
		//时间
		if(StringUtils.isNotBlank(sysUserLoginLogDto.getLoginDateStart()) && !StringUtils.isNotBlank(sysUserLoginLogDto.getLoginDateEnd())){
			querysql.append(" and su.login_date > ? ");
			params.add(sysUserLoginLogDto.getLoginDateStart());
		}
		if(!StringUtils.isNotBlank(sysUserLoginLogDto.getLoginDateStart()) && StringUtils.isNotBlank(sysUserLoginLogDto.getLoginDateEnd())){
			querysql.append(" and su.login_date < ? ");
			params.add(sysUserLoginLogDto.getLoginDateEnd());
		}
		if(StringUtils.isNotBlank(sysUserLoginLogDto.getLoginDateStart()) && StringUtils.isNotBlank(sysUserLoginLogDto.getLoginDateEnd())){
			querysql.append(" and su.login_date between ? and ? ");
			params.add(sysUserLoginLogDto.getLoginDateStart());
			params.add(sysUserLoginLogDto.getLoginDateEnd());
		}
		PageBaseInfo<SysUserLoginLogVO> sysUserLoginLogVoPage = jdbcTemplate.queryForMysqlPageInfo(SysUserLoginLogVO.class, querysql.toString(), Boolean.FALSE,null,page,params.toArray());
		return sysUserLoginLogVoPage;
	}
	/**
	 * @description: TODO 接口日志分页查询
	 * @Param: SysInterfaceLogDto,PageBaseInfo
	 * @author: tangxl
	 * @date: 2022年3月1日16:31:02
	 * @return: PageBaseInfo<SysInterfaceLogVo>
	 */
	public PageBaseInfo<SysInterfaceLogVO> findInterfaceLogPage(SysInterfaceLogDTO sysInterfaceLogDto, PageBaseInfo page) {
		String baseSql = " select * from sys_interface_log si where 1=1 ";
		StringBuffer querysql = new StringBuffer(baseSql);
		List<Object> params = new ArrayList<>();
		//用户学号
		if(StringUtils.isNotBlank(sysInterfaceLogDto.getUserCode())){
			querysql.append(" and si.user_code = ? ");
			params.add(sysInterfaceLogDto.getUserCode());
		}
		//用户名称
		if (StringUtils.isNotBlank(sysInterfaceLogDto.getUserName())) {
			querysql.append(" and si.user_name = ? ");
			params.add(sysInterfaceLogDto.getUserName());
		}
		//接口请求
		if (StringUtils.isNotBlank(sysInterfaceLogDto.getInterfaceUrl())) {
			querysql.append(" and si.interface_url like ? ");
			params.add("%"+sysInterfaceLogDto.getInterfaceUrl()+"%");
		}
		//请求方式
		if(StringUtils.isNotBlank(sysInterfaceLogDto.getMethod())){
			querysql.append(" and si.method = ? ");
			params.add(sysInterfaceLogDto.getMethod());
		}
		//ip地址
		if(StringUtils.isNotBlank(sysInterfaceLogDto.getIp())){
			querysql.append(" and si.ip like ? ");
			params.add("%"+sysInterfaceLogDto.getIp()+"%");
		}
		//操作时间
		if (StringUtils.isNotBlank(sysInterfaceLogDto.getCreateTimeStart()) && !StringUtils.isNotBlank(sysInterfaceLogDto.getCreateTimeEnd())) {
			querysql.append(" and si.create_time > ? ");
			params.add(sysInterfaceLogDto.getCreateTimeStart());
		}
		if (!StringUtils.isNotBlank(sysInterfaceLogDto.getCreateTimeStart()) && StringUtils.isNotBlank(sysInterfaceLogDto.getCreateTimeEnd())) {
			querysql.append(" and si.create_time < ? ");
			params.add(sysInterfaceLogDto.getCreateTimeEnd());
		}
		if (StringUtils.isNotBlank(sysInterfaceLogDto.getCreateTimeStart()) && StringUtils.isNotBlank(sysInterfaceLogDto.getCreateTimeEnd())) {
			querysql.append(" and si.create_time between ? and ? ");
			params.add(sysInterfaceLogDto.getCreateTimeStart());
			params.add(sysInterfaceLogDto.getCreateTimeEnd());
		}
		PageBaseInfo<SysInterfaceLogVO> sysInterfaceLogVoPage = jdbcTemplate.queryForMysqlPageInfo(SysInterfaceLogVO.class, querysql.toString(), Boolean.TRUE,"id",page,params.toArray());
		return sysInterfaceLogVoPage;
	}

	/**
	 * @description: TODO 接口图形化报表数据查询
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	public List<SysInterfaceReportVO> findInterfaceReport(SysInterfaceLogDTO sysInterfaceLogDto) {
		String baseSql = " select a.day, case when c.female is null then 0 else c.female end female, "
				+ "case when b.male is null then 0 else b.male end male, "
				+ "case when c.average is null then 0 else c.average end  average "
				+ "from( "
				+ "  SELECT  "
				+ "      DATE_FORMAT(ADDDATE( ? , INTERVAL @dateAdd:=@dateAdd+1 DAY),'%Y-%m-%d') day "
				+ "  FROM sys_interface_log "
				+ "  LEFT JOIN (SELECT @dateAdd:=-1) t ON 1=1 "
				+ "  WHERE  "
				+ "    DATE_FORMAT(ADDDATE( ? , INTERVAL @dateAdd DAY),'%Y-%m-%d') < DATE_FORMAT( ? ,'%Y-%m-%d') "
				+ "  ) a "
				+ "left join (select str_to_date(a.create_time,'%Y-%m-%d') day,count(1) male "
				+ "from sys_interface_log a where a.interface_url='/api/sys/login' GROUP BY day ) b on a.day=b.day "
				+ "left join (select str_to_date(a.create_time,'%Y-%m-%d') day,count(DISTINCT a.phone) female,count(1) average "
				+ "from sys_interface_log a "
				+ "GROUP BY day) c on a.day =c.day ";
		StringBuffer querySql = new StringBuffer(baseSql);
		List<Object> params = new ArrayList<>();
		//添加参数
		params.add(sysInterfaceLogDto.getCreateTimeStart());
		params.add(sysInterfaceLogDto.getCreateTimeStart());
		params.add(sysInterfaceLogDto.getCreateTimeEnd());
		return jdbcTemplate.queryForMysqlList(SysInterfaceReportVO.class,querySql.toString(),Boolean.FALSE,null,params.toArray());
	}

	/**
	 * @description: TODO 查询历年设计主题推荐
	 * @return:
	 * @author: tangxl
	 * @time:  22:08
	 */
    public PageBaseInfo<HistoryDesignThemeVO> findHistoryDesignTheme(HistoryDesignThemeDTO historyDesignThemeDTO, PageBaseInfo page) {
    	StringBuilder querySql = new StringBuilder("select * from history_design_theme hdt where 1=1");
		List<Object> params = new ArrayList<>();
		//主题类别条件
		if(StringUtils.isNotBlank(historyDesignThemeDTO.getType())){
			querySql.append(" and hdt.type = ? ");
			params.add(historyDesignThemeDTO.getType());
		}
		return jdbcTemplate.queryForMysqlPageInfo(HistoryDesignThemeVO.class,querySql.toString(),Boolean.FALSE,null,page,params.toArray());
	}

	/**
	 * @description: TODO 查询历年设计主题类型
	 * @return:
	 * @author: tangxl
	 * @time:  22:08
	 */
	public List<DataCodeDTO> findHistoryType() {
		StringBuilder querySql = new StringBuilder(" select cast((@i:=@i+1) as char) code,a.data from(select hdt.type data  from history_design_theme hdt GROUP BY hdt.type) a " +
				"left join  (select @i := 0) i on 1=1 ");

		return jdbcTemplate.queryForMysqlList(DataCodeDTO.class,querySql.toString(),Boolean.FALSE,null,null);
	}
}
