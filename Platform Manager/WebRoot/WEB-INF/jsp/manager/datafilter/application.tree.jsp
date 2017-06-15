<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	panel.applicationId = 0;

	var tree = Ext.create('Ext.tree.Panel', {
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
	
	// 选中校验
	tree.on('beforeselect', function(g, record, index){
		// 跟节点不能选中
		if(!panel.canRootSelect && record.get('id') == 0){
			return false;
		}
	})

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
	
	panel.getSelectedText = function(){
		var selection = tree.getSelectionModel().getSelection();
		if(selection && selection.length > 0){
			return selection[0].get('text');
		} else {
			return -1;
		}
	};

	panel.tree = tree;
	// 显示界面
	panel.add(tree);
	panel.doLayout();
});
</script>