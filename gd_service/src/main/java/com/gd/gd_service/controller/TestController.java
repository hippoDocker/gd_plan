package com.gd.gd_service.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gd.base.entity.SysRole;
import com.gd.base.entity.SysUserRole;
import com.gd.base.jpa.*;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysUserAddDTO;
import com.gd.base.pojo.dto.sys.SysUserImportDTO;
import com.gd.base.pojo.vo.redis.RedisSysUser;
import com.gd.base.pojo.vo.sys.SysUserExportVO;
import com.gd.base.util.BeanUtil;
import com.gd.base.util.EasyExcelUtils;
import com.gd.base.util.FtpUtil;
import com.gd.base.util.HttpClientUtil;
import com.gd.gd_service.listener.ImportUserListener;
import com.gd.gd_service.service.RedisService;
import com.gd.gd_service.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: tangxl
 * @Date: 2021年12月23日10:44:17
 * @Description:  TODO Ftp测试接口
 */
@RestController
@Log4j2
@Api(value = "测试接口汇总",tags = "测试接口汇总")
@RequestMapping("/sys/test")
public class TestController {

    @Autowired
    private FtpUtil ftpUtil;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysSchoolDao sysSchoolDao;
    @Autowired
    private SysMajorDao sysMajorDao;
    @Autowired
    private SysCollegeDao sysCollegeDao;
    @Autowired
    private SysClassDao sysClassDao;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;
    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private RedisService redisService;
    @Autowired
    private GdDesignThemeDao gdDesignThemeDao;
    @Autowired
    private SysFtpFileDao sysFtpFileDao;

    @PostMapping(value = "/ftp/upload")
    @ApiOperation("ftp文件上传")
    public CommonResult tesstUpdoade(@RequestParam("file") MultipartFile file,String ftpPath, HttpServletRequest request)  {
        boolean flag = false;
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            flag = ftpUtil.uploadFile(ftpPath,file.getOriginalFilename(),inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败！",e);
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return CommonResult.success("上传返回："+flag);

    }


    @PostMapping(value = "/ftp/down")
    @ApiOperation("ftp文件下载")
    public void down(String fileName,String ftpPath, HttpServletResponse response) {
        //从Ftp上读取文件放入响应中
        InputStream inputStream = null;
        ftpUtil.downloadFile(ftpPath,fileName,response);
    }


    @GetMapping("/excel/Export")
    @ApiOperation(value = "导出所有用户",notes = "用户查询条件：用户学号（userCode）,用户姓名（userName）,用户邮箱（email）,用户手机号码（phone）,用户出生日期范围开始时间和结束数据（birthdayStart，birthdayEnd）")
    public void findAllUserExport(SysUserAddDTO sysUserAddDto, HttpServletResponse response){
        List<SysUserExportVO> sysUserExportVOList = sysUserService.findUserExport(new String(""),sysUserAddDto,new PageBaseInfo(sysUserAddDto.getPageNo(),sysUserAddDto.getPageSize(),true));
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("用户信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String((fileName + ".xlsx").getBytes(
                    StandardCharsets.UTF_8),"iso-8859-1"));
            //写Excel
            EasyExcel.write(response.getOutputStream(), SysUserExportVO.class ).sheet().doWrite(sysUserExportVOList);
        }catch (Exception e) {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>();
            map.put("status", "500");
            map.put("message", "下载文件失败" + e.getMessage());
            try {
                response.getWriter().println(JSON.toJSONString(map));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @GetMapping("/excel/excelTempDown")
    @ApiOperation(value = "批量用户导入模板下载")
    public void addSomeUserTemp(HttpServletResponse response){
        try {
            String path = "/static/excel/批量添加用户信息模板.xlsx" ;
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            EasyExcelUtils.downExcelTemplate(response, path, fileName,this.getClass());
        }catch (Exception e){
            log.error(e);
        }
    }

    @PostMapping("/excel/addSomeUser")
    @ApiOperation(value = "批量用户导入")
    @Transactional(rollbackOn = Exception.class)
    public CommonResult addSomeUser(@RequestParam(("file")) MultipartFile file,String token) throws IOException {
        //只有管理员才能添加用户
        RedisSysUser redisSysUser = redisService.getSysUserByToken(token);
        if(BeanUtil.isEmpty(redisSysUser)){
            return CommonResult.failed("用户未登录！");
        }
        SysUserRole sysUserRole = sysUserRoleDao.findByUserIdAndState(redisSysUser.getUserId(),1L);
        SysRole sysRole = sysRoleDao.findByRoleIdAndState(sysUserRole.getRoleId(),1L);
        if(!"Admin".equals(sysRole.getRoleCode())){
            return CommonResult.failed("只有超级管理可以添加用户！");
        }
        CommonResult result = new CommonResult();
        boolean flag = false;
        // 限制文件类型只能为xlsx或者xls
        if (!EasyExcelUtils.isExcelFile(file)) {
            flag = true;
            return CommonResult.failed("文件类型出错,请上传正确的文件!");
        }
        //读取文件,保存用户
        EasyExcel.read(file.getInputStream(), SysUserImportDTO.class, new ImportUserListener(sysUserService,result, token,
                        sysSchoolDao,sysMajorDao,sysCollegeDao,sysClassDao,sysRoleDao)).doReadAllSync();
        return CommonResult.success("导入用户成功！");
    }

    /**
     * 跨域对话机器人请求
     * 青云客：http://api.qingyunke.com/
     * 接口：http://api.qingyunke.com/api.php?key=free&appid=0&msg=关键词
     */
    @CrossOrigin
    @GetMapping("/talk")
    public Map getTalk( @RequestParam(name = "msg") String msg) throws Exception{
        System.out.println("请求内容：" + msg);
        String url = "http://api.qingyunke.com/api.php";
        Map param = new HashMap();  // 配置
        param.put("key","free");
        param.put("appid","0");
        param.put("msg",msg);
        String res = HttpClientUtil.getNetData(url, param, "GET");     // 执行请求
        JSONObject object = JSONObject.parseObject(res);
        Map map = object.getInnerMap();    // 结果集转map
        return map;
    }



}
