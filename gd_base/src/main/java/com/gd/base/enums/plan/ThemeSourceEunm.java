package com.gd.base.enums.plan;

import com.gd.base.pojo.dto.DataCodeDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 主题来源枚举
 * @author: tangxl
 * @create: 2022-03-05 14:26
 */
public enum ThemeSourceEunm {
	THEME_SOURCE_ONE(1L,"教师推荐"),
	THEME_SOURCE_TWO(2L,"学生自拟");

	private Long type;
	private String typeName;

	public Long getType() {
		return type;
	}

	public String getTypeName() {
		return typeName;
	}

	ThemeSourceEunm(Long type, String typeName){
		this.type = type;
		this.typeName = typeName;
	}

	public static Long getTypeByTypeName(String typeName) {
		Long  result = 1L;
		for (ThemeSourceEunm item : values()) {
			if (item.typeName.equals(typeName)){
				result = item.getType();
			}
		}
		return result;
	}

	public static String getTypeNameByType(Long type) {
		String  result = "";
		for (ThemeSourceEunm item : values()) {
			if (item.getType().equals(type)){
				result = item.getTypeName();
			}
		}
		return result;
	}

	public static List<DataCodeDTO> getAllTypeAndTypeName(){
		List<DataCodeDTO> result = new ArrayList<DataCodeDTO>();
		for (ThemeSourceEunm item : values()) {
			result.add(new DataCodeDTO(item.getType().toString(),item.getTypeName()));
		}
		return result;
	}
}
