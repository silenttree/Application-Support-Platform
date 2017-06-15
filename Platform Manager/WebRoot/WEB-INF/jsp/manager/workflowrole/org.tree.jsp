<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');

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
		listeners : {
			beforeload : function (store, operation, eOpts) {
				if(operation.params){
					operation.params.containDept = false;
					return true;
				} else {					
					return false;
				}
			}
		}
	});
	
	var tree = Ext.create('Ext.tree.Panel', {
		region : 'west',
		minWidth : 150,
		split : true,
		border : false,
		useArrows: true,
		store : treestore,
		tbar : ['->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				tree.getStore().load({
					node : tree.getRootNode(),
					containDept : false
				});
			}
		}]
	});

	// 选中应用对象树节点
	tree.on('selectionchange', function(g, selected){
		Ext.callback(panel.selectionchange);
	});
	
	panel.getSelectedId = function(){
		var selection = tree.getSelectionModel().getSelection();
		if(selection && selection.length > 0){
			return selection[0].get('id');
		} else {
			return -1;
		}
	};

	// 显示界面
	panel.add(tree);
	panel.doLayout();
});
</script>