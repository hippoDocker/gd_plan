package com.gd.gd_service;

import com.gd.base.entity.SysUser;
import com.gd.base.jdbc.BaseJdbcTemplate;
import com.gd.base.jpa.*;
import com.gd.base.util.FtpUtil;
import com.gd.base.util.RedisUtil;
import com.gd.gd_service.controller.SysUserController;
import com.gd.gd_service.service.RedisService;
import com.gd.gd_service.service.SysUserService;
import com.gd.gd_service.service.SysmenuService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@SpringBootTest
@Log4j2
class GdServiceApplicationTests {
    @Autowired
    RedisService redisService;
    @Autowired
    SysUserDao sysUserDao;
    @Autowired
    SysUserRoleDao sysUserRoleDao;
    @Autowired
    SysRoleDao sysRoleDao;
    @Autowired
    SysmenuService sysmenuService;
    @Autowired
    FtpUtil ftpUtil;
    @Autowired
    SysMajorDao sysMajorDao;
    @Autowired
    SysCollegeDao sysCollegeDao;
    @Autowired
    SysUserController sysUserController;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private BaseJdbcTemplate jdbcTemplate;
    @Autowired
    private SysMenuRoleDao sysMenuRoleDao;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysStaticValueDao sysStaticValueDao;
    @Autowired
    private GdDesignDirectionDao gdDesignDirectionDao;
    @Autowired
    private GdPlanDetailDao gdPlanDetailDao;

    @Test
    @Transactional(rollbackOn = Exception.class)
    public void test() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        ids.add(3L);
        System.out.println(gdPlanDetailDao.findAllByIdInAndState(ids,1L));

    }

    public void save(SysUser sysUser){
        System.out.println(sysUserService.addtestTrans(sysUser));
    }

    @Test
    public void check(){
        System.out.println(Math.random());
    }


    @Test
    public void ftpTest() throws IOException {
        System.out.println(File.separator);
    }

    public void filetest() throws IOException {
        String zipPath = "D:\\Txl\\3.zip";      //压缩包路径
        String str1 = "测试test123abc";                      //需要写入的数据
        String str2 = "测试2";
        String Name1 = StringUtils.join("文件.json");      //压缩包里的文件
        String Name2 = StringUtils.join("file/文件1.json");  //在压缩包里创建file目录下的文件
        //创建压缩包
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipPath));
        //创建压缩包里的文件
        zipOutputStream.putNextEntry(new ZipEntry(Name1));
        byte[] bytes1 = str1.getBytes(StandardCharsets.UTF_8);
        zipOutputStream.write(bytes1, 0, bytes1.length);    //将数据写入到压缩包里的文件里面
        zipOutputStream.putNextEntry(new ZipEntry(Name1));
        zipOutputStream.closeEntry();

        zipOutputStream.putNextEntry(new ZipEntry(Name2));
        byte[] bytes2 = str2.getBytes(StandardCharsets.UTF_8);
        zipOutputStream.write(bytes2, 0, bytes2.length);

        zipOutputStream.closeEntry();

        zipOutputStream.flush();
        zipOutputStream.close();
    }

    @Autowired
    HistoryDesignThemeDao historyDesignThemeDao;
    @Test
    public void testTalk()  {
    }



}
