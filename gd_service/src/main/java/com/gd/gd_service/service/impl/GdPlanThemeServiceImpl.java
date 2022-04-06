package com.gd.gd_service.service.impl;

import com.gd.base.constant.Constans;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.DataCodeDTO;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.TeacherDesDirDTO;
import com.gd.base.pojo.dto.plan.TeacherDirectionPageDTO;
import com.gd.base.pojo.dto.plan.TeacherThemeDTO;
import com.gd.base.pojo.dto.plan.TeacherThemePageDTO;
import com.gd.base.entity.*;
import com.gd.base.enums.plan.ThemeSourceEunm;
import com.gd.base.jdbc.GdPlanThemeJdbc;
import com.gd.base.jpa.*;
import com.gd.base.util.BeanUtil;
import com.gd.base.util.DateUtil;
import com.gd.base.util.EncryptConfigUtil;
import com.gd.base.util.FtpUtil;
import com.gd.base.pojo.vo.plan.TeacherDirectionVO;
import com.gd.base.pojo.vo.plan.TeacherThemeVO;
import com.gd.base.pojo.vo.redis.RedisSysUser;
import com.gd.gd_service.service.GdPlanThemeService;
import com.gd.gd_service.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: gd_plan
 * @description: TODO 毕设主题选择业务层
 * @author: tangxl
 * @create: 2022-03-02 17:03
 */
@Service
@Slf4j
public class GdPlanThemeServiceImpl implements GdPlanThemeService {
	@Autowired
	private GdPlanThemeJdbc gdPlanThemeJdbc;
	@Autowired
	private GdDesignDirectionDao gdDesignDirectionDao;
	@Autowired
	private RedisService redisService;
	@Autowired
	private GdDesignThemeDao gdDesignThemeDao;
	@Autowired
	private FtpUtil ftpUtil;
	@Autowired
	private SysFtpFileDao sysFtpFileDao;
	@Autowired
	private SysFtpFileErrDao sysFtpFileErrDao;
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private GdPlanDetailDao gdPlanDetailDao;
	/**
	 * @description: TODO 教师设计方向分页查询
	 * @Param: token 令牌，teacherDesignDto 参数，page 分页信息
	 * @author: tangxl
	 * @date: 2022年3月3日09:14:26
	 * @return: CommonResult
	 */
	@Override
	public CommonResult findTeacherDesignDirectionPage(String token, TeacherDirectionPageDTO teacherDirectionPageDto, PageBaseInfo page) {
		RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
		if(redisService.findIsTeacher(token)){
			teacherDirectionPageDto.setTeacherName(redisSysUser.getUserName());
		}
		PageBaseInfo<TeacherDirectionVO> teacherDesignVoPage = gdPlanThemeJdbc.findTeacherDesignDirectionPage(teacherDirectionPageDto,page);
		return CommonResult.success(teacherDesignVoPage,"查询成功！");
	}
	/**
	 * @description: TODO 教师设计主题分页查询
	 * @Param: token 令牌，teacherThemeDto 参数，page 分页信息
	 * @author: tangxl
	 * @date: 2022年3月3日09:14:26
	 * @return: CommonResult
	 */
	@Override
	public CommonResult findTeacherDesignThemePage(String token, TeacherThemePageDTO teacherThemePageDto, PageBaseInfo page) {
		RedisSysUser  redisSysUser = redisService.getSysUserByToken(token);
		if(redisService.findIsTeacher(token)){
			teacherThemePageDto.setUserId(redisSysUser.getUserId());
		}
		PageBaseInfo<TeacherThemeVO> teacherThemeVoPage = gdPlanThemeJdbc.findTeacherDesignThemePage(teacherThemePageDto,page);
		return CommonResult.success(teacherThemeVoPage,"查询成功！");
	}
	/**
	 * @description: TODO 新增毕业设计方向
	 * @Param: token 用户令牌,addTeacherDesDirDto 参数
	 * @author: tangxl
	 * @date: 2022年3月5日14:11:04
	 * @return: CommonResult
	 */
	@Override
	public CommonResult addTeacherDesDir(String token, TeacherDesDirDTO teacherDesDirDto) {
		GdDesignDirection newGdDesignDirection = new GdDesignDirection();
		//获取用户信息
		RedisSysUser redisSysUser=redisService.getSysUserByToken(token);
		//初始化信息
		if(StringUtils.isEmpty(teacherDesDirDto.getDesignDirection())){
			return CommonResult.failed("设计方向为空！");
		}
		newGdDesignDirection.setDesignDirection(teacherDesDirDto.getDesignDirection());
		newGdDesignDirection.setSysUserId(redisSysUser.getUserId());
		newGdDesignDirection.setThemeNum(0L);
		newGdDesignDirection.setThemeMargin(0L);
		newGdDesignDirection.setCreateTime(DateUtil.dateToLocalDateTime(new Date()));
		newGdDesignDirection.setState(1L);
		gdDesignDirectionDao.save(newGdDesignDirection);
		return CommonResult.success("添加成功！");
	}

	/**
	 * @description: TODO 删除毕业设计方向
	 * @Param:  token 令牌，addTeacherDesDirDto 参数
	 * @author: tangxl
	 * @date: 2022年3月5日15:47:44
	 * @return: CommonResult
	 */
	@Override public CommonResult deleteTeacherDesDir(String token, TeacherDesDirDTO teacherDesDirDto) {
		if(teacherDesDirDto.getIds().isEmpty()){
			return CommonResult.failed("请先选择一条再删除！");
		}
		List<GdDesignTheme> gdDesignThemeList = gdDesignThemeDao.findAllByGdDesignDirectionIdInAndState(teacherDesDirDto.getIds(),1L);
		if(!CollectionUtils.isEmpty(gdDesignThemeList)){
			return CommonResult.failed("该方向下已有主题不可删除！");
		}
		List<GdDesignDirection> gdDesignDirectionList = gdDesignDirectionDao.findAllByIdInAndState(teacherDesDirDto.getIds(),1L);
		gdDesignDirectionList.stream().forEach(o->o.setState(0L));
		gdDesignDirectionDao.saveAll(gdDesignDirectionList);
		return CommonResult.success("删除成功！");
	}

	/**
	 * @description: TODO 查询设计主题来源
	 * @Param: token 令牌
	 * @author: tangxl
	 * @date: 2022年3月5日16:05:47
	 * @return: CommonResult
	 */
	@Override public CommonResult findThemeSource(String token) {
		List<DataCodeDTO> themeSourceDataCodeDTOList = ThemeSourceEunm.getAllTypeAndTypeName();
		return CommonResult.success(themeSourceDataCodeDTOList,"查询成功！");
	}

	/**
	 * @description: TODO 新增设计主题
	 * @Param: token 令牌
	 * @author: tangxl
	 * @date: 2022年3月5日16:15:12
	 * @return: CommonResult
	 */
	@Override public CommonResult addTeacherTheme(String token, TeacherThemeDTO teacherTheme) {
		//获取教师信息
		RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
		SysRole sysRole = sysRoleDao.findByRoleIdAndState(redisSysUser.getRoleId(), 1L);
		//校验数据
		if(BeanUtil.isEmpty(teacherTheme.getGdDesignDirectionId())){
			return CommonResult.failed("设计方向id为空！");
		}
		if(StringUtils.isEmpty(teacherTheme.getThemeName())){
			return CommonResult.failed("设计主题名称为空！");
		}
		//初始化设计主题数据
		GdDesignTheme gdDesignTheme = BeanUtil.copyObject(teacherTheme, GdDesignTheme.class);

		if(StringUtils.isEmpty(teacherTheme.getThemeSource())){
			if("Student".equals(sysRole.getRoleCode())){
				gdDesignTheme.setThemeSource("2");
			}
			if("Teacher".equals(sysRole.getRoleCode())){
				gdDesignTheme.setThemeSource("1");
			}
			if("Admin".equals(sysRole.getRoleCode())){
				gdDesignTheme.setThemeSource("1");
			}

		}
		gdDesignTheme.setSysUserId(redisSysUser.getUserId());
		gdDesignTheme.setCreateTime(DateUtil.dateToLocalDateTime(new Date()));
		gdDesignTheme.setThemeUploadState(0L);
		gdDesignTheme.setIsChoose(0L);
		gdDesignTheme.setState(1L);
		gdDesignThemeDao.save(gdDesignTheme);
		//更新设计方向中的主题个数
		GdDesignDirection gdDesignDirection = gdDesignDirectionDao.getById(teacherTheme.getGdDesignDirectionId());
		gdDesignDirection.setThemeNum(gdDesignDirection.getThemeNum()+1L);
		gdDesignDirection.setThemeMargin(gdDesignDirection.getThemeMargin()+1L);
		gdDesignDirectionDao.save(gdDesignDirection);
		return CommonResult.success("新增成功！");
	}

	/**
	 * @description: TODO 设计主题详情文档上传
	 * @Param: token 令牌,files 文件集合,teacherThemeDto 参数
	 * @author: tangxl
	 * @date: 2022年3月5日16:52:30
	 * @return: CommonResult
	 */
	@Override
	public CommonResult uploadGdThemeDetail(String token, MultipartFile[] files, TeacherThemeDTO teacherThemeDto) {
		RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
		//判断是否已上传,已上传删除原文件夹
		GdDesignTheme gdDesignTheme = gdDesignThemeDao.getById(teacherThemeDto.getId());
		if(gdDesignTheme.getThemeUploadState().equals(1L)){
			//获取已文件
			List<SysFtpFile> sysFtpFileList = sysFtpFileDao.findAllByBatchAndState(gdDesignTheme.getFtpFileBatch(),1L);
			//获取上传失败文件
			List<SysFtpFileErr> sysFtpFileErrList = sysFtpFileErrDao.findAllByBatchAndState(gdDesignTheme.getFtpFileBatch(),0L);
			String directoryName = "";
			if(!CollectionUtils.isEmpty(sysFtpFileList)){
				directoryName = sysFtpFileList.get(0).getFilePath();
				directoryName = directoryName.substring(directoryName.lastIndexOf("/") + 1);
			}else if (!CollectionUtils.isEmpty(sysFtpFileErrList)) {
				directoryName = sysFtpFileErrList.get(0).getFilePath();
				directoryName = directoryName.substring(directoryName.lastIndexOf("/") + 1);
			}
			//删除之前上传的文件夹
			if(!ftpUtil.deleteDirectory(Constans.THEME_DETAIL_PATH,directoryName)){
				return CommonResult.failed("原文件删除失败！请联系管理员");
			}
			//修改数据ftp文件状态
			if(!CollectionUtils.isEmpty(sysFtpFileList)){
				sysFtpFileList.stream().forEach(o->o.setState(0L));
				sysFtpFileDao.saveAll(sysFtpFileList);
			}else if (!CollectionUtils.isEmpty(sysFtpFileErrList)) {
				sysFtpFileErrList.stream().forEach(o -> o.setState(1L));
				sysFtpFileErrDao.saveAll(sysFtpFileErrList);
			}
			//修改设计主题信息
			gdDesignTheme.setFtpFileBatch("");
			gdDesignTheme.setThemeUploadState(0L);
		}
		//参数校验
		if(BeanUtil.isEmpty(teacherThemeDto.getId())){
			return CommonResult.failed("设计主题ID为空！");
		}
		if (files.length == 0 || files[0].getOriginalFilename().length() <= 0) {
			return CommonResult.failed("请选择详情文件！");
		}
		//生成批次号
		String batch = EncryptConfigUtil.encryptPwd(DateUtil.dateToStrDate(new Date(),Constans.DEFAULT_DATE_TIME_FORMAT3)+redisSysUser.getPhone());
		//文件上传--文件夹名称为当前时间
		String filePath = Constans.THEME_DETAIL_PATH+"/"+DateUtil.dateToStrDate(new Date(), Constans.DEFAULT_DATE_TIME_FORMAT3);
		boolean flag = false;
		String msg = "";

		for(MultipartFile file : files){
			try {
				log.info("上传设计主题详情{}{},设计主题ID:{}",filePath,file.getOriginalFilename(),teacherThemeDto.getId());
				flag = ftpUtil.uploadFile(filePath,file.getOriginalFilename(),file.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
				log.info("上传设计主题详情{}{}失败！设计主题ID:{}",filePath,file.getOriginalFilename(),teacherThemeDto.getId());
				msg = e.getMessage();
			}finally {
				if (flag) {
					//保存已成功上传文件
					SysFtpFile sysFtpFile = new SysFtpFile();
					sysFtpFile.setFileName(file.getOriginalFilename());
					sysFtpFile.setFilePath(filePath);
					sysFtpFile.setFileSize(file.getSize());
					sysFtpFile.setBatch(batch);
					sysFtpFile.setCreateTime(DateUtil.dateToLocalDateTime(new Date()));
					sysFtpFile.setUserId(redisSysUser.getUserId());
					sysFtpFile.setUserName(redisSysUser.getUserName());
					sysFtpFile.setState(1L);
					sysFtpFileDao.save(sysFtpFile);
				}else {
					//保存上传错误文件信息
					SysFtpFileErr sysFtpFileErr = new SysFtpFileErr();
					sysFtpFileErr.setFileName(file.getName());
					sysFtpFileErr.setFilePath(filePath);
					sysFtpFileErr.setCreateTime(DateUtil.dateToLocalDateTime(new Date()));
					sysFtpFileErr.setFileSize(file.getSize());
					sysFtpFileErr.setBatch(batch);
					sysFtpFileErr.setState(1L);
					sysFtpFileErr.setErrMessage(msg);
					sysFtpFileErr.setUserId(redisSysUser.getUserId());
					sysFtpFileErr.setUserName(redisSysUser.getUserName());
					sysFtpFileErrDao.save(sysFtpFileErr);
					log.info("ftp上传错误文件信息保存：{}",sysFtpFileErr);
				}
			}
		}
		//保存设计主题上传文件批次号
		gdDesignTheme.setFtpFileBatch(batch);
		gdDesignTheme.setThemeUploadState(1L);
		gdDesignThemeDao.save(gdDesignTheme);
		return CommonResult.success("上传成功！");
	}

	/**
	 * @description: TODO 查询设计主题详情文档名称列表
	 * @Param: token 令牌，teacherThemeDto 参数
	 * @author: tangxl
	 * @date: 2022年3月6日01:40:12
	 * @return: CommonResult
	 */
	@Override
	public CommonResult findGdThemeDetailName(String token, TeacherThemeDTO teacherThemeDto) {
		List<DataCodeDTO> themeDetailFiles = gdPlanThemeJdbc.findGdThemeDetailName(teacherThemeDto);
		return CommonResult.success(themeDetailFiles,"查询成功！");
	}

	/**
	 * @description: TODO
	 * @Param: token 令牌，teacherThemeDto 参数
	 * @author: tangxl
	 * @date: 2022年3月7日11:11:35
	 * @return: CommonResult
	 */
	@Override public void downloadGdThemeDetail(String token, TeacherThemeDTO teacherThemeDto, HttpServletResponse response) {
		if(BeanUtil.isEmpty(teacherThemeDto.getId())){
			throw new RuntimeException("主题详情ID为空！");
		}
		GdDesignTheme gdDesignTheme = gdDesignThemeDao.findByIdAndState(teacherThemeDto.getId(),1L);
		if(gdDesignTheme.getThemeUploadState().equals(0L)){
			throw new RuntimeException("该设计主题还未上传详情！");
		}
		List<SysFtpFile> sysFtpFileList = sysFtpFileDao.findAllByBatchAndState(gdDesignTheme.getFtpFileBatch(),1L);
		if(CollectionUtils.isEmpty(sysFtpFileList)){
			throw new RuntimeException("详情下载失败！请联系管理员");
		}
		String filePath = sysFtpFileList.get(0).getFilePath();
		List<String> fileNameList = sysFtpFileList.stream().map(o->o.getFileName()).collect(Collectors.toList());
		try {
			//设置文件ContentType类型，这样设置，会自动判断下载文件类型
			response.setContentType("application/octet-stream");
			response.setCharacterEncoding("UTF-8");
			//设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)
			response.setHeader("Content-Disposition", "attachment;filename="+new String((gdDesignTheme.getThemeName()+".zip").getBytes(StandardCharsets.UTF_8),"iso-8859-1"));
			ftpUtil.downZipFile(filePath,fileNameList,response);
			log.info("获取文件流成功！{}",response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
			log.info("文件下载失败！");
			throw new RuntimeException("文件下载失败！");
		}
		log.info("设计主题：{}，主题详情下载成功！",gdDesignTheme.getThemeName());

	}

	/**
	 * @description: TODO 学生选择毕业设计主题
	 * @Param: token 令牌，teacherThemeDto 参数
	 * @author: tangxl
	 * @date: 2022年3月8日15:09:35
	 * @return: CommonResult
	 */
	@Override public CommonResult addUserGdPlan(String token, TeacherThemeDTO teacherThemeDto) {
		RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
		SysRole sysRole = sysRoleDao.findByRoleIdAndState(redisSysUser.getRoleId(), 1L);
		if(!"Student".equals(sysRole.getRoleCode())){
			return CommonResult.failed("只有学生才允许选择！");
		}
		GdDesignTheme gdDesignTheme = gdDesignThemeDao.findByIdAndState(teacherThemeDto.getId(),1L);
		if(BeanUtil.isEmpty(gdDesignTheme)){
			return CommonResult.failed("设计主题不存在！");
		}
		if(StringUtils.isNotEmpty(gdDesignTheme.getOverTime().toString())&&gdDesignTheme.getOverTime().isBefore(LocalDateTime.now())){
			return CommonResult.failed("已过选题时间！");
		}
		//是否已选择过该主题
		List<GdPlanDetail> allGdPlanDetail = gdPlanDetailDao.findAllByUserIdAndState(redisSysUser.getUserId(), 1L);
		if(allGdPlanDetail.stream().anyMatch(o->o.getGdDesignThemeId().equals(teacherThemeDto.getId())&&!o.getExamineState().equals(2L))){
			return CommonResult.failed("已选择该主题！");
		}
		//创建毕业设计详情
		GdPlanDetail gdPlanDetail = new GdPlanDetail();
		gdPlanDetail.setUserId(redisSysUser.getUserId());
		gdPlanDetail.setGdDesignThemeId(teacherThemeDto.getId());
		gdPlanDetail.setCreateTime(DateUtil.dateToLocalDateTime(new Date()));
		gdPlanDetail.setSysUserId(gdDesignTheme.getSysUserId());
		gdPlanDetail.setCreateBy(redisSysUser.getUserName());
		gdPlanDetail.setExamineState(0L);
		gdPlanDetail.setOpenRequireState(0L);
		gdPlanDetail.setOpenReportState(0L);
		gdPlanDetail.setOpenExamineState(0L);
		gdPlanDetail.setMidReportState(0L);
		gdPlanDetail.setMidRequireState(0L);
		gdPlanDetail.setMidExamineState(0L);
		gdPlanDetail.setEndRequireState(0L);
		gdPlanDetail.setEndReportState(0L);
		gdPlanDetail.setExemptDefenseState(0L);
		gdPlanDetail.setExemptUploadState(0L);
		gdPlanDetail.setState(1L);
		gdPlanDetailDao.save(gdPlanDetail);
		return CommonResult.success("选择成功！");
	}

	/**
	 * @description: TODO 编辑毕业设计主题
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	@Override public CommonResult updateTeacherTheme(String token, TeacherThemeDTO teacherTheme) {
		if(StringUtils.isEmpty(teacherTheme.getThemeName())){
			return CommonResult.failed("主题名称不能为空！");
		}
		if(StringUtils.isEmpty(teacherTheme.getThemeSource())){
			return CommonResult.failed("主题来源不能为空！");
		}
		if(BeanUtil.isEmpty(teacherTheme.getId())){
			return CommonResult.failed("主题ID必传！");
		}
		//更新信息
		GdDesignTheme gdDesignTheme = gdDesignThemeDao.findByIdAndState(teacherTheme.getId(),1L);
		gdDesignTheme.setThemeSource(teacherTheme.getThemeSource());
		if(StringUtils.isNotEmpty(teacherTheme.getOverTime())){
			gdDesignTheme.setOverTime(DateUtil.stringToLocalDateTime(teacherTheme.getOverTime(),Constans.DEFAULT_DATE_TIME_FORMAT2));
		}
		gdDesignTheme.setThemeName(teacherTheme.getThemeName());
		gdDesignTheme.setRemark(teacherTheme.getRemark());
		gdDesignThemeDao.save(gdDesignTheme);
		return CommonResult.success("修改成功！");
	}

	/**
	 * @description: TODO 编辑毕业设计方向
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	@Override public CommonResult updateTeacherDesDir(String token, TeacherDesDirDTO teacherDesDirDto) {
		if(StringUtils.isEmpty(teacherDesDirDto.getDesignDirection())){
			return CommonResult.failed("方向名称不能为空！");
		}
		if(BeanUtil.isEmpty(teacherDesDirDto.getId())){
			return CommonResult.failed("主题ID必传！");
		}
		//更新信息
		GdDesignDirection gdDesignDirection = gdDesignDirectionDao.findAllByIdAndState(teacherDesDirDto.getId(),1L);
		gdDesignDirection.setDesignDirection(teacherDesDirDto.getDesignDirection());
		gdDesignDirectionDao.save(gdDesignDirection);
		return CommonResult.success("修改成功！");
	}

	/**
	 * @description: TODO 删除毕业设计主题
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	@Override public CommonResult deleteTeacherTheme(String token, TeacherThemeDTO teacherTheme) {
		if(CollectionUtils.isEmpty(teacherTheme.getIds())){
			return CommonResult.failed("请选择一条主题数据！");
		}
		//获取主题数据
		List<GdDesignTheme> gdDesignThemeList = gdDesignThemeDao.findAllByIdInAndState(teacherTheme.getIds(),1L);
		if(CollectionUtils.isEmpty(gdDesignThemeList) || gdDesignThemeList.size()!=teacherTheme.getIds().size()){
			return CommonResult.failed("主题数据错误！");
		}
		if(gdDesignThemeList.stream().anyMatch(o->o.getIsChoose().equals(1L))){
			return CommonResult.failed("该主题已被学生选择不可删除!");
		}
		//修改主题对应方向数据
		gdDesignThemeList.stream().forEach(o->o.setState(0L));
		gdDesignThemeList.stream().forEach(o -> {
			GdDesignDirection gdDesignDirection  = gdDesignDirectionDao.findByIdAndState(o.getGdDesignDirectionId(),1L);
			if(BeanUtil.isEmpty(gdDesignDirection)){
				throw new RuntimeException(gdDesignDirection.getDesignDirection()+"对应设计方向不存在！");
			}
			gdDesignDirection.setThemeNum(gdDesignDirection.getThemeNum()-1);
			gdDesignDirection.setThemeNum(gdDesignDirection.getThemeNum()-1);
			gdDesignDirectionDao.save(gdDesignDirection);
		});
		gdDesignThemeDao.saveAll(gdDesignThemeList);
		return CommonResult.success("删除成功！");
	}

	/**
	 * @description: TODO 查询当前登录教师对应的设计方向
	 * @return:
	 * @author: tangxl
	 * @time:  16:30
	 */
	@Override
	public CommonResult findDataCodeGdDerection(String token) {
		RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
		if(redisService.findIsStudent(token)){
			return CommonResult.failed("当前用户不是教师!");
		}
		List<DataCodeDTO> list = gdPlanThemeJdbc.findDataCodeGdDerection(redisSysUser);
		return CommonResult.success(list,"查询成功!");
	}

}
