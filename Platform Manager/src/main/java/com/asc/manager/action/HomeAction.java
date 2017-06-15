package com.asc.manager.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.asc.framework.app.AppConfiguration;

/**
 * Application Manager/HomeAction.java<br>
 * 平台管理工具入口
 * 
 * Mixky Co., Ltd. 2014<br>
 * @author Bill<br>
 */
@Controller
@RequestMapping(value="/home")
public class HomeAction {

	@RequestMapping(method=RequestMethod.GET)
	public String getView(Model model, HttpServletRequest request) {
		model.addAttribute("appConfiguration", AppConfiguration.instance());
		return "/home";
	}
	
}
