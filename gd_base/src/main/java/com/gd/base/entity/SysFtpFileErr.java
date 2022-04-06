package com.gd.base.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: gd_plan
 * @description: TODO ftp上传错误文件
 * @author: tangxl
 * @create: 2022-03-05 21:46
 */
@Data
@ApiModel(value = "ftp文件上传错误记录表")
@Table(name="sys_ftp_file_err")
@Entity
public class SysFtpFileErr  implements Serializable {
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	@ApiModelProperty(name="Id",value = "主键ID")
	private Long Id;
	@ApiModelProperty(name = "filePath",value = "文件路径")
	@Column(name = "file_path")
	private String filePath;
	@ApiModelProperty(name = "fileName",value = "文件名称")
	@Column(name = "file_name")
	private String fileName;
	@ApiModelProperty(name = "batch",value = "文件上传批次号")
	@Column(name = "batch")
	private String batch;
	@ApiModelProperty(name = "fileSize",value = "文件大小")
	@Column(name = "file_size")
	private Long fileSize;
	@ApiModelProperty(name = "createTime",value = "文件上传时间")
	@Column(name = "create_time")
	private LocalDateTime createTime;
	@ApiModelProperty(name = "userId",value = "文件上传人ID")
	@Column(name = "user_id")
	private Long userId;
	@ApiModelProperty(name = "userName",value = "上传人姓名")
	@Column(name = "user_name")
	private String userName;
	@ApiModelProperty(name = "errMessage",value = "错误原因")
	@Column(name = "err_message")
	private String errMessage;
	@ApiModelProperty(name = "state",value = "状态")
	@Column(name = "state")
	private Long state;
}
