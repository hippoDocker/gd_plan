package com.gd.base.jpa;

import com.gd.base.entity.GdDesignDirection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计方向数据操作接口
 * @author: tangxl
 * @create: 2022-03-05 14:14
 */
@Repository
public interface GdDesignDirectionDao extends JpaRepository<GdDesignDirection, Long> {
	List<GdDesignDirection> findAllByIdInAndState(List<Long> ids,long state);

	GdDesignDirection findAllByIdAndState(Long id, long state);

	GdDesignDirection findByIdAndState(Long gdDesignDirectionId, long state);
	//List<GdDesignDirection> findAllByIdInAndState(List<Long> ids, long state);

	//List<GdDesignDirection> findAllByIdIn(List<Long> ids);

}
