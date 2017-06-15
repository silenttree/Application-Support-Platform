package com.asc.portal.certification;

import com.asc.cas.server.IAuthenticationListener;
import com.asc.cas.server.authentication.ICredential;
import com.asc.cas.server.authentication.credentials.HttpBaseCredential;
import com.asc.commons.log.LogFactory;
import com.asc.commons.log.LogTypes;
import com.asc.commons.log.logger.CasLogger;

/**
 * Application Portal<br>
 * 用户验证监听实现(用于用户认证日志记录)
 * 
 * Mixky Co., Ltd. 2014<br>
 * @author Bill<br>
 */
public class HttpBaseAuthenticationListenerImp implements IAuthenticationListener {

	@Override
	public void verifyFailed(String loginName, ICredential credential, String message) {
		HttpBaseCredential c = (HttpBaseCredential)credential;
		CasLogger logger = (CasLogger)LogFactory.instance().getLogger(LogTypes.CasAuthentication, "cas", this.getClass());
		logger.loginFailed(loginName, credential.getTerminalType().toString(), c.getHttpRequset());
	}

	@Override
	public void verifySuccessed(String loginName, ICredential credential) {
		HttpBaseCredential c = (HttpBaseCredential)credential;
		CasLogger logger = (CasLogger)LogFactory.instance().getLogger(LogTypes.CasAuthentication, "cas", this.getClass());
		logger.loginSuccessed(loginName, credential.getCallbackUrl(), credential.getTicketId(), credential.getTerminalType().toString(), c.getHttpRequset());
	}

	@Override
	public void userLogout(String loginName, ICredential credential) {
		HttpBaseCredential c = (HttpBaseCredential)credential;
		CasLogger logger = (CasLogger)LogFactory.instance().getLogger(LogTypes.CasAuthentication, "cas", this.getClass());
		logger.logout(credential.getTicketId(), credential.getTerminalType().toString(), c.getHttpRequset());
		/*StringBuffer log = new StringBuffer();
		log.append("用户[]退出登录");
		logger.info(log.toString(), null, true);*/
	}

}
