package com.gd.gd_service.controller;

import com.gd.base.entity.SysMajor;
import com.gd.base.pojo.dto.CommonResult;
import com.gd.base.pojo.dto.DataCodeDTO;
import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysMajorDTO;
import com.gd.base.pojo.vo.sys.SysMajorVO;
import com.gd.gd_service.service.SysMajorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value ="/major")
@Api(value = "专业管理",tags = "专业管理")
@Log4j2
public class SysMajorController {
	@Autowired
	private SysMajorService sysMajorService;
	/**
	 * @Description: TODO 添加专业
	 * @return
	 */
	@PostMapping("/create")
	@ApiOperation(value = "添加专业",notes = "根据专业名称（MajorName）--必填，专业编码（MajorCode）--不必填")
	public CommonResult addMajor(@RequestHeader String token , @RequestBody SysMajorDTO sysMajorDto){
		return sysMajorService.addMajor(token,sysMajorDto);
	}

	/**
	 * @Description: TODO 删除专业
	 * @return
	 */
	@PostMapping("/delete")
	@ApiOperation(value = "删除专业", notes = "根据专业id集合（MajorIds）删除专业信息")
	public CommonResult deleteRole(@RequestHeader String token,@RequestBody SysMajorDTO sysMajorDto){
		return sysMajorService.deleteMajor(token,sysMajorDto);
	}

	/**
	 * @Description: TODO 更新专业信息
	 * @return
	 */
	@PostMapping("/update")
	@ApiOperation(value="更新专业",notes = "根据专业ID（MajorId）更新专业编码（MajorCode），专业名称（MajorName）")
	public CommonResult updateMajor(@RequestHeader String token,@RequestBody SysMajorDTO sysMajorDto){
		CommonResult commonResult = sysMajorService.updateMajor(token,sysMajorDto);
		return commonResult;
	}

	/**
	 * @Description: TODO 分页查询角色
	 * @return
	 */
	@PostMapping("/findPage")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = SysMajorVO.class)
	})
	@ApiOperation(value="分页查询专业",notes = "专业分页查询，也可通过专业名称（MajorNme)条件查询,分页大小(pageSize),分页页码(pageNo)")
	public CommonResult findMajorPage(@RequestHeader String token,@RequestBody SysMajorDTO sysMajorDto){
		PageBaseInfo<SysMajorVO> sysMajorPage = sysMajorService.findMajorPage(token,sysMajorDto,new PageBaseInfo(sysMajorDto.getPageNo(),sysMajorDto.getPageSize(),Boolean.FALSE));
		return CommonResult.success(sysMajorPage,"查询成功！");
	}
	/**
	 * @Description: TODO 根据ID查询专业信息
	 * @return
	 */
	@PostMapping("/findbyid")
	@ApiOperation(value="根据ID查询专业",notes = "根据专业ID（MajorId）查询角色信息")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = SysMajor.class)
	})
	public CommonResult findMajorById(@RequestHeader String token,@RequestBody SysMajorDTO sysMajorDto){
		CommonResult commonResult = sysMajorService.findMajorById(token,sysMajorDto);
		return commonResult;
	}

	/**
	 * @Description: TODO 查询所有专业
	 * @return
	 */
	@PostMapping("/findCodeDataMajor")
	@ApiResponses({
			@ApiResponse(code = 200,message = "查询成功！",response = DataCodeDTO.class)
	})
	@ApiOperation(value="查询Code-data形式的专业信息",notes = "查询所有专业信息，以code-data的数据形式返回，code对应专业id，date对应专业名称,下拉框数据可以使用")
	public CommonResult findCodeDataMajor(@RequestHeader String token){
		CommonResult commonResult = sysMajorService.findCodeDataMajor(token);
		return commonResult;
	}
}
