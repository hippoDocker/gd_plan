package com.gd.gd_service.service.impl;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysStaticTypeDTO;
import com.gd.base.pojo.dto.sys.SysStaticTypePageDTO;
import com.gd.base.pojo.dto.sys.SysStaticValueDTO;
import com.gd.base.pojo.dto.sys.SysStaticValuePageDTO;
import com.gd.base.entity.SysStaticType;
import com.gd.base.entity.SysStaticValue;
import com.gd.base.enums.ResultCodeEnums;
import com.gd.base.jdbc.SysStaticJdbc;
import com.gd.base.jpa.SysStaticTypeDao;
import com.gd.base.jpa.SysStaticValueDao;
import com.gd.base.util.BeanUtil;
import com.gd.base.pojo.vo.sys.SysStaticTypeVO;
import com.gd.base.pojo.vo.sys.SysStaticValueVO;
import com.gd.gd_service.service.SysStaticService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @Description: TODO 静态值业务处理层
 * @Auther: tangxl
 * @Date: 2022年3月2日11:38:56
 */
@Service
public class SysStaticServiceImpl implements SysStaticService {
    @Autowired
    private SysStaticJdbc sysStaticJdbc;
    @Autowired
    private SysStaticTypeDao sysStaticTypeDao;
    @Autowired
    private SysStaticValueDao sysStaticValueDao;
    /**
     * @Description TODO 添加静态值类型
     * @param token
     * @param sysStaticTypeDto
     * @return
     */
    @Override
    public CommonResult addSysStaticType(String token, SysStaticTypeDTO sysStaticTypeDto){
        //业务逻辑处理
        return CommonResult.success();
    }
    /**
     * @description: TODO 静态属性分页查询
     * @Param: SysStaticTypeDto,PageBaseInfo
     * @author: tangxl
     * @date: 2022年3月2日11:51:15
     * @return: CommonResult
     */
    @Override
    public CommonResult findStaticTypePage(String token, SysStaticTypePageDTO sysStaticTypePageDto, PageBaseInfo page) {
        PageBaseInfo<SysStaticTypeVO> sysStaticTypeVoPage = sysStaticJdbc.findStaticTypePage(sysStaticTypePageDto,page);
        return CommonResult.success(sysStaticTypeVoPage);
    }
    /**
     * @description: TODO 静态值分页查询
     * @Param: SysStaticValueDto,PageBaseInfo
     * @author: tangxl
     * @date: 2022年3月2日11:51:15
     * @return: CommonResult
     */
    @Override
    public CommonResult findStaticValuePage(String token, SysStaticValuePageDTO sysStaticValuePageDto, PageBaseInfo page) {
        PageBaseInfo<SysStaticValueVO> sysStaticValueVoPage = sysStaticJdbc.findStaticValuePage(sysStaticValuePageDto,page);
        return CommonResult.success(sysStaticValueVoPage);
    }
    /**
     * @description: TODO 添加静态属性
     * @Param: token 令牌，sysStaticTypeDto 静态属性参数
     * @author: tangxl
     * @date: 2022年3月2日14:36:11
     * @return: CommonResult
     */
    @Override public CommonResult addStaticType(String token, SysStaticTypeDTO sysStaticTypeDto) {
        //参数校验
        if(StringUtils.isEmpty(sysStaticTypeDto.getStaticName())){
            return CommonResult.failed("静态类型名称为空！");
        }
        if(StringUtils.isEmpty(sysStaticTypeDto.getStaticCode())){
            return CommonResult.failed("静态编码为空！");
        }
        if(BeanUtil.isEmpty(sysStaticTypeDto.getState())){
            return CommonResult.failed("数据状态未知！");
        }
        //初始化静态属性参数
        SysStaticType sysStaticType = BeanUtil.copyObject(sysStaticTypeDto, SysStaticType.class);
        sysStaticType.setState(1L);
        //保存静态属性
        sysStaticTypeDao.save(sysStaticType);
        return CommonResult.success("静态类型新增成功！");
    }
    /**
     * @description: TODO 新增静态值
     * @Param: token 令牌，sysStaticValueDto 静态值参数
     * @author: tangxl
     * @date: 2022年3月2日14:50:08
     * @return: CommonResult
     */
    @Override public CommonResult addStaticValue(String token, SysStaticValueDTO sysStaticValueDto) {
        //参数校验
        if(BeanUtil.isEmpty(sysStaticValueDto.getStaticId())){
            return CommonResult.failed("静态类型ID为空！");
        }
        if(StringUtils.isEmpty(sysStaticValueDto.getValueName())){
            return CommonResult.failed("静态名称为空！");
        }
        if(StringUtils.isEmpty(sysStaticValueDto.getValue())){
            return CommonResult.failed("静态值为空！");
        }
        //初始化数据
        SysStaticValue sysStaticValue = BeanUtil.copyObject(sysStaticValueDto, SysStaticValue.class);
        sysStaticValue.setStaticTypeId(sysStaticValueDto.getStaticId());
        //保存静态值
        sysStaticValueDao.save(sysStaticValue);
        return CommonResult.success("静态值保存成功！");
    }
    /**
     * @description: TODO 修改静态属性
     * @Param: token 令牌，sysStaticTypeDto 静态属性参数
     * @author: tangxl
     * @date: 2022年3月2日14:36:11
     * @return: CommonResult
     */
    @Override public CommonResult updateStaticType(String token, SysStaticTypeDTO sysStaticTypeDto) {
        //参数校验
        if(BeanUtil.isEmpty(sysStaticTypeDto.getStaticId())){
            return CommonResult.failed("静态类型ID为空！");
        }
        if(StringUtils.isEmpty(sysStaticTypeDto.getStaticName())){
            return CommonResult.failed("静态类型名称为空！");
        }
        if(StringUtils.isEmpty(sysStaticTypeDto.getStaticCode())){
            return CommonResult.failed("静态编码为空！");
        }
        if(BeanUtil.isEmpty(sysStaticTypeDto.getState())){
            return CommonResult.failed("数据状态未知！");
        }
        //初始化静态属性参数
        SysStaticType sysStaticType = sysStaticTypeDao.findOneByStaticIdAndState(sysStaticTypeDto.getStaticId(),1L);
        sysStaticType.setStaticCode(sysStaticTypeDto.getStaticCode());
        sysStaticType.setStaticName(sysStaticTypeDto.getStaticName());
        sysStaticType.setRemarkOne(sysStaticTypeDto.getRemarkOne());
        sysStaticType.setRemarkTwo(sysStaticTypeDto.getRemarkTwo());
        sysStaticType.setState(sysStaticTypeDto.getState());
        //更新数据
        sysStaticTypeDao.save(sysStaticType);
        return CommonResult.success("静态类型更新成功！");
    }
    /**
     * @description: TODO 修改静态值
     * @Param: token 令牌，sysStaticValueDto 静态值参数
     * @author: tangxl
     * @date: 2022年3月2日14:50:08
     * @return: CommonResult
     */
    @Override public CommonResult updateStaticValue(String token, SysStaticValueDTO sysStaticValueDto) {
        //参数校验
        if(BeanUtil.isEmpty(sysStaticValueDto.getStaticValueId())){
            return CommonResult.failed("静态值Id为空！");
        }
        if(StringUtils.isEmpty(sysStaticValueDto.getValueName())){
            return CommonResult.failed("静态名称为空！");
        }
        if(StringUtils.isEmpty(sysStaticValueDto.getValue())){
            return CommonResult.failed("静态值为空！");
        }
        if(BeanUtil.isEmpty(sysStaticValueDto.getState())){
            return CommonResult.failed("数据状态未知！");
        }
        //初始化静态值参数
        SysStaticValue sysStaticValue = sysStaticValueDao.findOneByStaticValueIdAndState(sysStaticValueDto.getStaticValueId(),1L);
        sysStaticValue.setValueName(sysStaticValueDto.getValueName());
        sysStaticValue.setValue(sysStaticValueDto.getValue());
        sysStaticValue.setRemarkOne(sysStaticValueDto.getRemarkOne());
        sysStaticValue.setRemarkTwo(sysStaticValueDto.getRemarkTwo());
        sysStaticValue.setState(sysStaticValueDto.getState());
        //更新数据
        sysStaticValueDao.save(sysStaticValue);
        return CommonResult.success("静态值更新成功！");
    }
}
