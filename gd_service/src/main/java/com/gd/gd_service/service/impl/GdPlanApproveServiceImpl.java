package com.gd.gd_service.service.impl;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.DeleteThemeExamineDTO;
import com.gd.base.pojo.dto.plan.GdPlanDetailStuPageDTO;
import com.gd.base.entity.GdPlanDetail;
import com.gd.base.jdbc.GdPlanApproveJdbc;
import com.gd.base.jpa.GdPlanDetailDao;
import com.gd.base.jpa.SysRoleDao;
import com.gd.base.util.BeanUtil;
import com.gd.base.pojo.vo.plan.GdPlanDetailStuPageVO;
import com.gd.base.pojo.vo.redis.RedisSysUser;
import com.gd.gd_service.service.GdPlanApproveService;
import com.gd.gd_service.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计主题审批相关业务处理
 * @author: tangxl
 * @create: 2022-03-08 19:12
 */
@Service
public class GdPlanApproveServiceImpl implements GdPlanApproveService {
	@Autowired
	private GdPlanApproveJdbc gdPlanApproveJdbc;
	@Autowired
	private RedisService redisService;
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private GdPlanDetailDao gdPlanDetailDao;
	/**
	 * @description: TODO 已选主题审核信息分页查询
	 * @Param: token 用户令牌，teacherDirectionPageDto 参数
	 * @author: tangxl
	 * @date: 2022年3月8日20:09:35
	 * @return: CommonResult
	 */
	@Override public CommonResult findGdPlanDetailPage(String token, GdPlanDetailStuPageDTO gdPlanDetailStuPageDto, PageBaseInfo page) {
		//学生只可查询自己所选主题，管理员可查看所有
		RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
		if(redisService.findIsStudent(token)){
			gdPlanDetailStuPageDto.setUserId(redisSysUser.getUserId());
		}
		PageBaseInfo<GdPlanDetailStuPageVO> gdPlanDetailStuPageVoPage = gdPlanApproveJdbc.findGdPlanDetailPage(gdPlanDetailStuPageDto,page);
		return CommonResult.success(gdPlanDetailStuPageVoPage);
	}
	/**
	 * @description: TODO 取消已选主题
	 * @Param: token 用户令牌，deleteThemeExamineDto 参数
	 * @author: tangxl
	 * @date: 2022年3月8日20:09:35
	 * @return: CommonResult
	 */
	@Override public CommonResult deleteThemeExamine(String token, DeleteThemeExamineDTO deleteThemeExamineDto) {
		if(CollectionUtils.isEmpty(deleteThemeExamineDto.getIds())){
			return CommonResult.failed("请选择一条数据！");
		}
		//获取选择取消的主题详情
		List<GdPlanDetail> gdPlanDetailList = gdPlanDetailDao.findAllByIdInAndState(deleteThemeExamineDto.getIds(),1L);
		//已审核通过的数据
		List<GdPlanDetail> examineList = new ArrayList<GdPlanDetail>();
		//未审核通过的数据(审核失败和未审核)
		List<GdPlanDetail> notExamineList = new ArrayList<GdPlanDetail>();
		//新的取消审核的数据
		List<GdPlanDetail> cancelExamineList = new ArrayList<GdPlanDetail>();
		gdPlanDetailList.stream().forEach(o ->{
			//已审核数据
			if(o.getExamineState().equals(1L)){
				examineList.add(o);
			}else if(o.getExamineState().equals(0L) || o.getExamineState().equals(2L)){
				notExamineList.add(o);
			}
		});
		//设置未审核通过的数据失效
		notExamineList.stream().forEach(o->o.setState(0L));
		//处理审核通过的数据生成新的取消审核数据
		examineList.stream().forEach(o ->{
			GdPlanDetail gdPlanDetail = new GdPlanDetail();
			gdPlanDetail = BeanUtil.copyObject(o,GdPlanDetail.class);
			gdPlanDetail.setExamineState(3L);
			gdPlanDetail.setId(null);
			cancelExamineList.add(gdPlanDetail);
		});
		gdPlanDetailDao.saveAll(cancelExamineList);
		if(!CollectionUtils.isEmpty(examineList)){
			return CommonResult.success("取消已审核数据审批中！");
		}
		return CommonResult.success("取消成功！");
	}
}
