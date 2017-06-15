<%@page import="java.io.IOException"%>
<%@page import="com.asc.framework.certfication.cas.CasWebAppIntergration"%>
<%@page import="com.asc.commons.certification.AscUserCertification"%>
<%@page import="com.asc.commons.organization.entity.User"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%
	String appTitle = "应用平台管理工具";
	String userCaption = "";
	User user = AscUserCertification.instance().getUser(request);
	// 用户类型0：普通用户，1：运维管理人员，2：开发人员
	// 普通人员不可以进入管理工具
	if (user != null && user.getF_type() == 0) {
		try {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "您不能进入管理界面！");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	if (user != null) {
		userCaption = user.getF_caption();
	}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title><%=appTitle%></title>
		<link rel="shortcut icon" href="resources/icon/logo.png"/>
		<link rel="stylesheet" type="text/css" href="dependencies/ext-4.2.1/resources/css/ext-all.css">
		<link rel="stylesheet" type="text/css" href="resources/ux/window/Notification.css">	
		<link rel="stylesheet" type="text/css" href="runtime/css/asc.icon.css">
		<link rel="stylesheet" type="text/css" href="resources/css/desktop.css">
		
		<script type="text/javascript" src="dependencies/ext-4.2.1/ext-all.js"></script>
		<script type="text/javascript" src="runtime/api.js"></script>
		<script type="text/javascript" src="app/home.js"></script>
		
	</head>
</html>