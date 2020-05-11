package com.vee.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SystemController extends JSONOutputMVCConroller {
	private static String CODE = "200";
	private static String MSG = "success";

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String showBlogs() {
		return "index";
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
}
