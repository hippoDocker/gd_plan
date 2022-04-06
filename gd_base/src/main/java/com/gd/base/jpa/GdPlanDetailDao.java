package com.gd.base.jpa;

import com.gd.base.entity.GdPlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 毕业设计详情数据处理接口
 * @author: tangxl
 * @create: 2022-03-08 18:55
 */
@Repository
public interface GdPlanDetailDao extends JpaRepository<GdPlanDetail, Long> {
	List<GdPlanDetail> findAllByIdInAndState(List<Long> ids, long state);

	GdPlanDetail findByIdAndState(Long id, long state);

	List<GdPlanDetail> findAllByUserIdAndState(Long userId, long state);

	GdPlanDetail findByIdAndStateAndExamineStateIn(Long id, long state, List<Long> asList);

	GdPlanDetail findByExamineStateAndStateAndUserId(long examineState, long state, Long userId);
}
