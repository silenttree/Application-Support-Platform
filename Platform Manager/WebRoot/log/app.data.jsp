<%@page import="com.google.gson.JsonObject"%>
<%@page import="com.asc.manager.appreg.ApplicationRegisterHandler"%>
<%@page import="com.google.gson.JsonArray"%>
<%
	JsonArray datas = ApplicationRegisterHandler.instance().loadApplicationsStates();
	JsonObject result = new JsonObject();
	result.addProperty("success", true);
	result.add("datas", datas);
	response.getWriter().write(result.toString());
%>