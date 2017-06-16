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
			
		</td>
	</tr>
	<tr>
		<td colspan=2 align=center>
			<span><a href="http://www.crmec.com.cn/" target="_blank"></a></span>
			<br>
			<span></span>
		</td>
	</tr>
</table>