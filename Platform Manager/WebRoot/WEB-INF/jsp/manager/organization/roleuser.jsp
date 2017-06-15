<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
%>
<script language=javascript>
Ext.onReady(function(){
	Ext.require('Asc.common.CdruSelector');
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_user';
	panel.orgId = 0;
	// 修改标题
	if(!panel.title){
		panel.setTitle(typeClass.text + '列表');
	}
	
	// 角色树窗口
	var roletree = Ext.create('Asc.extension.JspPanel', {
		disabled : true,
		layout : 'fit',
		region : 'center',
		header: false,
		split : true,
		border : false,
        collapseMode:'mini',
		width : 400,
		minWidth : 200,
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/organization/roletree',
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
			clearOnLoad : true,
			clearRemovedOnLoad : true,
			root : {
				id : 0,
				iconCls : 'icon-manager-organizationfolder',
				text : '组织机构',
				expanded : true
			},
			proxy : {
				type : 'direct',
				directFn : OrganizationDirect.loadCompanyNodes,
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
				roletree.delAll();
				tree.getSelectionModel().deselectAll();
				tree.getStore().load({
					node : tree.getRootNode()
				});
			}
		}]
	});
	
	// 选中对象树节点
	tree.on('selectionchange', function(g, selected){
		panel.ds = false;
		if(selected.length > 0){
			roletree.delAll();
			panel.orgId = selected[0].get('id');
			roletree.enable();
			roletree.reLoad(panel.orgId);
		}else{
			roletree.disable();
		}
	})
	
	panel.doApply = panel.doSave = function(){
		roletree.apply();
	}
	// 显示界面
	panel.add({
		layout : 'border',
		border : false,
		items : [tree, roletree]
	});
	panel.doLayout();
});
</script>