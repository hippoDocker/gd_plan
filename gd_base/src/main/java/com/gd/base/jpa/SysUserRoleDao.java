package com.gd.base.jpa;

import com.gd.base.entity.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysUserRoleDao extends JpaRepository<SysUserRole,Long> {
    /**
     * @Description TODO 根据用户查询关联角色Id
     * @param userId
     * @return
     */
    List<SysUserRole> findAllByUserId(Long userId);
    //根据用户ID查询单个关联信息
    SysUserRole findByUserIdAndState(Long userId,Long state);

    List<SysUserRole> findAllByUserIdAndState(Long userId, long l);
}
