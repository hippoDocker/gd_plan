package com.gd.gd_service.service.impl;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.DataCodeDTO;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysCollegeDTO;
import com.gd.base.entity.SysCollege;
import com.gd.base.jdbc.SysCollegeJdbc;
import com.gd.base.jpa.SysCollegeDao;
import com.gd.base.util.BeanUtil;
import com.gd.base.pojo.vo.sys.SysCollegeVO;
import com.gd.gd_service.service.SysCollegeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: tangxl
 * @Date: 2022年2月25日16:28:33
 * @Description: 学院管理数据处理
 */
@Service
public class SysCollegeServiceImpl implements SysCollegeService {
	@Autowired
	private SysCollegeDao sysCollegeDao;
	@Autowired
	private SysCollegeJdbc syscollegeJdbc;
	/**
	 * @Description TODO 添加学院
	 * @param token
	 * @param sysCollegeDto
	 * @return
	 */
	@Override
	public CommonResult addCollege(String token, SysCollegeDTO sysCollegeDto) {
		if(StringUtils.isEmpty(sysCollegeDto.getCollegeName())){
			return CommonResult.failed("学院名称不能为空！");
		}
		List<SysCollege> syscollegeList = sysCollegeDao.findAllByState(1L);
		Long count = syscollegeList.stream().filter(o ->
				o.getCollegeName().equals(sysCollegeDto.getCollegeName()) || o.getCollegeCode().equals(sysCollegeDto.getCollegeCode()) ).count();
		if (count>0) {
			return CommonResult.failed("此学院名称或编码已存在");
		}
		SysCollege sysCollege = BeanUtil.copyObject(sysCollegeDto,SysCollege.class);
		sysCollege.setState(1L);
		sysCollegeDao.save(sysCollege);
		return CommonResult.success("添加成功！");
	}

	/**
	 * @Description TODO 删除学院
	 * @param token
	 * @param sysCollegeDto
	 * @return
	 */
	@Override
	public CommonResult deleteCollege(String token, SysCollegeDTO sysCollegeDto) {
		if(sysCollegeDto==null || sysCollegeDto.getCollegeIds()==null){
			return CommonResult.failed("请至少选择一个学院进行删除！");
		}
		List<SysCollege> syscollegeList = sysCollegeDao.findAllByCollegeIdInAndState(sysCollegeDto.getCollegeIds(),1L);
		if (BeanUtil.isEmpty(syscollegeList)) {
			return CommonResult.failed("未查询到相关学院信息！");
		}
		syscollegeList.stream().forEach(o->o.setState(0L));
		sysCollegeDao.saveAll(syscollegeList);
		return CommonResult.success("删除成功！");
	}

	/**
	 * @Description TODO 更新学院信息
	 * @param token
	 * @param sysCollegeDto
	 * @return
	 */
	@Override
	public CommonResult updateCollege(String token, SysCollegeDTO sysCollegeDto) {
		//获取原始学院信息
		SysCollege sysCollege = sysCollegeDao.findBycollegeIdAndState(sysCollegeDto.getCollegeId(), 1L);
		if(BeanUtil.isEmpty(sysCollege)){
			return CommonResult.failed("学院未查询到！");
		}
		//更新信息
		sysCollege.setCollegeCode(sysCollegeDto.getCollegeCode());
		sysCollege.setCollegeName(sysCollegeDto.getCollegeName());
		sysCollegeDao.save(sysCollege);
		return CommonResult.success("更新成功！");
	}

	/**
	 * @Description TODO 学院分页查询
	 * @param token
	 * @param sysCollegeDto
	 * @param pageBaseInfo
	 * @return
	 */
	@Override
	public PageBaseInfo<SysCollegeVO> findCollegePage(String token, SysCollegeDTO sysCollegeDto, PageBaseInfo pageBaseInfo) {
		PageBaseInfo<SysCollegeVO> syscollegePage = syscollegeJdbc.findCollegePage(token, sysCollegeDto, pageBaseInfo);
		return syscollegePage;
	}

	/**
	 * @Description TODO
	 * @param token
	 * @param sysCollegeDto
	 * @return
	 */
	@Override
	public CommonResult findCollegeById(String token, SysCollegeDTO sysCollegeDto) {
		SysCollege sysCollege = sysCollegeDao.findBycollegeIdAndState(sysCollegeDto.getCollegeId(), 1L);
		if(BeanUtil.isEmpty(sysCollege)){
			return CommonResult.failed("未查询到学院信息！");
		}
		return CommonResult.success(sysCollege,"查询成功！");
	}

	/**
	 * @Description TODO 查询学院code-data类型数据
	 * @param token
	 * @return
	 */
	@Override
	public CommonResult findCodeDataCollege(String token) {
		List<SysCollege> sysCollegeList = sysCollegeDao.findAllByState(1L);
		List<DataCodeDTO> syscollegeDataCodeDTOS = sysCollegeList.stream().map(o->{
			return new DataCodeDTO(o.getCollegeId().toString(),o.getCollegeName());
		}).collect(Collectors.toList());
		return CommonResult.success(syscollegeDataCodeDTOS,"查询成功！");
	}
}
