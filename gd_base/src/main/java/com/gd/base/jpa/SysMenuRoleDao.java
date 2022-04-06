package com.gd.base.jpa;

import com.gd.base.entity.SysMenuRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuRoleDao extends JpaRepository<SysMenuRole,Long> {
    List<SysMenuRole> findAllByRoleIdInAndState(List<Long> roleIds,Long state);

    List<SysMenuRole> findAllByRoleIdAndState(Long roleId,Long state);

	List<SysMenuRole> findAllByState(long state);
}
