package com.vee.controller;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.vee.utils.HttpUtils;
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
	private static String WS_URL = "http://webservice.sptdch.cn:8082/WebInvoice.asmx/InvoiceTransData";
	@RequestMapping(value = "/index", method = {RequestMethod.GET,RequestMethod.POST})
	public String showBlogs(HttpServletRequest req,HttpServletResponse res) {
		String platformType = req.getParameter("platformType");
		if(platformType==null || "1".equals(platformType)){
			req.getSession().setAttribute("idCardNo",req.getParameter("idCardNo"));
			req.getSession().setAttribute("cardNo",req.getParameter("cardNo"));
//			req.setAttribute("idCardNo",req.getParameter("idCardNo"));
//			req.setAttribute("cardNo",req.getParameter("cardNo"));
			return "index";
		}else{
			return "form";
		}
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
//        String params = "strInJson={\"head\":{\"tradetime\": \"2020-05-09 13:44:48\",\"tradename\": \"JYMXXX\",\"chanel\":\"3\"},\"body\":{\"cardno\": \"Z00001946\",\"idcardno\": \"330328197302194420\",\"starttime\": \"2020-05-12 12:12:48\",\"endtime\":\"2020-05-14 23:59:59\"}}";
//		String strJson = "strInJson={\"head\":{\"tradetime\": \"2020-05-14 14:24:48\",\"tradename\": \"KPQQ\",\"chanel\":\"3\"},\"body\":{\"serialno\":\"81339577\",\"patienttype\": \"门诊\"}}";
//		String strJson1 = "strInJson={\"head\":{\"tradetime\": \"2020-05-09 13:44:48\",\"tradename\": \"JYMXXX\",\"chanel\":\"3\"},\"body\":{\"cardno\": \"Z00001946\",\"idcardno\": \"310104199011021111\",\"starttime\": \"2019-12-20 12:12:48\",\"endtime\":\"2019-12-27 23:59:59\"}}";
//		{"head":{"tradetime": "2020-05-09 13:44:48","tradename": "JYMXXX","chanel":"3"},"body":{"cardno": "Z00001946","idcardno": "330328197302194420","starttime": "2020-05-12 12:12:48","endtime":"2020-05-14 23:59:59"}}
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> headMap = new HashMap<String,Object>();
		Calendar cal = Calendar.getInstance();
		headMap.put("tradetime",cal.get(Calendar.YEAR)+"-01-01 00:00:00");
		headMap.put("tradename","JYMXXX");
		headMap.put("chanel","3");
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
//		{"head":{"tradetime":"2020-04-30 08:24:40","tradename":"KPQQ","chanel":"3"},
// "body":{"serialno":"81239017","patienttype":"门诊"}}
		sb.append("strInJson={\"").append("head\":{");
		sb.append("\"tradetime\":").append("\"").append(req.getParameter("TRADETIME")).append("\",");
		sb.append("\"tradename\":").append("\"KPQQ\",");
		sb.append("\"chanel\":").append("\"3\"},");
		sb.append("\"body\":{").append("\"serialno\":\"").append(req.getParameter("SERIALNO")).append("\",");
		sb.append("\"patienttype\":\"").append(req.getParameter("TRADETYPE")).append("\"}}");
//		Map<String,Object> data = new HashMap<String,Object>();
//		Map<String,Object> headMap = new HashMap<String,Object>();
//		headMap.put("tradetime",req.getParameter("TRADETIME"));
//		headMap.put("tradename","KPQQ");
//		headMap.put("chanel","3");
//		data.put("head",headMap);
//		Map<String,Object> bodyMap = new HashMap<String,Object>();
//		bodyMap.put("serialno",req.getParameter("SERIALNO"));
//		bodyMap.put("patienttype",req.getParameter("TRADETYPE"));
//		data.put("body",bodyMap);
//		String reqStr = "strInJson="+JSON.toJSONString(data);
//		String strJson = "strInJson={\"head\":{\"tradetime\": \"2020-05-14 14:24:48\",\"tradename\": \"KPQQ\",\"chanel\":\"3\"},\"body\":{\"serialno\":\"81339577\",\"patienttype\": \"门诊\"}}";
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
	@RequestMapping(value="/iframe",method = RequestMethod.GET)
	public String iframe(HttpServletRequest req,HttpServletResponse res) {
		if(!checkBrowser(req)){
			System.out.println("It's not weixin's browser");
			return "illegal";
		}
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
