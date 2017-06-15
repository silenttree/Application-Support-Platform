<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_dictionary';
	panel.dicId = 0;
	// 机构列表窗口
	var grid = Ext.create('Asc.extension.JspPanel', {
		layout : 'fit',
		region : 'center',
		split : true,
		header: false,
		border : false,
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/dictionary/dictionarygrid',
		params : {type : type}
	});

	//保存记录
	panel.doApply = panel.doSave = function(){
		grid.apply();
	}
	// 显示界面
	panel.add({
		layout : 'border',
		border : false,
		items : [grid]
	});
	panel.doLayout();
});
</script>