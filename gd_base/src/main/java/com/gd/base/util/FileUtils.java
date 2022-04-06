package com.gd.base.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 文件处理工具类
 * @author: tangxl
 * @create: 2022-03-08 11:29
 */
public class FileUtils {
	/**
	 * @description: TODO 文件格式判断
	 * @Param: file 文件，typeList 文件格式集合
	 * @author: tangxl
	 * @date: 2022年3月8日11:33:47
	 * @return: Boolean 是否符合
	 */
	public static Boolean checkFileFormat(MultipartFile file, List<String> typeList){
		boolean result = false;
		if(file==null){
			return result;
		}
		for (String type : typeList){
			if(file.getOriginalFilename().indexOf(type)>0){
				result = true;
			}
		}
		return result;
	}
}
