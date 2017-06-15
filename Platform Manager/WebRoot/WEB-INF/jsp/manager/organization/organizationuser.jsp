<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_org_user';
	// 修改标题
	if(!panel.title){
		panel.setTitle(typeClass.text + '列表');
	}
	
	// 机构列表窗口
	var grid = Ext.create('Asc.extension.JspPanel', {
		disabled : true,
		layout : 'fit',
		region : 'center',
		split : true,
		header: false,
		border : false,
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/organization/organizationusergrid',
		params : {type : type}
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
        collapseMode:'mini',
		store : Ext.create('Ext.data.TreeStore', {
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
			}
		}),
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
            		//tree.getSelectionModel().select(records);
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
		}]
	});
	
	// 选中校验
	tree.on('beforeselect', function(g, record, index){
		// 跟节点不能选中
		if(record.get('id') == 0){
			grid.romAll();
			grid.disable();
			return false;
		}
	})
	// 选中对象树节点
	tree.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			panel.orgId = selected[0].get('id');
			grid.enable();
			grid.reStore(panel.orgId);
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