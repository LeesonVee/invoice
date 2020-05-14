package com.vee.controller;

import java.io.PrintWriter;
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
	@RequestMapping(value = "/index", method = {RequestMethod.GET,RequestMethod.POST})
	public String showBlogs(HttpServletRequest req,HttpServletResponse res) {
		String platformType = req.getParameter("platformType");
		if(platformType==null || "1".equals(platformType)){
			req.setAttribute("idCardNo",req.getParameter("idCardNo"));
			req.setAttribute("cardNo",req.getParameter("cardNo"));
			return "index";
		}else{
			return "form";
		}
	}

	@RequestMapping(value="/queryInvoiceInfo",method = RequestMethod.POST)
	@ResponseBody
	public void queryInvoiceInfo(HttpServletRequest req,HttpServletResponse res) throws Exception{
		String idCardNo = req.getParameter("idCardNo");
		String cardNo = req.getParameter("cardNo");
		String starttime = req.getParameter("starttime");
		String endtime = req.getParameter("endtime");
		String url = "http://webservice.sptdch.cn:8082/WebInvoice.asmx/InvoiceTransData";
//        String params = "strInJson={\"head\":{\"tradetime\": \"2020-05-09 13:44:48\",\"tradename\": \"JYMXXX\",\"chanel\":\"3\"},\"body\":{\"cardno\": \"Z00001946\",\"idcardno\": \"330328197302194420\",\"starttime\": \"2020-05-12 12:12:48\",\"endtime\":\"2020-05-14 23:59:59\"}}";
//		String strJson = "strInJson={\"head\":{\"tradetime\": \"2020-05-14 14:24:48\",\"tradename\": \"KPQQ\",\"chanel\":\"3\"},\"body\":{\"serialno\":\"81339577\",\"patienttype\": \"门诊\"}}";
//		String strJson1 = "strInJson={\"head\":{\"tradetime\": \"2020-05-09 13:44:48\",\"tradename\": \"JYMXXX\",\"chanel\":\"3\"},\"body\":{\"cardno\": \"Z00001946\",\"idcardno\": \"310104199011021111\",\"starttime\": \"2019-12-20 12:12:48\",\"endtime\":\"2019-12-27 23:59:59\"}}";
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> headMap = new HashMap<String,Object>();
		headMap.put("tradetime","2020-05-09 13:44:48");
		headMap.put("tradename","JYMXXX");
		headMap.put("chanel","3");
		data.put("head",headMap);
		Map<String,Object> bodyMap = new HashMap<String,Object>();
		bodyMap.put("cardno",cardNo);
		bodyMap.put("idcardno",idCardNo);
		bodyMap.put("starttime",starttime);
		bodyMap.put("endtime",endtime);
		data.put("body",bodyMap);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("strInJson",data);
		String xmlStr = HttpUtils.httpPostWs(url,JSON.toJSONString(params));
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
//		{"head":{"tradetime": "2020-05-09 15:44:48","tradename": "KPQQ","chanel":"3"},"body":{"serialno":"","patienttype": "住院"}}
		String url = "http://webservice.sptdch.cn:8082/WebInvoice.asmx/InvoiceTransData";
//        String params = "strInJson={\"head\":{\"tradetime\": \"2020-05-09 13:44:48\",\"tradename\": \"JYMXXX\",\"chanel\":\"3\"},\"body\":{\"cardno\": \"Z00001946\",\"idcardno\": \"330328197302194420\",\"starttime\": \"2020-05-12 12:12:48\",\"endtime\":\"2020-05-14 23:59:59\"}}";
		String strJson = "strInJson={\"head\":{\"tradetime\": \"2020-05-14 14:24:48\",\"tradename\": \"KPQQ\",\"chanel\":\"3\"},\"body\":{\"serialno\":\"81339577\",\"patienttype\": \"门诊\"}}";
//		String strJson1 = "strInJson={\"head\":{\"tradetime\": \"2020-05-09 13:44:48\",\"tradename\": \"JYMXXX\",\"chanel\":\"3\"},\"body\":{\"cardno\": \"Z00001946\",\"idcardno\": \"310104199011021111\",\"starttime\": \"2019-12-20 12:12:48\",\"endtime\":\"2019-12-27 23:59:59\"}}";
//		String strJson = req.getParameter("strInJson");
		String xmlStr = HttpUtils.httpPostWs(url,strJson);
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
