package com.gd.base.jpa;

import com.gd.base.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysUserDao extends JpaRepository<SysUser,Long> {

    /**
     * @Description TODO 根据学号编码查询所有有效用户
     * @param userCode
     * @return
     */
    List<SysUser> findAllByUserCodeAndState(String userCode,Long state);
    List<SysUser> findAllByUserCode(String userCode);
    /**
     * @Description TODO 根据学号编码查询所有有效和锁定用户
     * @param userCode
     * @return
     */
    List<SysUser> findAllByUserCodeAndStateOrState(String userCode,Long stateOne,Long StateTwo);

    /**
     * @Description TODO 根据学号编码查询有效用户
     * @param userCode
     * @return
     */
    SysUser findByUserCodeAndState(String userCode,Long state);
    /**
     * @Description TODO 根据手机号查询所有有效用户
     * @param phone
     * @return
     */
    List<SysUser> findAllByPhoneAndState(String phone,Long state);
    List<SysUser> findAllByPhone(String phone);
    /**
     * @Description TODO 根据手机号查询所有有效和锁定用户
     * @param phone
     * @return
     */
    List<SysUser> findAllByPhoneAndStateOrState(String phone,Long stateOne,Long stateTwo);
    /**
     * @Description TODO 查询所以有效用户
     * @param state
     * @return
     */
    List<SysUser> findAllByState(Long state);

    SysUser findByPhoneAndState(String phone, long state);

    SysUser findByStateAndUserId(long state, Long userId);

    SysUser findByUserId(Long userId);
}
