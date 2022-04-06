package com.gd.base.jdbc;

import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.GdplanThemePastPageDTO;
import com.gd.base.util.BeanUtil;
import com.gd.base.pojo.vo.plan.GdplanThemePastPageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 主题审核数据处理JDBC
 * @author: tangxl
 * @create: 2022-03-10 18:54
 */
@Component
public class GdPlanThemePastJdbc {
	@Autowired
	private BaseJdbcTemplate jdbcTemplate;
	/**
	 * @description: TODO 审核主题数据分页查询
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	public PageBaseInfo<GdplanThemePastPageVO> findThemePastPage(GdplanThemePastPageDTO gdPlanReportPageDto, PageBaseInfo page) {
		String baseSql = " select pd.id,su.user_code,su.user_name studentName,sm.major_name,sc.class_name, "
				+ "su.phone,su.email,te.user_name teacherName,   "
				+ "dd.design_direction,dt.theme_name, pd.examine_state   "
				+ "from gd_plan_detail pd   "
				+ "left join gd_design_theme dt on pd.gd_design_theme_id = dt.id   "
				+ "left join sys_user su on pd.user_id = su.user_id   "
				+ "left join gd_design_direction dd on dt.gd_design_direction_id = dd.id   "
				+ "left join sys_user te on dt.sys_user_id = te.user_id   "
				+ "left join sys_major sm on su.major_id = sm.major_id   "
				+ "left join sys_class sc on su.class_id = sc.class_id   "
				+ "where pd.state=1 and dt.state=1 and dd.state=1 and su.state=1   "
				+ "and sm.state =1 and sc.state =1 and te.state=1 ";
		StringBuffer query = new StringBuffer(baseSql);
		List<Object> params = new ArrayList<>();
		//添加条件--教师只看自己审核的数据
		if(BeanUtil.isNotEmpty(gdPlanReportPageDto.getUserId())){
			query.append(" and pd.sys_user_id = ? ");
			params.add(gdPlanReportPageDto.getUserId());
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
		if(!StringUtils.isEmpty(gdPlanReportPageDto.getThemeName())){
			query.append(" and dt.theme_name like ? ");
			params.add("%"+gdPlanReportPageDto.getThemeName() + "%");
		}
		return jdbcTemplate.queryForMysqlPageInfo(GdplanThemePastPageVO.class, query.toString(),Boolean.FALSE,null,page,params.toArray());
	}
}
