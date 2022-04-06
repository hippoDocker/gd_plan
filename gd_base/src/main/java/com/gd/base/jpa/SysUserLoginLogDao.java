package com.gd.base.jpa;

import com.gd.base.entity.SysUserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserLoginLogDao extends JpaRepository<SysUserLoginLog,Long> {
}
