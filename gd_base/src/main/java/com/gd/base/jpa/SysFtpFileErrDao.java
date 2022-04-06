package com.gd.base.jpa;

import com.gd.base.entity.SysFtpFileErr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO Ftp上传错误文件信息数据处理接口
 * @author: tangxl
 * @create: 2022-03-05 22:17
 */
@Repository
public interface SysFtpFileErrDao extends JpaRepository<SysFtpFileErr, Long> {
	List<SysFtpFileErr> findAllByBatchAndState(String ftpFileBatch, long state);
}
