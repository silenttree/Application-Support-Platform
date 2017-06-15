package com.asc.bs.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <pre>
 * Capital Group Finance Service
 * Service - Account
 * 帐号服务监控页面控制器
 * 
 * Mixky Co., Ltd. 2015
 * @author Bill
 * </pre>
 */
@Controller
public class MonitorAction {
	
	@RequestMapping("/monitor")
	public String getHomeView(Model model, HttpServletRequest request, HttpServletResponse response) {
		return "monitor";
	}
	
}
