package com.gd.gd_service.service.impl;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.GdplanThemePastPageDTO;
import com.gd.base.entity.GdDesignDirection;
import com.gd.base.entity.GdDesignTheme;
import com.gd.base.entity.GdPlanDetail;
import com.gd.base.jdbc.GdPlanThemePastJdbc;
import com.gd.base.jpa.GdDesignDirectionDao;
import com.gd.base.jpa.GdDesignThemeDao;
import com.gd.base.jpa.GdPlanDetailDao;
import com.gd.base.util.BeanUtil;
import com.gd.base.pojo.vo.plan.GdplanThemePastPageVO;
import com.gd.base.pojo.vo.redis.RedisSysUser;
import com.gd.gd_service.service.GdPlanThemePastService;
import com.gd.gd_service.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 审核学生选题
 * @author: tangxl
 * @create: 2022-03-10 17:12
 */
@Service
public class GdPlanThemePastServiceImpl implements GdPlanThemePastService {
	@Autowired
	private RedisService redisService;
	@Autowired
	private GdPlanThemePastJdbc gdPlanThemePastJdbc;
	@Autowired
	private GdPlanDetailDao gdPlanDetailDao;
	@Autowired
	private GdDesignThemeDao gdDesignThemeDao;
	@Autowired
	private GdDesignDirectionDao gdDesignDirectionDao;

	/**
	 * @description: TODO 审核主题数据分页查询
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	@Override public CommonResult findThemePastPage(String token, GdplanThemePastPageDTO gdPlanReportPageDto, PageBaseInfo page) {
		RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
		if(redisService.findIsStudent(token)||redisService.findIsTeacher(token)){
			gdPlanReportPageDto.setUserId(redisSysUser.getUserId());
		}
		PageBaseInfo<GdplanThemePastPageVO> pastPageVoPage = gdPlanThemePastJdbc.findThemePastPage(gdPlanReportPageDto,page);
		return CommonResult.success(pastPageVoPage,"查询成功！");
	}

	/**
	 * @description: TODO 审核学生选题通过
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	@Override
	public CommonResult saveThemePast(String token, GdplanThemePastPageDTO gdPlanReportPageDto) {
		RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
		GdPlanDetail gdPlanDetail = gdPlanDetailDao.findByIdAndState(gdPlanReportPageDto.getId(),1L);
		//只能教师审核
		if(!redisService.findIsTeacher(token)){
			return CommonResult.failed("无权进行审核！");
		}
		if(BeanUtil.isEmpty(gdPlanDetail)){
			return CommonResult.failed("数据不存在！");
		}
		//不允许之前以及审核过的数据
		if(!gdPlanDetail.getExamineState().equals(0L)){
			return CommonResult.failed("该数据已审核过！");
		}
		//之前有审核过的数据就不能再审核
		if(BeanUtil.isNotEmpty(gdPlanDetailDao.findByExamineStateAndStateAndUserId(1L,1L,gdPlanDetail.getUserId()))){
			return CommonResult.failed("此学生已有通过主题！");
		}
		//审核当前主题数据修改其他学生选择的主题数据为审核失败
		//获取学生选择的所有主题数据
		List<GdPlanDetail> gdPlanDetailList = gdPlanDetailDao.findAllByUserIdAndState(gdPlanDetail.getUserId(),1L);
		gdPlanDetailList.stream().forEach(o->{
			if(gdPlanDetail.getId().equals(o.getId())){
				o.setExamineState(1L);
			} else {
				o.setExamineState(2L);
			}
		});
		//修改主题和方向信息
		GdDesignTheme gdDesignTheme = gdDesignThemeDao.findByIdAndState(gdPlanDetail.getGdDesignThemeId(),1L);
		//修改主题为已选择
		gdDesignTheme.setIsChoose(1L);
		//修改方向主题余量减一
		GdDesignDirection gdDesignDirection = gdDesignDirectionDao.findByIdAndState(gdDesignTheme.getGdDesignDirectionId(),1L);
		gdDesignDirection.setThemeMargin(gdDesignDirection.getThemeMargin()-1);
		gdDesignDirectionDao.save(gdDesignDirection);
		gdPlanDetailDao.saveAll(gdPlanDetailList);
		return CommonResult.success("操作成功！");
	}

	@Override public CommonResult saveThemeReturn(String token, GdplanThemePastPageDTO gdPlanReportPageDto) {
		RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
		//获取未审核或者审核通过的数据
		GdPlanDetail gdPlanDetail = gdPlanDetailDao.findByIdAndStateAndExamineStateIn(gdPlanReportPageDto.getId(),1L,
				Arrays.asList(0L,1L));
		if(BeanUtil.isEmpty(gdPlanDetail)){
			return CommonResult.failed("此数据不可退回！");
		}
		//只能教师审核
		if(!redisService.findIsTeacher(token)){
			return CommonResult.failed("无权进行审核！");
		}
		//审核过的数据需退回，未审核过的数据直接退回
		gdPlanDetail.setExamineState(2L);
		//未审核数据
		if(gdPlanDetail.getExamineState().equals(0L)){
			gdPlanDetailDao.save(gdPlanDetail);
			return CommonResult.success("操作成功！");
		}
		//已审核数据--修改方向和主题数据
		GdDesignTheme gdDesignTheme = gdDesignThemeDao.findByIdAndState(gdPlanDetail.getGdDesignThemeId(),1L);
		gdDesignTheme.setIsChoose(0L);
		GdDesignDirection gdDesignDirection = gdDesignDirectionDao.findByIdAndState(gdDesignTheme.getGdDesignDirectionId(),1L);
		gdDesignDirection.setThemeMargin(gdDesignDirection.getThemeMargin()+1);
		gdDesignDirectionDao.save(gdDesignDirection);
		gdDesignThemeDao.save(gdDesignTheme);
		gdPlanDetailDao.save(gdPlanDetail);
		return CommonResult.success("操作成功！");
	}
}
