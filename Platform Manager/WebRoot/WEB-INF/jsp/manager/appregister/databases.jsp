<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_database';
	panel.appId = 0;
	// 机构列表窗口
	var grid = Ext.create('Asc.extension.JspPanel', {
		disabled : true,
		layout : 'fit',
		region : 'center',
		split : true,
		header: false,
		border : false,
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/appregister/databasesgrid',
		params : {type : type}
	});
	
	var treestore = Ext.create('Ext.data.TreeStore', {
		root : {
			id : 0,
			iconCls : 'icon-manager-root',
			text : '集群应用',
			expanded : true
		},
		proxy : {
			type : 'direct',
			directFn : ApplicationRegisterDirect.loadClusterApplicationNodes,
			reader : {
				type: 'json',
				root : 'datas',
				totalProperty : 'totals',
				successProperty : 'successed',
				messageProperty : 'message'
			}
		},
        lazyFill: true
	});
	
	var tree = Ext.create('Ext.tree.Panel', {
		region : 'west',
		width : 230,
		minWidth : 150,
		split : true,
		border : false,
		useArrows: true,
		store : treestore,
		tbar : ['->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				grid.delAll();
				tree.getSelectionModel().deselectAll();
				tree.getStore().load({
					node : tree.getRootNode()
				});
			}
		}]
	});
	
	// 选中对象树节点
	tree.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			panel.appId = selected[0].get('id');
			grid.enable();
			grid.reStore(panel.appId);
		}
	})
	//保存记录
	panel.doApply = panel.doSave = function(){
		grid.apply();
	}
	// 显示界面
	panel.add({
		layout : 'border',
		border : false,
		items : [tree, grid]
	});
	panel.doLayout();
});
</script>