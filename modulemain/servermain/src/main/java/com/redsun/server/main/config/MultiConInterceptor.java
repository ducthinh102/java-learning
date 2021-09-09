package com.redsun.server.main.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class MultiConInterceptor extends HandlerInterceptorAdapter {

	private static final String TENANT_HEADER_NAME = "ConId";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String conId = request.getHeader(TENANT_HEADER_NAME);
		ConContext.setConId(conId);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		ConContext.setConId(null);
	}
}