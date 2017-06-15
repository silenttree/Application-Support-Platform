<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_datasource';
	panel.appId = 0;
	panel.key = '';
	panel.params = '';
	// 数据库实例配置列表
	var grid = Ext.create('Asc.extension.JspPanel', {
		//disabled : true,
		layout : 'fit',
		//region : 'center',
		split : true,
		header: false,
		border : false,
		title : '数据库实例配置列表',
		iconCls : 'icon-manager-applicationconfigfolder',
		jspUrl : 'manager/appregister/databasesconfgrid',
		params : {type : type, key : panel.key}
	});
	
	// 参数脚本编辑窗口
	var params = Ext.create('Asc.extension.JspPanel', {
		//disabled : true,
		layout : 'fit',
		//region : 'east',
		split : true,
		border : false,
        collapseMode:'mini',
		width : 250,
		minWidth : 200,
		title : '参数脚本编辑',
		iconCls : 'icon-manager-script',
		jspUrl : 'manager/appregister/databasesconfparam',
		params : {type : type},
        listeners: {
            activate: function(tab) {
            	tab.loader.params.route = panel.route;
            	tab.loader.params.key = panel.key;
            	//tab.loader.params.params = panel.params;
            	//console.log(tab.loader.params);
                tab.loader.load();
            },
            deactivate : function(tab){
            	tab.removeAll();
            }
        }
	});
	
	var tabPanel = Ext.create('Ext.tab.Panel', {
		disabled : true,
		region : 'center',
	    width: 400,
	    items: [grid, params]
	});
	
	var treestore = Ext.create('Ext.data.TreeStore', {
		clearRemovedOnLoad : true,
		root : {
			id : 0,
			iconCls : 'icon-manager-organizationfolder',
			text : '应用数据源',
			expanded : true
		},
		proxy : {
			type : 'direct',
			directFn : ApplicationRegisterDirect.loadApplicationDatasourceNodes
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
	
	// 选中校验
	tree.on('beforeselect', function(g, record, index){
		// 跟节点不能选中
		if(!record.get('leaf')){
			grid.romAll();
			tabPanel.disable();
			//grid.disable();
			//params.disable();
			return false;
		}
	})
	// 选中对象树节点
	tree.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			tabPanel.enable();
			params.enable();
			panel.key = selected[0].raw.key;
			panel.appId = selected[0].get('id');
			panel.route = selected[0].raw.route;
			panel.params = selected[0].raw.params;
			grid.reStore(panel.appId, tree, panel.key);
			if(tabPanel.activeTab.title == '参数脚本编辑'){
				params.setScript(selected[0].raw.route);
				params.setParam(panel.key, selected[0].raw.params);
				params.rel();
			}
		}else{
			grid.romAll();
			tabPanel.disable();
		}
	})
	// 刷新列表
	panel.refresh = function(){
		grid.getStore().reload();
		grid.getSelectionModel().deselectAll();
	}
	//保存记录
	panel.doApply = panel.doSave = function(){
		if(tabPanel.activeTab.title == '参数脚本编辑'){
			params.getScript();
			var record = tree.getSelectionModel().getSelection()[0];
			console.log(record);
			tree.getSelectionModel().deselectAll();
			tree.getStore().load();
			//tree.getSelectionModel().select(record);
		}
		grid.apply();
	}
	// 显示界面
	panel.add({
		layout : 'border',
		border : false,
		items : [tree, tabPanel]
	});
	panel.doLayout();
});
</script>