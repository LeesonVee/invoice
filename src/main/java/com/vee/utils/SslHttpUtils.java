package com.vee.utils;

 import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.net.URL;
 import java.net.URLEncoder;
 import java.security.cert.CertificateException;
 import java.security.cert.X509Certificate;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Map;
 import java.util.Map.Entry;

 import javax.net.ssl.HostnameVerifier;
 import javax.net.ssl.HttpsURLConnection;
 import javax.net.ssl.SSLContext;
 import javax.net.ssl.SSLSession;
 import javax.net.ssl.SSLSocketFactory;
 import javax.net.ssl.TrustManager;
 import javax.net.ssl.X509TrustManager;

 import org.apache.commons.lang.StringUtils;



public class SslHttpUtils {
    public static final String CHARSET = "UTF-8";

    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;

    /**
     * 初始化http请求参数
     *
     * @param url
     * @param method
     * @return
     * @throws Exception
     */
    protected static HttpsURLConnection initHttps(String url, String method) throws Exception {
        TrustManager[] tm =  {new MyX509TrustManager()};
        System.setProperty("https.protocols", "TLSv1");
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tm, new java.security.SecureRandom());
        //sslContext.init(null, tm, null);
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        URL _url = new URL(url);
        HttpsURLConnection http = (HttpsURLConnection) _url.openConnection();
        // 设置域名校验
        http.setHostnameVerifier(new TrustAnyHostnameVerifier());
        // 连接超时
        http.setConnectTimeout(DEF_CONN_TIMEOUT);
        // 读取超时 --服务器响应比较慢，增大时间
        http.setReadTimeout(DEF_READ_TIMEOUT);
        http.setUseCaches(false);
        http.setRequestMethod(method);
        http.setSSLSocketFactory(ssf);
        http.setDoOutput(true);
        http.setDoInput(true);
        try {
            http.connect();
        } catch (Exception ex) {
        }

        return http;
    }


    /**
     * 功能描述: 构造请求参数
     *
     * @return 返回类型:
     * @throws Exception
     */
    public static String initParams(String url, Map<String, Object> params)
            throws Exception {
        if (null == params || params.isEmpty()) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (url.indexOf("?") == -1) {
            sb.append("?");
        }
        sb.append(map2Url(params));
        return sb.toString();
    }

    /**
     * map构造url
     *
     * @return 返回类型:
     * @throws Exception
     */
    public static String map2Url(Map<String, Object> paramToMap)
            throws Exception {
        if (null == paramToMap || paramToMap.isEmpty()) {
            return null;
        }
        StringBuffer url = new StringBuffer();
        boolean isfist = true;
        for (Entry<String, Object> entry : paramToMap.entrySet()) {
            if (isfist) {
                isfist = false;
            } else {
                url.append("&");
            }
            url.append(entry.getKey()).append("=");
            String value = entry.getValue().toString();
            if (!StringUtils.isEmpty(value)) {
                url.append(URLEncoder.encode(value, CHARSET));
            }
        }
        return url.toString();
    }

    /**
     * 检测是否https
     *
     * @param url
     */
    protected static boolean isHttps(String url) {
        return url.startsWith("https");
    }

    /**
     * https 域名校验
     *
     * @return
     */
    public static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;// 直接返回true
        }
    }


    public static class MyX509TrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }



    }




    /**
     * 忽视证书HostName
     */
    private static HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
        public boolean verify(String s, SSLSession sslsession) {
            System.out.println("WARNING: Hostname is not matched for cert.");
            return true;
        }
    };

    /**
     * Ignore Certification
     */
    private static TrustManager ignoreCertificationTrustManger = new X509TrustManager(){
        private X509Certificate[] certificates;
        public void checkClientTrusted(X509Certificate certificates[],
                                       String authType) throws CertificateException {
            if (this.certificates == null) {
                this.certificates = certificates;
            }
        }
        public void checkServerTrusted(X509Certificate[] ax509certificate,
                                       String s) throws CertificateException {
            if (this.certificates == null) {
                this.certificates = ax509certificate;
            }
        }
        public X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }
    };

    public static  String sendSSLGetMethod(String urlString) throws Exception{
        String repString = null;
        InputStream is = null;
        HttpsURLConnection connection = null;
        try {

            URL url = new URL(urlString);
            /*
             * use ignore host name verifier
             */
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            connection = (HttpsURLConnection) url.openConnection();
            // Prepare SSL Context
            TrustManager[] tm = { ignoreCertificationTrustManger };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            connection.setSSLSocketFactory(ssf);
            if(connection.getResponseCode() != 200){

            }
            is = connection.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String valueString = null;
            StringBuffer bufferRes = new StringBuffer();
            while ((valueString = read.readLine()) != null) {
                bufferRes.append(valueString);
            }
            return bufferRes.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(null != is){
                is.close();
                is = null;
            }
            if(null != connection){
                connection.disconnect();
            }
        }
        return repString;
    }

    public static String sendSSLPostMethod(String urlString,String postData) throws Exception{
        String repString = null;
        InputStream is = null;
        HttpsURLConnection connection = null;
        try {

            URL url = new URL(urlString);
            /*
             * use ignore host name verifier
             */
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            // Prepare SSL Context
            TrustManager[] tm = { ignoreCertificationTrustManger };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(ssf);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type","text/json");
            connection.setRequestProperty("content-length",String.valueOf(postData.getBytes().length));
            connection.getOutputStream().write(postData.getBytes("utf-8"));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();

            if(connection.getResponseCode() != 200){

            }
            is = connection.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String valueString = null;
            StringBuffer bufferRes = new StringBuffer();
            while ((valueString = read.readLine()) != null) {
                bufferRes.append(valueString);
            }
            return bufferRes.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(null != is){
                is.close();
                is = null;
            }
            if(null != connection){
                connection.disconnect();
            }
        }
        return repString;
    }


    public static String sendSSLPostMethod(String urlString,Map<String, Object> params){
        String repString = null;
        InputStream is = null;
        HttpsURLConnection connection = null;
        try {

            URL url = new URL(initParams(urlString,params));
            /*
             * use ignore host name verifier
             */
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            // Prepare SSL Context
            TrustManager[] tm = { ignoreCertificationTrustManger };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(ssf);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type","text/json");
            //connection.setRequestProperty("content-length",String.valueOf(postData.getBytes().length));
            //connection.getOutputStream().write(postData.getBytes("utf-8"));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();

            if(connection.getResponseCode() != 200){

            }
            is = connection.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String valueString = null;
            StringBuffer bufferRes = new StringBuffer();
            while ((valueString = read.readLine()) != null) {
                bufferRes.append(valueString);
            }
            return bufferRes.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(null != is){
                try {
                    is.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                is = null;
            }
            if(null != connection){
                connection.disconnect();
            }
        }
        return repString;
    }

}
