package com.gd.gd_service.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.gd.base.entity.SysRole;
import com.gd.base.entity.SysUserRole;
import com.gd.base.jpa.*;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysChangeUserRoleAndPwdDTO;
import com.gd.base.pojo.dto.sys.SysUserAddDTO;
import com.gd.base.pojo.dto.sys.SysUserImportDTO;
import com.gd.base.pojo.vo.redis.RedisSysUser;
import com.gd.base.pojo.vo.sys.SysUserExportVO;
import com.gd.base.util.BeanUtil;
import com.gd.base.util.EasyExcelUtils;
import com.gd.base.util.FileUtils;
import com.gd.gd_service.listener.ImportUserListener;
import com.gd.gd_service.service.RedisService;
import com.gd.gd_service.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Auther: tangxl
 * @Date: 2022年2月11日15:34:40
 * @Description: 系统用户管理
 */
@RestController
@RequestMapping("/sys")
@Api(value = "系统用户信息",tags = "系统用户信息")
@Log4j2
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;
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

    @PostMapping("/addOneUser")
    @ApiOperation(value = "新增一个用户",notes = "学生--账号：手机号/学号，密码：默认学号后六位(如果学号不够6位，默认123456)；  "
            + "教师--账号：手机号，密码：默认123456;   "
            + "管理员--账号：手机号/学号(学号可随意)，密码：默认admin   "
            + "出生日期(birthday)格式：yyyy-MM-dd")
    @ApiResponses({
            @ApiResponse(code = 200, message = "添加成功！",response = SysUserAddDTO.class)
    })
    public CommonResult addOneUser(@RequestHeader String token, @RequestBody SysUserAddDTO sysUserAddDto){
        return sysUserService.addOneUser(token,sysUserAddDto);
    }

    @GetMapping("/addSomeUserTemp")
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

    @PostMapping("/addSomeUser")
    @ApiOperation(value = "批量用户导入",notes = "学生--账号：手机号/学号，密码：默认学号后六位(如果学号不够6位，默认123456)；  教师--账号：手机号，密码：默认123456;   管理员--账号：手机号/学号(学号可随意)，密码：默认admin")
    public CommonResult addSomeUser(@RequestHeader String token,
                                    @RequestParam(value = "file", required = true) MultipartFile file){
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
        try {
            EasyExcel.read(file.getInputStream(), SysUserImportDTO.class, new ImportUserListener(sysUserService,result, token,
                    sysSchoolDao,sysMajorDao,sysCollegeDao,sysClassDao,sysRoleDao)).doReadAllSync();
        } catch (IOException e) {
            e.printStackTrace();
            return CommonResult.failed("导入用户失败！");
        }
        return CommonResult.success("导入用户成功！");
    }

    @GetMapping("/findHeadImg")
    @ApiOperation(value = "获取用户头像",notes = "根据返回的用户信息的地址获取用户头像")
    public ResponseEntity<byte[]> findHeadImg( String phone)  {
        String token= "";
        //初始化header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //获取头像
        sysUserService.findHeadImg(token,phone,baos);
        byte[] bs = baos.toByteArray();
        try{
            baos.close();
        }catch (Exception e){
            log.info("头像获取失败：{}",e);
            throw new RuntimeException("头像获取失败：{}",e);
        }
        return new ResponseEntity<>(bs,headers, HttpStatus.OK);
    }

    @PostMapping("/findAllUser")
    @ApiOperation(value = "用户分页查询",notes = "用户分页查询：当前页码（pageNo）,每页条数（pageSize）,用户学号（userCode）,用户姓名（userName）," +
            " 用户邮箱（email）,用户手机号码（phone）,用户出生日期范围开始时间和结束数据（birthdayStart，birthdayEnd）,学院（collegeName），" +
            "专业（majorName），班级（className）,角色（roleName）,分页大小(pageSize),分页页码(pageNo)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "查询成功！",response = SysUserExportVO.class)
    })
    public CommonResult findAllUser(@RequestHeader String token,@RequestBody SysUserAddDTO sysUserAddDto){
        PageBaseInfo<SysUserExportVO> sysUserPage =  sysUserService.findUserPage(token,sysUserAddDto,new PageBaseInfo(sysUserAddDto.getPageNo(),sysUserAddDto.getPageSize(),false));
        return CommonResult.success(sysUserPage,"查询成功！");
    }

    @PostMapping("/findAllUserExport")
    @ApiOperation(value = "导出所有用户",notes = "用户查询条件：用户学号（userCode）,用户姓名（userName）,用户邮箱（email）,用户手机号码（phone）,用户出生日期范围开始时间和结束数据（birthdayStart，birthdayEnd）")
    public void findAllUserExport(@RequestHeader String token, @RequestBody SysUserAddDTO sysUserAddDto, HttpServletResponse response){
        List<SysUserExportVO> sysUserExportVOList = sysUserService.findUserExport(token,sysUserAddDto,new PageBaseInfo(sysUserAddDto.getPageNo(),sysUserAddDto.getPageSize(),true));
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
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "500");
            map.put("message", "下载文件失败" + e.getMessage());
            try {
                response.getWriter().println(JSON.toJSONString(map));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @PostMapping("/addUserHead")
    @ApiOperation(value = "修改用户头像",notes = "修改用户头像:传一个图片文件(file)支持格式(image png jpg jpeg bmp)")
    public CommonResult addUserHead(@RequestHeader String token,@RequestParam(value = "file") MultipartFile file){
        List<String> fileTypes = new ArrayList<String>(Arrays.asList(".image",".png",".jpg",".jpeg",".bmp"));
        if(!FileUtils.checkFileFormat(file,fileTypes)){
            return CommonResult.failed("头像格式错误！");
        }
        return sysUserService.addUserHead(token,file);
    }


    @PostMapping("/updateUserPwd")
    @ApiOperation(value = "修改用户密码",notes = "修改用户密码:密码6~15位，必须是6-20位的字母、数字、下划线（这里字母、数字、下划线是指任意组合，没有必须三类均包含）" +
            ",参数用户ID(userId)和用户新密码(userPwd)")
    public CommonResult updateUserPwd(@RequestHeader String token,@RequestBody SysChangeUserRoleAndPwdDTO UserDto){
        return sysUserService.updateUserPwd(token,UserDto);
    }

    @PostMapping("/updateUserRole")
    @ApiOperation(value = "修改用户绑定角色权限",notes = "修改用户绑定角色权限:角色信息通过/api/role/findCodeDataRole接口获取,可以采取下拉框选择,参数用户ID(userId)，角色ID(roleId)" +
            ",参数用户ID(userId)和角色ID(roleId)")
    public CommonResult updateUserRole(@RequestHeader String token,@RequestBody SysChangeUserRoleAndPwdDTO UserDto){
        return sysUserService.updateUserRole(token,UserDto);
    }

    @PostMapping("/updateUserDetail")
    @ApiOperation(value = "修改用户信息",notes = "修改用户信息:可修改信息包含用户姓名、姓名、手机号码、邮箱、学历、性别、专业、学院、班级)，" +
            "学院专业这些可以通过data-code接口获取，传对应的code给我,参数包含用户ID(userId),其他的查看下面对应字段名")
    public CommonResult updateUserDetail(@RequestHeader String token,@RequestBody SysUserAddDTO UserDto){
        return sysUserService.updateUserDetail(token,UserDto);
    }

    @PostMapping("/updateUserState")
    @ApiOperation(value = "用户解锁",notes = "用户解锁:根据用户ID(userId)解锁,如果用户锁定将用户恢复正常状态(清除用户密码错误次数+将用户状态修改为正常)")
    public CommonResult updateUserState(@RequestHeader String token,@RequestBody SysChangeUserRoleAndPwdDTO UserDto){
        return sysUserService.updateUserState(token,UserDto);
    }

    @PostMapping("/deleteUser")
    @ApiOperation(value = "删除用户",notes = "删除用户:根据用户ID(userId)删除用户，状态改为无效")
    public CommonResult deleteUser(@RequestHeader String token,@RequestBody SysChangeUserRoleAndPwdDTO UserDto){
        return sysUserService.deleteUser(token,UserDto);
    }
}
