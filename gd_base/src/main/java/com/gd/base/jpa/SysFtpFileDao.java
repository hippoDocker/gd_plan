package com.gd.base.jpa;

import com.gd.base.entity.SysFtpFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysFtpFileDao extends JpaRepository<SysFtpFile,Long> {
    SysFtpFile findByFilePathAndState(String filePath,Long state);

    List<SysFtpFile> findAllByBatchAndState(String ftpFileBatch, long state);
}
