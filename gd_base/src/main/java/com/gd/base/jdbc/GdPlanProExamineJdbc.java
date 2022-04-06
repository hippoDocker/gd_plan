package com.gd.base.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: gd_plan
 * @description: TODO 毕业流程审核数据处理JDBC
 * @author: tangxl
 * @create: 2022-03-11 09:17
 */
@Component
public class GdPlanProExamineJdbc {
	@Autowired
	private BaseJdbcTemplate jdbcTemplate;
}
