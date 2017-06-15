<%@page import="com.asc.commons.notify.NotifyServiceAgent"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%
	long sendto = 1;
	String title = "read notify title";
	String moduleName = "Module Name";
	String dataType = "mkModule.docForTest";
	long dataId = 12345;
	long sender = 2;
	NotifyServiceAgent.instance().sendReadNotify(sendto, title, moduleName, dataType, dataId, sender);
	
	long sid = 123;
	NotifyServiceAgent.instance().sendTodoNotify(sendto, title, moduleName, dataType, dataId, sid, sender);
%>