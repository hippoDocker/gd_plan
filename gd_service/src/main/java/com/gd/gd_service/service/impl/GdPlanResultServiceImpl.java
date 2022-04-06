package com.gd.gd_service.service.impl;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.GdPlanReportPageDTO;
import com.gd.base.jdbc.GdPlanResultJdbc;
import com.gd.base.pojo.vo.plan.GdPlanScorePageVO;
import com.gd.base.pojo.vo.redis.RedisSysUser;
import com.gd.gd_service.service.GdPlanResultService;
import com.gd.gd_service.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计最终结果相关业务处理
 * @author: tangxl
 * @create: 2022-03-09 15:49
 */
@Service
public class GdPlanResultServiceImpl implements GdPlanResultService {
	@Autowired
	private GdPlanResultJdbc gdPlanResultJdbc;
	@Autowired
	private RedisService redisService;

	/**
	 * @description: TODO 获取毕设信息得分情况分页查询
	 * @Param: token令牌，gdPlanReportPageDto条件参数，page分页参数
	 * @author: tangxl
	 * @date: 2022年3月10日15:20:45
	 * @return: CommonResult
	 */
	@Override public CommonResult findGdPlanScorePage(String token, GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo page) {
		//学生只能查询自己的
		RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
		if(redisService.findIsStudent(token)){
			gdPlanReportPageDto.setUserId(redisSysUser.getUserId());
		}
		PageBaseInfo<GdPlanScorePageVO> gdPlanScorePageVoPage = gdPlanResultJdbc.findGdPlanScorePage(gdPlanReportPageDto,page);
		return CommonResult.success(gdPlanScorePageVoPage,"查询成功！");
	}
}
