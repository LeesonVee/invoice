package com.vee.utils;

import com.sun.deploy.net.HttpResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by weilishan on 2020/5/14.
 */
public class HttpUtils {
//    "application/x-www-form-urlencoded;charset=utf-8"
    public static String httpPost(String reqUrl,String param,String contentType){
        System.out.println("url="+reqUrl);
        System.out.println("param="+param);
        String result = "";
        try {
            // 1 创建URL：
            URL url = new URL(reqUrl);
            // 2 建立连接，并将连接强转为Http连接
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            HttpURLConnection con = (HttpURLConnection) conn;

            // 3，设置请求方式和请求头：
            con.setDoInput(true); // 是否有入参
            con.setDoOutput(true); // 是否有出参
            con.setRequestMethod("POST"); // 设置请求方式
            con.setRequestProperty("content-type", contentType);
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            con.setConnectTimeout(5 * 1000);

            // 4，通过流的方式将请求体发送出去：
            OutputStream out = con.getOutputStream();
            byte[] data = param.getBytes("UTF-8");
            out.write(data,0,data.length);
            out.close();
            // 5，服务端返回正常：
            int code = con.getResponseCode();
            if (code == 200) {// 服务端返回正常
                InputStream is = con.getInputStream();
                byte[] b = new byte[1024];
                StringBuffer sb = new StringBuffer();
                int len = 0;
                while ((len = is.read(b)) != -1) {
                    String str = new String(b, 0, len, "UTF-8");
                    sb.append(str);
                }
                result = sb.toString();
                is.close();
            }
            con.disconnect();
        } catch (Exception e) {
            System.err.println("请求异常"+e.getLocalizedMessage());
        }
        return result;
    }
    public static String httpPostWs(String wsUrl,String param){
        return httpPost(wsUrl,param,"application/x-www-form-urlencoded;charset=utf-8");
    }

    public static String doGet(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpurl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            connection.setRequestProperty("content-type", "application/json; charset=utf-8");
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
    }

    public static void main(String[] args) {
        String url = "http://webservice.sptdch.cn:8082/WebInvoice.asmx/InvoiceTransData";
//        String params = "strInJson={\"head\":{\"tradetime\": \"2020-05-09 13:44:48\",\"tradename\": \"JYMXXX\",\"chanel\":\"3\"},\"body\":{\"cardno\": \"Z00001946\",\"idcardno\": \"330328197302194420\",\"starttime\": \"2020-05-12 12:12:48\",\"endtime\":\"2020-05-14 23:59:59\"}}";
        String strJson = "strInJson={\"head\":{\"tradetime\": \"2020-05-14 14:24:48\",\"tradename\": \"KPQQ\",\"chanel\":\"3\"},\"body\":{\"serialno\":\"81339577\",\"patienttype\": \"门诊\"}}";
//        strJson = "strInJson={\"head\":{\"chanel\":\"3\",\"tradename\":\"KPQQ\",\"tradetime\":\"2020-01-01 00:00:00\"},\"body\":{\"patienttype\":\"住院\",\"serialno\":\"2483740\"}}";
        strJson = "strInJson={\"head\":{\"tradetime\":\"2020-04-30 08:24:40\",\"tradename\":\"KPQQ\",\"chanel\":\"3\"},\"body\":{\"serialno\":\"81239017\",\"patienttype\":\"门诊\"}}";
        System.out.println(HttpUtils.httpPostWs(url,strJson));
    }
}
