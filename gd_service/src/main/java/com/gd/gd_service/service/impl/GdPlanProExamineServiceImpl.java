package com.gd.gd_service.service.impl;

import com.gd.base.constant.Constans;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.plan.GdPlanPaperScoreDTO;
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
import com.gd.base.pojo.vo.redis.RedisSysUser;
import com.gd.gd_service.service.GdPlanProExamineService;
import com.gd.gd_service.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: gd_plan
 * @description: TODO 毕业流程审核业务处理
 * @author: tangxl
 * @create: 2022-03-11 09:17
 */
@Service
@Slf4j
public class GdPlanProExamineServiceImpl implements GdPlanProExamineService {
	@Autowired
	private GdPlanProcessJdbc gdPlanProcessJdbc;
	@Autowired
	private RedisService redisService;
	@Autowired
	private FtpUtil ftpUtil;
	@Autowired
	private GdPlanDetailDao gdPlanDetailDao;
	@Autowired
	private SysFtpFileDao sysFtpFileDao;
	@Autowired
	private SysFtpFileErrDao sysFtpFileErrDao;

	/**
	 * @description: TODO 开题报告/中期检测的教师要求文件上传
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	@Override public CommonResult uploadRequireFile(String token, MultipartFile[] files, GdPlanUploadFileDTO gdPlanUploadFileDto) {
		RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
		if(BeanUtil.isEmpty(gdPlanUploadFileDto.getType())){
			return CommonResult.failed("来源类型type为空！");
		}
		if(StringUtils.isEmpty(gdPlanUploadFileDto.getOverTime())){
			return CommonResult.failed("截止时间为空！");
		}
		GdPlanDetail gdPlanDetail = gdPlanDetailDao.findByIdAndState(gdPlanUploadFileDto.getId(),1L);
		if(BeanUtil.isEmpty(gdPlanDetail)){
			return CommonResult.failed("数据不存在！");
		}
		//如果文件为空只更新时间--开题报告
		if((files==null || files.length == 0) && gdPlanUploadFileDto.getType().equals(1L)){
			gdPlanDetail.setOpenOverTime(DateUtil.stringToLocalDateTime(gdPlanUploadFileDto.getOverTime(), Constans.DEFAULT_DATE_TIME_FORMAT));
			gdPlanDetailDao.save(gdPlanDetail);
			return CommonResult.success("操作成功！");
		}
		//如果文件为空只更新时间--开题报告
		if((files==null || files.length == 0) && gdPlanUploadFileDto.getType().equals(2L)){
			gdPlanDetail.setMidOverTime(DateUtil.stringToLocalDateTime(gdPlanUploadFileDto.getOverTime(),Constans.DEFAULT_DATE_TIME_FORMAT));
			gdPlanDetailDao.save(gdPlanDetail);
			return CommonResult.success("操作成功！");
		}
		//如果文件为空只更新时间--开题报告
		if((files==null || files.length == 0) && gdPlanUploadFileDto.getType().equals(3L)){
			gdPlanDetail.setEndOverTime(DateUtil.stringToLocalDateTime(gdPlanUploadFileDto.getOverTime(),Constans.DEFAULT_DATE_TIME_FORMAT));
			gdPlanDetailDao.save(gdPlanDetail);
			return CommonResult.success("操作成功！");
		}
		//已上传过的需要删除原来上传的文件--开题报告
		if(gdPlanUploadFileDto.getType().equals(1L)) {
			if (gdPlanDetail.getOpenRequireState().equals(1L)) {
				//获取已文件
				List<SysFtpFile> sysFtpFileList = sysFtpFileDao.findAllByBatchAndState(gdPlanDetail.getOpenRequireBatch(), 1L);
				//获取上传失败文件
				List<SysFtpFileErr> sysFtpFileErrList = sysFtpFileErrDao.findAllByBatchAndState(gdPlanDetail.getOpenRequireBatch(), 0L);
				//删除原上传文件
				if (getObjectCommonResult(sysFtpFileList, sysFtpFileErrList)) {
					return CommonResult.failed("原文件删除失败！请联系管理员");
				}
				//修改报告信息为未上传状态
				gdPlanDetail.setOpenRequireBatch("");
				gdPlanDetail.setOpenRequireState(0L);
			}
			//开题文件上传
			//生成批次号
			String batch = getBatchString(files, gdPlanUploadFileDto, redisSysUser);
			//保存上传文件批次号
			gdPlanDetail.setOpenRequireBatch(batch);
			gdPlanDetail.setOpenRequireState(1L);
			gdPlanDetail.setOpenOverTime(DateUtil.stringToLocalDateTime(gdPlanUploadFileDto.getOverTime(),Constans.DEFAULT_DATE_TIME_FORMAT));
			gdPlanDetailDao.save(gdPlanDetail);
			return CommonResult.success("上传成功！");
		}
		//中期检测要求文件
		if (gdPlanUploadFileDto.getType().equals(2L)){
			if (gdPlanDetail.getMidRequireState().equals(1L)) {
				//获取已文件
				List<SysFtpFile> sysFtpFileList = sysFtpFileDao.findAllByBatchAndState(gdPlanDetail.getMidRequireBatch(), 1L);
				//获取上传失败文件
				List<SysFtpFileErr> sysFtpFileErrList = sysFtpFileErrDao.findAllByBatchAndState(gdPlanDetail.getMidRequireBatch(), 0L);
				//删除原上传文件
				if (getObjectCommonResult(sysFtpFileList, sysFtpFileErrList)) {
					return CommonResult.failed("原文件删除失败！请联系管理员");
				}
				//修改报告信息为未上传状态
				gdPlanDetail.setMidRequireBatch(null);
				gdPlanDetail.setMidRequireState(0L);
			}
			//文件上传
			String batch = getBatchString(files, gdPlanUploadFileDto, redisSysUser);
			//保存上传文件批次号
			gdPlanDetail.setMidRequireBatch(batch);
			gdPlanDetail.setMidRequireState(1L);
			gdPlanDetail.setMidOverTime(DateUtil.stringToLocalDateTime(gdPlanUploadFileDto.getOverTime(),Constans.DEFAULT_DATE_TIME_FORMAT));
			gdPlanDetailDao.save(gdPlanDetail);
			return CommonResult.success("上传成功！");
		}

		//毕业论文要求文件
		if (gdPlanUploadFileDto.getType().equals(3L)){
			if (gdPlanDetail.getEndRequireState().equals(1L)) {
				//获取已文件
				List<SysFtpFile> sysFtpFileList = sysFtpFileDao.findAllByBatchAndState(gdPlanDetail.getEndRequireBatch(), 1L);
				//获取上传失败文件
				List<SysFtpFileErr> sysFtpFileErrList = sysFtpFileErrDao.findAllByBatchAndState(gdPlanDetail.getEndRequireBatch(), 0L);
				//删除原上传文件
				if (getObjectCommonResult(sysFtpFileList, sysFtpFileErrList)) {
					return CommonResult.failed("原文件删除失败！请联系管理员");
				}
				//修改报告信息为未上传状态
				gdPlanDetail.setEndRequireBatch(null);
				gdPlanDetail.setEndRequireState(0L);
			}
			//文件上传
			String batch = getBatchString(files, gdPlanUploadFileDto, redisSysUser);
			//保存上传文件批次号
			gdPlanDetail.setEndRequireBatch(batch);
			gdPlanDetail.setEndRequireState(1L);
			gdPlanDetail.setEndOverTime(DateUtil.stringToLocalDateTime(gdPlanUploadFileDto.getOverTime(),Constans.DEFAULT_DATE_TIME_FORMAT));
			gdPlanDetailDao.save(gdPlanDetail);
			return CommonResult.success("上传成功！");
		}
		return CommonResult.failed("上传失败！");
	}

	/**
	 * @description: TODO 开题报告/中期检测/论文学生上传的的文件下载
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	@Override public void downReportFile(String token, GdPlanUploadFileDTO gdPlanUploadFileDto, HttpServletResponse response) {
		if(BeanUtil.isEmpty(gdPlanUploadFileDto.getId())){
			throw new RuntimeException("数据ID为空！");
		}
		if(BeanUtil.isEmpty(gdPlanUploadFileDto.getType())){
			throw new RuntimeException("来源类型type为空！");
		}
		//获取详情数据
		GdPlanDetail gdPlanDetail = gdPlanDetailDao.findByIdAndState(gdPlanUploadFileDto.getId(),1L);
		//开题报告下载
		if(gdPlanUploadFileDto.getType().equals(1L)){
			if(gdPlanDetail.getOpenReportState().equals(0L)){
				throw new RuntimeException("开题报告还未上传！");
			}
			List<SysFtpFile> sysFtpFileList = sysFtpFileDao.findAllByBatchAndState(gdPlanDetail.getOpenReportBatch(),1L);
			//文件下载
			extracted(response, gdPlanDetail, sysFtpFileList);
		}
		//中期检测文件下载
		if (gdPlanUploadFileDto.getType().equals(2L)) {
			if(gdPlanDetail.getMidReportState().equals(0L)){
				throw new RuntimeException("开题报告还未上传！");
			}
			List<SysFtpFile> sysFtpFileList = sysFtpFileDao.findAllByBatchAndState(gdPlanDetail.getMidReportBatch(),1L);
			//文件下载
			extracted(response, gdPlanDetail, sysFtpFileList);
		}
		//论文下载
		if (gdPlanUploadFileDto.getType().equals(3L)) {
			if(gdPlanDetail.getEndReportState().equals(0L)){
				throw new RuntimeException("开题报告还未上传！");
			}
			List<SysFtpFile> sysFtpFileList = sysFtpFileDao.findAllByBatchAndState(gdPlanDetail.getEndReportBatch(),1L);
			//文件下载
			extracted(response, gdPlanDetail, sysFtpFileList);
		}

	}

	/**
	 * @description: TODO 开题报告/中期检测/免答辩申请 学生上传的的文件退回
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	@Override public CommonResult updateReportReturn(String token, GdPlanUploadFileDTO gdPlanUploadFileDto) {
		if(BeanUtil.isEmpty(gdPlanUploadFileDto.getType())){
			return CommonResult.failed("操作来源未知！");
		}
		if(BeanUtil.isEmpty(gdPlanUploadFileDto.getId())){
			return CommonResult.failed("数据ID未知！");
		}
		//获取详情数据
		GdPlanDetail gdPlanDetail = gdPlanDetailDao.findByIdAndState(gdPlanUploadFileDto.getId(), 1L);
		//退回修改审核状态为审核失败
		if(gdPlanUploadFileDto.getType().equals(1L)){
			if(gdPlanDetail.getOpenExamineState().equals(2L)){
				return CommonResult.failed("不可退回！");
			}
			gdPlanDetail.setOpenExamineState(2L);
		}
		if (gdPlanUploadFileDto.getType().equals(2L)) {
			if(gdPlanDetail.getMidExamineState().equals(2L)){
				return CommonResult.failed("不可退回！");
			}
			gdPlanDetail.setMidExamineState(2L);
		}
		if(gdPlanUploadFileDto.getType().equals(4L)){
			if(gdPlanDetail.getExemptDefenseState().equals(2L)){
				return CommonResult.failed("不可退回！");
			}
			gdPlanDetail.setExemptDefenseState(2L);
		}
		gdPlanDetailDao.save(gdPlanDetail);
		return CommonResult.success("操作成功！");
	}

	/**
	 * @description: TODO 开题报告/中期检测/免答辩申请 三个流程通过
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	@Override public CommonResult updateReportPass(String token, GdPlanUploadFileDTO gdPlanUploadFileDto) {
		if(BeanUtil.isEmpty(gdPlanUploadFileDto.getType())){
			return CommonResult.failed("操作来源未知！");
		}
		if(BeanUtil.isEmpty(gdPlanUploadFileDto.getId())){
			return CommonResult.failed("数据ID未知！");
		}
		//获取详情数据
		GdPlanDetail gdPlanDetail = gdPlanDetailDao.findByIdAndState(gdPlanUploadFileDto.getId(), 1L);
		//通过修改审核状态为审核成功
		if(gdPlanUploadFileDto.getType().equals(1L)){
			gdPlanDetail.setOpenExamineState(1L);
		}else if (gdPlanUploadFileDto.getType().equals(2L)) {
			gdPlanDetail.setMidExamineState(1L);
		}else if(gdPlanUploadFileDto.getType().equals(4L)){
			gdPlanDetail.setExemptDefenseState(1L);
		}else {
			return CommonResult.failed("操作来源错误！");
		}
		gdPlanDetailDao.save(gdPlanDetail);
		return CommonResult.success("操作成功！");
	}

	/**
	 * @description: TODO 毕业论文打分处理
	 * @Param:
	 * @author: tangxl
	 * @date:
	 * @return:
	 */
	@Override public CommonResult updateReportScore(String token, GdPlanPaperScoreDTO gdPlanPaperScoreDto) {
		if(BeanUtil.isEmpty(gdPlanPaperScoreDto.getId())){
			return CommonResult.failed("数据ID未知！");
		}
		if(StringUtils.isEmpty(gdPlanPaperScoreDto.getInstructorScore())||StringUtils.isEmpty(gdPlanPaperScoreDto.getDefenseGroupScore())){
			return CommonResult.failed("必须两个成绩一起提交！");
		}
		//指导老师分数
		BigDecimal teacherScore = new BigDecimal(gdPlanPaperScoreDto.getInstructorScore());
		//答辩组分数
		BigDecimal groupScore = new BigDecimal(gdPlanPaperScoreDto.getDefenseGroupScore());
		if(teacherScore.compareTo(new BigDecimal("0"))==-1 || teacherScore.compareTo(new BigDecimal("100"))==1){
			return CommonResult.failed("请修改指导老师分数(0~100)!");
		}
		if(groupScore.compareTo(new BigDecimal("0"))==-1 || groupScore.compareTo(new BigDecimal("100"))==1){
			return CommonResult.failed("请修改答辩组分数(0~100)!");
		}
		//获取详情数据
		GdPlanDetail gdPlanDetail = gdPlanDetailDao.findByIdAndState(gdPlanPaperScoreDto.getId(), 1L);
		//指导老师分数占30%，答辩组分数占70%
		BigDecimal sum = (teacherScore.multiply(new BigDecimal("0.4"))).add(
			groupScore.multiply(new BigDecimal("0.6"))
		);
		//90~100 优秀、80~90 良好、70~80 中等、60~70 及格、<60 不及格
		if(sum.compareTo(new BigDecimal("100"))<=0 && sum.compareTo(new BigDecimal("90"))>=0){
			gdPlanDetail.setScore("优秀");
		}
		if(sum.compareTo(new BigDecimal("90"))==-1 && sum.compareTo(new BigDecimal("80"))>=0){
			gdPlanDetail.setScore("良好");
		}
		if(sum.compareTo(new BigDecimal("80"))==-1 && sum.compareTo(new BigDecimal("70"))>=0){
			gdPlanDetail.setScore("中等");
		}
		if(sum.compareTo(new BigDecimal("70"))==-1 && sum.compareTo(new BigDecimal("60"))>=0){
			gdPlanDetail.setScore("及格");
		}
		if(sum.compareTo(new BigDecimal("60"))==-1){
			gdPlanDetail.setScore("不及格");
		}
		//保存成绩
		gdPlanDetail.setInstructorScore(gdPlanPaperScoreDto.getInstructorScore());
		gdPlanDetail.setDefenseGroupScore(gdPlanPaperScoreDto.getDefenseGroupScore());
		gdPlanDetailDao.save(gdPlanDetail);
		return CommonResult.success("提交成功！");
	}

	/**
	 * @description: TODO ftp上传的文件下载
	 * @Param: response http响应，gdPlanDetail 详情参数，sysFtpFileList ftp文件数据集合
	 * @author: tangxl
	 * @date: 2022年3月11日15:28:30
	 * @return: void
	 */
	private void extracted(HttpServletResponse response, GdPlanDetail gdPlanDetail, List<SysFtpFile> sysFtpFileList) {
		if (CollectionUtils.isEmpty(sysFtpFileList)) {
			log.info("此数据ID{} 未查出Ftp上传成功的文件！", gdPlanDetail.getId());
			throw new RuntimeException("详情下载失败！请联系管理员");
		}
		String filePath = sysFtpFileList.get(0).getFilePath();
		List<String> fileNameList = sysFtpFileList.stream().map(o -> o.getFileName()).collect(Collectors.toList());
		try {
			//设置文件ContentType类型，这样设置，会自动判断下载文件类型
			response.setContentType("application/octet-stream");
			response.setCharacterEncoding("UTF-8");
			//设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)
			response.setHeader("Content-Disposition", "attachment;filename=" + new String((gdPlanDetail.getCreateBy()+".zip").getBytes(
					StandardCharsets.UTF_8),"iso-8859-1"));
			ftpUtil.downZipFile(filePath, fileNameList, response);
			log.info("获取文件流成功！{}", response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
			log.info("文件下载失败！");
			throw new RuntimeException("文件下载失败！");
		}
		log.info("设计主题：{}，主题详情下载成功！", gdPlanDetail.getCreateBy());
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
