package com.gd.base.jpa;

import com.gd.base.entity.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SysMenuDao extends JpaRepository<SysMenu,Long> {
    /**
     *
     * @param idList 菜单ID列表
     * @param state 数据状态
     * @return
     */
    List<SysMenu> findAllByMenuIdInAndState(List<Long> idList, long state);
    /**
     *
     * @param id 菜单ID
     * @param state 数据状态
     * @return
     */
    SysMenu findByMenuIdAndState(Long id, long state);
    /**
     * @Description TODO 获取所有有效菜单
     * @param state
     * @return
     */
    List<SysMenu> findAllByState(Long state);
    /**
     * @Description TODO 获取所有有效父级菜单
     * @param state
     * @return
     */
    List<SysMenu> findAllByStateAndParentId(Long state,Long parentId);

    /**
     * @Description TODO 通过菜单ID列表和状态查询所有的菜单
     * @param menuIds
     * @param state
     * @return
     */
    List<SysMenu> findAllByMenuIdInAndState(List<Long> menuIds,Long state);
}
