package com.vee.utils;/**
 * Arthur Administrator
 * date 2020/5/20.
 */

import com.alibaba.fastjson.JSON;
import com.vee.inter.Constants;
import com.vee.rsa.RSAEncrypt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@ClassName RsaUtils
 *@Description TODO
 *@Author Administrator
 *@Date 2020/5/20 19:52
 *@Version 1.0
 **/
public class RsaUtils {
//    public static final String PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCq3fkB22THdbg593Oq8kAbIU1JqMpJW0beFkyIRb6+f5YUjSsUcRdG2kSdaoJcqAusMyrzquOzMgOp2q8RoJGeQI/J7xcUJIitopnwWcqZyt9AzH/LUWwQutqmf7bNVF4rJOoaMmEKmHM+DjtlwHY3VcwrZ7x6BZUCLKSHsAVa5wIDAQAB";
//    public static final String PRI_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKrd+QHbZMd1uDn3c6ryQBshTUmoyklbRt4WTIhFvr5/lhSNKxRxF0baRJ1qglyoC6wzKvOq47MyA6narxGgkZ5Aj8nvFxQkiK2imfBZypnK30DMf8tRbBC62qZ/ts1UXisk6hoyYQqYcz4OO2XAdjdVzCtnvHoFlQIspIewBVrnAgMBAAECgYA2RGqWBuainMZGjstrQyZOF40S9GtO0hEXbxCehTw4B5CUdWN1yhdA9wis0EuX/v06E0Q0gJVx79QPCbfpisxEZxwREgNAxs4wfqdkMOkEsTgBZ+nK3YtlHP5/cG9FRN//fTgKufLUh8iRZ/oqikWB24ffl+FWIBhae6OGuh3zCQJBAO+PI8drqfUkur7BQTG8Vmtw2ue/NkARuFcUOO6PS/vzojISYTpLkEUSrKFrNrPKRxuXCVN7ilHNda+dKAFRclkCQQC2l/gSf7M9vN4jHuIh8lY2TPFg/Uf6mjy8jg/cYRl6P2wMlZdB3yvcsIQioueEwngCWSfc2/qcx8gQv0vinw8/AkBqEuyZ0c94z+Gss2/FsQpnGpqjgSRpbC8+x6KJ54OStYd9Omw43tqr6x/rfcjEBW5FBjTH9W0C2cIhl6nuI31JAkAuyj6dru+lkHJ0GYPlIw6Il6r7Kax6QW/y+YyC8rbuprXve53pGWFKCkmx/oDJenP6VhJq/Ev6RQ+1iu/NQOlNAkEAyFXZUMNQko7veLe0vEkRinz4y7pF21r5Jz6lF8U03Cc2BgO0laKWIlkTjPZMAVtoHjcnqd5UlsgFGSaLoGV8Tg==";
//    public static final String CORP_ID = "46bd2c06b52a4115a7567fe8e60adaf3";
    public static void main(String[] args) throws Exception{
        getIdCardNo("600120","ebff1acb3dd4ed545870bd0e8a5cbb824519d70a82d6124f27cb70a8663a88c6");
//        System.out.println(priDecode("GK2eyTE9HPDaEIkTqCTumD6YDZFUPLzA1GputYzWU6yZydwYgLTBZPyo0U46UC/3vqDIsSc9E/SfbThtGsO6dA0b8A2J7UL707Ep7Rrv/8Z0vxvrv07GfxUaRpARi5SiAMeoz6GYEabgpSUNQXrHnY/HEXZVsbtCfv7/uPvAfzY="));
//        System.out.println(priDecode("d+l0NarluB3B+F11XRwj/z6mopAeqP9rYlZeb+7f0eSkQX8I9f9AMogk6i2O3njChZ5VqxEjpRSCQjzpNBMwFlaBcEUZ10Etxpe0MUYe+KHAMWnaBtVnPS1oX4dhh9wveGO6QAmRgxQIQCfxBgFxRWBJGBRJnhQhZ1lL0ODPYlY="));
//        System.out.println(priDecode("G4mMbrT8Ei/7tDA/7GhEAxNhdLeHQMu4aBfsVtOjT3bablD6Tfx93sF9QnZHOxPA/hNke+QsIbqyMyBJ4Jl9BJwk0kBuOre6VByYiipMKlQRAeHN4XX2fBAUV5x+mqHTKoHtwPFZuWnqlkBSI5+c6YJru726JOeJRsUG943qfeU="));

    }

    public static Map<String,String> getIdCardNo(String appId,String loginCode) throws Exception{
        Map<String,String> cardMap= new HashMap<String, String>();
        String idCardNo = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp=df.format(new Date())+"000";
        StringBuffer signStr = new StringBuffer(0);
        signStr.append("corpid=").append(Constants.CORP_ID).append("&timestamp=").append(timestamp);
        String sign=Signature.rsaSign(signStr.toString(), Constants.PRI_KEY, "UTF-8");
        signStr.append("&sign=").append(sign);
        String url="https://openapi.cmsfg.com/api/user/userappuser?"+signStr.toString();
        StringBuffer body = new StringBuffer(0);
        body.append("{\"method\":\"GetUserAllRelateInfoForOut\",");
        body.append("\"params\":[{\"AppId\":\"").append(priEncode(appId)).append("\",");
        body.append("\"LoginCode\":\"").append(priEncode(loginCode)).append("\",");
        body.append("\"CorpId\":\"").append(Constants.CORP_ID).append("\",");
        body.append("\"Sign\":\"").append(sign).append("\"}]");
        body.append("}");
        String str = HttpUtils.httpPost(url,body.toString(),"application/json; charset=utf-8");
        Map<String,Object> result = getResutl(str);
        String code =result.get("code").toString();
        cardMap.put("code",code);
        if("0".equals(code)){
            Map<String,Object> map = (Map<String, Object>) result.get("result");
            List<Map<String,Object>> cards = (List<Map<String, Object>>) map.get("Cards");
            for(Map<String,Object> temp:cards){
                if("1".equals(temp.get("IsMain").toString())){
                    idCardNo = temp.get("IDCard").toString();
                    break;
                }
            }
        }
        cardMap.put("idCardNo",idCardNo);
        return cardMap;
    }

    public static Map<String,Object> getResutl(String str){
        System.out.println("返回结果："+str);
        Map<String,Object> result = new HashMap<String, Object>();
        if(str!=null && str.indexOf("{")>-1){
            result = JSON.parseObject(str,Map.class);
        }
        return result;
    }

    public static void test(String plainText) throws Exception{
        System.out.println(priDecode(pubEncode(plainText)));
        System.out.println(pubDecode(priEncode(plainText)));
    }

    public static String pubEncode(String plainText)throws Exception{
        //公钥加密过程
        byte[] cipherData= RSAEncrypt.encrypt(RSAEncrypt.loadPublicKeyByStr(Constants.PUB_KEY),plainText.getBytes());
        return com.vee.rsa.Base64.encode(cipherData);
    }
    public static String priEncode(String plainText)throws Exception{
        //私钥加密过程
        byte[] cipherData=RSAEncrypt.encrypt(RSAEncrypt.loadPrivateKeyByStr(Constants.PRI_KEY),plainText.getBytes());
        return com.vee.rsa.Base64.encode(cipherData);
    }
    public static String pubDecode(String cipher)throws Exception{
        //公钥解密过程
        byte[] res=RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStr(Constants.PUB_KEY), com.vee.rsa.Base64.decode(cipher));
        return new String(res);
    }
    public static String priDecode(String cipher)throws Exception{
        //私钥解密过程
        byte[] res=RSAEncrypt.decrypt(RSAEncrypt.loadPrivateKeyByStr(Constants.PRI_KEY), com.vee.rsa.Base64.decode(cipher));
        return new String(res);
    }
}
