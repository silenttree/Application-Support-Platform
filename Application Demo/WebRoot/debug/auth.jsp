<%@page import="com.asc.commons.engine.authority.relation.Expression"%>
<%@page import="java.util.List"%>
<%@page import="com.asc.commons.engine.authority.OrganizationRelationService"%>
<%@page import="com.asc.framework.cache.aop.CacheService"%>
<%@page import="com.asc.commons.engine.design.entity.common.Button"%>
<%@page import="com.asc.commons.design.DesignObjectFactory"%>
<%@page import="com.asc.framework.designobject.service.DesignObjectService"%>
<%@page import="com.asc.commons.certification.AscUserCertification"%>
<%@page import="com.asc.commons.organization.entity.User"%>
<%@page import="com.asc.commons.engine.authority.DesignObjectAuthorityService"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%
	String moduleId = "mBasedata";
	String objId = "mBasedata.vLine.btnAdd";
	Button object = DesignObjectFactory.instance().getObject(objId);
	
	User user = AscUserCertification.instance().getUser(request);
	List<Expression> es = OrganizationRelationService.instance().getUserExpressions(user.getId());
	out.print(es.toString());
	out.print("<br>");
	//out.print();
	boolean has = DesignObjectAuthorityService.instance().isUserHasDesignObjectAuth(user, moduleId, object, null);
	out.print(has);
	CacheService.instance().clear();
%>