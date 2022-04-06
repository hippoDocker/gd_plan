package com.gd.gd_service.service.impl;

import com.gd.base.constant.Constans;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.GdPlanReportPageDTO;
import com.gd.base.pojo.dto.plan.GdPlanUploadFileDTO;
import com.gd.base.entity.GdPlanDetail;
import com.gd.base.entity.SysFtpFile;
import com.gd.base.entity.SysFtpFileErr;
import com.gd.base.jdbc.GdPlanProcessJdbc;
import com.gd.base.jpa.GdPlanDetailDao;
import com.gd.base.jpa.SysFtpFileDao;
import com.gd.base.jpa.SysFtpFileErrDao;
import com.gd.base.util.BeanUtil;
import com.gd.base.util.DateUtil;
import com.gd.base.util.EncryptConfigUtil;
import com.gd.base.util.FtpUtil;
import com.gd.base.pojo.vo.plan.GdPlanExemptDefensePageVO;
import com.gd.base.pojo.vo.plan.GdPlanReportPageVO;
import com.gd.base.pojo.vo.redis.RedisSysUser;
import com.gd.gd_service.service.GdPlanProcessService;
import com.gd.gd_service.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计流程业务处理
 * @author: tangxl
 * @create: 2022-03-09 08:49
 */
@Service
@Slf4j
public class GdPlanProcessServiceImpl implements GdPlanProcessService {
	@Autowired
	private GdPlanProcessJdbc gdPlanProcessJdbc;
	@Autowired
	private RedisService redisService;
	@Autowired
	private GdPlanDetailDao gdPlanDetailDao;
	@Autowired
	private SysFtpFileDao sysFtpFileDao;
	@Autowired
	private SysFtpFileErrDao sysFtpFileErrDao;
	@Autowired
	private FtpUtil ftpUtil;

	/**
	 * @description: TODO 开题报告分页查询
	 * @Param: token 令牌，gdPlanReportPageDto 参数，page 分页信息
	 * @author: tangxl
	 * @date: 2022年3月9日11:43:28
	 * @return: CommonResult
	 */
	@Override public CommonResult findOpenReportPage(String token, GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo Page) {
		RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
		if(redisService.findIsStudent(token)){
			gdPlanReportPageDto.setUserId(redisSysUser.getUserId());
		}
		PageBaseInfo<GdPlanReportPageVO> gdPlanReportPageVoPage = gdPlanProcessJdbc.findOpenReportPage(gdPlanReportPageDto, Page);
		return CommonResult.success(gdPlanReportPageVoPage,"查询成功！");
	}
	/**
	 * @description: TODO 中期检测分页查询
	 * @Param: token 令牌，gdPlanReportPageDto 参数，page 分页信息
	 * @author: tangxl
	 * @date: 2022年3月9日11:43:28
	 * @return: CommonResult
	 */
	@Override public CommonResult findMidReportPage(String token, GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo page) {
		RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
		if(redisService.findIsStudent(token)){
			gdPlanReportPageDto.setUserId(redisSysUser.getUserId());
		}
		PageBaseInfo<GdPlanReportPageVO> gdPlanReportPageVoPage = gdPlanProcessJdbc.findMidReportPage(gdPlanReportPageDto,page);
		return CommonResult.success(gdPlanReportPageVoPage,"查询成功！");
	}
	/**
	 * @description: TODO 毕业论文分页查询
	 * @Param: token 令牌，gdPlanReportPageDto 参数，page 分页信息
	 * @author: tangxl
	 * @date: 2022年3月9日11:43:28
	 * @return: CommonResult
	 */
	@Override public CommonResult findEndReportPage(String token, GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo page) {
		RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
		if(redisService.findIsStudent(token)){
			gdPlanReportPageDto.setUserId(redisSysUser.getUserId());
		}
		PageBaseInfo<GdPlanReportPageVO> gdPlanReportPageVoPage = gdPlanProcessJdbc.findEndReportPage(gdPlanReportPageDto,page);
		return CommonResult.success(gdPlanReportPageVoPage,"查询成功！");
	}
	/**
	 * @description: TODO 毕业免答辩数据分页查询
	 * @Param: token 令牌，gdPlanReportPageDto 参数，page 分页信息
	 * @author: tangxl
	 * @date: 2022年3月9日11:43:28
	 * @return: CommonResult
	 */
	@Override public CommonResult findExemptReportPage(String token, GdPlanReportPageDTO gdPlanReportPageDto, PageBaseInfo page) {
		RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
		if(redisService.findIsStudent(token)){
			gdPlanReportPageDto.setUserId(redisSysUser.getUserId());
		}
		if(redisService.findIsTeacher(token)){
			gdPlanReportPageDto.setTeacherId(redisSysUser.getUserId());
		}
		PageBaseInfo<GdPlanExemptDefensePageVO> gdPlanExemptDefensePage = gdPlanProcessJdbc.findExemptReportPage(gdPlanReportPageDto,page);
		return CommonResult.success(gdPlanExemptDefensePage,"查询成功！");
	}

	/**
	 * @description: TODO 开题报告/中期检测/毕业论文/免答辩申请
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	@Override public CommonResult uploadReport(String token, MultipartFile[] files, GdPlanUploadFileDTO gdPlanUploadFileDto) {
		RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
		GdPlanDetail gdPlanDetail = gdPlanDetailDao.findByIdAndState(gdPlanUploadFileDto.getId(),1L);
		if(BeanUtil.isEmpty(gdPlanDetail)){
			return CommonResult.failed("数据不存在！");
		}
		//开题报告上传
		if(gdPlanUploadFileDto.getType().equals(1L)){
			if (gdPlanDetail.getOpenReportState().equals(1L)) {
				//获取已文件
				List<SysFtpFile> sysFtpFileList = sysFtpFileDao.findAllByBatchAndState(gdPlanDetail.getOpenReportBatch(), 1L);
				//获取上传失败文件
				List<SysFtpFileErr> sysFtpFileErrList = sysFtpFileErrDao.findAllByBatchAndState(gdPlanDetail.getOpenReportBatch(), 0L);
				//删除原上传文件
				if (getObjectCommonResult(sysFtpFileList, sysFtpFileErrList)) {
					return CommonResult.failed("原文件删除失败！请联系管理员");
				}
				//修改报告信息为未上传状态
				gdPlanDetail.setOpenReportBatch(null);
				gdPlanDetail.setOpenReportState(0L);
			}
			//文件上传
			//生成批次号
			String batch = getBatchString(files, gdPlanUploadFileDto, redisSysUser);
			//保存上传文件批次号
			gdPlanDetail.setOpenReportBatch(batch);
			gdPlanDetail.setOpenReportState(1L);
			gdPlanDetailDao.save(gdPlanDetail);
			return CommonResult.success("上传成功！");
		}
		//中期检测文件上传
		if(gdPlanUploadFileDto.getType().equals(2L)){
			if (gdPlanDetail.getMidReportState().equals(1L)) {
				//获取已文件
				List<SysFtpFile> sysFtpFileList = sysFtpFileDao.findAllByBatchAndState(gdPlanDetail.getMidReportBatch(), 1L);
				//获取上传失败文件
				List<SysFtpFileErr> sysFtpFileErrList = sysFtpFileErrDao.findAllByBatchAndState(gdPlanDetail.getMidReportBatch(), 0L);
				//删除原上传文件
				if (getObjectCommonResult(sysFtpFileList, sysFtpFileErrList)) {
					return CommonResult.failed("原文件删除失败！请联系管理员");
				}
				//修改报告信息为未上传状态
				gdPlanDetail.setMidReportBatch(null);
				gdPlanDetail.setMidReportState(0L);
			}
			//文件上传
			//生成批次号
			String batch = getBatchString(files, gdPlanUploadFileDto, redisSysUser);
			//保存上传文件批次号
			gdPlanDetail.setMidReportBatch(batch);
			gdPlanDetail.setMidReportState(1L);
			gdPlanDetailDao.save(gdPlanDetail);
			return CommonResult.success("上传成功！");
		}
		//毕业论文文件上传
		if(gdPlanUploadFileDto.getType().equals(3L)){
			if (gdPlanDetail.getEndReportState().equals(1L)) {
				//获取已文件
				List<SysFtpFile> sysFtpFileList = sysFtpFileDao.findAllByBatchAndState(gdPlanDetail.getEndReportBatch(), 1L);
				//获取上传失败文件
				List<SysFtpFileErr> sysFtpFileErrList = sysFtpFileErrDao.findAllByBatchAndState(gdPlanDetail.getEndReportBatch(), 0L);
				//删除原上传文件
				if (getObjectCommonResult(sysFtpFileList, sysFtpFileErrList)) {
					return CommonResult.failed("原文件删除失败！请联系管理员");
				}
				//修改报告信息为未上传状态
				gdPlanDetail.setEndReportBatch(null);
				gdPlanDetail.setEndReportState(0L);
			}
			//文件上传
			//生成批次号
			String batch = getBatchString(files, gdPlanUploadFileDto, redisSysUser);
			//保存上传文件批次号
			gdPlanDetail.setEndReportBatch(batch);
			gdPlanDetail.setEndReportState(1L);
			gdPlanDetailDao.save(gdPlanDetail);
			return CommonResult.success("上传成功！");
		}
		//免答辩申请文件上传
		if(gdPlanUploadFileDto.getType().equals(4L)){
			if (gdPlanDetail.getExemptUploadState().equals(1L)) {
				//获取已文件
				List<SysFtpFile> sysFtpFileList = sysFtpFileDao.findAllByBatchAndState(gdPlanDetail.getExemptUploadBatch(), 1L);
				//获取上传失败文件
				List<SysFtpFileErr> sysFtpFileErrList = sysFtpFileErrDao.findAllByBatchAndState(gdPlanDetail.getExemptUploadBatch(), 0L);
				//删除原上传文件
				if (getObjectCommonResult(sysFtpFileList, sysFtpFileErrList)) {
					return CommonResult.failed("原文件删除失败！请联系管理员");
				}
				//修改报告信息为未上传状态
				gdPlanDetail.setExemptUploadBatch(null);
				gdPlanDetail.setExemptUploadState(0L);
			}
			//文件上传
			//生成批次号
			String batch = getBatchString(files, gdPlanUploadFileDto, redisSysUser);
			//保存上传文件批次号
			gdPlanDetail.setExemptUploadBatch(batch);
			gdPlanDetail.setExemptUploadState(1L);
			gdPlanDetailDao.save(gdPlanDetail);
			return CommonResult.success("上传成功！");
		}
		return CommonResult.failed("文件来源未知！");
	}

	/**
	 * @description: TODO 开题报告/中期检测/毕业论文 要求下载
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	@Override public void downRequire(String token, GdPlanUploadFileDTO gdPlanUploadFileDto, HttpServletResponse response) {
		log.info("开题报告/中期检测/毕业论文 要求下载 id:{},type:{}",gdPlanUploadFileDto.getId(),gdPlanUploadFileDto.getType());
		if (BeanUtil.isEmpty(gdPlanUploadFileDto.getType())){
			throw new RuntimeException("来源类型type为空!");
		}
		if(BeanUtil.isEmpty(gdPlanUploadFileDto.getId())){
			throw new RuntimeException("主题详情ID为空！");
		}
		GdPlanDetail gdPlanDetail = gdPlanDetailDao.findByIdAndState(gdPlanUploadFileDto.getId(),1L);
		List<SysFtpFile> sysFtpFileList = new ArrayList<SysFtpFile>();
		//开题报告要求
		if(gdPlanUploadFileDto.getType().equals(1L)){
			if(gdPlanDetail.getOpenRequireState().equals(0L)){
				throw new RuntimeException("开题报告要求还未上传要求！");
			}
			sysFtpFileList = sysFtpFileDao.findAllByBatchAndState(gdPlanDetail.getOpenRequireBatch(),1L);
		}
		//中期检测
		if(gdPlanUploadFileDto.getType().equals(2L)){
			if(gdPlanDetail.getMidRequireState().equals(0L)){
				throw new RuntimeException("中期检测要求还未上传要求！");
			}
			sysFtpFileList = sysFtpFileDao.findAllByBatchAndState(gdPlanDetail.getMidRequireBatch(),1L);
		}
		//论文
		if(gdPlanUploadFileDto.getType().equals(3L)){
			if(gdPlanDetail.getMidRequireState().equals(0L)){
				throw new RuntimeException("论文要求还未上传要求！");
			}
			sysFtpFileList = sysFtpFileDao.findAllByBatchAndState(gdPlanDetail.getEndRequireBatch(),1L);
		}

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
			response.setHeader("Content-Disposition", "attachment;filename="+new String((gdPlanDetail.getCreateBy()+".zip").getBytes(
					StandardCharsets.UTF_8),"iso-8859-1"));
			ftpUtil.downZipFile(filePath,fileNameList,response);
			log.info("获取文件流成功！{}",response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
			log.info("文件下载失败！");
			throw new RuntimeException("文件下载失败！");
		}
		log.info("主题要求：{},type={}，要求详情下载成功！",gdPlanDetail.getCreateBy(),gdPlanUploadFileDto.getType());
	}

	/**
	 * @description: TODO FTp删除原上传的文件
	 * @Param: sysFtpFileList 历史上传成功的文件数据，sysFtpFileErrList 历史上传失败的文件数据
	 * @author: tangxl
	 * @date: 2022年3月11日15:11:26
	 * @return: boolean 是否删除成功
	 */
	private boolean getObjectCommonResult(List<SysFtpFile> sysFtpFileList, List<SysFtpFileErr> sysFtpFileErrList) {
		String directoryName = "";
		if (!CollectionUtils.isEmpty(sysFtpFileList)) {
			directoryName = sysFtpFileList.get(0).getFilePath();
			directoryName = directoryName.substring(directoryName.lastIndexOf("/") + 1);
		} else if (!CollectionUtils.isEmpty(sysFtpFileErrList)) {
			directoryName = sysFtpFileErrList.get(0).getFilePath();
			directoryName = directoryName.substring(directoryName.lastIndexOf("/") + 1);
		}
		//删除之前上传的文件夹
		if (!ftpUtil.deleteDirectory(Constans.THEME_DETAIL_PATH, directoryName)) {
			return true;
		}
		//修改数据ftp文件状态
		if (!CollectionUtils.isEmpty(sysFtpFileList)) {
			sysFtpFileList.stream().forEach(o -> o.setState(0L));
			sysFtpFileDao.saveAll(sysFtpFileList);
		} else if (!CollectionUtils.isEmpty(sysFtpFileErrList)) {
			sysFtpFileErrList.stream().forEach(o -> o.setState(1L));
			sysFtpFileErrDao.saveAll(sysFtpFileErrList);
		}
		return false;
	}

	/**
	 * @description: TODO Ftp文件上传返回文件批次号
	 * @Param: files 文件集合，gdPlanUploadFileDto 参数，redisSysUser 用户信息
	 * @author: tangxl
	 * @date: 2022年3月11日15:05:59
	 * @return: String 批次号
	 */
	private String getBatchString(MultipartFile[] files, GdPlanUploadFileDTO gdPlanUploadFileDto, RedisSysUser redisSysUser) {
		//生成批次号
		String batch = EncryptConfigUtil.encryptPwd(
				DateUtil.dateToStrDate(new Date(), Constans.DEFAULT_DATE_TIME_FORMAT3) + redisSysUser.getPhone());
		//文件上传--文件夹名称为当前时间
		String filePath = Constans.THEME_DETAIL_PATH + "/" + DateUtil.dateToStrDate(new Date(), Constans.DEFAULT_DATE_TIME_FORMAT3);
		boolean flag = false;
		String msg = "";

		for (MultipartFile file : files) {
			try {
				log.info("上传开题报告要求{}{},数据ID:{}", filePath, file.getOriginalFilename(), gdPlanUploadFileDto.getId());
				flag = ftpUtil.uploadFile(filePath, file.getOriginalFilename(), file.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
				log.info("上传开题报告要求{}{}失败！设计主题ID:{}", filePath, file.getOriginalFilename(), gdPlanUploadFileDto.getId());
				msg = e.getMessage();
			} finally {
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
				} else {
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
					log.info("ftp上传错误文件信息保存：{}", sysFtpFileErr);
				}
			}
		}
		return batch;
	}
}
