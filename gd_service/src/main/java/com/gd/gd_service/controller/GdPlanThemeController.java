package com.gd.gd_service.controller;

import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.DataCodeDTO;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.plan.TeacherDesDirDTO;
import com.gd.base.pojo.dto.plan.TeacherDirectionPageDTO;
import com.gd.base.pojo.dto.plan.TeacherThemeDTO;
import com.gd.base.pojo.dto.plan.TeacherThemePageDTO;
import com.gd.base.pojo.vo.plan.TeacherDirectionVO;
import com.gd.base.pojo.vo.plan.TeacherThemeVO;
import com.gd.base.util.BeanUtil;
import com.gd.gd_service.service.GdPlanThemeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @program: gd_plan
 * @description: TODO 毕设主题相关接口
 * @author: tangxl
 * @create: 2022-03-02 17:02
 */
@RestController
@RequestMapping("/gdplan")
@Api(value = "GdPlanThemeController",tags = "毕设主题信息发布+毕设主题选择")
public class GdPlanThemeController {
	@Autowired
	private GdPlanThemeService gdPlanThemeService;

	@PostMapping("/findTeacherDesDirPage")
	@ApiOperation(value = "发布设计方向/毕设方向汇总(毕设信息管理)--分页查询",notes = "老师设计方向分页查询：老师设计方向分页查询--条件查询：教师姓名(teacherName),教师电话(phone),设计方向(designDirection),分页大小(pageSize),分页页码(pageNo)")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = TeacherDirectionVO.class)
	})
	public CommonResult findTeacherDesignDirectionPage(@RequestHeader String token, @RequestBody TeacherDirectionPageDTO teacherDirectionPageDto){
		return gdPlanThemeService.findTeacherDesignDirectionPage(token, teacherDirectionPageDto,
				new PageBaseInfo(teacherDirectionPageDto.getPageNo(), teacherDirectionPageDto.getPageSize(), Boolean.FALSE));
	}

	@PostMapping("/findTeacherDesignThemePage")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = TeacherThemeVO.class)
	})
	@ApiOperation(value = "发布设计主题/设计主题汇总(毕设信息管理)--分页查询",notes = "老师设计主题分页查询：老师设计主题分页查询--条件查询：教师姓名(teacherName),教师电话(phone),设计方向(designDirection),主题名称(themeName),分页大小(pageSize),分页页码(pageNo)")
	public CommonResult findTeacherDesignThemePage(@RequestHeader String token, @RequestBody TeacherThemePageDTO teacherThemePageDto){
		return gdPlanThemeService.findTeacherDesignThemePage(token, teacherThemePageDto,
				new PageBaseInfo(teacherThemePageDto.getPageNo(), teacherThemePageDto.getPageSize(), Boolean.FALSE));
	}

	@PostMapping("/addTeacherDesDir")
	@ApiOperation(value = "发布设计方向--新增",notes = "新增毕业设计方向：添加传参--设计方向(designDirection)")
	public CommonResult addTeacherDesDir(@RequestHeader String token, @RequestBody TeacherDesDirDTO teacherDesDirDto){
		return gdPlanThemeService.addTeacherDesDir(token, teacherDesDirDto);
	}
	@PostMapping("/updateTeacherDesDir")
	@ApiOperation(value = "发布设计方向--编辑",notes = "编辑毕业设计方向：添加传参--设计方向ID(id),设计方向(designDirection)")
	public CommonResult updateTeacherDesDir(@RequestHeader String token, @RequestBody TeacherDesDirDTO teacherDesDirDto){
		return gdPlanThemeService.updateTeacherDesDir(token, teacherDesDirDto);
	}

	@PostMapping("/addTeacherTheme")
	@ApiOperation(value = "发布设计主题--新增",notes = "新增毕业设计主题：添加传参--关联设计方向ID(gdDesignDirectionId),选题截止时间(overTime),主题来源(themeSource),备注(remark);"
			+ "注：前端主题来源可通过接口获取传code给后端，如果不传这个参数,主题来源默认如果是老师或管理员添加主题来源就是教师推荐；选题截止时间可以不传，后面修改再改")
	public CommonResult addTeacherTheme(@RequestHeader String token, @RequestBody TeacherThemeDTO teacherTheme){
		return gdPlanThemeService.addTeacherTheme(token, teacherTheme);
	}

	@PostMapping("/updateTeacherTheme")
	@ApiOperation(value = "发布设计主题--编辑",notes = "编辑毕业设计主题：添加传参--设计主题ID(id),选题截止时间(overTime),主题来源(themeSource),备注(remark);"
			+ "注：前端主题来源可通过接口获取传code给后端，如果不传这个参数,主题来源默认如果是老师或管理员添加主题来源就是教师推荐，选题截止时间可以不传")
	public CommonResult updateTeacherTheme(@RequestHeader String token, @RequestBody TeacherThemeDTO teacherTheme){
		return gdPlanThemeService.updateTeacherTheme(token, teacherTheme);
	}

	@PostMapping("/deleteTeacherDesDir")
	@ApiOperation(value = "发布设计方向--删除",notes = "删除毕业设计方向：添加传参--设计方向ID集合(ids)")
	public CommonResult deleteTeacherDesDir(@RequestHeader String token, @RequestBody TeacherDesDirDTO teacherDesDirDto){
		return gdPlanThemeService.deleteTeacherDesDir(token, teacherDesDirDto);
	}

	@PostMapping("/deleteTeacherTheme")
	@ApiOperation(value = "发布设计主题--删除",notes = "删除毕业设计主题：添加传参--设计主题ID集合(ids)")
	public CommonResult deleteTeacherDesDir(@RequestHeader String token, @RequestBody TeacherThemeDTO teacherTheme){
		return gdPlanThemeService.deleteTeacherTheme(token, teacherTheme);
	}

	@GetMapping("/findThemeSource")
	@ApiOperation(value = "查询设计主题来源(code-data格式数据)接口",notes = "查询设计主题来源接口,无参数")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = DataCodeDTO.class)
	})
	public CommonResult findThemeSource(@RequestHeader String token){
		return gdPlanThemeService.findThemeSource(token);
	}

	@PostMapping("/uploadGdThemeDetail")
	@ApiOperation(value = "设计主题详情文档上传",notes = "设计主题详情文档上传：可传多个文件(files)，传参设计主题ID(id)，对于已上传过设计主题详情的，再次上传会将原来上传的文件删除")
	public CommonResult uploadGdThemeDetail(@RequestHeader String token,@RequestParam(value = "files", required = false) MultipartFile[] files,@RequestParam Long id){
//		TeacherThemeDto teacherThemeDto = JSON.parseObject(id, TeacherThemeDto.class);
		TeacherThemeDTO teacherThemeDto = new TeacherThemeDTO();
		teacherThemeDto.setId(id);
		if(files.length < 1){
			return CommonResult.failed("请选择一个文件！");
		}
		if(BeanUtil.isEmpty(teacherThemeDto.getId())){
			return CommonResult.failed("主题ID为空！");
		}
		teacherThemeDto.setId(teacherThemeDto.getId());
		return gdPlanThemeService.uploadGdThemeDetail(token,files,teacherThemeDto);
	}

	@PostMapping("/findGdThemeDetailName")
	@ApiOperation(value = "查询设计主题详情文档名称列表",notes = "查询设计主题详情文档名称列表：Body传参设计主题ID(id),返回设计主题详情文档名称集合")
	public CommonResult findGdThemeDetailName(@RequestHeader String token,@RequestBody TeacherThemeDTO teacherThemeDto){
		return gdPlanThemeService.findGdThemeDetailName(token,teacherThemeDto);
	}

	@PostMapping("/downloadGdThemeDetail")
	@ApiOperation(value = "毕设主题选择--设计主题详情文档下载",notes = "设计主题详情文档下载：设计主题ID(id)")
	public void downloadGdThemeDetail(@RequestHeader String token, @RequestBody TeacherThemeDTO teacherThemeDto, HttpServletResponse response){
		gdPlanThemeService.downloadGdThemeDetail(token,teacherThemeDto,response);
	}

	@PostMapping("/chooseGdPlan")
	@ApiOperation(value = "毕设主题选择--选择",notes = "学生选择毕业设计主题参数：设计主题ID(id);"
			+ "注：后端做了限制老师或管理员不能选择")
	public CommonResult chooseGdPlan(@RequestHeader String token,@RequestBody TeacherThemeDTO teacherThemeDto){
		return gdPlanThemeService.addUserGdPlan(token,teacherThemeDto);
	}

	@PostMapping("/findDataCodeGdDerection")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = DataCodeDTO.class)
	})
	@ApiOperation(value = "查询教师对应的毕设方向",notes = "查询教师对应的毕设方向：返回教师关联的方向数据，用于新增主题下拉框选择毕设方向")
	public CommonResult findDataCodeGdDerection(@RequestHeader String token){
		return gdPlanThemeService.findDataCodeGdDerection(token);
	}
}
