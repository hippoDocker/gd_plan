package com.gd.base.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @Auther: tangxl
 * @Date: 2022年2月17日18:31:26
 * @Description: Excell工具类
 */
@Log4j2
public class EasyExcelUtils {
    private static final String EXCEL_FILE_XLSX = ".xlsx";
    private static final String EXCEL_FILE_XLS = ".xls";

    /**
     * @param clazzTo 导出类
     * @param data 集合数据
     * @param fileName 导出文件名称
     * @param response
     */
//    public static void writeExcell(Class<T> clazzTo, List<T> data, String fileName, HttpServletResponse response){
//
//    }

    /**
     * Excell模板下载
     * @param response
     * @param path 模板路径
     * @param fileName 文件名称
     * @throws IOException
     */
    public static void downExcelTemplate(HttpServletResponse response, String path, String fileName,Class classTo) throws IOException {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        InputStream inputStream = classTo.getResourceAsStream(path);
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
        outputStream.close();
    }

    /**
     * 判断传的文件是不是excel文件
     */
    public static Boolean isExcelFile(MultipartFile multipartFile) {
        return multipartFile != null && !multipartFile.isEmpty() && (
                multipartFile.getOriginalFilename().indexOf(EXCEL_FILE_XLSX) > 0
                        || multipartFile.getOriginalFilename().indexOf(EXCEL_FILE_XLS) > 0);
    }





}


//    private static final Logger LOGGER = LoggerFactory.getLogger(ExcellUtil.class);
//
//    private ExcellUtil() {}
//
//    public static <T> ExcelReaderBuilder read(String pathName, Class<T> head, Integer pageSize, Consumer<List<T>> consumer) {
//        return read(pathName, head, new EasyExcelConsumerListener<>(pageSize, consumer));
//    }
//
//    public static <T> ExcelReaderBuilder read(File file, Class<T> head, Integer pageSize, Consumer<List<T>> consumer) {
//        return read(file, head, new EasyExcelConsumerListener<>(pageSize, consumer));
//    }
//
//    public static <T> ExcelReaderBuilder read(InputStream inputStream, Class<T> head, Integer pageSize, Consumer<List<T>> consumer) {
//        return read(inputStream, head, new EasyExcelConsumerListener<>(pageSize, consumer));
//    }
//
//    public static void write(HttpServletResponse response, List<T> data, String fileName, String sheetName, Class clazz) throws Exception {
//        EasyExcel.write(getOutputStream(fileName, response), clazz)
//                .excelType(ExcelTypeEnum.XLSX)
//                .sheet(sheetName)
//                .registerWriteHandler(buildCellStyle())
//                .registerWriteHandler(new CustomColumnWidthHandler())
//                .doWrite(data);
//    }
//
//    public static OutputStream getOutputStream(String fileName, HttpServletResponse response) throws Exception {
//        fileName = URLEncoder.encode(fileName, "UTF-8");
//        response.setContentType("application/vnd.ms-excel");
//        response.setCharacterEncoding("utf8");
//        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
//        return response.getOutputStream();
//    }
//
//    /**
//     * 获取表头
//     */
//    public static Map<String, String> getHeadMap(Class<?> aClass) {
//        Map<String, String> HeadMap = new LinkedHashMap<>();
//        Field[] declaredFields = aClass.getDeclaredFields();
//        ExcelProperty excelProperty;
//        for (Field field : declaredFields) {
//            if (field != null) {
//                field.setAccessible(true);
//                if (field.isAnnotationPresent(ExcelProperty.class)) {
//                    excelProperty = field.getAnnotation(ExcelProperty.class);
//                    HeadMap.put(field.getName(), StringUtils.join(Arrays.asList(excelProperty.value()), ","));
//                }
//            }
//        }
//        return HeadMap;
//    }
//
//    /**
//     * 生成通用表格样式
//     */
//    public static HorizontalCellStyleStrategy buildCellStyle(){
//        //表头样式
//        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
//        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);//设置水平对齐的样式为居中对齐;
//        headWriteCellStyle.setFillForegroundColor(IndexedColors.TEAL.getIndex());
//        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); //设置垂直对齐的样式为居中对齐;
//        WriteFont font = new WriteFont();
//        font.setFontName("Microsoft YaHei Light");
//        font.setColor(IndexedColors.WHITE.getIndex());
//        font.setFontHeightInPoints((short) 11);
//        headWriteCellStyle.setWriteFont(font);
//        //内容样式
//        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
//        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
//        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
//    }
