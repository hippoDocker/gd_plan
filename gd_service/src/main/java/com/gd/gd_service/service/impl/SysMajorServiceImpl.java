package com.gd.gd_service.service.impl;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.DataCodeDTO;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysMajorDTO;
import com.gd.base.entity.SysMajor;
import com.gd.base.jdbc.SysMajorJdbc;
import com.gd.base.jpa.SysMajorDao;
import com.gd.base.util.BeanUtil;
import com.gd.base.pojo.vo.sys.SysMajorVO;
import com.gd.gd_service.service.SysMajorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: tangxl
 * @Date:2022年2月25日16:36:13
 * @Description: 专业管理数据处理
 */
@Service
public class SysMajorServiceImpl implements SysMajorService {
	@Autowired
	private SysMajorDao sysMajorDao;
	@Autowired
	private SysMajorJdbc sysMajorJdbc;
	/**
	 * @Description TODO 添加专业
	 * @param token
	 * @param sysMajorDto
	 * @return
	 */
	@Override
	public CommonResult addMajor(String token, SysMajorDTO sysMajorDto) {
		if(StringUtils.isEmpty(sysMajorDto.getMajorName())){
			return CommonResult.failed("专业名称不能为空！");
		}
		List<SysMajor> sysMajorList = sysMajorDao.findAllByState(1L);
		Long count = sysMajorList.stream().filter(o ->
				o.getMajorName().equals(sysMajorDto.getMajorName()) || o.getMajorCode().equals(sysMajorDto.getMajorCode()) ).count();
		if (count>0) {
			return CommonResult.failed("此专业名称或编码已存在");
		}
		SysMajor sysMajor = BeanUtil.copyObject(sysMajorDto,SysMajor.class);
		sysMajor.setState(1L);
		sysMajorDao.save(sysMajor);
		return CommonResult.success("添加成功！");
	}

	/**
	 * @Description TODO 删除专业
	 * @param token
	 * @param sysMajorDto
	 * @return
	 */
	@Override
	public CommonResult deleteMajor(String token, SysMajorDTO sysMajorDto) {
		if(sysMajorDto==null || sysMajorDto.getMajorIds()==null){
			return CommonResult.failed("请至少选择一个专业进行删除！");
		}
		List<SysMajor> sysMajorList = sysMajorDao.findAllByMajorIdInAndState(sysMajorDto.getMajorIds(),1L);
		if (BeanUtil.isEmpty(sysMajorList)) {
			return CommonResult.failed("未查询到相关专业信息！");
		}
		sysMajorList.stream().forEach(o->o.setState(0L));
		sysMajorDao.saveAll(sysMajorList);
		return CommonResult.success("删除成功！");
	}

	/**
	 * @Description TODO 更新专业信息
	 * @param token
	 * @param sysMajorDto
	 * @return
	 */
	@Override
	public CommonResult updateMajor(String token, SysMajorDTO sysMajorDto) {
		//获取原始专业信息
		SysMajor sysMajor = sysMajorDao.findByMajorIdAndState(sysMajorDto.getMajorId(), 1L);
		if(BeanUtil.isEmpty(sysMajor)){
			return CommonResult.failed("专业未查询到！");
		}
		//更新信息
		sysMajor.setMajorCode(sysMajorDto.getMajorCode());
		sysMajor.setMajorName(sysMajorDto.getMajorName());
		sysMajorDao.save(sysMajor);
		return CommonResult.success("更新成功！");
	}

	/**
	 * @Description TODO 专业分页查询
	 * @param token
	 * @param sysMajorDto
	 * @param pageBaseInfo
	 * @return
	 */
	@Override
	public PageBaseInfo<SysMajorVO> findMajorPage(String token, SysMajorDTO sysMajorDto, PageBaseInfo pageBaseInfo) {
		PageBaseInfo<SysMajorVO> sysMajorPage = sysMajorJdbc.findMajorPage(token, sysMajorDto, pageBaseInfo);
		return sysMajorPage;
	}

	/**
	 * @Description TODO
	 * @param token
	 * @param sysMajorDto
	 * @return
	 */
	@Override
	public CommonResult findMajorById(String token, SysMajorDTO sysMajorDto) {
		SysMajor sysMajor = sysMajorDao.findByMajorIdAndState(sysMajorDto.getMajorId(), 1L);
		if(BeanUtil.isEmpty(sysMajor)){
			return CommonResult.failed("未查询到专业信息！");
		}
		return CommonResult.success(sysMajor,"查询成功！");
	}

	/**
	 * @Description TODO 查询专业code-data类型数据
	 * @param token
	 * @return
	 */
	@Override
	public CommonResult findCodeDataMajor(String token) {
		List<SysMajor> sysMajorList = sysMajorDao.findAllByState(1L);
		List<DataCodeDTO> sysMajorDataCodeDTOS = sysMajorList.stream().map(o->{
			return new DataCodeDTO(o.getMajorId().toString(),o.getMajorName());
		}).collect(Collectors.toList());
		return CommonResult.success(sysMajorDataCodeDTOS,"查询成功！");
	}
}
