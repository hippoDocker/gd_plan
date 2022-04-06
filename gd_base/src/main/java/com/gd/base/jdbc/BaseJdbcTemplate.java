package com.gd.base.jdbc;

import com.gd.base.pojo.dto.PageBaseInfo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Auther: tangxl
 * @Date: 2021年12月21日20:16:22
 * @Description: 分页查询基础类
 */
@Component
@Log4j2
public class BaseJdbcTemplate extends JdbcTemplate {
    Logger logger = LoggerFactory.getLogger(BaseJdbcTemplate.class);

    public BaseJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * Mysql分页查询（根据 page.getExport 参数区分是否导出查询，导出查询所有的数据）
     * @param clazzTo
     * @param limitSql sql
     * @param isSort 是否降序排列
     * @param field 排序字段 不排序传null
     * @param page 分页信息
     * @param params 参数
     * @param <T> 类
     * @return
     */
    public <T>PageBaseInfo<T> queryForMysqlPageInfo(Class<T> clazzTo, String limitSql,boolean isSort,String field, PageBaseInfo page, Object... params){
        //总条数sql
        String totalSql = " select count(1) from ( " + limitSql +" ) a ";
        //获取总条数
        log.info("总条数查询sql:{}", totalSql);
        Long totalSize = queryCount(totalSql,params);
        PageBaseInfo<T> ts = queryForBeansPage(clazzTo, limitSql, totalSize,isSort,field, page, params);
        return ts;
    }

    /**
     * Mysql集合数据查询
     * @param clazzTo
     * @param limitSql sql
     * @param isSort 是否降序排列
     * @param field 排序字段
     * @param params 参数
     * @param <T> 类
     * @return
     */
    public <T>List<T> queryForMysqlList(Class<T> clazzTo, String limitSql,boolean isSort,String field, Object... params){
        //排序处理 默认 ASC 升序
        if(StringUtils.isNotBlank(field)){
            limitSql = " select * from ( " + limitSql + " ) a order by a."+field;
            if(isSort){
                limitSql = limitSql + " desc ";
            }
        }
        List<T> rows = query(limitSql, BeanPropertyRowMapper.newInstance(clazzTo), params);
        return rows;
    }

    /**
     * 查询总条数
     * @param countSql 总条数查询sql
     * @param params 参数
     * @return
     */
    private Long queryCount(String countSql, Object[] params) {
        return queryForObject(countSql, params, Long.class);
    }

    /**
     * 分页数据查询
     * @param clazzTo 类
     * @param limitSql sql
     * @param isSort 是否降序排列
     * @param field 排序字段
     * @param totalSize 总条数
     * @param page 分页信息
     * @param params 参数
     * @param <T>
     * @return
     */
    public <T> PageBaseInfo<T> queryForBeansPage(Class<T> clazzTo, String limitSql, Long totalSize,boolean isSort,String field, PageBaseInfo page, Object... params) {
        // 获取分页数据
        List<Object> lists = new ArrayList<>(Arrays.asList(params));
        if (page.getExport() == null || Boolean.FALSE.equals(page.getExport())) {
            lists.add((page.getPageNo() - 1) * page.getPageSize());
            lists.add( page.getPageSize());
        } else {
            lists.add(0);
            lists.add(page.getMaxSize());
        }
        //排序处理 默认 ASC 升序
        if(StringUtils.isNotBlank(field)){
            limitSql = " select * from ( " + limitSql + " ) a order by a."+field;
            if(isSort){
                limitSql = limitSql + " desc ";
            }
        }
        //数据集合
        List<T> rows = new ArrayList<T>();
        //分页信息
        PageBaseInfo<T> pgs = new PageBaseInfo<T>();
        //分页判断
        if(page.getExport()){
            log.info("不分页查询sql:{}",limitSql);
            rows = query(limitSql, BeanPropertyRowMapper.newInstance(clazzTo), params);
            pgs = new PageBaseInfo(rows);
        }else {
            limitSql = limitSql+" limit ?,? ";
            log.info("分页查询sql:{}",limitSql);
            rows = query(limitSql, BeanPropertyRowMapper.newInstance(clazzTo), lists.toArray());
            pgs = new PageBaseInfo(page.getPageNo(),page.getPageSize(),(int) Math.ceil(Double.valueOf(totalSize.toString()) / Double.valueOf(page.getPageSize().toString())),Integer.parseInt(totalSize.toString()),rows);
        }

        return pgs;
    }
}
