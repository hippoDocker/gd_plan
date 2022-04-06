package com.gd.base.jpa;

import com.gd.base.entity.SysStaticValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: gd_plan
 * @description: TODO 静态值数据接口
 * @author: tangxl
 * @create: 2022-03-02 14:42
 */
@Repository
public interface SysStaticValueDao extends JpaRepository<SysStaticValue, Long> {
	SysStaticValue findOneByStaticValueIdAndState(Long staticValueId, long state);

	List<SysStaticValue> findAllByStaticTypeIdAndState(Long staticId, long state);
}
