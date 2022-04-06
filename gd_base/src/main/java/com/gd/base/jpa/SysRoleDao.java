package com.gd.base.jpa;

import com.gd.base.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleDao extends JpaRepository<SysRole,Long> {
    /**
     *
     * @param idList 角色ID列表
     * @param state 数据状态
     * @return
     */
    //@Query(value = "select * from sys_role sr where sr.role_id in(?1) and sr.state=?2 ",nativeQuery = true)
    List<SysRole> findAllByRoleIdInAndState(List<Long> idList, Long state);
    /**
     *
     * @param id 菜单ID
     * @param state 数据状态
     * @return
     */
    SysRole findByRoleIdAndState(Long id, long state);

    SysRole findByRoleNameAndState(String roleName, long state);

	List<SysRole> findAllByState(long state);

}
