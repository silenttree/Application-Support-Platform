<%@page import="com.asc.framework.importfile.FileImportor"%>
<%@page import="com.google.gson.JsonArray"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String contextPath = request.getContextPath();
	long fileImportLogId = Long.valueOf(request
			.getParameter("fileImportLogId"));
	String appKey = request.getParameter("appKey");
	FileImportor fi = FileImportor.load(fileImportLogId);
	JsonArray columns = fi.getOperationGridColumns();
%>
<script>
Ext.onReady(function () {
	var panel = Ext.getCmp('<%=panelid%>');
	var fileImportLogId = <%=fileImportLogId%>;
	var appKey = '<%=appKey%>';
	var columns = <%=columns%>;
	var i = 0, dataFields = [];
	var displayColumn = [];
	for(i = 0; i < columns.length; i++) {
		dataFields.push(columns[i].dataIndex);
		if(columns[i].dataIndex=='rowIndex'){
			displayColumn.push({
				text : columns[i].text,
				dataIndex : columns[i].dataIndex,
				width : 50
			});
		}else if(columns[i].dataIndex=='primaryKeys'){
			displayColumn.push({
				text : columns[i].text,
				dataIndex : columns[i].dataIndex,
				width : 170
			});
		}else if(columns[i].dataIndex=='operatorType'){
			displayColumn.push({
				text : columns[i].text,
				dataIndex : columns[i].dataIndex,
				width : 60
			});
		}else{
			displayColumn.push(columns[i]);
		}
	}
	var appManager = AscApp.getController('AscAppManager');
	var queryOperationDataFn = appManager.getEngineDirectFn(appKey, 'getOperationList');
	var operationDatasConfig = {
		clearRemovedOnLoad : true,
		autoLoad : false,
		pageSize : 30,//每页显示几条数据(初始值)
		proxy : {
			type : 'direct',
			directFn : queryOperationDataFn,
			extraParams : {
			},
			paramOrder : 'logId page limit range',
			paramsAsHash : true,
			reader : {
				type: 'json',
				root : 'datas',
				totalProperty : 'total',
				successProperty : 'successed',
				messageProperty : 'message'
			}
		},
		fields : dataFields,
		listeners : {
			'beforeload' : function(store, operation) {
				if(!fileImportLogId) {
					return false;
				}
				var range = {};
				range['add'] = displayPanel.down('#displayAdd').getValue();
				range['delete'] = displayPanel.down('#displayDelete').getValue();
				range['error'] = displayPanel.down('#displayError').getValue();
				range['noOperation'] = displayPanel.down('#displayNoOperation').getValue();
				range['notCheck'] = displayPanel.down('#displayNotCheck').getValue();
				range['update'] = displayPanel.down('#displayUpdate').getValue();
				operation.params = {logId : fileImportLogId, range : range};
			}
		}
	};
	
	var operationDataStore = new Ext.data.Store(operationDatasConfig);
	
	panel.refresh = function() {
		displayPanel.getStore().reload();
	};
	
	var pagingBar = Ext.create('Asc.extension.PagingToolbar', {
		border : false,
		store: operationDataStore,
		displayInfo: true
	});

	var displayPanel = Ext.create('Ext.grid.Panel', {
		width : '100%',
		bodyPadding: 10,
		frame : true,
		border : false,
		columns : displayColumn,
		store : operationDataStore,
		tbar : [{
			xtype : 'checkboxfield',
			boxLabel  : '添加',
			inputValue: 'add',
			checked : true,
			itemId		: 'displayAdd',
			width	  : 60
		 }, {
			 xtype : 'checkboxfield',
			 boxLabel  : '更新',
			 inputValue: 'update',
			 checked : true,
			 itemId		: 'displayUpdate',
			 width	  : 60
		 }, {
			 xtype : 'checkboxfield',
			 boxLabel  : '删除',
			 inputValue: 'delete',
			 checked : true,
			 itemId		: 'displayDelete',
			 width	   : 60
		 }, {
			 xtype : 'checkboxfield',
			 boxLabel  : '异常',
			 inputValue: 'error',
			 checked : true,
			 itemId		: 'displayError',
			 width	   : 60
		 }, {
			 xtype 	   : 'checkboxfield',
			 boxLabel  : '不操作',
			 inputValue: 'noOperation',
			 checked : true,
			 itemId		: 'displayNoOperation',
			 width	   : 60
		}, {
			 xtype 	   : 'checkboxfield',
			 boxLabel  : '未检查',
			 inputValue: 'notCheck',
			 checked : true,
			 itemId		: 'displayNotCheck',
			 width	   : 60
		},'->',{
			xtype : 'button',
			iconCls : 'icon-sys-query',
			text : '查询',
			handler : panel.refresh
		}],
		dockedItems : [{
			dock: 'bottom',
			border : false,
			items: [pagingBar]
		}]
	});
	var datasPanel = Ext.create('Ext.panel.Panel', {
		frame : true,
		border : false,
		layout : 'fit',
		region : 'south',
		items : [displayPanel],
		bbar : ['->',{
			text: '取消',
			iconCls : 'icon-sys-cancel',
			handler: function(){
				var win = panel.up('window');
				win.close();
			}
		},	{
			text: '确认导入',
			iconCls : 'icon-sys-confirm',
			handler: function() {
				if(!fileImportLogId) {
					return;
				}
				var appManager = AscApp.getController('AscAppManager');
				var doFileImportFn = appManager.getEngineDirectFn(appKey, 'doFileImport');
				var win = panel.up('window');
				win.body.mask();
				doFileImportFn(fileImportLogId, function(result, e) {
					if(e.status && result.success){
						var data = result.resultDatas;
						var msg = '';
						msg = msg + '添加成功：【' + data.addSuccessCount + '】条<br/>添加失败：【' + data.addFailCount + '】条<br/>更新成功：【'
						 + data.updateSuccessCount + '】条<br/>更新失败：【'+ data.updateFailCount + '】条<br/>删除成功：【'  + data.deleteSuccessCount + '】条<br/>删除失败：【'
						 + data.deleteFailCount + '】条';
						 Ext.Msg.alert('导入信息提示',msg,function(opt){
							win.close();
						});
					} else {
						Asc.common.Message.showError("执行导入失败");
						win.body.unmask();
					}
				});
			}
		}]
	});
	panel.add(datasPanel);
	panel.refresh();
});
</script>