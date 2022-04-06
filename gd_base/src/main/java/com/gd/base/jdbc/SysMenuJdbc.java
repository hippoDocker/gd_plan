package com.gd.base.jdbc;

import com.gd.base.pojo.dto.PageBaseInfo;
import com.gd.base.pojo.dto.sys.SysMenuDTO;
import com.gd.base.entity.SysMenu;
import com.gd.base.util.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
/**
 * @Auther: tangxl
 * @Date:2022年2月25日14:34:28
 * @Description: 菜单数据处理JDBC
 */
@Repository
public class SysMenuJdbc {
    @Autowired
    private BaseJdbcTemplate jdbcTemplate;
    /**
     * 菜单集合分页查询
     * @param sysMenuDto  查询条件
     * @param page  分页信息
     * @return
     */
    public PageBaseInfo<SysMenu> findMenuList(SysMenuDTO sysMenuDto, PageBaseInfo page){
        StringBuffer sql = new StringBuffer(" select * from sys_menu sm where 1 = 1 and sm.state =1 ");
        List<Object> parms = new ArrayList<>();
        //添加条件
        if(StringUtils.isNotBlank(sysMenuDto.getMenuName())){
            sql.append(" and sm.menu_name like ? ");
            parms.add("%"+sysMenuDto.getMenuName()+"%");
        }
        if (StringUtils.isNotBlank(sysMenuDto.getMenuUrl())) {
            sql.append(" and sm.menu_url like ? ");
            parms.add("%"+sysMenuDto.getMenuUrl()+"%");
        }
        if (StringUtils.isNotBlank(sysMenuDto.getMenuType())) {
            sql.append(" and sm.menu_type like ? ");
            parms.add("%"+sysMenuDto.getMenuType()+"%");
        }
        if (BeanUtil.isNotNull(sysMenuDto.getParentId())) {
            sql.append(" and sm.parent_id = ? ");
            parms.add(sysMenuDto.getParentId());
        }
        if (StringUtils.isNotBlank(sysMenuDto.getCreateTimeBegin())&&StringUtils.isNotBlank(sysMenuDto.getCreateTimeEnd())) {
            sql.append(" and sm.create_time between ? and ? ");
            parms.add(sysMenuDto.getCreateTimeBegin());
            parms.add(sysMenuDto.getCreateTimeEnd());
        }
        PageBaseInfo<SysMenu> sMenuPage = jdbcTemplate.queryForMysqlPageInfo(SysMenu.class,sql.toString(),false,null,page,parms.toArray());
        return sMenuPage;
    }
}
