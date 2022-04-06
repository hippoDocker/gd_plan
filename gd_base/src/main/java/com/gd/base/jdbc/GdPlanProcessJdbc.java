package com.gd.base.jdbc;

import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.GdPlanReportPageDTO;
import com.gd.base.util.BeanUtil;
import com.gd.base.pojo.vo.plan.GdPlanExemptDefensePageVO;
import com.gd.base.pojo.vo.plan.GdPlanReportPageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计流程数据处理JDBC
 * @author: tangxl
 * @create: 2022-03-09 08:50
 */
@Component
public class GdPlanProcessJdbc {
	@Autowired
	private BaseJdbcTemplate jdbcTemplate;

	public PageBaseInfo<GdPlanReportPageVO> findOpenReportPage(GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo page) {
		String baseSql = " select pd.id,su.user_code,su.user_name studentName,sm.major_name,sc.class_name,te.user_name teacherName, "
				+ "te.phone,te.email,dd.design_direction,dt.theme_name, "
				+ "pd.open_examine_state examineState,pd.open_require_state requireState,pd.open_report_state reportState,pd.open_over_time overTime,dt.remark "
				+ "from gd_plan_detail pd "
				+ "left join gd_design_theme dt on pd.gd_design_theme_id = dt.id "
				+ "left join sys_user su on pd.user_id = su.user_id "
				+ "left join gd_design_direction dd on dt.gd_design_direction_id = dd.id "
				+ "left join sys_user te on dt.sys_user_id = te.user_id "
				+ "left join sys_major sm on su.major_id = sm.major_id "
				+ "left join sys_class sc on su.class_id = sc.class_id "
				+ "where pd.state=1 and dt.state=1 and dd.state=1 and su.state=1 "
				+ "and sm.state =1 and sc.state =1 and te.state=1 ";
		StringBuffer query = new StringBuffer(baseSql);
		List<Object> params = new ArrayList<>();
		//添加条件--学生只查看已审核通过的数据
		if(BeanUtil.isNotEmpty(gdPlanReportPageDto.getUserId())){
			query.append(" and pd.examine_state = ? ");
			params.add(1L);
			query.append(" and pd.user_id = ? ");
			params.add(gdPlanReportPageDto.getUserId());
		}else{
			query.append(" and pd.examine_state = ? ");
			params.add(1L);
		}
		if(!StringUtils.isEmpty(gdPlanReportPageDto.getExamineState())){
			query.append(" and pd.open_examine_state = ? ");
			params.add(gdPlanReportPageDto.getExamineState());
		}
		if(!StringUtils.isEmpty(gdPlanReportPageDto.getStudentName())){
			query.append(" and st.user_name like ? ");
			params.add("%"+gdPlanReportPageDto.getStudentName()+"%");
		}
		if(!StringUtils.isEmpty(gdPlanReportPageDto.getTeacherName())){
			query.append(" and te.user_name like ? ");
			params.add("%"+gdPlanReportPageDto.getTeacherName() + "%");
		}
		if(!StringUtils.isEmpty(gdPlanReportPageDto.getDesignDirection())){
			query.append(" and dd.design_direction like ? ");
			params.add("%"+gdPlanReportPageDto.getDesignDirection() + "%");
		}
		if(!StringUtils.isEmpty(gdPlanReportPageDto.getThemeName())){
			query.append(" and dt.theme_name like ? ");
			params.add("%"+gdPlanReportPageDto.getThemeName() + "%");
		}
		return jdbcTemplate.queryForMysqlPageInfo(GdPlanReportPageVO.class, query.toString(),Boolean.FALSE,null,page,params.toArray());
	}

	public PageBaseInfo<GdPlanReportPageVO> findMidReportPage(GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo page) {
		String baseSql = " select pd.id,su.user_code,su.user_name studentName,sm.major_name,sc.class_name,te.user_name teacherName,  "
				+ "te.phone,te.email,dd.design_direction,dt.theme_name,  "
				+ "pd.mid_examine_state examineState,pd.mid_require_state requireState,pd.mid_report_state reportState,pd.mid_over_time overTime,dt.remark  "
				+ "from gd_plan_detail pd  "
				+ "left join gd_design_theme dt on pd.gd_design_theme_id = dt.id  "
				+ "left join sys_user su on pd.user_id = su.user_id  "
				+ "left join gd_design_direction dd on dt.gd_design_direction_id = dd.id  "
				+ "left join sys_user te on dt.sys_user_id = te.user_id  "
				+ "left join sys_major sm on su.major_id = sm.major_id  "
				+ "left join sys_class sc on su.class_id = sc.class_id  "
				+ "where pd.state=1 and dt.state=1 and dd.state=1 and su.state=1  "
				+ "and sm.state =1 and sc.state =1 and te.state=1 ";
		StringBuffer query = new StringBuffer(baseSql);
		List<Object> params = new ArrayList<>();
		//添加条件--学生只查看已审核通过的数据
		if(BeanUtil.isNotEmpty(gdPlanReportPageDto.getUserId())){
			query.append(" and pd.examine_state = ? ");
			params.add(1L);
			query.append(" and pd.user_id = ? ");
			params.add(gdPlanReportPageDto.getUserId());
		}else{
			query.append(" and pd.examine_state = ? ");
			params.add(1L);
		}
		if(!StringUtils.isEmpty(gdPlanReportPageDto.getExamineState())){
			query.append(" and pd.mid_examine_state = ? ");
			params.add(gdPlanReportPageDto.getExamineState());
		}
		if(!StringUtils.isEmpty(gdPlanReportPageDto.getStudentName())){
			query.append(" and st.user_name like ? ");
			params.add("%"+gdPlanReportPageDto.getStudentName()+"%");
		}
		if(!StringUtils.isEmpty(gdPlanReportPageDto.getTeacherName())){
			query.append(" and te.user_name like ? ");
			params.add("%"+gdPlanReportPageDto.getTeacherName() + "%");
		}
		if(!StringUtils.isEmpty(gdPlanReportPageDto.getDesignDirection())){
			query.append(" and dd.design_direction like ? ");
			params.add("%"+gdPlanReportPageDto.getDesignDirection() + "%");
		}
		if(StringUtils.isEmpty(gdPlanReportPageDto.getThemeName())){
			query.append(" and dt.theme_name like ? ");
			params.add("%"+gdPlanReportPageDto.getThemeName() + "%");
		}
		return jdbcTemplate.queryForMysqlPageInfo(GdPlanReportPageVO.class, query.toString(),Boolean.FALSE,null,page,params.toArray());
	}

	public PageBaseInfo<GdPlanReportPageVO> findEndReportPage(GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo page) {
		String baseSql = " select pd.id,su.user_code,su.user_name studentName,sm.major_name,sc.class_name,te.user_name teacherName,  "
				+ "te.phone,te.email,dd.design_direction,dt.theme_name,  "
				+ "pd.examine_state,pd.end_require_state requireState,pd.end_report_state reportState,pd.end_over_time overTime,dt.remark  "
				+ "from gd_plan_detail pd  "
				+ "left join gd_design_theme dt on pd.gd_design_theme_id = dt.id  "
				+ "left join sys_user su on pd.user_id = su.user_id  "
				+ "left join gd_design_direction dd on dt.gd_design_direction_id = dd.id  "
				+ "left join sys_user te on dt.sys_user_id = te.user_id  "
				+ "left join sys_major sm on su.major_id = sm.major_id  "
				+ "left join sys_class sc on su.class_id = sc.class_id  "
				+ "where pd.state=1 and dt.state=1 and dd.state=1 and su.state=1  "
				+ "and sm.state =1 and sc.state =1 and te.state=1   ";
		StringBuffer query = new StringBuffer(baseSql);
		List<Object> params = new ArrayList<>();
		//添加条件--学生只查看已审核通过的数据
		if(BeanUtil.isNotEmpty(gdPlanReportPageDto.getUserId())){
			query.append(" and pd.examine_state = ? ");
			params.add(1L);
			query.append(" and pd.user_id = ? ");
			params.add(gdPlanReportPageDto.getUserId());
		}else{
			query.append(" and pd.examine_state = ? ");
			params.add(1L);
		}
		if(!StringUtils.isEmpty(gdPlanReportPageDto.getExamineState())){
			query.append(" and pd.examine_state = ? ");
			params.add(gdPlanReportPageDto.getExamineState());
		}
		if(!StringUtils.isEmpty(gdPlanReportPageDto.getStudentName())){
			query.append(" and st.user_name like ? ");
			params.add("%"+gdPlanReportPageDto.getStudentName()+"%");
		}
		if(!StringUtils.isEmpty(gdPlanReportPageDto.getTeacherName())){
			query.append(" and te.user_name like ? ");
			params.add("%"+gdPlanReportPageDto.getTeacherName() + "%");
		}
		if(!StringUtils.isEmpty(gdPlanReportPageDto.getDesignDirection())){
			query.append(" and dd.design_direction like ? ");
			params.add("%"+gdPlanReportPageDto.getDesignDirection() + "%");
		}
		if(StringUtils.isEmpty(gdPlanReportPageDto.getThemeName())){
			query.append(" and dt.theme_name like ? ");
			params.add("%"+gdPlanReportPageDto.getThemeName() + "%");
		}
		return jdbcTemplate.queryForMysqlPageInfo(GdPlanReportPageVO.class, query.toString(),Boolean.FALSE,null,page,params.toArray());
	}

	public PageBaseInfo<GdPlanExemptDefensePageVO> findExemptReportPage(GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo page) {
		String baseSql = " select pd.id,su.user_code,su.user_name studentName,sm.major_name,sc.class_name,te.user_name teacherName,  "
				+ "te.phone,te.email,dd.design_direction,dt.theme_name,  "
				+ "pd.examine_state,pd.exempt_defense_state defenseState,pd.exempt_upload_state uploadState,dt.remark  "
				+ "from gd_plan_detail pd  "
				+ "left join gd_design_theme dt on pd.gd_design_theme_id = dt.id  "
				+ "left join sys_user su on pd.user_id = su.user_id  "
				+ "left join gd_design_direction dd on dt.gd_design_direction_id = dd.id  "
				+ "left join sys_user te on dt.sys_user_id = te.user_id  "
				+ "left join sys_major sm on su.major_id = sm.major_id  "
				+ "left join sys_class sc on su.class_id = sc.class_id  "
				+ "where pd.state=1 and dt.state=1 and dd.state=1 and su.state=1  "
				+ "and sm.state =1 and sc.state =1 and te.state=1   ";
		StringBuffer query = new StringBuffer(baseSql);
		List<Object> params = new ArrayList<>();
		//添加条件--学生只查看已审核通过的数据
		query.append(" and pd.examine_state = ? ");
		params.add(1L);
		if(BeanUtil.isNotEmpty(gdPlanReportPageDto.getUserId())){
			query.append(" and pd.user_id = ? ");
			params.add(gdPlanReportPageDto.getUserId());
		}
		//教师只能查询与自己相关主题的免答辩审核数据，且免答辩资料为已上传的
		if(BeanUtil.isNotEmpty(gdPlanReportPageDto.getTeacherId())){
			query.append(" and pd.exempt_upload_state = 1 and te.user_id = ? ");
			params.add(gdPlanReportPageDto.getTeacherId());
		}
		if(!StringUtils.isEmpty(gdPlanReportPageDto.getExamineState())){
			query.append(" and pd.examine_state = ? ");
			params.add(gdPlanReportPageDto.getExamineState());
		}
		if(!StringUtils.isEmpty(gdPlanReportPageDto.getStudentName())){
			query.append(" and st.user_name like ? ");
			params.add("%"+gdPlanReportPageDto.getStudentName()+"%");
		}
		if(!StringUtils.isEmpty(gdPlanReportPageDto.getTeacherName())){
			query.append(" and te.user_name like ? ");
			params.add("%"+gdPlanReportPageDto.getTeacherName() + "%");
		}
		if(!StringUtils.isEmpty(gdPlanReportPageDto.getDesignDirection())){
			query.append(" and dd.design_direction like ? ");
			params.add("%"+gdPlanReportPageDto.getDesignDirection() + "%");
		}
		if(StringUtils.isEmpty(gdPlanReportPageDto.getThemeName())){
			query.append(" and dt.theme_name like ? ");
			params.add("%"+gdPlanReportPageDto.getThemeName() + "%");
		}
		return jdbcTemplate.queryForMysqlPageInfo(GdPlanExemptDefensePageVO.class, query.toString(),Boolean.FALSE,null,page,params.toArray());
	}
}
