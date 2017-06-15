<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_log_manager';
	panel.appkey = '';
	// 机构列表窗口
	var grid = Ext.create('Asc.extension.JspPanel', {
		disabled : true,
		layout : 'fit',
		region : 'center',
		header: false,
		split : true,
		border : false,
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/log/appsyslog',
		params : {type : type}
	});
	
	var tree = Ext.create('Ext.tree.Panel', {
		region : 'west',
		width : 230,
		minWidth : 150,
		split : true,
		border : false,
        collapseMode:'mini',
		store : Ext.create('Ext.data.TreeStore', {
			root : {
				id : 0,
				iconCls : 'icon-manager-root',
				text : '已注册应用',
				expanded : true
			},
			proxy : {
				type : 'direct',
				directFn : AppEntDirect.loadApplicationNodes,
				reader : {
					type: 'json',
					root : 'datas',
					totalProperty : 'totals',
					successProperty : 'successed',
					messageProperty : 'message'
				}
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
	
	// 选中对象树节点
	tree.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			panel.appkey = selected[0].raw.key;
			grid.enable();
			grid.reStore(panel.appkey);
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