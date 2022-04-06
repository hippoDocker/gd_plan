package com.gd.base.jdbc;

import com.gd.base.pojo.dto.DataCodeDTO;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.TeacherDirectionPageDTO;
import com.gd.base.pojo.dto.plan.TeacherThemeDTO;
import com.gd.base.pojo.dto.plan.TeacherThemePageDTO;
import com.gd.base.pojo.dto.sys.GdPlanProcessReportDTO;
import com.gd.base.pojo.dto.sys.SysInterfaceLogDTO;
import com.gd.base.pojo.vo.redis.RedisSysUser;
import com.gd.base.util.BeanUtil;
import com.gd.base.pojo.vo.plan.TeacherDirectionVO;
import com.gd.base.pojo.vo.plan.TeacherThemeVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 毕设主题选择相关数据处理
 * @author: tangxl
 * @create: 2022-03-02 17:06
 */
@Repository
public class GdPlanThemeJdbc {
	@Autowired
	private BaseJdbcTemplate jdbcTemplate;

	/**
	 * @description: TODO 查询设计主题详情文档名称列表
	 * @Param: teacherThemeDto 参数
	 * @author: tangxl
	 * @date: 2022年3月6日01:43:28
	 * @return: List<DataCodeDto>
	 */
	public List<DataCodeDTO> findGdThemeDetailName(TeacherThemeDTO teacherThemeDto) {
		String baseSql = " select cast((@i:=@i+1) as char) code,ff.file_name data "
				+ "from gd_design_theme dt "
				+ "left join sys_ftp_file ff on dt.ftp_file_batch = ff.batch "
				+ "LEFT JOIN (select @i := 0) i ON 1=1 "
				+ "where dt.state=1 and ff.state=1 ";
		StringBuffer querySql = new StringBuffer(baseSql);
		List<Object> params = new ArrayList<>();
		//条件
		if(null != teacherThemeDto.getId()){
			querySql.append(" and dt.id = ? ");
			params.add(teacherThemeDto.getId());
		}
		List<DataCodeDTO> dataCodeDTOList = jdbcTemplate.queryForMysqlList(DataCodeDTO.class,querySql.toString(),Boolean.FALSE,null,params.toArray());
		return dataCodeDTOList;
	}

	/**
	 * @description: TODO 教师设计方向分页查询
	 * @Param: teacherDesignDto 教师设计方向分页查询参数，page 分页信息
	 * @author: tangxl
	 * @date: 2022年3月4日15:58:09
	 * @return: PageBaseInfo<TeacherDesignVo>
	 */
	public PageBaseInfo<TeacherDirectionVO> findTeacherDesignDirectionPage(TeacherDirectionPageDTO teacherDirectionPageDto, PageBaseInfo page) {
		String sqlStr = " select dd.id,su.user_name teacherName,su.phone,su.email,dd.design_direction,dd.theme_num,dd.theme_margin "
				+ "from gd_design_direction dd "
				+ "left join sys_user su on dd.sys_user_id = su.user_id "
				+ "where dd.state=1 and su.state=1  ";
		StringBuffer querySql = new StringBuffer(sqlStr);
		List<Object> params = new ArrayList<>();
		//条件
		if(StringUtils.isNotBlank(teacherDirectionPageDto.getTeacherName())){
			querySql.append(" and su.user_name like ? ");
			params.add("%"+ teacherDirectionPageDto.getTeacherName()+"%");
		}
		if(StringUtils.isNotBlank(teacherDirectionPageDto.getPhone())){
			querySql.append(" and su.phone ");
			params.add(teacherDirectionPageDto.getPhone());
		}
		if(StringUtils.isNotBlank(teacherDirectionPageDto.getDesignDirection())){
			querySql.append(" and dd.design_direction like ? ");
			params.add("%"+ teacherDirectionPageDto.getDesignDirection()+"%");
		}
//		querySql.append(" group by dd.design_direction ");
		PageBaseInfo<TeacherDirectionVO> teacherDesignDirectionVoPage = null;
		teacherDesignDirectionVoPage = jdbcTemplate.queryForMysqlPageInfo(TeacherDirectionVO.class,querySql.toString(),Boolean.FALSE,null,page,params.toArray());
		return teacherDesignDirectionVoPage;
	}
	/**
	 * @description: TODO 教师设计主题分页查询
	 * @Param: teacherThemeDto 教师设计方向分页查询参数，page 分页信息
	 * @author: tangxl
	 * @date: 2022年3月4日15:58:09
	 * @return: PageBaseInfo<TeacherThemeVo>
	 */
	public PageBaseInfo<TeacherThemeVO> findTeacherDesignThemePage(TeacherThemePageDTO teacherThemePageDto, PageBaseInfo page) {
		String sqlStr = " select dt.id,su.user_name teacherName,su.phone,su.email,dd.design_direction,dt.theme_name,dt.theme_source,dt.remark,"
				+ "cast(dt.theme_upload_state as char) themeUploadState,cast(dt.is_choose as char) isChoose,dt.over_time "
				+ "from gd_design_direction dd "
				+ "left join sys_user su on dd.sys_user_id = su.user_id "
				+ "left join gd_design_theme dt on dt.gd_design_direction_id=dd.id "
				+ "where dd.state=1 and su.state=1 and dt.state=1   ";
		StringBuffer querySql = new StringBuffer(sqlStr);
		List<Object> params = new ArrayList<>();
		//条件
		if(BeanUtil.isNotEmpty(teacherThemePageDto.getUserId())){
			querySql.append(" and dt.sys_user_id = ?");
			params.add(teacherThemePageDto.getUserId());
		}
		if(StringUtils.isNotBlank(teacherThemePageDto.getTeacherName())){
			querySql.append(" and su.user_name like ? ");
			params.add("%"+ teacherThemePageDto.getTeacherName()+"%");
		}
		if(StringUtils.isNotBlank(teacherThemePageDto.getPhone())){
			querySql.append(" and su.phone ");
			params.add(teacherThemePageDto.getPhone());
		}
		if(StringUtils.isNotBlank(teacherThemePageDto.getDesignDirection())){
			querySql.append(" and dd.design_direction like ? ");
			params.add("%"+ teacherThemePageDto.getDesignDirection()+"%");
		}
		if(StringUtils.isNotBlank(teacherThemePageDto.getThemeName())){
			querySql.append(" and dt.theme_name like ? ");
			params.add("%"+ teacherThemePageDto.getThemeName()+"%");
		}
		PageBaseInfo<TeacherThemeVO> teacherThemeVoPage = null;
		teacherThemeVoPage = jdbcTemplate.queryForMysqlPageInfo(TeacherThemeVO.class,querySql.toString(),Boolean.FALSE,null,page,params.toArray());
		return teacherThemeVoPage;
	}

	public List<DataCodeDTO> findDataCodeGdDerection(RedisSysUser redisSysUser) {
		StringBuffer querySql = new StringBuffer(" select a.id code,a.design_direction data from gd_design_direction a where a.sys_user_id = ?  ");
		List<Object> params = new ArrayList<>();
		params.add(redisSysUser.getUserId());
		return jdbcTemplate.queryForMysqlList(DataCodeDTO.class,querySql.toString(),Boolean.FALSE,null,params.toArray());
	}

	/**
	 * @description: TODO 查询毕业设计流程图形化报表数据
	 * @return:
	 * @author: tangxl
	 * @time:  14:45
	 */
    public List<GdPlanProcessReportDTO> findGdPlanProcessRePort(SysInterfaceLogDTO sysInterfaceLogDTO) {
		StringBuffer querySql = new StringBuffer(" select a.day, " +
				"case when b.themeNum is null then 0 else b.themeNum end themeNum, " +
				"case when c.openNum is null then 0 else c.openNum end openNum, " +
				"case when d.midNum is null then 0 else d.midNum end midNum, " +
				"case when e.scoreNum is null then 0 else e.scoreNum end scoreNum, " +
				"case when f.defenseNum is null then 0 else f.defenseNum end defenseNum " +
				"from ( " +
				" select DATE_FORMAT(ADDDATE( ? , INTERVAL @dateAdd:=@dateAdd+1 DAY),'%Y-%m-%d') day   " +
				" from sys_interface_log    " +
				" right join (select @dateAdd:=-1) t ON 1=1    " +
				" where  DATE_FORMAT(ADDDATE( ?  , INTERVAL @dateAdd DAY),'%Y-%m-%d') < DATE_FORMAT( ? ,'%Y-%m-%d')  " +
				" ) a " +
				"left join ( " +
				"  select str_to_date(pd.create_time,'%Y-%m-%d') day,count(pd.examine_state) themeNum " +
				"  from gd_plan_detail pd  " +
				"  where pd.state=1 and  pd.examine_state=1 group by day " +
				" ) b on a.day = b.day " +
				"left join( " +
				"  select str_to_date(pd.create_time,'%Y-%m-%d') day,count(pd.open_examine_state) openNum " +
				"  from gd_plan_detail pd  " +
				"  where pd.state=1 and  pd.examine_state=1 and pd.open_examine_state = 1 group by day " +
				" ) c on a.day = c.day " +
				"left join( " +
				"  select str_to_date(pd.create_time,'%Y-%m-%d') day,count(pd.mid_examine_state) midNum " +
				"  from gd_plan_detail pd  " +
				"  where pd.state=1 and  pd.examine_state=1 and pd.mid_examine_state = 1 group by day " +
				" ) d on a.day = d.day " +
				"left join( " +
				"  select str_to_date(pd.create_time,'%Y-%m-%d') day,count(pd.score) scoreNum " +
				"  from gd_plan_detail pd  " +
				"  where pd.state=1 and  pd.examine_state=1 and pd.score is not null group by day " +
				" ) e on a.day = e.day " +
				"left join( " +
				"  select str_to_date(pd.create_time,'%Y-%m-%d') day,count(pd.exempt_defense_state) defenseNum " +
				"  from gd_plan_detail pd  " +
				"  where pd.state=1 and  pd.examine_state=1 and pd.exempt_defense_state = 1 group by day " +
				" ) f on a.day = f.day ");
		List<Object> parms = new ArrayList<>();
		parms.add(sysInterfaceLogDTO.getCreateTimeStart());
		parms.add(sysInterfaceLogDTO.getCreateTimeStart());
		parms.add(sysInterfaceLogDTO.getCreateTimeEnd());
		return jdbcTemplate.queryForMysqlList(GdPlanProcessReportDTO.class, querySql.toString(), Boolean.FALSE,"day",parms.toArray());
    }
}
