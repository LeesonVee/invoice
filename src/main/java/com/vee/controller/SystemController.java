package com.vee.controller;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.vee.inter.Constants;
import com.vee.utils.DateUtil;
import com.vee.utils.HttpUtils;
import com.vee.utils.RsaUtils;
import com.vee.utils.XmlAndString;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SystemController extends JSONOutputMVCConroller {
	private static String CODE = "200";
	private static String MSG = "success";
//	private static String WS_URL = "http://webservice.sptdch.cn:8082/WebInvoice.asmx/InvoiceTransData";
	private static String WS_URL = "http://192.168.40.19:8082/WebInvoice.asmx/InvoiceTransData";
	private static String WS_BASE_URL = "http://192.168.40.19:8082/WebInvoice.asmx/ValidateBasicInfo";
	private static String[] INVOICE_TYPE = {"JYMXXX","KPQQ"};
	private static String[] CHANELS={"1","2","3"};
	private static String CHANEL_ITEM = "1";
	@RequestMapping(value = "/index", method = {RequestMethod.GET,RequestMethod.POST})
	public String showBlogs(HttpServletRequest req,HttpServletResponse res) throws Exception{
		String idCardNo=null;
		String platformType = req.getParameter("platformType");
		if(platformType==null || "1".equals(platformType)){
			String appId=req.getParameter("AppId");
			String loginCode=req.getParameter("LoginCode");
			if(appId==null || loginCode==null){
				throw new Exception("入参错误");
			}
			Map<String,String> cardMap =RsaUtils.getIdCardNo(appId,loginCode);
			if(cardMap.isEmpty() || "1".equals(cardMap.get("code"))){
				return "index_error";
			}
			idCardNo = cardMap.get("idCardNo");
			if(idCardNo==null || "".equals(idCardNo)){
				return "index_error";
			}
			req.getSession().setAttribute("idCardNo",idCardNo);
			req.getSession().setAttribute("cardNo",req.getParameter("cardNo"));
			return "index";
		}else if("2".equals(platformType)){
			idCardNo = req.getParameter("idCardNo");
			if(idCardNo==null || "".equals(idCardNo)){
				return "form";
			}else{
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
		boolean flag = false;
		String idCardNo = req.getParameter("idCardNo")==null?"":req.getParameter("idCardNo");
		String idCardName = req.getParameter("name")==null?"":req.getParameter("name");
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
		HttpSession session = req.getSession();
		if(session.getAttribute("idCardNo")==null){
			throw new Exception("身份证信息不能为空");
		}
		String idCardNo = session.getAttribute("idCardNo").toString();
		String cardNo = session.getAttribute("cardNo")==null?"":session.getAttribute("cardNo").toString();
		String starttime = req.getParameter("starttime");
		String endtime = req.getParameter("endtime");
//		idCardNo="330328197302194420";
//		cardNo="Z00001946";
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
			System.out.println(jsonString);
		}
		if(jsonString==""){
			jsonString = "{\"message\":{\"errcode\":\"0\",\"info\":{\"errinfo\":\"不能识别请求中的开票条件！\"}}}";
		}
//		jsonString="{\"message\":{\"errcode\":\"1\",\"info\":{\"url\":\"http://webservice.sptdch.cn:8001/agency-front/agency/invoice/computerbill/industryMain.do?noise=cccdabb6780343abbbb26adbf193d800&eBillPicUrls=1a1050027010d3e3ae424b3495bb631b8e128b0125f9d6528020eb6a9cd82672739ddcc60d851d06e7992b719509a06d41af8d6b7e9f1ab8fd394b0ed022b38978edbb1d091e1e51616bcf0d82891c62d2834ce421b7bd3d8cf566f4778acbc201e6730a5f5fb85275c8bcf532530dfbc5103e46dbcf7a8f1b547d219b54427535b4c6f49c56d24b22dc90879229cb4aafc9401bba1cc31e452a85b0bb232eb7a8bbbf24c61715ae9c53f1379c51a8df\"}}}";
		Map<String,Object> map = JSON.parseObject(jsonString,Map.class);
		Map<String,Object> messageMap = (Map<String, Object>) map.get("message");
		if("1".equals(messageMap.get("errcode"))){
			Map<String,Object> info = (Map<String, Object>) messageMap.get("info");
			req.getSession().setAttribute("url",info.get("url"));
		}
		jsonOutput(res, JSON.parseObject(jsonString),false);

	}
	@RequestMapping(value="/MP_verify_JgGJflv1sgAzxyio",method = RequestMethod.GET)
	@ResponseBody
	public void readTxt(HttpServletRequest req,HttpServletResponse res) throws Exception{
		PrintWriter out = res.getWriter();
		out.print("JgGJflv1sgAzxyio");
		out.flush();
		out.close();
	}
	@RequestMapping(value="/layout",method = RequestMethod.GET)
	public String layoutPage(HttpServletRequest req,HttpServletResponse res,ModelMap model) {
		if(!checkBrowser(req)){
			System.out.println("It's not weixin's browser");
			return "illegal";
		}
		return "layout";
	}
	@RequestMapping(value="/show",method = RequestMethod.GET)
	public String showInvoice(HttpServletRequest req,HttpServletResponse res,ModelMap model) {
		String str = req.getParameter("p");
		String patienttype = this.codeToName(str.substring(0,2));
		String serialno = str.substring(2,str.length());

		StringBuffer sb = new StringBuffer(0);
		sb.append("strInJson={\"").append("head\":{");
		sb.append("\"tradetime\":").append("\"").append(DateUtil.getDateFormatter()).append("\",");
		sb.append("\"tradename\":\"").append(INVOICE_TYPE[1]).append("\",");
		sb.append("\"chanel\":\"3\"},");
		sb.append("\"body\":{").append("\"serialno\":\"").append(serialno).append("\",");
		sb.append("\"patienttype\":\"").append(patienttype).append("\"}}");

		String xmlStr = HttpUtils.httpPostWs(WS_URL,sb.toString());
		String jsonString="";
		if(xmlStr!=""){
			jsonString = XmlAndString.stringxmlToString(xmlStr,"/string");
			System.out.println(jsonString);
		}
		if(jsonString==""){
			jsonString = "{\"message\":{\"errcode\":\"0\",\"info\":{\"errinfo\":\"不能识别请求中的开票条件！\"}}}";
		}
		Map<String,Object> map = JSON.parseObject(jsonString,Map.class);
		Map<String,Object> messageMap = (Map<String, Object>) map.get("message");
		if("1".equals(messageMap.get("errcode"))){
			Map<String,Object> info = (Map<String, Object>) messageMap.get("info");
			req.getSession().setAttribute("qrCodeUrl",info.get("url"));
		}else{
			return "show_invoice_error";
		}
		return "show_invoice";
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
	@RequestMapping(value="/iframe",method = RequestMethod.GET)
	public String iframe(HttpServletRequest req,HttpServletResponse res) {
//		if(!checkBrowser(req)){
//			System.out.println("It's not weixin's browser");
//			return "illegal";
//		}
		req.setAttribute("url",req.getSession().getAttribute("url"));
		return "invoice";
	}
	@RequestMapping(value="/jumpIndex",method = RequestMethod.GET)
	public String jumpIndex(HttpServletRequest req,HttpServletResponse res,ModelMap model) {
//		if(!checkBrowser(req)){
//			System.out.println("It's not weixin's browser");
//			return "illegal";
//		}
		return "form";
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
	private Map<String, Object> resMap(Map<String,Object> msgCodeMap){
		Map<String, Object> map = new HashMap<String, Object>();
		if(msgCodeMap.containsKey("body")){
			map = msgCodeMap;
		}else{
			map.put("body", msgCodeMap);
		}
		map.put("code", CODE);
		map.put("msg", MSG);
		return map;
	}
	private boolean strIsEmpet(String str){
		boolean flag = true;
		if(str!=null && !"".equals(str)){
			flag = false;
		}
		return  flag;
	}
}
