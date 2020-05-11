package com.vee.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class WebInterceptor implements HandlerInterceptor {
	private String path;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("execute preHandle...");
//		path = request.getServletPath();
//		if (path.contains("test1.html")) {
//			
//            StringBuffer sb = new StringBuffer();
//            sb.append("{");
//
//            Enumeration<String> headers = request.getHeaderNames();
//            int i = 0;
//            while (headers.hasMoreElements()) {
//                String header = headers.nextElement();
//
//                if (i > 0)
//                    sb.append(", ");
//                sb.append(header + ": " + request.getHeader(header));
//                i++;
//            }
//            sb.append("}");
//            System.out.println("Pre handling request: {}, headers: {}"+getRequestInfo(request, true)+"--"+sb.toString());
////            logger.debug("Pre handling request: {}, headers: {}", getRequestInfo(request, true), sb.toString());
//        }
        return true;
	}

	private static String getRequestInfo(HttpServletRequest request, boolean requestDetails) {
        StringBuffer sb = new StringBuffer();
        sb.append(request.getMethod()).append(" ");
        sb.append(request.getRequestURI());
        if (requestDetails) {
            Enumeration<String> e = request.getParameterNames();
            sb.append("{");
            int i = 0;
            while (e.hasMoreElements()) {
                String name = e.nextElement();
                String val = request.getParameter(name);

                if (val != null && !"".equals(val)) {
                    if (i > 0)
                        sb.append(", ");
                    sb.append(name).append(": ").append(val);

                    i++;
                }
            }
            sb.append("}");
        }

        return sb.toString();
    }
	
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("execute postHandle...");
		response.getOutputStream();
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)throws Exception {
//		path = request.getServletPath();
//		if(path.contains("test1.html")){
//			String requestInfo = getRequestInfo(request, false);
//			System.out.println("Complete request: {}"+requestInfo);
//		}
		System.out.println("execute afterCompletion...");
	}

}
