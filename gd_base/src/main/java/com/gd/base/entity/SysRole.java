package com.gd.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "角色表")
@Table(name="sys_role")
@Entity
public class SysRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("角色ID")
    @Column(name = "role_id")
    private Long roleId;

    @ApiModelProperty("角色编码")
    @Column(name = "role_code")
    private String roleCode;

    @ApiModelProperty("角色名称")
    @Column(name = "role_name")
    private String roleName;

    @ApiModelProperty("角色创建时间")
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    @ApiModelProperty("状态")
    @Column(name = "state")
    private Long state;

    @ApiModelProperty("创建人")
    @Column(name = "create_by")
    private String createBy;


}
