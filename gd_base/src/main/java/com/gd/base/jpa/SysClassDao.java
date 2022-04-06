package com.gd.base.jpa;

import com.gd.base.entity.SysClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysClassDao extends JpaRepository<SysClass,Long> {
    SysClass findByClassIdAndState(Long classId,Long state);

    SysClass findByClassNameAndState(String className, long state);

	List<SysClass> findAllByState(long state);

	List<SysClass> findAllByClassIdInAndState(List<Long> classIds, long state);
}
