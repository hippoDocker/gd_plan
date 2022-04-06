package com.gd.base.jdbc;

import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.GdPlanReportPageDTO;
import com.gd.base.pojo.vo.plan.GdPlanDetailPageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计信息管理数据处理
 * @author: tangxl
 * @create: 2022-03-13 16:26
 */
@Component
public class GdPlanInformManageJdbc {
	@Autowired
	private BaseJdbcTemplate jdbcTemplate;
	public PageBaseInfo<GdPlanDetailPageVO> findGdplanDetailPage(GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo page) {
		String baseSql = " select pd.id,su.user_code,su.user_name studentName,su.phone studentPhone,te.user_name teacherName,te.phone teacherPhone, "
				+ "dd.design_direction,dt.theme_name,cast(pd.examine_state as char) examineState, "
				+ "cast(pd.open_require_state as char) open_require_state,cast(pd.open_report_state as char) open_report_state, "
				+ "cast(pd.open_examine_state as char) open_examine_state,cast(pd.mid_require_state as char) mid_require_state, "
				+ "cast(pd.mid_report_state as char) mid_report_state,cast(pd.mid_examine_state as char) mid_examine_state, "
				+ "cast(pd.end_require_state as char) end_require_state,cast(pd.end_report_state as char) end_report_state,"
				+ "pd.score, "
				+ "cast(pd.exempt_upload_state as char) exempt_upload_state,cast(pd.exempt_defense_state as char) exempt_defense_state, "
				+ "dt.remark "
				+ "from gd_plan_detail pd "
				+ "left join sys_user su on su.user_id=pd.user_id "
				+ "left join gd_design_theme dt on dt.id = pd.gd_design_theme_id "
				+ "left join sys_user te on dt.sys_user_id=te.user_id "
				+ "left join gd_design_direction dd on dd.id = dt.gd_design_direction_id "
				+ "where pd.state=1 and dt.state=1 and dd.state=1  ";
		StringBuffer querySql = new StringBuffer(baseSql);
		List<Object> params = new ArrayList<>();
		//条件判断
		if(StringUtils.isNotBlank(gdPlanReportPageDto.getStudentName())){
			querySql.append(" and su.user_name = ? ");
			params.add(gdPlanReportPageDto.getStudentName());
		}
		if(StringUtils.isNotBlank(gdPlanReportPageDto.getTeacherName())){
			querySql.append(" and te.user_name = ? ");
			params.add(gdPlanReportPageDto.getTeacherName());
		}
		if(StringUtils.isNotBlank(gdPlanReportPageDto.getDesignDirection())){
			querySql.append(" dd.design_direction = ? ");
			params.add(gdPlanReportPageDto.getDesignDirection());
		}
		if(StringUtils.isNotBlank(gdPlanReportPageDto.getThemeName())){
			querySql.append(" dt.theme_name = ? ");
			params.add(gdPlanReportPageDto.getThemeName());
		}
		return jdbcTemplate.queryForMysqlPageInfo(GdPlanDetailPageVO.class,querySql.toString(),Boolean.FALSE,"",page,params.toArray());
	}
}
