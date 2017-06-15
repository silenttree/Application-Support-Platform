<%@page import="com.asc.commons.organization.entity.User"%>
<%@page import="com.asc.workflow.core.util.ProcessorUtil"%>
<%@page import="com.asc.commons.design.DesignObjectFactory"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.asc.workflow.core.template.FlowRole"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%
	FlowRole fr = DesignObjectFactory.instance().getObject("flowInfo.frCheck");
	List<FlowRole> roles = Arrays.asList(fr);
	List<User> users = ProcessorUtil.listUsers(roles);
	for (User u : users) {
		out.print(u.getF_caption());
		out.print("<br>");
	}
%>