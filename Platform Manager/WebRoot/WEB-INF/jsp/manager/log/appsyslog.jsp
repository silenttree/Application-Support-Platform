<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	//String type = request.getParameter("type");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 'SystemLog';
	var typeClass = AscApp.ClassManager.getClass(type);
	// 获得对象属性列表
	var properties = AscApp.ClassManager.getProperties(type);
	// 初始化视图列
	var columns = AscApp.ClassManager.getGridColumns(properties, typeClass.propertyColumns);
	// 初始化存储字段
	var fields = AscApp.ClassManager.getStoreFields(properties);
	// 初始化数据存储对象
	var store = new Ext.data.Store({
		clearRemovedOnLoad : true,
		proxy : {
			type : 'ajax',
			//directFn : LogDirect.loadLogConfig,
			//url : 'manager/applog.jsp',
			extraParams : {
				type : 'app'
			},
			paramOrder : 'type',
			paramsAsHash : true,
			reader : {
				type: 'json',
				root : 'datas',
				messageProperty : 'message'
			}
		},
		fields : fields
	});
	
	// 属性编辑窗口
	var editor = Ext.create('Asc.extension.JspPanel', {
		disabled : true,
		layout : 'fit',
		region : 'east',
		split : true,
		border : false,
        collapseMode:'mini',
		width : 300,
		minWidth : 200,
		title : '属性编辑窗口',
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/properties.editor',
		params : {type : type}
	});
	
	// 数据列表界面
	var grid = Ext.create('Ext.grid.Panel', {
		region : 'center',
		border : false,
		sortableColumns : false,
	    viewConfig: {stripeRows: false},
		allowDeselect : true,
	    columns : columns,
		store : store,
		tbar : ['->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				grid.getStore().reload();
			}
		}]
	});
	// 选中
	grid.on('selectionchange', function(g, selected){
		editor.clearRecord();
		if(selected.length > 0){
			editor.loadRecord(selected[0]);
		}
	})
	
	panel.reStore = function(appkey){
		panel.appkey = appkey;
		grid.getStore().reload();
	}
	
	// 刷新列表
	panel.refresh = function(){
		grid.getStore().reload();
		grid.getSelectionModel().deselectAll();
	}

	// 显示界面
	panel.add({
		layout : 'border',
		border : false,
		items : [grid, editor]
	});
	panel.doLayout();
	// 装载数据
	grid.getStore().load();
});
</script>