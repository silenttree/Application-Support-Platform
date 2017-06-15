package com.asc.portal.certification;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.asc.cas.server.CentralAuthenticationService;
import com.asc.commons.certification.ICertification;
import com.asc.commons.organization.entity.User;

/**
 * Application Portal/AscUserCertificationPortalImp.java<br>
 * 门户服务的用户验证实现
 * 
 * Mixky Co., Ltd. 2014<br>
 * @author Bill<br>
 */
public class AscUserCertificationPortalImp implements ICertification {

	@Override
	public User getUser(HttpServletRequest request) {
		User user = CentralAuthenticationService.instance().getUserInfo(request);
		return user;
	}

	@Override
	public void login(String ticketId, String loginName, HttpServletRequest request, HttpServletResponse reponse) {
	}

	@Override
	public void logout(String ticketId) {
	}
	
}
