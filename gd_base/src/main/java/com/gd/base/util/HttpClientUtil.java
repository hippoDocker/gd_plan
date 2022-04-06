package com.gd.base.util;

import com.alibaba.fastjson.JSON;
import com.gd.base.pojo.dto.sys.ParticipleResponseDTO;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @Auther: tangxl
 * @Date: 2022年1月21日14:13:33
 * @Description: 第三方接口调用
 */
public class HttpClientUtil {
    public static final String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    public static final int DEF_CONN_TIMEOUT = 120000;//120秒
    public static final int DEF_READ_TIMEOUT = 120000;
    public static final String DEF_CHATSET = "UTF-8";
    /**
     * @Description: TODO 带参Get请求
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String,String> params){
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            //创建URI
            URIBuilder uriBuilder = new URIBuilder(url);
            //设置参数
            if(params!=null){
                for(String key:params.keySet()){
                    uriBuilder.addParameter(key,params.get(key));
                }
            }
            URI uri = uriBuilder.build();
            //创建http Get请求
            HttpGet httpGet = new HttpGet(uri);
            //执行请求
            response = httpClient.execute(httpGet);
            //判断返回的状态是否为200
            System.out.println("=====>>接口调用"+url+"返回状态"+response.getStatusLine().getStatusCode());
            if(response.getStatusLine().getStatusCode() == 200){
                resultString = EntityUtils.toString(response.getEntity(),"UTF-8");
            }else {
                throw new RuntimeException("=====>>"+url+"接口调用失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null){
                    response.close();
                }
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    /**
     * 创建Http连接并调用接口数据Post+Get
     */
    public static String getNetData(String strUrl, Map param, String method) throws Exception{
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String res = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(null == method || "GET".equals(method)){
                strUrl = strUrl + "?" + urlEncode(param);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(null == method || "GET".equals(method)){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent",userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (null != param && "POST".equals(method)){
                try {
                    DataOutputStream dos =new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(urlEncode(param));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while (null != (strRead = reader.readLine())){
                sb.append(strRead);
            }
            res = sb.toString();
        }catch (IOException e){
            e.printStackTrace();
            throw new Exception();
        }finally {
            if (null != reader){
                reader.close();
            }
            if (null != conn){
                conn.disconnect();
            }
        }
        return res;
    }

    /**
     * 转码
     */
    public static String urlEncode(Map<String,Object>data){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {

//        //青云客智能机器人
//        String msg = "";
//        Scanner sc = new Scanner(System.in);
//        Map param = new HashMap();  // 配置
//        param.put("key","free");
//        param.put("appid","0");
//        while (!"结束".equals(msg)){
//            System.out.print("请输入对话:");
//            msg = sc.next();
//            String url = "http://api.qingyunke.com/api.php";
//            param.put("msg",msg);
//            String res = getNetData(url, param, "GET");
//            SysTalkDTO sysTalkDTO = JSON.parseObject(res,SysTalkDTO.class);
//            if(StringUtils.isNotEmpty(sysTalkDTO.getContent()) || sysTalkDTO.getContent().contains("菲菲")){
//                sysTalkDTO.setContent(sysTalkDTO.getContent().replaceAll("菲菲", "小岑"));
//            }
//            System.out.println("回答:"+sysTalkDTO.getContent());
//        }


        //智能分词
        String getUrl= "http://api.pullword.com/get.php";//get请求路径
        String postUrl = "http://api.pullword.com/post.php";//Post请求路径
        String text = "";
        Map params = new LinkedHashMap();
        Scanner sc = new Scanner(System.in);
        while (!"结束".equals(text)) {
            System.out.print("请输入文本:");
            text = sc.next();
            params.put("source",text);//分词语句
            params.put("param1",0);//保留准确概率(0~1之间的小数)
            params.put("param2",1);//调试模式:param2 = 0调试模式关闭,param2 = 1调试模式打开(显示每个单词的准确概率)
            params.put("json",1);//可选参数:json = 1以json格式返回,json = 0不以json格式返回
            String res = getNetData(getUrl,params,"GET");
            List<ParticipleResponseDTO> responseDTO = JSON.parseArray(res, ParticipleResponseDTO.class);
            responseDTO.stream().forEach(o->{
                System.out.println("单词:"+o.getT()+";概率:"+o.getP());
            });
        }
    }
}
