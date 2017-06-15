<%@page import="com.mixky.toolkit.JsonObjectTool"%>
<%@page import="com.google.gson.JsonObject"%>
<%@page import="com.asc.workflow.track.WorkflowInstanceTrackAgent"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%
	long flowlogId = 160;
	JsonObject json = WorkflowInstanceTrackAgent.get(flowlogId).loadTrackObject().toJsonObject();
	out.print("<pre>");
	out.print(JsonObjectTool.format(json.toString()));
	out.print("</pre>");
%>