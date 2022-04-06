package com.gd.gd_service.config;

import com.gd.base.jdbc.BaseJdbcTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @Auther: tangxl
 * @Date:2022年1月24日10:45:57
 * @Description:JdbcTemplate数据源配置类
 */
@Configuration
public class JdbcTemplateConfig {
    //@Qualifier("primaryDataSource")指明要注入的bean，后面的参数名无所谓
    @Primary
    @Bean("primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("primaryDataSource")DataSource dataSource){
        return new BaseJdbcTemplate(dataSource);
    }

    @Bean(name = "secondaryJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(@Qualifier("secondaryDataSource") DataSource dataSource){
        return new BaseJdbcTemplate(dataSource);
    }
}
