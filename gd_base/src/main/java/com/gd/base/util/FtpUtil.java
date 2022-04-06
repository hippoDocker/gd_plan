package com.gd.base.util;

import com.gd.base.constant.Constans;
import com.gd.base.pojo.dto.FtpClient.FtpClientYml;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Auther: tangxl
 * @Date: 2021年12月23日10:08:07
 * @Description: TODO Ftp工具类
 */
@Component
@Log4j2
public class FtpUtil {
    //ftp配置文件实体类
    @Autowired
    private FtpClientYml ftpClientYml;

    //本地文件保存地址
    private String localFilePath=" ";




    /**
     * @Description: TODO 登陆FTP并获取FTPClient对象
     * @return fTPClient
     */
    public FTPClient connectFtp(){
        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            //设置连接超时时间
            ftpClient.setConnectTimeout(1000*30);//30秒
            //连接Ftp服务器
            ftpClient.connect(ftpClientYml.getFtpIp(),ftpClientYml.getFtpPort());
            //登录Ftp服务器
            ftpClient.login(ftpClientYml.getFtpUser(),ftpClientYml.getFtpPassword());
            //设置编码防止中文乱码
            ftpClient.setControlEncoding("UTF-8");
            //设置文件类型为二进制（如果从FTP下载或上传的文件是压缩文件的时候，不进行该设置可能会导致获取的压缩文件解压失败）
            setFileType(ftpClient,true);
            //设置为passive-被动模式:ftp client告诉ftp server开通一个端口来传输数据,这个设置允许被动连接--访问远程ftp时需要(需要21端口打开)
            ftpClient.enterLocalPassiveMode();
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                log.info("=====>>连接FTP失败，用户名或密码错误!");
                ftpClient.disconnect();
            } else {
                log.info("=====>>FTP连接成功!");
                log.info("ReplyCode:{}",ftpClient.getReplyCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftpClient;
    }

    /**
     * @Description TODO 设置文件传输类型
     * @throws
     * @throws IOException
     */
    private void setFileType(FTPClient ftpClient,boolean BinaryTransfer) {
        try {
            if (BinaryTransfer) {
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            } else {
                ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
            }
        } catch (IOException e) {
            throw new RuntimeException("设置ftpClient文件传输类型出错{}.", e);
        }
    }

    /**
     * @Description TODO 关闭Ftp连接
     * @param ftpClient
     * @throws IOException
     */
    public void disConnectFtp(FTPClient ftpClient) {
        try {
            if(ftpClient != null){
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("=====>>关闭Ftp连接错误：{}",e);
            throw new RuntimeException("======>>关闭FTP连接发生异常！", e);
        }
    }



    /**
     * @Description TODO 更改Ftp连接文件目录
     * @param ftpClient 已经登陆成功的FTPClient
     * @param fileDir 连接文件目录路径
     * @return
     */
    public boolean changeWorkingDirectory(FTPClient ftpClient,String fileDir){
        boolean flag = false;
        try {
            flag = ftpClient.changeWorkingDirectory(fileDir);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("=====>>修改文件目录失败！{}",e);
        }
        return flag;
    }

    /**
     * @Description TODO 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
     * @param ftpClient 已经登陆成功的FTPClient
     * @param remotePath 多层目录路径
     * @return flag 是否创建成功
     * @throws IOException
     */
    public boolean createDirectory(FTPClient ftpClient,String remotePath) throws IOException {
        boolean flag = true;
        String directory = remotePath+"/";
        //如果远程目录不存在，则通过递归创建远程服务器目录
        if(!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(ftpClient,new String(directory))){
            int start = 0;
            int end =0;
            if(directory.startsWith("/")){
                start =1;
            }else {
                start =0;
            }
            end = directory.indexOf("/",start);
            String path = "";
            String paths = "";
            while (true){
                String subDirectory = new String(remotePath.substring(start,end).getBytes(StandardCharsets.UTF_8),"iso-8859-1");
                path = path + "/" + subDirectory;
                if (!existFile(ftpClient,path)) {
                    if (makeDirectory(ftpClient,subDirectory)) {
                        changeWorkingDirectory(ftpClient,subDirectory);
                    } else {
                        log.info("创建目录[" + subDirectory + "]失败");
                        changeWorkingDirectory(ftpClient,subDirectory);
                    }
                } else {
                    changeWorkingDirectory(ftpClient,subDirectory);
                }

                paths = paths + "/" + subDirectory;
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }

            }
        }
        return flag;
    }

    /**
     * @Description TODO 判断ftp服务器文件是否存在
     * @param ftpClient  已经登陆成功的FTPClient
     * @param path 文件
     * @return flag 是否存在
     * @throws IOException
     */
    private boolean existFile(FTPClient ftpClient,String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * @Description TODO 创建目录
     * @param ftpClient 已经登陆成功的FTPClient
     * @param dir 文件目录
     * @return
     */
    private static boolean makeDirectory(FTPClient ftpClient,String dir) {
        boolean flag = true;
        try {
            flag = ftpClient.makeDirectory(dir);
            if (flag) {
                log.info("创建文件夹" + dir + " 成功！");

            } else {
                log.info("创建文件夹" + dir + " 失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * @Description TODO 获取FTP某一特定目录下的所有文件名称
     * @param ftpClient     已经登陆成功的FTPClient
     * @param ftpDirPath    FTP上的目标文件路径
     */
    public List<String> getFileNameList(FTPClient ftpClient, String ftpDirPath) {
        List<String> list = new ArrayList();
        try {
            if (ftpDirPath.startsWith("/") && ftpDirPath.endsWith("/")) {
                // 通过提供的文件路径获取FTPFile对象列表
                FTPFile[] files = ftpClient.listFiles(ftpDirPath);
                // 遍历文件列表，打印出文件名称
                for (int i = 0; i < files.length; i++) {
                    FTPFile ftpFile = files[i];
                    // 此处只打印文件，未遍历子目录（如果需要遍历，加上递归逻辑即可）
                    if (ftpFile.isFile()) {
//                        log.info(ftpDirPath + ftpFile.getName());
                        list.add(ftpFile.getName());
                    }
                }
                log.info("当前FTP路径可用");
            } else {
                log.info("当前FTP路径不可用");
            }
        } catch (IOException e) {
            log.error("错误"+e);
        }
        return list;
    }

    /**
     * 获取到服务器文件夹里面最新创建的文件名称
     * @param ftpDirPath 文件路径
     * @param ftpClient ftp的连接
     * @return fileName 文件名称
     */
    public static String getNewFile(FTPClient ftpClient, String ftpDirPath) throws Exception {
        if (ftpDirPath.startsWith("/") && ftpDirPath.endsWith("/")) {
            // 通过提供的文件路径获取FTPFile对象列表
            FTPFile[] files = ftpClient.listFiles(ftpDirPath);
            if(files == null) {
                throw new Exception("文件数组为空");
            }
            Arrays.sort(files, new Comparator<FTPFile>() {
                @Override
                public int compare(FTPFile f1, FTPFile f2) {
                    return f1.getTimestamp().compareTo(f2.getTimestamp());
                }
                @Override
                public boolean equals(Object obj) {
                    return true;
                }
            });
            return ftpDirPath+"/"+files[files.length-1].getName();
        }else {
            throw new Exception("文件夹路径错误！");
        }
    }



//    // 创建文件夹，并切换到该文件夹
//    // 比如： hello/test
//    //最终会切换到test 文件夹返回
//    private void createDir(FTPClient client, String path) throws IOException {
//        String[] dirs = path.split("/");
//        for (String dir : dirs) {
//            if (StringUtils.isEmpty(dir)) {
//                continue;
//            }
//            if (!client.changeWorkingDirectory(dir)) {
//                client.makeDirectory(dir);
//            }
//            client.changeWorkingDirectory(dir);
//        }
//    }







//    /**
//     * @Description: TODO 连接Ftp
//     * @return ftpClient 实例
//     * @throws IOException
//     */
//    public FTPClient connectFTP() throws IOException {
//        FTPClient ftpClient = new FTPClient();
//        //被动模式+超时时间设置,解决卡死的问题 jiao_zg 注
//        ftpClient.setDefaultTimeout(ftpClientProperties.getClientTimeout());
//        ftpClient.setBufferSize(1024*1024*1024);
//        ftpClient.setDataTimeout(ftpClientProperties.getClientTimeout()*1000);
//        //默认端口号：21
//        ftpClient.connect(ftpClientProperties.getHostname(), ftpClientProperties.getPort());
//        ftpClient.login(ftpClientProperties.getUsername(), ftpClientProperties.getPassword());
//        //设置为passive模式:ftp client告诉ftp server开通一个端口来传输数据
//        ftpClient.enterLocalPassiveMode();
//        //设置文件数据类型二进制
//        setFileType(ftpClient);
//        //设置读取数据时阻塞链路的超时时间
//        ftpClient.setSoTimeout(ftpClientProperties.getClientTimeout());
//        int replyCode = ftpClient.getReplyCode();
//        log.info("FTPClient ReplyCode:{}", replyCode);
//        /**
//         * replyCode含义：
//         * 220：连接成功
//         * 230：登录成功
//         * 250：目录切换成功
//         * 。。。。。。。此处省略一大部分
//         */
//        if (!FTPReply.isPositiveCompletion(replyCode)) {
//            log.info("=====>>未连接到ftp，用户名密码错误");
//            ftpClient.disconnect();
//            throw new RuntimeException("未连接到ftp");
//        }
//        return ftpClient;
//    }
//    /**
//     * @Description TODO 设置文件传输类型
//     * @throws
//     * @throws IOException
//     */
//    private void setFileType(FTPClient ftpClient) {
//        try {
//            if (ftpClientProperties.getBinaryTransfer()) {
//                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//            } else {
//                ftpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("设置ftpClient文件传输类型出错{}.", e);
//        }
//
//    }
//
//    /**
//     * @Description TODO 关闭Ftp连接
//     * @param ftpClient
//     * @throws IOException
//     */
//    public void disConn(FTPClient ftpClient) throws IOException {
//        try {
//            ftpClient.disconnect();
//        } catch (IOException e) {
//            e.printStackTrace();
//            log.info("关闭Ftp连接错误：{}",e);
//            throw new RuntimeException("关闭FTP连接发生异常！", e);
//        }
//    }
//
//    /**
//     * 上传文件的原始方法
//     *
//     * @param filename
//     * @param inputStream
//     * @return 返回的Map中包含filename 和 相对路径
//     * @throws IOException
//     */
//    public Boolean uploadFile(String filename, InputStream inputStream) throws IOException {
//        FTPClient ftpClient = connectFTP();
//        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//        //设置文件传输编码
//        ftpClient.setControlEncoding(ftpClientProperties.getControlEncoding());
//        ftpClient.setBufferSize(ftpClientProperties.getBufferSize());
//        ftpClient.enterLocalPassiveMode();
//        //设置文件上传目录
//        ftpClient.changeWorkingDirectory(ftpClientProperties.getFileDir());
//        boolean storeFile = ftpClient.storeFile(filename, inputStream);
//        disConn(ftpClient);
//        return storeFile;
//    }
//
//    /**
//     * @Description TODO Ftp下载文件
//     * @param fileName
//     * @return
//     * @throws IOException
//     */
//    public InputStream downloadFIle(String fileName) throws IOException {
//        FTPClient ftpClient = connectFTP();
//        ftpClient.enterLocalPassiveMode();
//        ftpClient.changeWorkingDirectory(ftpClientProperties.getFileDir());
//        InputStream inputStream = ftpClient.retrieveFileStream(new String(fileName.getBytes(), "UTF-8"));
//        return inputStream;
//    }
//
//    public boolean mkDir(String dir) {
//        FTPClient ftpClient = null;
//        try {
//            ftpClient = connectFTP();
//            if (!ftpClient.changeWorkingDirectory(dir)) {
//                ftpClient.makeDirectory(dir);
//                ftpClient.changeWorkingDirectory(dir);
//            }
//            ftpClient.disconnect();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }

    /**
     * @Description TODO 文件下载通用类
     * @param ftpPath 文件所在目录路径
     * @param fileName 文件名称
     * @return
     */
    public void downloadFile(String ftpPath, String fileName, HttpServletResponse response)  {
        FTPClient ftpClient =null;
        FileInputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            //获取连接
            ftpClient = connectFtp();
//            获取Ftp上文件
//            inputStream = ftpClient.retrieveFileStream(new String((ftpPath+fileName).getBytes(StandardCharsets.UTF_8),"ISO-8859-1"));
//            //设置处理多个文件,同步阻塞方法
//            //只有在获取返回流时，才需要调用completePendingCommad方法，因为返回流不是立刻处理的。所以需用手动调用结束方法。
//            ftpClient.completePendingCommand();
            //设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            //设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            outputStream = response.getOutputStream();
            // 切换到下载目录
            ftpClient.changeWorkingDirectory(ftpPath);
            FTPFile[] fs = ftpClient.listFiles();
            int flag =0;
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    log.info("已查询到文件："+ftpPath+"/"+fileName);
                    flag =1;
                    ftpClient.retrieveFile(new String(ff.getName().getBytes(StandardCharsets.UTF_8), "ISO-8859-1"), outputStream);
                }
            }
            if(flag==0){
                log.info("未查询到此文件："+ftpPath+"/"+fileName);
                throw new RuntimeException("未查询到此文件!");
            }
            if(flag==1){
                log.info(ftpPath+"/"+fileName+"文件下载成功！");
            }
        }catch (IOException e) {
            log.error("=====>>FTP文件读取失败！文件名："+fileName + e);
        }finally {
            try {
                //记住在文件处理异常后也要关闭流
                if(inputStream != null){
                    log.info("=====>>FTP文件读取成功！");
                    inputStream.close();
                }
                //关闭Ftp连接
                if(ftpClient.isConnected()) {
                    disConnectFtp(ftpClient);
                }
            }catch (Exception e){
                log.info("文件下载流关闭出错！{}",e);
            }
        }
    }

    /**
     * @Description TODO 根据路径和文件名称获取用户头像文件流
     * @param ftpPath ftp路径
     * @param phone 文件名称(不含文件后缀)
     */
    public void getUserHeadStream(String ftpPath, String phone,ByteArrayOutputStream baos) {
        FTPClient ftpClient =null;
        InputStream inputStream =null;
        String asB64 =null;
        BufferedImage image =null;
        //获取文件流
        ByteArrayInputStream fis = null;
        String fileName="";
        try {
            //获取连接
            ftpClient = connectFtp();
            // 切换到下载目录
            ftpClient.changeWorkingDirectory(ftpPath);
            FTPFile[] fs = ftpClient.listFiles();
            int flag =0;
            log.info("开始查询文件："+ftpPath+"/"+fileName);
            for (FTPFile ff : fs) {
                fileName = ff.getName();
                if (fileName.substring(0,fileName.lastIndexOf(".")).equals(phone)) {
                    log.info("已查询到文件："+ftpPath+"/"+fileName);
                    flag =1;
                    //读取相应文件名的文件
                    inputStream = ftpClient.retrieveFileStream(new String(ff.getName().getBytes(StandardCharsets.UTF_8), "ISO-8859-1"));
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    //获取ByteArrayOutputStream流
                    while ((len = inputStream.read(buffer)) != -1 ) {
                        baos.write(buffer, 0, len);
                    }
                    baos.flush();
                }
            }
            if(flag==0){
                log.info("未查询到此文件："+ftpPath+"/"+fileName);
                throw new RuntimeException("未查询到此文件!");
            }
            if (null == inputStream) {
                throw new FileNotFoundException("未找到文件："+fileName);
            }
            if(flag==1){
                log.info(ftpPath+"/"+fileName+"文件获取成功！");
            }
        }catch (IOException e) {
            log.error("=====>>FTP文件读取失败！文件名："+fileName + e);
        }finally {
            try {
                if(fis != null){
                    fis.close();
                }
                if(inputStream != null){
                    inputStream.close();
                }
                //关闭Ftp连接
                if(ftpClient.isConnected()) {
                    disConnectFtp(ftpClient);
                }
            }catch (Exception e){
                log.info("文件下载流关闭出错！{}",e);
            }
        }
    }

    /**
     * @description: TODO 删除ftp文件
     * @Param: filePath 文件路径，fileName  文件名称
     * @author: tangxl
     * @date: 2022年3月7日09:38:11
     * @return: boolean 删除状态
     */
    public boolean deleteFtpFile (String filePath,String fileName){
        FTPClient ftpClient = null;
        boolean result = false;
        try {
            ftpClient = connectFtp();
            ftpClient.changeWorkingDirectory(filePath);
            result = ftpClient.deleteFile(new String(fileName.getBytes(StandardCharsets.UTF_8),"iso-8859-1"));
        } catch (IOException e) {
            e.printStackTrace();
            log.info("{}文件删除失败！",filePath+"/"+fileName);
        } finally {
            log.info("{}文件删除状态{}",filePath+"/"+fileName,result);
            disConnectFtp(ftpClient);
            return result;
        }
    }

    /**
     * @description: TODO 删除用户头像
     * @Param: filePath 文件路径，phone 手机号码
     * @author: tangxl
     * @date: 2022年3月7日09:38:11
     * @return: boolean 删除状态
     */
    public boolean deleteUserHead (String filePath,String phone){
        FTPClient ftpClient = null;
        boolean result = false;
        String headImgName = "";
        try {
            ftpClient = connectFtp();
            ftpClient.changeWorkingDirectory(filePath);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile ftpFile : ftpFiles) {
                if (ftpFile.getName().substring(0,ftpFile.getName().lastIndexOf(".")).equals(phone)) {
                    headImgName = ftpFile.getName();
                    result = ftpClient.deleteFile(new String(ftpFile.getName().getBytes(StandardCharsets.UTF_8),"iso-8859-1"));
				}
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("{}文件删除失败！",filePath+"/"+headImgName);
        } finally {
            log.info("{}文件删除状态{}",filePath+"/"+headImgName,result);
            disConnectFtp(ftpClient);
            return result;
        }
    }

    /**
     * @description: TODO 删除ftp文件夹
     * @Param: filePath 文件夹路径
     * @author: tangxl
     * @date: 2022年3月7日09:43:01
     * @return: boolean 删除状态
     */
    public boolean deleteDirectory(String filePath,String directoryName) {
        FTPClient ftpClient = null;
        boolean result = false;
        try {
            ftpClient = connectFtp();
            //切换到删除文件夹目录，清空里面文件
            ftpClient.changeWorkingDirectory(filePath+"/"+directoryName);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile ftpFile : ftpFiles) {
                ftpClient.deleteFile(new String(ftpFile.getName().getBytes(StandardCharsets.UTF_8),"iso-8859-1"));
            }
            //切换到删除文件夹前一级目录
            ftpClient.changeWorkingDirectory(filePath);
            //只能当文件夹中没有文件才能进行删除
            result = ftpClient.removeDirectory(directoryName);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("{}文件夹删除失败！",filePath+"/"+directoryName);
        }finally {
            log.info("{}文件夹删除状态{}",filePath+"/"+directoryName,result);
            disConnectFtp(ftpClient);
            return result;
        }
    }

    /**
     * @Description TODO 上传文件
     * @param serviceDec ftp服务保存地址
     * @param fileName 上传到ftp的文件名
     * @param inputStream 输入文件流
     * @return
     */
    public boolean uploadFile(String serviceDec, String fileName,InputStream inputStream){
        FTPClient ftpClient = null;
        try{
            ftpClient = connectFtp();
            log.info("=====>>文件{}上传至{}中",fileName,serviceDec);
            createDirectory(ftpClient,serviceDec);
            ftpClient.makeDirectory(serviceDec);
            ftpClient.changeWorkingDirectory(serviceDec);
            //上传文件
            ftpClient.storeFile(new String(fileName.getBytes(StandardCharsets.UTF_8),"iso-8859-1"), inputStream);
            log.info("=====>>Ftp上传{}成功!",fileName);
        }catch (Exception e) {
            log.error("=====>Ftp上传{}失败,原因{}",fileName,e);
            return false;
        }finally{
            try {
                if(ftpClient.isConnected()) {
                    disConnectFtp(ftpClient);
                }
                if(null != inputStream) {
                    inputStream.close();
                }

            } catch (IOException e) {
                log.error("=====>Ftp上传{}失败,原因{}",fileName,e);
                return false;
            }
            return true;
        }

    }

    /**
     * @Description TODO 初始化用户头像 -- 以用户手机号命名
     * @param filePath 文件路径
     * @param fileName 文件名称
     */
    public void addNewUserHead(String filePath, String fileName) {
        FTPClient ftpClient = null;
        ByteArrayInputStream  in = null;
        ByteArrayOutputStream out = null;
        String defaultImgName = Constans.HEAD_IMG_DEFAULT_NAME;
        try{
            ftpClient = connectFtp();
            ftpClient.changeWorkingDirectory(filePath);
            FTPFile[] fs = ftpClient.listFiles();
            int flag =0;
            boolean flagUp = false;
            for (FTPFile ff : fs) {
                if (ff.getName().equals(defaultImgName)) {
                    log.info("已查询到文件："+filePath+"/"+defaultImgName);
                    flag =1;
                    //读取文件，使用下载文件的方法把文件写入内存,绑定到out流上
                    out = new ByteArrayOutputStream();
                    ftpClient.retrieveFile(new String(ff.getName().getBytes("UTF-8"),"ISO-8859-1"), out);
                    in = new ByteArrayInputStream(out.toByteArray());
                    //文件复制，先读，再写
                    //二进制
                    ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                    log.info("=====>>文件{}上传至{}中",fileName,filePath);
                    flagUp = ftpClient.storeFile(new String(fileName.getBytes(StandardCharsets.UTF_8),"iso-8859-1"), in);
                }
            }
            if(flag==0){
                log.info("未查询到此文件："+filePath+"/"+defaultImgName);
                throw new RuntimeException("未查询到此文件!");
            }
            if (null == in) {
                throw new FileNotFoundException("未找到文件："+defaultImgName);
            }
            if(flag==1){
                log.info(filePath+"/"+defaultImgName+"文件获取成功！");
            }
            if(flagUp){
                log.info("生成头像成功！");
            }else{
                log.info("生成头像失败！");
            }

        }catch (Exception e) {
            log.error("=====>Ftp上传{}失败,原因{}",fileName,e);
            throw new RuntimeException("Ftp上传失败,原因{}",e);
        }finally{
            try {
                if(ftpClient.isConnected()) {
                    disConnectFtp(ftpClient);
                }
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("=====>Ftp上传{}失败,原因{}",fileName,e);
            }
        }
    }

    /**
     * @description: TODO 从Ftp获取多个文件然后放在一个压缩包中实现下载
     * @Param: filePath 目标路径，outputStream 文件输出流
     * @author: tangxl
     * @date: 2022年3月7日11:42:03
     * @return: null
     */
    public void downZipFile(String filePath,List<String> fileNameList, HttpServletResponse response) {
        FTPClient ftpClient = null;
        //定义压缩输出流
        ZipOutputStream zipOut = null;
        InputStream inputStream = null;
        try {
            zipOut = new ZipOutputStream(response.getOutputStream());
//            zipOut = new ZipOutputStream(new FileOutputStream("D:\\Txl\\主题详情.zip"));
            ftpClient = connectFtp();
            ftpClient.changeWorkingDirectory(filePath);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (String fileName:fileNameList) {
                //文件放入压缩包
                zipOut.putNextEntry(new ZipEntry(fileName));
                inputStream = ftpClient.retrieveFileStream(new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO-8859-1"));
                if (inputStream == null) {
                    log.error("{}文件流获取是失败！",fileName);
                    continue;
                }
                log.info("{}文件流获取成功！",fileName);
                int len = 0;
                byte[] buffer = new byte[1024];
                //获取ByteArrayOutputStream流
                while ((len = inputStream.read(buffer)) != -1 ) {
                    zipOut.write(buffer, 0, len);
                }
                inputStream.close();
                ftpClient.completePendingCommand();

//                for (FTPFile ftpFile : ftpFiles) {
//                    if (fileName.equals(ftpFile.getName())){
//                        //文件放入压缩包
//                        zipOut.putNextEntry(new ZipEntry(ftpFile.getName()));
//                        inputStream = ftpClient.retrieveFileStream(new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO-8859-1"));
//                        if (inputStream == null) {
//                            log.error("{}文件流获取是失败！",ftpFile.getName());
//                            continue;
//                        }
//                        log.info("{}文件流获取成功！",ftpFile.getName());
//                        int len = 0;
//                        byte[] buffer = new byte[1024];
//                        //获取ByteArrayOutputStream流
//                        while ((len = inputStream.read(buffer)) != -1 ) {
//                            zipOut.write(buffer, 0, len);
//                        }
//                        inputStream.close();
//                    }
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(zipOut != null ){
                    zipOut.closeEntry();
                    zipOut.flush();
                    zipOut.close();
                }

            }catch (IOException e) {
                log.error("关闭流出错！");
            }
            disConnectFtp(ftpClient);
        }
    }

}

