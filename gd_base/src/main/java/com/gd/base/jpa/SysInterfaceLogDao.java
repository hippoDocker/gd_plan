package com.gd.base.jpa;

import com.gd.base.entity.SysInterfaceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysInterfaceLogDao extends JpaRepository<SysInterfaceLog,Long> {
}
