package com.gd.base.pojo.dto.FtpClient;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@ConfigurationProperties(prefix = "ftp")//读取配置文件封装成实体类
@ApiModel(value = "Ftp配置文件实体类")
@Data
public class FtpClientYml  implements Serializable {
    @ApiModelProperty("ftp服务器IP地址")
    public String ftpIp;
    @ApiModelProperty("端口")
    public Integer ftpPort;
    @ApiModelProperty("用户名")
    public String ftpUser;
    @ApiModelProperty("密码")
    public String ftpPassword;
    @ApiModelProperty("ftp文件保存地址")
    public String FtpSavePase;
}
