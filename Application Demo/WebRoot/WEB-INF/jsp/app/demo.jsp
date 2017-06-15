<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script language=javascript>
	Ext.onReady(function(){
		var panel = Ext.getCmp('<%=panelid%>');
		panel.add({
			html : 'test 222222'
		});
		panel.doLayout();
	});
</script>