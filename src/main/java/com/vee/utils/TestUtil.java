package com.vee.utils;/**
 * Arthur Administrator
 * date 2020/5/14.
 */

import com.alibaba.fastjson.JSON;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 *@ClassName TestUtil
 *@Description TODO
 *@Author Administrator
 *@Date 2020/5/14 20:03
 *@Version 1.0
 **/
public class TestUtil {
    public static String method1(){
        try {
            // 1 指定WebService服务的请求地址：
            String wsUrl = "http://webservice.sptdch.cn:8082/WebInvoice.asmx/InvoiceTransData";
            // 2 创建URL：
            URL url = new URL(wsUrl);
            // 3 建立连接，并将连接强转为Http连接
            URLConnection conn = url.openConnection();
            HttpURLConnection con = (HttpURLConnection) conn;

            // 4，设置请求方式和请求头：
            con.setDoInput(true); // 是否有入参
            con.setDoOutput(true); // 是否有出参
            con.setRequestMethod("POST"); // 设置请求方式
            con.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            // 5，手动构造请求体
            String strJson = "{\"head\":{\"tradetime\": \"2020-05-14 14:24:48\",\"tradename\": \"KPQQ\",\"chanel\":\"3\"},\"body\":{\"serialno\":\"81339577\",\"patienttype\": \"门诊\"}}";

            String requestBody = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
            requestBody += "<soap:Body>";
            requestBody += "<InvoiceTransData xmlns=\"http://tempuri.org/\">";
            requestBody += "<strInJson>" + strJson + "</strInJson> ";
            requestBody += "</InvoiceTransData>";
            requestBody += "</soap:Body>";
            requestBody += "</soap:Envelope>";
            // 6，通过流的方式将请求体发送出去：
            OutputStream out = con.getOutputStream();
            String aa = "strInJson="+strJson;
            byte[] data = aa.getBytes();
//            out.write(requestBody.getBytes());
            out.write(data,0,data.length);
            out.close();
            // 7，服务端返回正常：
            int code = con.getResponseCode();
//            if (code == 200) {// 服务端返回正常
                InputStream is = con.getInputStream();
                byte[] b = new byte[1024];
                StringBuffer sb = new StringBuffer();
                int len = 0;
                while ((len = is.read(b)) != -1) {
                    String str = new String(b, 0, len, "UTF-8");
                    sb.append(str);
                }
                System.out.println(sb.toString());
                is.close();
//            }
            con.disconnect();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        TestUtil.method1();
    }
}
