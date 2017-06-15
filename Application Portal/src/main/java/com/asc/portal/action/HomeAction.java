package com.asc.portal.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asc.cas.server.CentralAuthenticationService;
import com.asc.commons.organization.entity.User;

/**
 * Application Portal/HomeAction.java<br>
 * 应用门户主界面
 * 
 * Mixky Co., Ltd. 2014<br>
 * @author Bill<br>
 */
@Controller
public class HomeAction {
	private String homeView = "/home";
	
	@RequestMapping("/")
	public String getRootView(Model model, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("appTitle", "Application Portal");
		User user = CentralAuthenticationService.instance().getUserInfo(request);
		model.addAttribute("user", user);
		return homeView;
	}

	@RequestMapping("/home")
	public String getHomeView(Model model, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("appTitle", "Application Portal");
		User user = CentralAuthenticationService.instance().getUserInfo(request);
		model.addAttribute("user", user);
		return homeView;
	}

}
