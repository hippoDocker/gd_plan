package com.gd.gd_service.service.impl;

import com.alibaba.fastjson.JSON;
import com.gd.base.constant.Constans;
import com.gd.base.constant.RedisKeyConstants;
import com.gd.base.entity.SysRole;
import com.gd.base.jpa.SysRoleDao;
import com.gd.base.pojo.vo.redis.RedisSysUser;
import com.gd.base.util.BeanUtil;
import com.gd.base.util.RedisUtil;
import com.gd.gd_service.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: tangxl
 * @Date:2021年12月21日15:14:47
 * @Description: redis缓存处理层
 */
@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysRoleDao sysRoleDao;

    /**
     * @Description 存入key和字符串value
     * @param key
     * @param value
     */
    @Override
    public boolean setStrValue(String key, String value) {
        try {
            setStrValue(key, value, null);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public void setStrValue(String key, String value, Long time) {
        stringRedisTemplate.opsForValue().set(key, value);
        if (time != null){
            stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }
    /**
     * @description: TODO 根据key获取value
     * @return:
     * @author: tangxl
     * @time:  16:23
     */
    @Override
    public Object getValueByKey(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
    /**
     * @description: TODO 根据key删除
     * @return:
     * @author: tangxl
     * @time:  16:24
     */
    @Override
    public boolean delKey(String key) {
        try {
            stringRedisTemplate.delete(key);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    /**
     * @Description: TODO 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    @Override
    public void delKeys(String ... key){
        if(key!=null&&key.length>0){
            if(key.length==1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * @Description: TODO 存入key和Object值
     * @param key
     * @param value
     */
    @Override
    public boolean setObjectValue(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    /**
     * @Description: TODO  判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    @Override
    public boolean hasKey(String key){
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description: TODO 获取用户缓存
     * @param token 令牌
     * @return 用户缓存
     */
    @Override
    public RedisSysUser getSysUserByToken(String token) {
        String redisSysUserJson = JSON.toJSONString(redisUtil.get(RedisKeyConstants.LOGIN_VALIDATE_SYS_USER+token));
        RedisSysUser redisSysUser = JSON.parseObject(redisSysUserJson,RedisSysUser.class);
        return redisSysUser;
    }

    /**
     * @Description: TODO 删除用户缓存
     * @param token 令牌
     * @return 用户缓存
     */
    @Override
    public void deleteSysUserByToken(String token) {
        redisUtil.removeOne(RedisKeyConstants.LOGIN_VALIDATE_SYS_USER+token);
    }

    /**
     * @Description: TODO 获取用户token令牌
     * @param userCode 用户编码
     * @return token
     */
    @Override
    public String getTokenByUserCode(String userCode) {
        return (String) redisUtil.get(RedisKeyConstants.LOGIN_VALIDATE_SYS_TOKEN+userCode);
    }

    /**
     * @Description: TODO 删除用户token令牌
     * @param userCode 用户编码
     * @return token
     */
    @Override
    public void deleteTokenByUserCode(String userCode) {
        redisUtil.removeOne(RedisKeyConstants.LOGIN_VALIDATE_SYS_TOKEN+userCode);
    }

    /**
     * @Description: TODO 通过token获取用户角色
     * @param token
     * @return
     */
    @Override public SysRole findRoleByToken(String token) {
        RedisSysUser redisSysUser = this.getSysUserByToken(token);
        if(BeanUtil.isEmpty(redisSysUser)){
            throw new RuntimeException(Constans.SYS_USER_TOKEN_OVER);
        }
        SysRole sysRole = sysRoleDao.findByRoleIdAndState(redisSysUser.getRoleId(), 1L);
        if(BeanUtil.isEmpty(sysRole)){
            throw new RuntimeException("角色信息获取失败！");
        }
        return sysRole;
    }
    /**
     * @Description: TODO 通过token获取用户是否是学生
     * @param token
     * @return
     */
    @Override
    public boolean findIsStudent(String token) {
        RedisSysUser redisSysUser = this.getSysUserByToken(token);
        if(BeanUtil.isEmpty(redisSysUser)){
            throw new RuntimeException(Constans.SYS_USER_TOKEN_OVER);
        }
        SysRole sysRole = sysRoleDao.findByRoleIdAndState(redisSysUser.getRoleId(), 1L);
        if(BeanUtil.isEmpty(sysRole)){
            throw new RuntimeException("角色信息获取失败！");
        }
        if ("Student".equals(sysRole.getRoleCode())){
            return true;
        }
        return false;
    }
    /**
     * @Description: TODO 通过token获取用户是否是教师
     * @param token
     * @return
     */
    @Override
    public boolean findIsTeacher(String token){
        RedisSysUser redisSysUser = this.getSysUserByToken(token);
        if(BeanUtil.isEmpty(redisSysUser)){
            throw new RuntimeException(Constans.SYS_USER_TOKEN_OVER);
        }
        SysRole sysRole = sysRoleDao.findByRoleIdAndState(redisSysUser.getRoleId(), 1L);
        if(BeanUtil.isEmpty(sysRole)){
            throw new RuntimeException("角色信息获取失败！");
        }
        if ("Teacher".equals(sysRole.getRoleCode())){
            return true;
        }
        return false;
    }

    /**
     * @description: TODO  通过用户phone清理用户缓存
     * @return:
     * @author: tangxl
     * @time:  16:14
     */
    @Override
    public void cleanUser(String phone) {
        if(hasKey(RedisKeyConstants.LOGIN_VALIDATE_SYS_TOKEN+phone)) {
            Object obj = getValueByKey(RedisKeyConstants.LOGIN_VALIDATE_SYS_TOKEN + phone);
            delKeys(RedisKeyConstants.LOGIN_VALIDATE_SYS_TOKEN+phone,RedisKeyConstants.LOGIN_VALIDATE_SYS_TOKEN+obj.toString());
        }
    }

}
