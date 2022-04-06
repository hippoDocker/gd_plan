package com.gd.base.pojo.vo.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
@ApiModel(value = "菜单VO实体类")
@Data
public class SysMenuVO implements Serializable {
    @ApiModelProperty("菜单ID")
    private Long menuId;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单路径")
    private String menuUrl;

    @ApiModelProperty("菜单图标")
    private String menuIcon;

    @ApiModelProperty("菜单描述")
    private String menuType;

    @ApiModelProperty("菜单描述")
    private String menuDescription;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    @ApiModelProperty("菜单创建人")
    private String createBy;

    @ApiModelProperty("最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastModifyTime;

    @ApiModelProperty("最后修改人")
    private String lastModifyBy;

    @ApiModelProperty("状态")
    private Long state;

    @ApiModelProperty("菜单父级id")
    private Long parentId;

    @ApiModelProperty("子集菜单")
    private List<SysMenuVO> sysMenuVOList;
}
