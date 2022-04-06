package com.gd.base.jdbc;

import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.GdPlanDetailStuPageDTO;
import com.gd.base.util.BeanUtil;
import com.gd.base.pojo.vo.plan.GdPlanDetailStuPageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计主题审批相关数据处理JDBC
 * @author: tangxl
 * @create: 2022-03-08 19:14
 */
@Component
public class GdPlanApproveJdbc {
	@Autowired
	private BaseJdbcTemplate jdbcTemplate;

	/**
	 * @description: TODO 已选主题审核信息分页查询
	 * @Param: teacherDirectionPageDto 参数
	 * @author: tangxl
	 * @date: 2022年3月8日20:09:35
	 * @return: PageBaseInfo<GdPlanDetailStuPageVo>
	 */
	public PageBaseInfo<GdPlanDetailStuPageVO> findGdPlanDetailPage(GdPlanDetailStuPageDTO gdPlanDetailStuPageDto, PageBaseInfo page) {
		String baseSql = " select pd.id,pd.create_by studentName,dd.design_direction,dt.theme_name, "
				+ "dt.theme_source,su.phone,dt.theme_upload_state,pd.examine_state "
				+ "from gd_plan_detail pd "
				+ "left join gd_design_theme dt on pd.gd_design_theme_id = dt.id "
				+ "left join gd_design_direction dd on dt.gd_design_direction_id = dd.id "
				+ "left join sys_user su on pd.user_id = su.user_id "
				+ "where pd.state=1 and dt.state=1 and dd.state=1 and su.state=1  ";
		StringBuffer querySql = new StringBuffer(baseSql);
		List<Object> params = new ArrayList<>();
		//参数
		if(BeanUtil.isNotEmpty(gdPlanDetailStuPageDto.getUserId())){
			querySql.append(" and pd.user_id = ? ");
			params.add(gdPlanDetailStuPageDto.getUserId());
		}
		if (StringUtils.isNotEmpty(gdPlanDetailStuPageDto.getTeacherName())) {
			querySql.append(" su.user_name = ? ");
			params.add(gdPlanDetailStuPageDto.getTeacherName());
		}
		if (StringUtils.isNotEmpty(gdPlanDetailStuPageDto.getDesignDirection())) {
			querySql.append(" dd.design_direction like ? ");
			params.add("%"+gdPlanDetailStuPageDto.getDesignDirection()+"%");
		}
		if (StringUtils.isNotEmpty(gdPlanDetailStuPageDto.getThemeName())){
			querySql.append(" dt.theme_name like ? ");
			params.add("%"+gdPlanDetailStuPageDto.getThemeName() + "%");
		}
		if (StringUtils.isNotEmpty(gdPlanDetailStuPageDto.getExamineState())){
			querySql.append(" pd.examine_state = ? ");
			params.add(gdPlanDetailStuPageDto.getExamineState());
		}
		PageBaseInfo<GdPlanDetailStuPageVO> gdPlanDetailStuPageVoPage = null;
		gdPlanDetailStuPageVoPage = jdbcTemplate.queryForMysqlPageInfo(GdPlanDetailStuPageVO.class,querySql.toString(),Boolean.FALSE,null,page,params.toArray());
		return gdPlanDetailStuPageVoPage;
	}
}
