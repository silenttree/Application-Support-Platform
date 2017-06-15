<%@page import="com.asc.commons.engine.authority.OrganizationRelationService"%>
<%@page import="com.asc.commons.engine.authority.relation.Expression"%>
<%@page import="java.util.List"%>
<%@page import="com.asc.commons.organization.entity.User"%>
<%@page import="com.asc.commons.certification.AscUserCertification"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%
	User user = AscUserCertification.instance().getUser(request);
	out.print("user=" + user.getF_caption());
	
	List<Expression> es = OrganizationRelationService.instance().getUserExpressions(user.getId());
	for (int i = 0; i < es.size(); i ++) {
		out.print(es.get(i).getExpStr() + ";");
	}
%>