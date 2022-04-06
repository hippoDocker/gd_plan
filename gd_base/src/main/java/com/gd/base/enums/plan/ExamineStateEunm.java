package com.gd.base.enums.plan;

import com.gd.base.pojo.dto.DataCodeDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 审核状态枚举
 * @author: tangxl
 * @create: 2022-03-08 19:44
 */
public enum ExamineStateEunm {
	UEXAMINE_STATE_ZERO(0L,"未审核"),
	UEXAMINE_STATE_ONE(1L,"已通过"),
	UEXAMINE_STATE_TWO(2L,"未通过");

	private Long type;
	private String typeName;

	public Long getType() {
		return type;
	}

	public String getTypeName() {
		return typeName;
	}

	ExamineStateEunm(Long type, String typeName){
		this.type = type;
		this.typeName = typeName;
	}

	public static Long getTypeByTypeName(String typeName){
		Long result = 1L;
		for (ExamineStateEunm item : values()){
			if (item.getTypeName().equals(typeName)) {
				result = item.getType();
			}
		}
		return result;
	}

	public static String getTypeNameByType(Long type){
		String  result = "";
		for (ExamineStateEunm item : values()){
			if (item.getType().equals(type)) {
				result = item.getTypeName();
			}
		}
		return result;
	}

	public static List<DataCodeDTO> getAllTypeAndTypeName(){
		List<DataCodeDTO> result = new ArrayList<DataCodeDTO>();
		for (ExamineStateEunm item : values()) {
			result.add(new DataCodeDTO(item.getType().toString(),item.getTypeName()));
		}
		return result;
	}
}
