package com.gd.base.enums.sys;

/**
 * @Auther: tangxl
 * @Date: 2021年11月27日15:32:41
 * @Description: 用户性别
 */
public enum SysFtpFileEnum {
    HEAD_IMG_PATH(1L,"/gd_plan_ftp/head_img/默认头像"),
    USER_SEX_2L(2L,"女");

    private Long type;
    private String typeName;

    public Long getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    SysFtpFileEnum(Long type, String typeName){
        this.type = type;
        this.typeName = typeName;
    }

    public static Long getTypeByTypeName(String typeName) {
        Long  result = 1L;
        for (SysFtpFileEnum item : values()) {
            if (item.typeName.equals(typeName)){
                result = item.getType();
            }
        }
        return result;
    }

    public static String getTypeNameByType(Long type) {
        String  result = "未知";
        for (SysFtpFileEnum item : values()) {
            if (item.type.equals(type)){
                result = item.getTypeName();
            }
        }
        return result;
    }

}
