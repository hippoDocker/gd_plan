package com.gd.base.enums.plan;

import com.gd.base.pojo.dto.DataCodeDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 是否被选择 枚举
 * @author: tangxl
 * @create: 2022-03-16 09:37
 */
public enum IsChooseEnum {
	IS_CHOOSE_ONE(0L,"未选择"),
	IS_CHOOSE_TWO(2L,"已选择");

	private Long type;
	private String typeName;

	public Long getType() {
		return type;
	}

	public String getTypeName() {
		return typeName;
	}

	IsChooseEnum(Long type, String typeName){
		this.type = type;
		this.typeName = typeName;
	}

	public static Long getTypeByTypeName(String typeName) {
		Long  result = 1L;
		for (IsChooseEnum item : values()) {
			if (item.typeName.equals(typeName)){
				result = item.getType();
			}
		}
		return result;
	}

	public static String getTypeNameByType(Long type) {
		String  result = "";
		for (IsChooseEnum item : values()) {
			if (item.getType().equals(type)){
				result = item.getTypeName();
			}
		}
		return result;
	}

	public static List<DataCodeDTO> getAllTypeAndTypeName(){
		List<DataCodeDTO> result = new ArrayList<DataCodeDTO>();
		for (IsChooseEnum item : values()) {
			result.add(new DataCodeDTO(item.getType().toString(),item.getTypeName()));
		}
		return result;
	}
}
