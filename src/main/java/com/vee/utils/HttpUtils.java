package com.vee.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by weilishan on 2020/5/14.
 */
public class HttpUtils {
    public static String httpPostWs(String wsUrl,String param){
        System.out.println("url="+wsUrl);
        System.out.println("param="+param);
        String result = "";
        try {
            // 1 创建URL：
            URL url = new URL(wsUrl);
            // 2 建立连接，并将连接强转为Http连接
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            HttpURLConnection con = (HttpURLConnection) conn;

            // 3，设置请求方式和请求头：
            con.setDoInput(true); // 是否有入参
            con.setDoOutput(true); // 是否有出参
            con.setRequestMethod("POST"); // 设置请求方式
            con.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            con.setConnectTimeout(5 * 1000);

            // 4，通过流的方式将请求体发送出去：
            OutputStream out = con.getOutputStream();
            byte[] data = param.getBytes();
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
//        url=http://webservice.sptdch.cn:8082/WebInvoice.asmx/InvoiceTransData
//        param=strInJson={"head":{"chanel":"3","tradename":"KPQQ","tradetime":"2020-01-01 00:00:00"},"body":{"patienttype":"住院","serialno":"2483740"}}
//        param=strInJson={"head":{"tradetime": "2020-05-14 14:24:48","tradename": "KPQQ","chanel":"3"},"body":{"serialno":"81339577","patienttype": "门诊"}}
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