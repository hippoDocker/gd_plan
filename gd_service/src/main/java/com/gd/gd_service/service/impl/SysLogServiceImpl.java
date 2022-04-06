package com.gd.gd_service.service.impl;

import com.alibaba.fastjson.JSON;
import com.gd.base.entity.GdDesignTheme;
import com.gd.base.entity.SysInterfaceLog;
import com.gd.base.jdbc.SysLogJdbc;
import com.gd.base.jpa.GdDesignThemeDao;
import com.gd.base.jpa.HistoryDesignThemeDao;
import com.gd.base.jpa.SysInterfaceLogDao;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.DataCodeDTO;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.HistoryDesignThemeDTO;
import com.gd.base.pojo.vo.sys.ParticipleCountVO;
import com.gd.base.pojo.dto.sys.ParticipleResponseDTO;
import com.gd.base.pojo.dto.sys.SysInterfaceLogDTO;
import com.gd.base.pojo.dto.sys.SysUserLoginLogDTO;
import com.gd.base.pojo.vo.plan.HistoryDesignThemeVO;
import com.gd.base.pojo.vo.redis.RedisSysUser;
import com.gd.base.pojo.vo.sys.SysInterfaceLogVO;
import com.gd.base.pojo.vo.sys.SysInterfaceReportVO;
import com.gd.base.pojo.vo.sys.SysUserLoginLogVO;
import com.gd.base.util.BeanUtil;
import com.gd.base.util.DateUtil;
import com.gd.base.util.HttpClientUtil;
import com.gd.gd_service.service.RedisService;
import com.gd.gd_service.service.SysLogService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: tangxl
 * @Date: 2021年12月10日16:47:05
 * @Description:日志保存
 */
@Service
@Log4j2
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysInterfaceLogDao sysInterfaceLogDao;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SysLogJdbc sysLogJdbc;
    @Autowired
    private GdDesignThemeDao gdDesignThemeDao;
    @Autowired
    private HistoryDesignThemeDao historyDesignThemeDao;

    @Override
    public void addsysInterfaceLog(HttpServletRequest request, Object[] args) throws IOException {
        //保存日志
        SysInterfaceLog sysInterfaceLog = new SysInterfaceLog();
        //获取请求方式
        sysInterfaceLog.setMethod(request.getMethod());
        //获取接口
        sysInterfaceLog.setInterfaceUrl(request.getRequestURI());

        //请求参数--处理无法获取HttpServletRequest参数
        try{
            sysInterfaceLog.setParam(JSON.toJSONString(args));
        }catch (Exception e) {
            log.info("当前接口有HttpServletRequest参数，无法获取接口参数！");
            sysInterfaceLog.setParam("['HttpServletRequest':'request']");
        }
        //设置调用时间
        sysInterfaceLog.setCreateTime(DateUtil.dateToLocalDateTime(new Date()));
        //获取用户数据
        RedisSysUser redisSysUser = redisService.getSysUserByToken(request.getHeader("token"));
        //获取用户名和学号和电话
        if(BeanUtil.isEmpty(redisSysUser)){
            sysInterfaceLog.setUserCode("**********");
            sysInterfaceLog.setUserName("***");
            sysInterfaceLog.setPhone("************");
        }else {
            sysInterfaceLog.setUserCode(redisSysUser.getUserCode());
            sysInterfaceLog.setUserName(redisSysUser.getUserName());
            sysInterfaceLog.setPhone(redisSysUser.getPhone());
        }
        //获取用户ip地址
        String ip = getIpAddress(request);
        log.info("访问IP：{}",ip);
        sysInterfaceLog.setIp(ip);
        //保存sysInterfaceLog实体类到数据库
        sysInterfaceLog = sysInterfaceLogDao.save(sysInterfaceLog);
        Long sysInterfaceLogCount = sysInterfaceLogDao.count();
        log.info("=====>>记录接口请求日志:"+sysInterfaceLog);
        log.info("=====>>累计记录系统日志"+sysInterfaceLogCount+"条");


    }

    /**
     * @description: TODO 用户登录日志分页查询
     * @Param: SysUserLoginLogDto
     * @return: SysUserLoginLogVo
     * @author: tangxl
     * @date: 2022年3月1日16:20:36
     */
    @Override
    public CommonResult findLoginLogPage(String token, SysUserLoginLogDTO sysUserLoginLogDto, PageBaseInfo page) {
        PageBaseInfo<SysUserLoginLogVO> sysUserLoginLogVoPage = sysLogJdbc.findLoginLogPage(sysUserLoginLogDto,page);
        return CommonResult.success(sysUserLoginLogVoPage);
    }

    /**
     * @description: TODO 接口日志分页查询
     * @Param: SysInterfaceLogDto
     * @author: tangxl
     * @date: 2022年3月1日16:25:51
     * @return: SysInterfaceLogVo
     */
    @Override
    public CommonResult findInterfaceLogPage(String token, SysInterfaceLogDTO sysInterfaceLogDto, PageBaseInfo page) {
        PageBaseInfo<SysInterfaceLogVO> sysInterfaceLogVoPage = sysLogJdbc.findInterfaceLogPage(sysInterfaceLogDto,page);
        return CommonResult.success(sysInterfaceLogVoPage);
    }

    /**
     * @description: TODO 接口图形化报表数据处理
     * @Param:
     * @author: tangxl
     * @date:
     * @return:
     */
    @Override public CommonResult findInterfaceReport(String token, SysInterfaceLogDTO sysInterfaceLogDto) {
        if(StringUtils.isEmpty(sysInterfaceLogDto.getCreateTimeStart())){
            return CommonResult.failed("开始时间为空！");
        }
        if(StringUtils.isEmpty(sysInterfaceLogDto.getCreateTimeEnd())){
            return CommonResult.failed("结束时间为空！");
        }
        List<SysInterfaceReportVO> sysInterfaceReportVoList = sysLogJdbc.findInterfaceReport(sysInterfaceLogDto);
        return CommonResult.success(sysInterfaceReportVoList,"查询成功！");
    }

    /**
     * @description: TODO 设计主题词频统计
     * @return:
     * @author: tangxl
     * @time:  19:02
     */
    @Override
    public CommonResult findParticipleCount(Integer amount) {
        List<GdDesignTheme> gdDesignThemeList = gdDesignThemeDao.findAllByState(1L);
        StringBuilder allTheme = new StringBuilder("");
        gdDesignThemeList.stream().forEach(o->{
            allTheme.append(o.getThemeName());
        });
        //调用智能分词接口
        String getUrl= "http://api.pullword.com/get.php";//get请求路径
        String postUrl = "http://api.pullword.com/post.php";//Post请求路径
        Map params = new LinkedHashMap();
        params.put("source",allTheme);//分词语句
        params.put("param1",0.5);//保留准确概率(0~1之间的小数)
        params.put("param2",0);//调试模式:param2 = 0调试模式关闭,param2 = 1调试模式打开(显示每个单词的准确概率)
        params.put("json",1);//可选参数:json = 1以json格式返回,json = 0不以json格式返回
        String res = null;
        try {
            res = HttpClientUtil.getNetData(getUrl,params,"GET");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<ParticipleResponseDTO> responseDTO = JSON.parseArray(res, ParticipleResponseDTO.class);
        Map<String,Long> map = new HashMap<>();
        //智能词频统计
        responseDTO.stream().forEach(o->{
            if(map.containsKey(o.getT())){
                map.put(o.getT(),map.get(o.getT())+1L);
            }else {
                map.put(o.getT(), 1L);
            }
        });
        List<ParticipleCountVO> ParticipleCountVOList = new ArrayList<>();
        for(Map.Entry<String,Long> entry:map.entrySet()){
            ParticipleCountVOList.add(new ParticipleCountVO(entry.getKey(), entry.getValue()));
        }
        //排序
        ParticipleCountVOList =  ParticipleCountVOList.stream().sorted(Comparator.comparing(ParticipleCountVO::getValue).reversed()).limit(amount).collect(Collectors.toList());
        return CommonResult.success(ParticipleCountVOList,"统计成功！");
    }

    /**
     * @description: TODO 查询历年设计主题推荐
     * @return:
     * @author: tangxl
     * @time:  21:40
     */
    @Override
    public CommonResult findHistoryDesignTheme(HistoryDesignThemeDTO historyDesignThemeDTO,PageBaseInfo page) {
        PageBaseInfo<HistoryDesignThemeVO> historyDesignThemeVOS = sysLogJdbc.findHistoryDesignTheme(historyDesignThemeDTO,page);
        return CommonResult.success(historyDesignThemeVOS,"查询成功！");
    }

    /**
     * @description: TODO 获取历史主题类型
     * @return:
     * @author: tangxl
     * @time:  8:58
     */
    @Override
    public CommonResult findHistoryType() {
        List<DataCodeDTO> dataCodeDTOS = sysLogJdbc.findHistoryType();
        return CommonResult.success(dataCodeDTOS,"查询成功！");
    }

    /**
     * @Description TODO:获取IP
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
         String ip = request.getHeader("x-forwarded-for");
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                 ip = request.getHeader("Proxy-Client-IP");
             }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                 ip = request.getHeader("WL-Proxy-Client-IP");
             }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                 ip = request.getHeader("HTTP_CLIENT_IP");
             }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                 ip = request.getHeader("HTTP_X_FORWARDED_FOR");
             }
         if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                 ip = request.getRemoteAddr();
             }
         return ip;
        }
}
