package com.gd.base.jpa;

import com.gd.base.entity.GdDesignTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计主题数据处理接口
 * @author: tangxl
 * @create: 2022-03-05 16:26
 */
@Repository
public interface GdDesignThemeDao extends JpaRepository<GdDesignTheme, Long> {
	GdDesignTheme findByIdAndState(Long id, Long l);

	List<GdDesignTheme> findAllByIdInAndState(List<Long> ids, long state);

    List<GdDesignTheme> findAllByGdDesignDirectionIdInAndState(List<Long> ids, long state);

    List<GdDesignTheme> findAllByState(long state);
}
