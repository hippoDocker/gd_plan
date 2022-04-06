package com.gd.gd_service.service.impl;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.DataCodeDTO;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysClassDTO;
import com.gd.base.entity.SysClass;
import com.gd.base.jdbc.SysClassJdbc;
import com.gd.base.jpa.SysClassDao;
import com.gd.base.util.BeanUtil;
import com.gd.base.pojo.vo.sys.SysClassVO;
import com.gd.gd_service.service.SysClassService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: tangxl
 * @Date:2022年2月25日16:38:11
 * @Description: 班级管理数据处理
 */
@Service
public class SysClassServiceImpl implements SysClassService {
	@Autowired
	private SysClassDao sysClassDao;
	@Autowired
	private SysClassJdbc sysClassJdbc;
	/**
	 * @Description TODO 添加班级
	 * @param token
	 * @param sysClassDto
	 * @return
	 */
	@Override
	public CommonResult addClass(String token, SysClassDTO sysClassDto) {
		if(StringUtils.isEmpty(sysClassDto.getClassName())){
			return CommonResult.failed("班级名称不能为空！");
		}
		List<SysClass> sysClassList = sysClassDao.findAllByState(1L);
		Long count = sysClassList.stream().filter(o ->
				o.getClassName().equals(sysClassDto.getClassName()) || o.getClassCode().equals(sysClassDto.getClassCode()) ).count();
		if (count>0) {
			return CommonResult.failed("此班级名称或编码已存在");
		}
		SysClass sysClass = BeanUtil.copyObject(sysClassDto,SysClass.class);
		sysClass.setState(1L);
		sysClassDao.save(sysClass);
		return CommonResult.success("添加成功！");
	}

	/**
	 * @Description TODO 删除班级
	 * @param token
	 * @param sysClassDto
	 * @return
	 */
	@Override
	public CommonResult deleteClass(String token, SysClassDTO sysClassDto) {
		if(sysClassDto==null || sysClassDto.getClassIds()==null){
			return CommonResult.failed("请至少选择一个班级进行删除！");
		}
		List<SysClass> sysClassList = sysClassDao.findAllByClassIdInAndState(sysClassDto.getClassIds(),1L);
		if (BeanUtil.isEmpty(sysClassList)) {
			return CommonResult.failed("未查询到相关班级信息！");
		}
		sysClassList.stream().forEach(o->o.setState(0L));
		sysClassDao.saveAll(sysClassList);
		return CommonResult.success("删除成功！");
	}

	/**
	 * @Description TODO 更新班级信息
	 * @param token
	 * @param sysClassDto
	 * @return
	 */
	@Override
	public CommonResult updateClass(String token, SysClassDTO sysClassDto) {
		//获取原始班级信息
		SysClass sysClass = sysClassDao.findByClassIdAndState(sysClassDto.getClassId(), 1L);
		if(BeanUtil.isEmpty(sysClass)){
			return CommonResult.failed("班级未查询到！");
		}
		//更新信息
		sysClass.setClassCode(sysClassDto.getClassCode());
		sysClass.setClassName(sysClassDto.getClassName());
		sysClassDao.save(sysClass);
		return CommonResult.success("更新成功！");
	}

	/**
	 * @Description TODO 班级分页查询
	 * @param token
	 * @param sysClassDto
	 * @param pageBaseInfo
	 * @return
	 */
	@Override
	public PageBaseInfo<SysClassVO> findClassPage(String token, SysClassDTO sysClassDto, PageBaseInfo pageBaseInfo) {
		PageBaseInfo<SysClassVO> sysClassPage = sysClassJdbc.findClassPage(token, sysClassDto, pageBaseInfo);
		return sysClassPage;
	}

	/**
	 * @Description TODO  根据班级ID查询班级
	 * @param token
	 * @param sysClassDto
	 * @return
	 */
	@Override
	public CommonResult findClassById(String token, SysClassDTO sysClassDto) {
		SysClass sysClass = sysClassDao.findByClassIdAndState(sysClassDto.getClassId(), 1L);
		if(BeanUtil.isEmpty(sysClass)){
			return CommonResult.failed("未查询到班级信息！");
		}
		return CommonResult.success(sysClass,"查询成功！");
	}

	/**
	 * @Description TODO 查询班级code-data类型数据
	 * @param token
	 * @return
	 */
	@Override
	public CommonResult findCodeDataClass(String token) {
		List<SysClass> sysClassList = sysClassDao.findAllByState(1L);
		List<DataCodeDTO> sysClassDataCodeDTOS = sysClassList.stream().map(o->{
			return new DataCodeDTO(o.getClassId().toString(),o.getClassName());
		}).collect(Collectors.toList());
		return CommonResult.success(sysClassDataCodeDTOS,"查询成功！");
	}
}
