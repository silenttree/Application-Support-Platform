<%@page import="com.asc.commons.context.ContextVariableTypes"%>
<%@page import="com.asc.commons.context.ContextVariablesParser"%>
<%@page import="com.asc.commons.datafilter.entity.Policy"%>
<%@page import="java.util.List"%>
<%@page import="com.asc.commons.datafilter.util.DatafilterUtil"%>
<%@page import="com.asc.commons.datafilter.service.DataFilterSettigsService"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%
	String appId = "1";
	String objId = "mSchedules.fnXYZ.p123.btnABC";
	String ues = "U_1;R_2;D_3;";
	List<Policy> policies = DataFilterSettigsService.instance().listFilterPolicies(appId, objId, ues);
	String sql = DatafilterUtil.genSQL(policies);
	out.println(sql);
%>