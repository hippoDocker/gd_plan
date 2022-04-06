package com.gd.base.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author LX
 */
@Data
@ApiModel(value = "设计主题表")
@Table(name="gd_design_theme")
@Entity
public class GdDesignTheme implements Serializable {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(name="id",value = "主键ID")
    private Long id;
    @ApiModelProperty(name = "sysUserId",value = "关联教师ID")
    @Column(name = "sys_user_id")
    private Long sysUserId;
    @ApiModelProperty(name = "gdDesignDirectionId",value = "关联设计方向ID")
    @Column(name = "gd_design_direction_id")
    private Long gdDesignDirectionId;
    @ApiModelProperty(name = "themeName",value = "主题名称")
    @Column(name = "theme_name")
    private String themeName;
    @ApiModelProperty(name = "themeSource",value = "主题来源")
    @Column(name = "theme_source")
    private String themeSource;
    @ApiModelProperty(name = "remark",value = "备注")
    @Column(name = "remark")
    private String remark;
    @ApiModelProperty(name = "themeUploadState",value = "主题详情上传状态（1：已上传；0：未上传）")
    @Column(name = "theme_upload_state")
    private Long themeUploadState;
    @ApiModelProperty(name = "createTime",value = "创建时间")
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @ApiModelProperty(name = "state",value = "状态")
    @Column(name = "state")
    private Long state;
    @ApiModelProperty(name = "ftpFileBatch",value = "详情文件上传批次号")
    @Column(name = "ftp_file_batch")
    private String ftpFileBatch;

    @ApiModelProperty(name = "overTime",value = "选题截止时间")
    @Column(name = "over_time")
    private LocalDateTime overTime;

    @ApiModelProperty(name = "isChoose",value = "是否已被选择审核通过（0：未被选中；1：已被选择）")
    @Column(name = "is_choose")
    private Long isChoose;
}
