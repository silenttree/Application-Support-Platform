package com.asc.cas.server.action;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.asc.cas.server.AuthenticationException;
import com.asc.cas.server.CentralAuthenticationService;
import com.asc.cas.server.authentication.AbstractVerification;
import com.asc.cas.server.authentication.ICredential;
import com.asc.cas.server.authentication.credentials.HttpBaseCredential;
import com.asc.cas.server.ticket.Ticket;
import com.asc.cas.server.ticket.TicketServiceException;
import com.asc.cas.util.UrlUtil;
import com.asc.commons.organization.entity.User;
import com.asc.commons.organization.exception.OrganizationException;
import com.asc.util.PasswordUtil;

/**
 * Central Authentication Service<br>
 * 用户登录入口（含用户登录页面及登录请求）
 * 
 * Mixky Co., Ltd. 2014<br>
 * @author Bill<br>
 */
@Controller
@RequestMapping(value="/login")
public class UserLoginAction {
	private String loginView = "/login";
	private String casHomeView = "/home";
	
	/**
	 * 请求登录页面地址<br>
	 * 1. 用户直接访问登录地址<br>
	 * 2. 系统跳转到登陆地址（需要处理ticketid、访问目标地址）<br>
	 * 如用户已登录，直接跳转到门户首页（1）或访问目标地址（2）
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	public String getView(Model model, HttpServletRequest request, HttpServletResponse response) {
		String vp = loginView;
		User u = CentralAuthenticationService.instance().getUserInfo(request);
		// 检查是否提交ticketid
		String ts = request.getParameter("ticket");
		// TODO: 校验令牌编码格式

		String ru = UrlUtil.getRedirectUrl(request);
		if (ru != null) {
			ru = URLDecoder.decode(ru);
		}
		if (u != null) {
			Ticket t = CentralAuthenticationService.instance().getTicket(u, new HttpBaseCredential(request, response));
			if (t == null || t.getId() == null) {
				throw new IllegalStateException("用户未分配令牌，服务器状态异常");
			}
			// 用户已登录cas，计算跳转目标（检查是否需要跳转到请求地址）
			if (ru != null && !"".equals(ru)) {
				vp = UrlUtil.constructRedirectUrl(ru, t.getId());
			}else{
				vp = "redirect:" + casHomeView;
			}
		} else {
			Ticket ticket = CentralAuthenticationService.instance().getTicket(ts);
			ICredential credential = new HttpBaseCredential(request, response);
			// TODO: 校验令牌信息与请求信息是否吻合/匹配
			if (validateTicket(ticket, credential)) {
				u = CentralAuthenticationService.instance().getUserInfo(ts);
				if (u != null) {
					// 用户已登录cas，计算跳转目标（检查是否需要跳转到请求地址）
					vp = UrlUtil.constructRedirectUrl(ru, ts);
				}
			}
		}
		return vp;
	}

	/**
	 * 输入用户名口令后，提交登录<br>
	 * 1. 直接登录门户<br>
	 * 2. 登录后跳转到用户访问的地址（需处理访问目标地址）
	 * 登录成功后，直接跳转到门户首页（1）或访问目标地址（2）
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public String submit(Model model, HttpServletRequest request, HttpServletResponse response) {
		String vp = loginView;
		User u = CentralAuthenticationService.instance().getUserInfo(request);
		try {
			if (u != null && u.getId() > 0) {
				// TODO: 如果用户已登录，要求用户先执行退出后重新登录
				throw AuthenticationException.forUserAlreadyLogin(u.getF_caption());
			}
			// TODO: 判断是否提交ticket？
			String loginName = request.getParameter("u");
			String passwd = request.getParameter("p");//PasswordUtil.decodeString(request.getParameter("p"));
			String validCode = request.getParameter("v");
			String sysrandom = (String) request.getSession(true).getAttribute("randomString");//图形验证码
			// 先判断验证码是否正确
			if(sysrandom.equalsIgnoreCase(validCode)){
				ICredential credential = new HttpBaseCredential(request, response);
				AbstractVerification auth = CentralAuthenticationService.instance().getAuthService().getVerifyService();
				Ticket ticket = auth.authenticate(loginName, passwd, credential);
				// 校验用户信息
				if (ticket != null && ticket.getId() != null) {
					// 用户认证成功（基本信息及认证策略均通过）
					
					// 检查是否需要跳转到请求地址
					String ru = UrlUtil.getRedirectUrl(request);
					if (ru != null && !"".equals(ru)) {
						ru = URLDecoder.decode(ru);
						vp = UrlUtil.constructRedirectUrl(ru, ticket.getId());
					} else {
						vp = "redirect:" + casHomeView;
					}
					model.addAttribute("user", ticket.getUser());
					model.addAttribute("ticket", ticket);
				}
			}else{
				model.addAttribute("message", "验证码错误!");
			}
			
		} catch (AuthenticationException e) {
			model.addAttribute("message", e.getMessage());
			e.printStack();
		} catch (OrganizationException e) {
			model.addAttribute("message", e.getMessage());
			e.printStack();
		} catch (TicketServiceException e) {
			model.addAttribute("message", e.getMessage());
			e.printStack();
		}
		
		return vp;
	}
	
	/**
	 * 校验令牌信息和访问凭证信息是否匹配
	 * 
	 * @param ticket
	 * @param credential
	 */
	private boolean validateTicket(Ticket ticket, ICredential credential) {
		if (ticket == null || ticket.getId() == null) {
			return false;
		}
		if (credential == null) {
			return false;
		}
		// 校验ip地址
		
		// 通过策略控制器的策略检查
		
		return true;
	}
	
}
