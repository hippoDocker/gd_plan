package com.gd.gd_service.service.impl;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.DataCodeDTO;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysSchoolDTO;
import com.gd.base.entity.SysSchool;
import com.gd.base.jdbc.SysSchoolJdbc;
import com.gd.base.jpa.SysSchoolDao;
import com.gd.base.util.BeanUtil;
import com.gd.base.pojo.vo.sys.SysSchoolVO;
import com.gd.gd_service.service.SysSchoolService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: tangxl
 * @Date: 2022年2月25日14:11:14
 * @Description: 系统学校管理
 */
@Service
public class SysSchoolServiceImpl implements SysSchoolService {
	@Autowired
	private SysSchoolDao sysSchoolDao;
	@Autowired
	private SysSchoolJdbc sysSchoolJdbc;
	/**
	 * @Description TODO 添加学校
	 * @param token
	 * @param sysSchoolDto
	 * @return
	 */
	@Override
	public CommonResult addSchool(String token, SysSchoolDTO sysSchoolDto) {
		if(StringUtils.isEmpty(sysSchoolDto.getSchoolName())){
			return CommonResult.failed("学校名称不能为空！");
		}
		List<SysSchool> sysSchoolList = sysSchoolDao.findAllByState(1L);
		Long count = sysSchoolList.stream().filter(o ->
				o.getSchoolName().equals(sysSchoolDto.getSchoolName()) || o.getSchoolCode().equals(sysSchoolDto.getSchoolCode()) ).count();
		if (count>0) {
			return CommonResult.failed("此学校名称或编码已存在");
		}
		SysSchool sysSchool = BeanUtil.copyObject(sysSchoolDto,SysSchool.class);
		sysSchool.setState(1L);
		sysSchoolDao.save(sysSchool);
		return CommonResult.success("添加成功！");
	}

	/**
	 * @Description TODO 删除学校
	 * @param token
	 * @param sysSchoolDto
	 * @return
	 */
	@Override
	public CommonResult deleteSchool(String token, SysSchoolDTO sysSchoolDto) {
		if(sysSchoolDto==null || sysSchoolDto.getSchoolIds()==null){
			return CommonResult.failed("请至少选择一个学校进行删除！");
		}
		List<SysSchool> sysSchoolList = sysSchoolDao.findAllBySchoolIdInAndState(sysSchoolDto.getSchoolIds(),1L);
		if (BeanUtil.isEmpty(sysSchoolList)) {
			return CommonResult.failed("未查询到相关学校信息！");
		}
		sysSchoolList.stream().forEach(o->o.setState(0L));
		sysSchoolDao.saveAll(sysSchoolList);
		return CommonResult.success("删除成功！");
	}

	/**
	 * @Description TODO 更新学校信息
	 * @param token
	 * @param sysSchoolDto
	 * @return
	 */
	@Override
	public CommonResult updateSchool(String token, SysSchoolDTO sysSchoolDto) {
		//获取原始学校信息
		SysSchool sysSchool = sysSchoolDao.findBySchoolIdAndState(sysSchoolDto.getSchoolId(), 1L);
		if(BeanUtil.isEmpty(sysSchool)){
			return CommonResult.failed("学校未查询到！");
		}
		//更新信息
		sysSchool.setSchoolCode(sysSchoolDto.getSchoolCode());
		sysSchool.setSchoolName(sysSchoolDto.getSchoolName());
		sysSchoolDao.save(sysSchool);
		return CommonResult.success("更新成功！");
	}

	/**
	 * @Description TODO 学校分页查询
	 * @param token
	 * @param sysSchoolDto
	 * @param pageBaseInfo
	 * @return
	 */
	@Override
	public PageBaseInfo<SysSchoolVO> findShcoolPage(String token, SysSchoolDTO sysSchoolDto, PageBaseInfo pageBaseInfo) {
		PageBaseInfo<SysSchoolVO> sysSchoolPage = sysSchoolJdbc.findShcoolPage(token, sysSchoolDto, pageBaseInfo);
		return sysSchoolPage;
	}

	/**
	 * @Description TODO
	 * @param token
	 * @param sysSchoolDto
	 * @return
	 */
	@Override
	public CommonResult findSchoolByRoleId(String token, SysSchoolDTO sysSchoolDto) {
		SysSchool sysSchool = sysSchoolDao.findBySchoolIdAndState(sysSchoolDto.getSchoolId(), 1L);
		if(BeanUtil.isEmpty(sysSchool)){
			return CommonResult.failed("未查询到学校信息！");
		}
		return CommonResult.success(sysSchool,"查询成功！");
	}

	/**
	 * @Description TODO 查询学校code-data类型数据
	 * @param token
	 * @return
	 */
	@Override
	public CommonResult findCodeDataSchool(String token) {
		List<SysSchool> sysSchoolList = sysSchoolDao.findAllByState(1L);
		List<DataCodeDTO> sysSchoolDataCodeDTOS = sysSchoolList.stream().map(o->{
			return new DataCodeDTO(o.getSchoolId().toString(),o.getSchoolName());
		}).collect(Collectors.toList());
		return CommonResult.success(sysSchoolDataCodeDTOS,"查询成功！");
	}
}
