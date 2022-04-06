package com.gd.gd_service.service;

import com.gd.base.entity.SysRole;
import com.gd.base.pojo.vo.redis.RedisSysUser;

public interface RedisService {

    boolean setStrValue(String key, String value);
    Object getValueByKey(String key);
    boolean delKey(String key);
    boolean setObjectValue(String key,Object object);
    boolean hasKey(String key);
    void delKeys(String ... key);
    RedisSysUser getSysUserByToken(String token);
    void deleteSysUserByToken(String token);
    String getTokenByUserCode(String userCode);
    void deleteTokenByUserCode(String userCode);

    SysRole findRoleByToken(String token);
    boolean findIsStudent(String token);
    boolean findIsTeacher(String token);

    void cleanUser(String phone);
}
