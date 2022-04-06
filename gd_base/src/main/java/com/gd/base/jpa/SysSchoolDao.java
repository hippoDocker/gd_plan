package com.gd.base.jpa;

import com.gd.base.entity.SysSchool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysSchoolDao extends JpaRepository<SysSchool,Long> {
    SysSchool findBySchoolIdAndState(Long schoolId,Long state);

    SysSchool findBySchoolNameAndState(String schoolName, long state);

	List<SysSchool> findAllBySchoolIdInAndState(List<Long> schoolIds, long state);

	List<SysSchool> findAllByState(long state);
}
