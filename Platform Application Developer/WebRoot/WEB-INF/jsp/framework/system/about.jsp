<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script language=javascript>
	Ext.onReady(function(){
		var panel = Ext.getCmp('<%=panelid%>');
	})
</script>
<table height=100%>
	<tr>
		<td width=80 align=center><image src="resources/images/logo-asc.png"/></td>
		<td>
			铁路物资综合电子商务服务平台，是支撑铁路物资综合业务的综合服务平台，架构于ASC（Application Support Center）应用支撑中心。
		</td>
	</tr>
	<tr>
		<td colspan=2 align=center>
			<span><a href="http://www.crmec.com.cn/" target="_blank">中铁物总电子商务技术有限公司 版权所有</a></span>
			<br>
			<span>CRM E-commerce tech.Co.Ltd All Rights Reserved.</span>
		</td>
	</tr>
</table>