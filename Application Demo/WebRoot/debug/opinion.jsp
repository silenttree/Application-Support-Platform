<%@page import="com.asc.workflow.core.dao.ProcessLog"%>
<%@page import="com.asc.workflow.core.dao.NodeLog"%>
<%@page import="com.asc.workflow.core.dao.FlowLog"%>
<%@page import="com.asc.workflow.core.util.InstanceDataUtil"%>
<%@page import="com.asc.commons.certification.AscUserCertification"%>
<%@page import="com.asc.commons.organization.entity.User"%>
<%@page import="com.asc.workflow.opinion.OpinionDataUtil"%>
<%@page import="com.asc.workflow.opinion.dao.OpinionLog"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%
	String dataType = "mDemo.docDemo";
	long dataId = 11;
	String objectId = "";
	String opinion = "xxxxxx";
	User user = AscUserCertification.instance().getUser(request);
	if (user == null) {
		throw new IllegalStateException("用户未登录");
	}
	FlowLog flog = InstanceDataUtil.getFlowLog(dataType, dataId);
	if (flog == null) {
		throw new IllegalArgumentException("流程实例不存在");
	}
	NodeLog cnlog = InstanceDataUtil.getFlowCurrentNodelog(flog.getId());
	if (cnlog == null) {
		throw new IllegalArgumentException("节点实例不存在");
	}
	ProcessLog plog = InstanceDataUtil.getProcessingLog(cnlog.getId(), user.getId());
	if (plog == null) {
		throw new IllegalArgumentException("用户办理实例不存在");
	}
	OpinionDataUtil.submitOpinion(plog.getId(), objectId, opinion, user.getId(), user.getF_caption());

	long processId = plog.getId();
	List<OpinionLog> ologs = OpinionDataUtil.listOpinionLogs(null, 0, 0, processId, "");
	for (OpinionLog ol : ologs) {
		out.print(OpinionDataUtil.format(ol));
		out.print("<br/>");
	}
%>