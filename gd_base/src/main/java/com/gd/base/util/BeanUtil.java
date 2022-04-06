package com.gd.base.util;

import com.alibaba.fastjson.JSON;
import com.gd.base.entity.SysMenu;
import com.gd.base.pojo.vo.sys.SysMenuVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Auther: tangxl
 * @Date:2021年12月13日14:11:38
 * @Description: @Description TODO 通用工具类
 */
@Component
public class BeanUtil extends BeanUtils {
    /**
     * @Description: @Description TODO list<bean> 类列表深拷贝
     * @param
     * @return
     */
    public static <T> List<T> deepCopyListBean(List<T> src){
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();//目标输出流
            ObjectOutputStream out = null;//对象输出流
            out = new ObjectOutputStream(byteOut);
            //对参数指定的obj对象进行序列化，把得到的字节序列写到一个目标输出流中
            out.writeObject(src);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);//对象输入流
            //readObject()方法从一个源输入流中读取 字节序列，再把它们反序列化为一个对象
            return (List<T>)in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    /**
     * @Description: @Description TODO bean 对象深拷贝
     * @param
     * @return
     */
    public static <T> T replicationBean(T src){
        if (src == null) {
            throw new RuntimeException("src object list Can't be empty");
        }
        return (T)JSON.parseObject(JSON.toJSONString(src),src.getClass());
    }

    /**
     * @Description: @Description TODO 深拷贝list对象
     * @param
     * @return
     */
    public static <T> List<T> replicationBeanList(List<T> src){
        if (CollectionUtils.isEmpty(src)) {
            throw new RuntimeException("src object list Can't be empty");
        }
        List<T> list = (List<T>) JSON.parseArray(JSON.toJSONString(src), src.get(0).getClass());
        return list;
    }


    /**
     * 从List<A> copy到List<B>
     * @param list List<B>
     * @param clazz B
     * @return List<B>
     */
    public static <T> List<T> copyListObject(List<?> list,Class<T> clazz){
        String oldOb = JSON.toJSONString(list);
        return JSON.parseArray(oldOb, clazz);
    }

    /**
     * 从对象A copy到 对象B
     * @param ob A
     * @param clazz B.class
     * @return B
     */
    public static <T> T copyObject(Object ob,Class<T> clazz){
        String oldOb = JSON.toJSONString(ob);
        return JSON.parseObject(oldOb, clazz);
    }

    /**
     * @Description TODO 菜单序列化工具类
     * @Param sysMenus 所有菜单
     * @Param parrentId 父级菜单ID
     * @return
     */
    public static List<SysMenuVO>  menuSerialize(List<SysMenu> sysMenus, Long parentId){
        //获取所有父级菜单
        List<SysMenu> sysMenuParents = new ArrayList<>();
        sysMenus.stream().forEach(o->{
            if(o.getParentId().equals(parentId)){
                sysMenuParents.add(o);
            }
        });
        List<SysMenuVO> sysMenuVOParents = BeanUtil.copyListObject(sysMenuParents, SysMenuVO.class);
        //获取所有菜单
        List<SysMenuVO> sysMenuVOS = BeanUtil.copyListObject(sysMenus, SysMenuVO.class);
        //获取所有父级菜单子菜单
        for(SysMenuVO sysMenuVo: sysMenuVOParents){
            sysMenuVo.setSysMenuVOList(setSysMenuVoList(sysMenuVOS,sysMenuVo.getMenuId()));
        }
        return sysMenuVOParents;
    }
    /**
     * TODO 生成子集菜单
     * @param sysMenuVOList
     * @param menuId 父级菜单ID
     * @return
     */
    private static List<SysMenuVO> setSysMenuVoList(List<SysMenuVO> sysMenuVOList, Long menuId) {
        List<SysMenuVO> newSysMenuVOList = new ArrayList<>();
        for(SysMenuVO sysMenuVo: sysMenuVOList){
            if(sysMenuVo.getParentId().equals(menuId)){
                List<SysMenuVO> sysMenuVOS = setSysMenuVoList(sysMenuVOList,sysMenuVo.getMenuId());
                sysMenuVo.setSysMenuVOList(sysMenuVOS);
                newSysMenuVOList.add(sysMenuVo);
            }
        }
        return newSysMenuVOList;
    }

    /**
     * @Description TODO 判断对象是否为null
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * @Description TODO 判断对象是否不为null
     * @param obj
     * @return
     */
    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    /**
     * @Description TODO 判断对象是否为空
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) return true;
        else if (obj instanceof CharSequence) return ((CharSequence) obj).length() == 0;
        else if (obj instanceof Collection) return ((Collection) obj).isEmpty();
        else if (obj instanceof Map) return ((Map) obj).isEmpty();
        else if (obj.getClass().isArray()) return Array.getLength(obj) == 0;

        return false;
    }

    /**
     * @Description TODO 判断对象是否不为空
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 把List<Object[]>转换成List<T>
     */
    public static <T> List<T> listObjectToBean(List<Object[]> objList, Class<T> clz) throws Exception{
        if (objList==null || objList.size()==0) {
            return null;
        }

        Class<?>[] cz = null;
        Constructor<?>[] cons = clz.getConstructors();
        for (Constructor<?> ct : cons) {
            Class<?>[] clazz = ct.getParameterTypes();
            if (objList.get(0).length == clazz.length) {
                cz = clazz;
                break;
            }
        }

        List<T> list = new ArrayList<T>();
        for (Object[] obj : objList) {
            Constructor<T> cr = clz.getConstructor(cz);
            list.add(cr.newInstance(obj));
        }
        return list;
    }


}


