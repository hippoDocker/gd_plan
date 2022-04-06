package com.gd.gd_service.controller;

import com.alibaba.fastjson.JSON;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.HistoryDesignThemeDTO;
import com.gd.base.pojo.vo.sys.ParticipleCountVO;
import com.gd.base.pojo.dto.sys.SysInterfaceLogDTO;
import com.gd.base.pojo.dto.sys.SysTalkDTO;
import com.gd.base.pojo.dto.sys.SysUserLoginLogDTO;
import com.gd.base.pojo.vo.plan.HistoryDesignThemeVO;
import com.gd.base.pojo.vo.sys.SysInterfaceLogVO;
import com.gd.base.pojo.vo.sys.SysInterfaceReportVO;
import com.gd.base.pojo.vo.sys.SysUserLoginLogVO;
import com.gd.base.util.HttpClientUtil;
import com.gd.gd_service.config.TalkException;
import com.gd.gd_service.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: gd_plan
 * @description: TODO 日志相关接口控制层
 * @author: tangxl
 * @create: 2022-03-01 15:35
 */
@RestController
@RequestMapping("/sys")
@Api(value = "日志管理",tags = {"日志管理+AI实验室" })
@Slf4j
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;

	@PostMapping("/findLoginLogPage")
	@ApiOperation(value = "登录日志分页查询",notes = "登录日志分页查询--可选条件：用户姓名、登录的开始时间和结束时间,分页大小(pageSize),分页页码(pageNo)")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！", response = SysUserLoginLogVO.class)
	})
	public CommonResult findLoginLogPage(@RequestHeader("token") String  token, @RequestBody SysUserLoginLogDTO sysUserLoginLogDto){
		return sysLogService.findLoginLogPage(token,sysUserLoginLogDto,
				new PageBaseInfo(sysUserLoginLogDto.getPageNo(),sysUserLoginLogDto.getPageSize(),Boolean.FALSE));
	}

	@PostMapping("/findInterfaceLogPage")
	@ApiOperation(value = "接口日志分页查询" ,notes = "接口日志分页查询--可选条件：学号、姓名、接口、ip地址、接口调用的开始时间和结束时间,分页大小(pageSize),分页页码(pageNo)")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！", response = SysInterfaceLogVO.class)
	})
	public CommonResult findInterfaceLogPage(@RequestHeader("token") String  token, @RequestBody SysInterfaceLogDTO sysInterfaceLogDto){
		return sysLogService.findInterfaceLogPage(token,sysInterfaceLogDto,
				new PageBaseInfo(sysInterfaceLogDto.getPageNo(),sysInterfaceLogDto.getPageSize(),Boolean.FALSE));
	}

	@PostMapping("/findInterfaceReport")
	@ApiOperation(value = "日志图形化报表数据" ,notes = "日志图形化报表数据--条件：开始时间(createTimeStart),结束时间(createTimeEnd)")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！", response = SysInterfaceReportVO.class)
	})
	public CommonResult findInterfaceReport(@RequestHeader("token") String  token, @RequestBody SysInterfaceLogDTO sysInterfaceLogDto){
		return sysLogService.findInterfaceReport(token,sysInterfaceLogDto);
	}

	@CrossOrigin
	@GetMapping("/talk")
	@ApiOperation(value = "智能聊天机器人API" ,notes = "智能聊天机器人API:支持人工智能聊天、歌词、天气、翻译、IP查询、笑话、计算、手机号码归属地查询、成语查询、五笔/拼音" +
			",设置超时90秒自动回复：小智脑子没有转过来，你重新问个问题吧！")
	public CommonResult getTalk(@RequestParam(name = "msg") String msg) throws TalkException {
		log.info("请求内容：" + msg);
		String url = "http://api.qingyunke.com/api.php";
		Map param = new HashMap();  // 配置
		param.put("key","free");
		param.put("appid","0");
		param.put("msg",msg);
		SysTalkDTO sysTalkDTO = new SysTalkDTO();
		try {
			String res = HttpClientUtil.getNetData(url, param, "GET");     // 执行请求
			sysTalkDTO = JSON.parseObject(res,SysTalkDTO.class);
			if(StringUtils.isNotEmpty(sysTalkDTO.getContent()) || sysTalkDTO.getContent().contains("菲菲")){
				sysTalkDTO.setContent(sysTalkDTO.getContent().replaceAll("菲菲", "小智"));
			}
			//sysTalkDTO.setContent(sysTalkDTO.getContent().replaceAll("\\{br}", ""));
		}catch (Exception e) {
		    msg = "小智脑子没有转过来，你换个问题试试吧！";
			log.info("回答内容:"+msg);
			throw new TalkException(msg);
		}

		log.info("回答内容:"+sysTalkDTO);
		return CommonResult.success(sysTalkDTO);
	}

	@PostMapping("/findParticipleCount")
	@ApiOperation(value = "设计主题词频统计" ,notes = "设计主题词频统计--条件：词个数(amount)")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！", response = ParticipleCountVO.class)
	})
	public CommonResult findParticipleCount(@RequestParam Integer amount){
		if(null == amount){
			amount = 50;
		}
		return sysLogService.findParticipleCount(amount);
	}

	@PostMapping("/findHistoryDesignTheme")
	@ApiOperation(value = "历年设计主题推荐" ,notes = "历年设计主题推荐--条件专业类型(type),通过接口获取")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！", response = HistoryDesignThemeVO.class)
	})
	public CommonResult findHistoryDesignTheme(@RequestBody HistoryDesignThemeDTO historyDesignThemeDTO){

		return sysLogService.findHistoryDesignTheme(historyDesignThemeDTO,
				new PageBaseInfo(historyDesignThemeDTO.getPageNo(),historyDesignThemeDTO.getPageSize(),Boolean.FALSE));
	}

	@PostMapping("/findHistoryType")
	@ApiOperation(value = "历年设计主题推荐专业类型" ,notes = "历年设计主题推荐专业类型--条件专业类型(type),通过接口获取")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！", response = HistoryDesignThemeVO.class)
	})
	public CommonResult findHistoryType(){

		return sysLogService.findHistoryType();
	}
}
