package com.gd.base.util;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;

/**
 * @Auther: tangxl
 * @Date: 2021年12月24日11:34:57
 * @Description: TODO 文件工具类
 */
@Log4j2
public class FileUtil {
    /**
     * @Description TODO 字节输入流转换为包含二进制数据+文件名称的MultipartFile文件
     * @param fileName 文件名
     * @param inputStream 字节输入流
     * @return
     */
    public static MultipartFile inputStreamToMultipartFile(String fileName, InputStream inputStream) throws IOException {
        //该方法只能用于测试return new MockMultipartFile(fileName,fileName, MediaType.MULTIPART_FORM_DATA_VALUE,inputStream);
        FileItem fileItem = createFileItem(inputStream, fileName);
        //CommonsMultipartFile是feign对multipartFile的封装，但是要FileItem类对象
        return new CommonsMultipartFile(fileItem);
    }

    /**
     * @Description TODO FileItem类对象创建
     * @param inputStream inputStream
     * @param fileName    fileName
     * @return FileItem
     */
    public static FileItem createFileItem(InputStream inputStream, String fileName) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "file";
        FileItem item = factory.createItem(textFieldName, MediaType.MULTIPART_FORM_DATA_VALUE, true, fileName);
        int bytesRead = 0;
        byte[] buffer = new byte[10 * 1024 * 1024];
        OutputStream os = null;
        //使用输出流输出输入流的字节
        try {
            os = item.getOutputStream();
            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        } catch (IOException e) {
            log.error("=====>>流关闭异常!", e);
            throw new IllegalArgumentException("=====>>文件写入失败！");
        } finally {
            //关闭流
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    log.error("=====>>流关闭异常!", e);

                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("=====>>流关闭异常!", e);
                }
            }
        }

        return item;
    }

    /**
     * @Description TODO MultipartFile类型转File类型
     * @param multipartFile MultipartFile类型
     * @return file File类型
     * @throws IOException
     */
    public static File MultipartFileToFile(MultipartFile multipartFile)  {
        InputStream inputStream = null;
        File file = null;
        String fileName = "";
        try {
            inputStream = multipartFile.getInputStream();
            fileName = multipartFile.getOriginalFilename();
            file = new File(fileName);
            file = inputStreamToFile(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("=====>>MultipartFile类型转File类型出错！");
        }finally {
            return file;
        }
    }

    /**
     * @Description TODO 输入流InputStream转File类型
     * @param inputStream 输入流
     * @return file File类型
     */
    public static File inputStreamToFile(InputStream inputStream) {
        File file = null;
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("=====>>输入流InputStream转File类型出错！");
        }finally {
            //关闭流
            try {
                if(outputStream !=null){
                    outputStream.close();

                }
                if(inputStream!=null){
                    inputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
                log.error("=====>>关闭流出错！");
            }
            return file;
        }
    }


}
