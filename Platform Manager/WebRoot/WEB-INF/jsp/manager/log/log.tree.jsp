<%@page import="com.google.gson.JsonObject"%>
<%@page import="com.asc.manager.log.handler.LogConfigHandler"%>
<%@page import="com.google.gson.JsonArray"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = panel.object.type;
	panel.appkey = '';
	Ext.define('treeJsonModel',{
 		extend : 'Ext.data.Model',
 		fields: ['id','text','url']
 	});
	var tree = Ext.create('Ext.tree.Panel', {
		region : 'west',
		width : 300,
		minWidth : 150,
		split : true,
		border : false,
        collapseMode : 'mini',
		store : Ext.create('Ext.data.TreeStore', {
			model : 'treeJsonModel',
			root : {
				id : 0,
				iconCls : 'icon-manager-root',
				text : '应用系统',
				expanded : true
			},
			proxy : {
				type : 'direct',
				directFn : LogDirect.loadAppTree,
				extraParams : {
					type : type
				},
				paramOrder : 'type',
				paramsAsHash : true
			}
		}),
		tbar : ['->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				tree.getStore().load({
					node : tree.getRootNode()
				});
			}
		}]
	});
	
	// 应用系统
	var grid = Ext.create('Asc.extension.JspPanel', {
		disabled : true,
		layout : 'fit',
		region : 'center',
		header: false,
		split : true,
		border : false,
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/log/loglist',
		//params : {type : type},
		object : {url : '',type : type}
	});
	// 选中对象树节点
	tree.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			//panel.appkey = selected[0].get('id');
			grid.enable();
			grid.description.body.update('');
			grid.description.disable();
			//节点的url传递过去
			grid.object.url = selected[0].get('url');
			grid.refresh(grid.object.url);
		}
	});
	//保存记录
	panel.doApply = panel.doSave = function(){
		grid.apply();
	};
	// 显示界面
	panel.add({
		layout : 'border',
		border : false,
		items : [tree, grid]
	});
	panel.doLayout();
});
</script>