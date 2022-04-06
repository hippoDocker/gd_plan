package com.gd.base.jpa;

import com.gd.base.entity.SysCollege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysCollegeDao extends JpaRepository<SysCollege,Long> {
    SysCollege findBycollegeIdAndState(Long collegeId,Long state);

    SysCollege findBycollegeNameAndState(String collegeName, long state);

	List<SysCollege> findAllByState(long state);

	List<SysCollege> findAllByCollegeIdInAndState(List<Long> collegeIds, long state);

    SysCollege findByCollegeIdAndState(Long collegeId, long state);
}
