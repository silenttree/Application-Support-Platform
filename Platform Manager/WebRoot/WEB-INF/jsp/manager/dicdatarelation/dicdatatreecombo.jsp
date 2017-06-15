<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_application_entrance_test';
	var typeClass = AscApp.ClassManager.getClass(type);
	// 获得对象属性列表
	var properties = AscApp.ClassManager.getProperties(type);
	// 初始化视图列
	var columns = AscApp.ClassManager.getGridColumns(properties, typeClass.propertyColumns);
	columns[1].xtype = 'treecolumn';
	// 初始化存储字段
	var fields = AscApp.ClassManager.getStoreFields(properties);
	panel.dicId = 0;
	panel.dataId = 0;
	
	// 初始化数据存储对象
	var store = new Ext.data.TreeStore({
		clearRemovedOnLoad : true,
		autoLoad : false,
		root : {
			id: 0,
			f_level: 0,
	        expanded: true,
	        children: []
	    },
		proxy : {
			type : 'direct',
			directFn : DicdataRelationDirect.loadDictionaryDataNodes,
			extraParams:{
				dicId : panel.dicId,
				dataId : panel.dataId
			},
			reader : {
				type: 'json',
				root : 'datas',
				totalProperty : 'totals',
				successProperty : 'successed',
				messageProperty : 'message'
			}
		},
		fields : fields,
		listeners : {
			'beforeload' : function(s, operation, eOpts){
				operation.params = {dicId : panel.dicId, dataId : panel.dataId};
			}
		}
	});

	
	// 数据列表界面
	var grid = Ext.create('Ext.tree.Panel', {
		region : 'center',
		border : false,
		rowLines : true,
		sortableColumns : false,
        useArrows: true,
        rootVisible: false,
	    viewConfig: {stripeRows: true},
		allowDeselect : true,
	    columns : columns,
	    flex : 70,
		store : store,
		tbar : [{
			
		}, '->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				grid.getStore().reload();
			}
		}]
	});

	
	// 选中
	grid.on('beforeselect', function(g, record, index){
		// 被删除的记录不能选中
		if(record.isDeleted){
			return false;
		}
	});
	// 选中
	grid.on('selectionchange', function(g, selected){
		if(selected.length > 0){

		}else{

		}
	});
	
	//点击字典重新加载grid和editor
	panel.reStore = function(dicId){
		panel.dicId = dicId;
		panel.dataId = dicId;
		dicdatatree.getStore().load();
	}
	
	panel.delAll = function(){
		dicdatatree.getSelectionModel().deselectAll();
	}
	
	panel.reLoad = function(dicId){
		panel.dicId = dicId;
    	dicdatatree.getStore().load(panel.dicId);
	}
	
	// 刷新列表
	grid.refresh = function(){
		grid.getStore().reload();
		grid.getSelectionModel().deselectAll();
	};

	// 显示界面
	panel.add({
		layout : 'border',
		border : false,
		items : [grid]
	});
	panel.doLayout();

	
});
</script>