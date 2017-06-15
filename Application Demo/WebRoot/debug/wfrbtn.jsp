<%@page import="com.asc.workflow.action.FlowAction"%>
<%@page import="com.asc.workflow.core.dao.NodeLog"%>
<%@page import="com.asc.workflow.core.util.InstanceDataUtil"%>
<%@page import="com.asc.workflow.core.dao.ProcessLog"%>
<%@page import="com.asc.workflow.action.FlowActionUtil"%>
<%@page import="com.asc.workflow.core.template.Route"%>
<%@page import="com.asc.commons.certification.AscUserCertification"%>
<%@page import="com.asc.commons.organization.entity.User"%>
<%@page import="java.util.List"%>
<%@page import="com.mixky.toolkit.JsonObjectTool"%>
<%@page import="com.google.gson.JsonObject"%>
<%@page import="com.asc.workflow.track.WorkflowInstanceTrackAgent"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%
	String flowId = "flowX";
	int versionId = -1;
	long flowlogId = 46;
	User user = AscUserCertification.instance().getUser(request);
	// listCreatorRoutesAsActions
	List<FlowAction> routes = FlowActionUtil.listCreatorRoutesAsActions(flowId, versionId, user);
	if (routes != null) {
		out.print("<pre>");
		out.print(JsonObjectTool.format(JsonObjectTool.objectList2JsonArray(routes).toString()));
		out.print("</pre>");
	}
	out.print("<br>");
	// listRoutesAsActions
	NodeLog nl = InstanceDataUtil.getFlowCurrentNodelog(flowlogId);
	if (nl == null) {
		out.print("nodelog not exist.");
	} else {
		ProcessLog pl = InstanceDataUtil.getProcessingLog(nl.getId(), user.getId());
		if (pl == null) {
			out.print("processlog not exist.");
		} else {
			routes = FlowActionUtil.listRoutesAsActions(pl.getId(), user);
			out.print("<pre>");
			out.print(JsonObjectTool.format(JsonObjectTool.objectList2JsonArray(routes).toString()));
			out.print("</pre>");
		}
	}
	
%>