package com.gd.base.jdbc;

import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.GdPlanReportPageDTO;
import com.gd.base.util.BeanUtil;
import com.gd.base.pojo.vo.plan.GdPlanScorePageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计最终结果相关数据处理
 * @author: tangxl
 * @create: 2022-03-09 15:51
 */
@Component
public class GdPlanResultJdbc {
	@Autowired
	private BaseJdbcTemplate jdbcTemplate;

	/**
	 * @description: TODO  获取毕设信息得分情况分页查询
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	public PageBaseInfo<GdPlanScorePageVO> findGdPlanScorePage(GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo page) {
		String baseSql = " select pd.id,su.user_code,su.user_name studentName,sm.major_name,sc.class_name,te.user_name teacherName,   "
				+ "te.phone,te.email,dd.design_direction,dt.theme_name,   "
				+ "pd.Instructor_score instrScore,pd.defense_group_score defGroScore, "
				+ "pd.score,dt.remark   "
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
		//添加条件--学生只查看已审核通过的数据,其他人能看所有审核的数据
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
		return jdbcTemplate.queryForMysqlPageInfo(GdPlanScorePageVO.class, query.toString(),Boolean.FALSE,null,page,params.toArray());
	}
}
