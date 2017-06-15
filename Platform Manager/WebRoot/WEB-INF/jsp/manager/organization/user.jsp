<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_user';
	panel.orgId = 0;
	panel.userId = 0;
	
	panel.selectedPath = undefined;
	
	// 机构列表窗口
	var grid = Ext.create('Asc.extension.JspPanel', {
		disabled : true,
		layout : 'fit',
		region : 'center',
		split : true,
		header: false,
		border : false,
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/organization/usergrid',
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
        viewConfig: {
			markDirty: false
		},
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
				tree.getStore().load();
			}
		}]
	});
	// 选中对象树节点
	tree.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			if(selected[0].data.text == '已删除用户'){
				Ext.getCmp('add').disable();
				Ext.getCmp('ch').disable();
				Ext.getCmp('ps').disable();
			}else{
				Ext.getCmp('add').enable();
				Ext.getCmp('ch').enable();
				Ext.getCmp('ps').enable();
			}
			panel.orgId = selected[0].get('id');
			panel.selectedPath = selected[0].getPath();
			grid.enable();
			grid.reStore(panel.orgId, tree);
			
			var gridtype = 't_asc_user';
			// 获得对象属性列表
			var properties = AscApp.ClassManager.getProperties(gridtype);

			OrganizationDirect.getDeptCaption(panel.orgId,function(result,e){
				if(result&&result.success){
					var left = grid.items.get(0).items.get(0);
					var array = [];
					if(panel.orgId != -1){
						array.push([panel.orgId,result.text]);
						properties.f_dept_id.store = array;
					}else{
						for(var i=0;i<result.datas.length;i++){
							array.push([result.datas[i].f_dept_id,result.datas[i].f_dept_caption]);
						}
						properties.f_dept_id.store = array;
					}
					left.getStore().reload();
				}
			});
		}
	});
	// 加载树添加已删除的用户
	tree.on('load', function(t, node){
		if(node.isRoot()) {
			var newNode = Ext.create('Ext.data.NodeInterface',{  
				leaf : false  
			});
			var node = t.getRootNode().createNode(newNode);
			node.set("id", -1);
			node.set("text", "已删除用户");
			node.set("leaf", true);
			t.getRootNode().appendChild(node);
			if(panel.selectedPath) {
				tree.selectPath(panel.selectedPath);
			}
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