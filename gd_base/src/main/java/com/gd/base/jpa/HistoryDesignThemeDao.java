package com.gd.base.jpa;

import com.gd.base.entity.HistoryDesignTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @program: gd_plan
 * @description: TODO 历史主题数据处理接口
 * @author: tangxl
 * @create: 2022-03-08 18:55
 */
@Repository
public interface HistoryDesignThemeDao extends JpaRepository<HistoryDesignTheme,Long> {

}
