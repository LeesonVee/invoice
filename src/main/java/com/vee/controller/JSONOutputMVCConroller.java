package com.vee.controller;

import com.vee.utils.JSONUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import javax.servlet.http.HttpServletResponse;



public abstract class JSONOutputMVCConroller{

	protected void jsonOutput(HttpServletResponse response,Object resData,boolean gzip) throws IOException{
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		OutputStream os = null;
		if(gzip){
			os = buildGzipOutputStream(response);
		}
		else{
			os = response.getOutputStream();
		}
		JSONUtils.write(os,resData);
	}
	public static OutputStream buildGzipOutputStream(HttpServletResponse response) throws IOException {
		response.setHeader("Content-Encoding", "gzip");
		response.setHeader("Vary", "Accept-Encoding");
		return new GZIPOutputStream(response.getOutputStream());
	}
}
