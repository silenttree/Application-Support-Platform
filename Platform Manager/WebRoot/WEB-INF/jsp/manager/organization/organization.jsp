<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_org';
	
	panel.selectedPath = undefined;
	// 获得对象属性列表
	var properties = AscApp.ClassManager.getProperties(type);
	//增加初始化机构类型的代码 begin
	DictionaryDirect.getDictionaryDatasByParentCode('JGLB', function(result,e){
		if(result && result.success){
			var array = [];
			for(var i=0;i<result.datas.length;i++){
				array.push([result.datas[i].f_key,result.datas[i].f_value]);
			}
			properties.f_orgtype.store = array;
		}
	});
	//end
	
	panel.orgId = 0;
	// 机构列表窗口
	var grid = Ext.create('Asc.extension.JspPanel', {
		disabled : true,
		layout : 'fit',
		region : 'center',
		header: false,
		split : true,
		border : false,
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/organization/organizationgrid',
		params : {type : type}
	});
	
	var treestore = Ext.create('Ext.data.TreeStore', {
		root : {
			id : 0,
			iconCls : 'icon-manager-organizationfolder',
			text : '组织机构',
			expanded : true
		},
		proxy : {
			type : 'direct',
			directFn : OrganizationDirect.loadOrganizationNodes,
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
	
	var findOrg = Ext.create('Ext.data.Store', {
		fields : [{name: 'id'},{name: 'text'},{name: 'leaf'},{name: 'iconCls'},{name: 'path'}],
		proxy : {
			type : 'direct',
			directFn : OrganizationDirect.loadOrganizationNode,
			reader : {
				type: 'json',
				root : 'datas',
				totalProperty : 'totals',
				successProperty : 'successed',
				messageProperty : 'message'
			}
		}
	});
	
	var tree = Ext.create('Ext.tree.Panel', {
		region : 'west',
		width : 230,
		minWidth : 150,
		split : true,
		border : false,
		useArrows: true,
		store : treestore,
		tbar : [{
			xtype: 'combobox',
            store: findOrg,
            displayField: 'text',
            width: '70%',
            anchor: '100%',
            applyTo: 'search',
            minChars: 2,
            listeners: {
            	'select': function(combo, records){
        			tree.expandPath(records[0].data.path);
        			tree.selectPath(records[0].data.path + "/" + records[0].data.id);
        			panel.selectedPath = selected[0].getPath();
            	}
            },
            listConfig: {
                loadingText: 'Searching...',
                emptyText: 'No matching posts found.',
                getInnerTpl: function() {
                    return '{text}';
                }
            },
            pageSize: 10
		},'->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				grid.delAll();
				tree.getSelectionModel().deselectAll();
				tree.getStore().load({
					node : tree.getRootNode()
				});
			}
		}],
		bbar : [{
			text : '生成编码',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				OrganizationDirect.createOrgCode(function(result, e){
					if(result && result.success){
						grid.refresh();
					}
				});
			}
		}]
	});
	
	// 选中对象树节点
	tree.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			panel.orgId = selected[0].get('id');
			grid.enable();
			grid.reStore(panel.orgId, tree);
			panel.selectedPath = selected[0].getPath();
		}
	});
	

	// 加载树添加已删除的用户
	tree.on('load', function(t, node){
		if(node.isRoot()) {
			if(panel.selectedPath) {
				tree.selectPath(panel.selectedPath);
			}
		}
	});
	//保存记录
	panel.doApply = panel.doSave = function(){
		// 获取选中的节点
		var selectedNodes = tree.getSelectionModel().getSelection();
		grid.apply(function() {
			if(selectedNodes.length > 0) {
				tree.getStore().load({
					node : selectedNodes[0]
				});
			}
		});
		
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