package com.gd.base.jdbc;

import com.gd.base.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @Auther: tangxl
 * @Date:2022年2月25日14:35:02
 * @Description: 用户登录数据操作JDBC
 */
@Repository
public class UserLoginJdbc {
    @Autowired
    JdbcTemplate jdbcTemplate;


    public List<SysUser> userLogin(){
        String sql = "select * from sys_user where 1=1";
        List<SysUser> sysUsersList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(SysUser.class));
        return sysUsersList;
    }
}
