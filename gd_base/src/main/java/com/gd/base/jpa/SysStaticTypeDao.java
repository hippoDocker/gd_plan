package com.gd.base.jpa;

import com.gd.base.entity.SysStaticType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description: TODO 静态属性数据接口
 * @Auther: tangxl
 * @Date: 2022年3月2日14:41:08
 */
@Repository
public interface SysStaticTypeDao extends JpaRepository<SysStaticType, Long> {
	SysStaticType findOneByStaticIdAndState(Long staticId, long state);

	SysStaticType findByStaticCodeAndState(String sysRoleMenu, long state);
}
