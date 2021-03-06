package com.vee.controller;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.vee.inter.Constants;
import com.vee.utils.DateUtil;
import com.vee.utils.HttpUtils;
import com.vee.utils.RsaUtils;
import com.vee.utils.XmlAndString;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

@Controller
public class SystemController extends JSONOutputMVCConroller {
	private static final Logger logger = LoggerFactory.getLogger(SystemController.class);
	private static String CODE = "200";
	private static String MSG = "success";
	private static String[] INVOICE_TYPE = {"JYMXXX","KPQQ"};

	@Value("${ws_url}")
	private String WS_URL;
	@Value("${ws_base_url}")
	private String WS_BASE_URL;
	@Value("${chanel_item}")
	private String CHANEL_ITEM;

	@RequestMapping(value = "/index", method = {RequestMethod.GET,RequestMethod.POST})
	public String showBlogs(HttpServletRequest req,HttpServletResponse res) throws Exception{
		logger.info("进入showBlogs方法...");
		String idCardNo=null;
		String platformType = req.getParameter("platformType");
		if(platformType==null || "1".equals(platformType)){
			String appId=req.getParameter("AppId");
			String loginCode=req.getParameter("LoginCode");
			if(appId==null || loginCode==null){
				logger.error("appId=【"+appId+"】,loginCode=【"+loginCode+"】");
				throw new Exception("入参错误");
			}
			Map<String,String> cardMap =RsaUtils.getIdCardNo(appId,loginCode);
			if(cardMap.isEmpty() || "1".equals(cardMap.get("code"))){
				logger.error("解析身份信息失败："+JSON.toJSONString(cardMap));
				return "index_error";
			}
			idCardNo = cardMap.get("idCardNo");
			if(idCardNo==null || "".equals(idCardNo)){
				logger.error("无有效身份信息");
				return "index_error";
			}
			logger.info("身份证号码："+idCardNo);
			req.getSession().setAttribute("idCardNo",idCardNo);
			req.getSession().setAttribute("cardNo",req.getParameter("cardNo"));
			return "index";
		}else if("2".equals(platformType)){
			idCardNo = req.getParameter("idCardNo");
			if(idCardNo==null || "".equals(idCardNo)){
				logger.error("身份证信息为空");
				return "form";
			}else{
				logger.info("身份证号码："+idCardNo);
				req.getSession().setAttribute("idCardNo",idCardNo);
				req.getSession().setAttribute("cardNo",req.getParameter("cardNo"));
				return "index";
			}
		}
		return "form";
	}

	@RequestMapping(value="/checkIdCardNo",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void checkIdCardNo(HttpServletRequest req,HttpServletResponse res) throws Exception{
		logger.info("进入checkIdCardNo方法...");
		boolean flag = false;
		String idCardNo = req.getParameter("idCardNo")==null?"":req.getParameter("idCardNo");
		String idCardName = req.getParameter("name")==null?"":req.getParameter("name");
		logger.info("身份证号码："+idCardNo);
		StringBuffer sb = new StringBuffer(0);
		sb.append("strInJson={\"").append("head\":{");
		sb.append("\"tradetime\":").append("\"").append(DateUtil.getDateFormatter()).append("\",");
		sb.append("\"tradename\":\"").append(INVOICE_TYPE[0]).append("\",");
		sb.append("\"chanel\":\"1\"},");
		sb.append("\"body\":{").append("\"name\":\"").append(idCardName).append("\",");
		sb.append("\"idcardno\":\"").append(idCardNo).append("\"}}");
		String xmlStr = HttpUtils.httpPostWs(WS_BASE_URL,sb.toString());
		String jsonString="";
		if(xmlStr!=""){
			jsonString = XmlAndString.stringxmlToString(xmlStr,"/string");
			System.out.println(jsonString);
		}
		if(jsonString==""){
			jsonString = "{\"message\":{\"errcode\":\"0\",\"info\":false}}";
		}
		Map<String,Object> map = JSON.parseObject(jsonString,Map.class);
		Map<String,Object> messageMap = (Map<String, Object>) map.get("message");
		if("1".equals(messageMap.get("errcode"))){
			Map<String,Object> info = (Map<String, Object>) messageMap.get("info");
			flag = Boolean.parseBoolean(info.get("tradeinfo").toString());
		}
		flag = true;
		jsonOutput(res, flag,false);
	}

	@RequestMapping(value="/queryInvoiceInfo",method = RequestMethod.POST)
	@ResponseBody
	public void queryInvoiceInfo(HttpServletRequest req,HttpServletResponse res) throws Exception{
		logger.info("进入queryInvoiceInfo方法...");
		HttpSession session = req.getSession();
		if(session.getAttribute("idCardNo")==null){
			throw new Exception("身份证信息不能为空");
		}
		String idCardNo = session.getAttribute("idCardNo").toString();
		logger.info("身份证号码："+idCardNo);
		String cardNo = session.getAttribute("cardNo")==null?"":session.getAttribute("cardNo").toString();
		String starttime = req.getParameter("starttime");
		String endtime = req.getParameter("endtime");
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> headMap = new HashMap<String,Object>();
		Calendar cal = Calendar.getInstance();
		headMap.put("tradetime",cal.get(Calendar.YEAR)+"-01-01 00:00:00");
		headMap.put("tradename",INVOICE_TYPE[0]);
		headMap.put("chanel",CHANEL_ITEM);
		data.put("head",headMap);
		Map<String,Object> bodyMap = new HashMap<String,Object>();
		bodyMap.put("cardno",cardNo);
		bodyMap.put("idcardno",idCardNo);
		bodyMap.put("starttime",starttime);
		bodyMap.put("endtime",endtime);
		data.put("body",bodyMap);
		String reqStr = "strInJson="+JSON.toJSONString(data);
		String xmlStr = HttpUtils.httpPostWs(WS_URL,reqStr);
		String jsonString="";
		if(xmlStr!=""){
			jsonString = XmlAndString.stringxmlToString(xmlStr,"/string");
			System.out.println(jsonString);
		}
		if(jsonString==""){
			jsonString = "{\"message\":{\"errcode\":\"0\",\"info\":{\"tradeinfo\":\"[]\"}}}";
		}
//		jsonString="{\"message\":{\"errcode\":\"1\",\"info\":{\"tradeinfo\":\"[{\\\"SERIALNO\\\":\\\"2367216\\\",\\\"TRADETIME\\\":\\\"2019-12-23 08:57:50\\\",\\\"AMOUNT\\\":26366.08,\\\"TRADETYPE\\\":\\\"住院\\\",\\\"STATUS\\\":\\\"已开票\\\"},{\\\"SERIALNO\\\":\\\"2367214\\\",\\\"TRADETIME\\\":\\\"2019-12-23 08:53:59\\\",\\\"AMOUNT\\\":35102.08,\\\"TRADETYPE\\\":\\\"住院\\\",\\\"STATUS\\\":\\\"未开票\\\"},{\\\"SERIALNO\\\":\\\"2367212\\\",\\\"TRADETIME\\\":\\\"2019-12-23 08:43:10\\\",\\\"AMOUNT\\\":35102.08,\\\"TRADETYPE\\\":\\\"住院\\\",\\\"STATUS\\\":\\\"未开票\\\"}]\"}}}";
		jsonOutput(res, JSON.parseObject(jsonString),false);

	}
	@RequestMapping(value="/queryInvoiceItemInfo",method = RequestMethod.POST)
	@ResponseBody
	public void queryInvoiceItemInfo(HttpServletRequest req,HttpServletResponse res) throws Exception{
		logger.info("进入queryInvoiceItemInfo方法...");
		logger.info("身份证号码："+req.getSession().getAttribute("idCardNo"));
		List<String> imgs = new ArrayList<String>();
		Map<String,Object> result = new HashMap<String, Object>();
		this.resMap(result);
		StringBuffer sb = new StringBuffer(0);
		sb.append("strInJson={\"").append("head\":{");
		sb.append("\"tradetime\":").append("\"").append(req.getParameter("TRADETIME")).append("\",");
		sb.append("\"tradename\":\"").append(INVOICE_TYPE[1]).append("\",");
		sb.append("\"chanel\":\"3\"},");
		sb.append("\"body\":{").append("\"serialno\":\"").append(req.getParameter("SERIALNO")).append("\",");
		sb.append("\"patienttype\":\"").append(req.getParameter("TRADETYPE")).append("\"}}");
		String xmlStr = HttpUtils.httpPostWs(WS_URL,sb.toString());
		String jsonString="";
		if(xmlStr!=""){
			jsonString = XmlAndString.stringxmlToString(xmlStr,"/string");
			logger.info("获取发票URL返回数据："+jsonString);
		}
		if(jsonString==""){
			jsonString = "{\"message\":{\"errcode\":\"0\",\"info\":{\"errinfo\":\"不能识别请求中的开票条件！\"}}}";
		}
//		jsonString="{\"message\":{\"errcode\":\"1\",\"info\":{\"url\":\"http://webservice.sptdch.cn:8001/agency-front/agency/invoice/computerbill/industryMain.do?noise=cccdabb6780343abbbb26adbf193d800&eBillPicUrls=1a1050027010d3e3ae424b3495bb631b8e128b0125f9d6528020eb6a9cd82672739ddcc60d851d06e7992b719509a06d41af8d6b7e9f1ab8fd394b0ed022b38978edbb1d091e1e51616bcf0d82891c62d2834ce421b7bd3d8cf566f4778acbc201e6730a5f5fb85275c8bcf532530dfbc5103e46dbcf7a8f1b547d219b54427535b4c6f49c56d24b22dc90879229cb4aafc9401bba1cc31e452a85b0bb232eb7a8bbbf24c61715ae9c53f1379c51a8df\"}}}";
		Map<String,Object> map = JSON.parseObject(jsonString,Map.class);
		Map<String,Object> messageMap = (Map<String, Object>) map.get("message");
		if("1".equals(messageMap.get("errcode"))){
			Map<String,Object> info = (Map<String, Object>) messageMap.get("info");
			if(info.get("url")!=null){
				imgs = this.getImgPaths(info.get("url").toString());
			}
		}else{
			result.put("msg","无发票明细");
		}
		result.put("body",imgs);
		jsonOutput(res, result,false);
	}
	private List<String> getImgPaths(String invoiceUrl){
		List<String> imgPaths = new ArrayList<String>();
		try {
			String busData = "";
			Document document = Jsoup.connect(invoiceUrl).get();
			Elements els = document.select("script");
			for(Element el:els){
				String content = el.data();
				if(content.contains("busData") && content.contains("JSON.stringify(")){
					busData = content.substring(content.indexOf("JSON.stringify(")+15,content.indexOf("\"});")+2);
					break;
				}
			}
			if(!"".equals(busData)){
				String[] urls = invoiceUrl.split("/computerbill/");
				StringBuffer url = new StringBuffer(urls[0]);
				url.append("/computerbill/showData.do?");
				Map<String,String> jsonMap = JSON.parseObject(busData,Map.class);
				url.append("billBatchCode=").append(jsonMap.get("billBatchCode"));
				url.append("&billNo=").append(jsonMap.get("billNo"));
				String result = HttpUtils.doGet(url.toString());
				if(result!=null && !"".equals(result)){
					Map<String,Object> resultMap = JSON.parseObject(result,Map.class);
					List<String> imgs = (List<String>) resultMap.get("picStr");
					for(String img:imgs){
						String path = this.generateImage(img);
						if(path!=null && !"".equals(path)){
							imgPaths.add(path);
						}
					}
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return imgPaths;
	}
	private String generateImage(String imgStr) {// 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return null;
		String tomcatBinPath = System.getProperty("user.dir");
		String tomcatRootPath = tomcatBinPath.substring(0, tomcatBinPath.lastIndexOf(File.separator));
		String imgFilePath = tomcatRootPath+File.separator+"webapps"+File.separator+"invoice"+File.separator+ "lib" + File.separator + "img"+ File.separator;
		Calendar cal = Calendar.getInstance();
		String fileName = cal.getTimeInMillis()+".png";
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] bytes = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
					bytes[i] += 256;
				}
			}
			// 生成jpeg图片
			OutputStream out = new FileOutputStream(imgFilePath+fileName);
			out.write(bytes);
			out.flush();
			out.close();
			return "/invoice/lib/img/"+fileName;
		} catch (Exception e) {
			logger.error("base64转图片失败:"+e.getMessage());
			return null;
		}
	}
	private String codeToName(String code){
		String name = "门诊";
		if(code.equalsIgnoreCase(Constants.MZ)){
			name = "门诊";
		}else if(code.equalsIgnoreCase(Constants.ZY)){
			name = "住院";
		}
		return name;
	}
	/**
	 * 检验是否为微信浏览器
	 * @author leeson
	 * @param request
	 * @return boolean
	 * @date 2018年1月23日 09:23:47
	 * */
	private boolean checkBrowser(HttpServletRequest request){
		boolean validation = false;
		String ua = ((HttpServletRequest) request).getHeader("user-agent").toLowerCase();
		if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
			validation = true;
		}
//		validation = true;
		return validation;
	}
	/**
	 * 返回值处理
	 * @author leeson
	 * @param msgCodeMap
	 * @return boolean
	 * @date 2018年1月25日 13:44:55
	 * */
	private void resMap(Map<String,Object> msgCodeMap){
		if(msgCodeMap==null){
			msgCodeMap = new HashMap<String, Object>();
		}
		msgCodeMap.put("code", CODE);
		msgCodeMap.put("msg", MSG);
	}
	private boolean strIsEmpet(String str){
		boolean flag = true;
		if(str!=null && !"".equals(str)){
			flag = false;
		}
		return  flag;
	}
}
