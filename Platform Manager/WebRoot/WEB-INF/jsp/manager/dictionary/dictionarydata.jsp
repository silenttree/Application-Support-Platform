<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_dictionary_data';
	panel.dicId = 0;
	
	// 字典数据树
	var dicdatatree = Ext.create('Asc.extension.JspPanel', {
		disabled : true,
		layout : 'fit',
		region : 'center',
		split : true,
		header: false,
		border : false,
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/dictionary/dictionarydatatree',
		params : {type : type, dicid: panel.dicId}
	});
	
	var treestore = Ext.create('Ext.data.TreeStore', {
		root : {
			id : 0,
			iconCls : 'icon-manager-dictionarymanager',
			text : '数据字典',
			expanded : true
		},
		proxy : {
			type : 'direct',
			directFn : DictionaryDirect.loadDictionaryNodes,
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
				dicdatatree.delAll();
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
			dicdatatree.delAll();
			dicdatatree.disable();
			return false;
		}
	})
	// 选中对象树节点
	tree.on('selectionchange', function(g, selected){
		if(selected.length > 0){   
			panel.dicId = selected[0].get('id');
			dicdatatree.delAll();
			dicdatatree.enable();
			dicdatatree.reStore(panel.dicId);
		}
	})
	//保存记录
	panel.doApply = panel.doSave = function(){
		dicdatatree.applys();
	}
	// 显示界面
	panel.add({
		layout : 'border',
		border : false,
		items : [tree, dicdatatree]
	});
	panel.doLayout();
});
</script>