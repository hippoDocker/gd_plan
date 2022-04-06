package com.gd.base.jpa;

import com.gd.base.entity.SysMajor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ResponseBody
public interface SysMajorDao extends JpaRepository<SysMajor,Long> {
    SysMajor findByState(Long state);
    SysMajor findByMajorIdAndState(Long majorId,Long state);

    SysMajor findByMajorNameAndState(String majorName, long state);

	List<SysMajor> findAllByState(long state);

	List<SysMajor> findAllByMajorIdInAndState(List<Long> majorIds, long state);
}
