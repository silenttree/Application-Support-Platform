<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
	String dbsid = request.getParameter("dbsid");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_database_sync_table';
	var typeClass = AscApp.ClassManager.getClass(type);
	var orderFieldName = 'id';
	panel.dbsId = <%=dbsid%>;
	panel.key = '';
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
			type : 'direct',
			directFn : ApplicationRegisterDirect.loadDataSyncTable,
			extraParams : {
				dbsId : 0
			},
			paramOrder : 'dbsId',
			paramsAsHash : true,
			reader : {
				type: 'json',
				root : 'datas',
				totalProperty : 'totals',
				successProperty : 'successed',
				messageProperty : 'message'
			}
		},
		fields : fields,
		sorters : [{
			property : orderFieldName,
			direction: 'ASC'
		}],
		listeners : {
			'beforeload' : function(s, operation, eOpts){
				operation.params = {dbsId : panel.dbsId};
			}
		}
	});
	
	// 内外网交换表
	var exchangetable = Ext.create('Ext.grid.Panel', {
		region : 'center',
		border : false,
		sortableColumns : false,
	    viewConfig: {stripeRows: false},
		allowDeselect : true,
	    columns : columns,
		store : store,
		width : 250,
		//plugins: [Ext.create('Ext.grid.plugin.CellEditing', {clicksToEdit: 2})],
		split : true,
		tbar : [{
			text : '添加',
			iconCls : 'icon-sys-add',
			handler : function(){
				panel.showTable();
			}
		}, '-',{
			text : '删除',
			iconCls : 'icon-sys-delete',
			handler : function(){
				var record = exchangetable.getSelectionModel().getSelection();
				Ext.MessageBox.confirm('操作提示','确定要删除吗？',
					function(btn){
						if(btn == 'yes'){
							panel.deleteRecord(record);
						}
				});
			}
		}, '->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				exchangetable.getStore().reload();
			}
		}]
	});
	
	//添加交换表
	panel.showTable = function(record){
		var store = new Ext.data.Store({
			clearRemovedOnLoad : true,
			fields : ['f_name'],
			data : [{'f_name': 't_mk_test1'},{'f_name': 't_mk_test2'},{'f_name': 't_mk_test3'}]
		});
		
		var databasesgrid = Ext.create('Ext.grid.Panel', {
			border : false,
			sortableColumns : false,
		    viewConfig: {stripeRows: false},
			allowDeselect : true,
		    columns : [{
		    	text : '表名',
		    	dataIndex: 'f_name',
		    	width : 300
		    }],
		    //selModel : new Ext.selection.CheckboxModel,
			store : store,
			bbar :['->', {
				text : '确定',
				handler : function(){
					var selected = databasesgrid.getSelectionModel().getSelection();
					if(selected.length > 0){
						for(var i = 0; i < selected.length; i++){
							var re = selected[i];
							ApplicationRegisterDirect.saveSyncTable(panel.dbsId, re.get('f_name'), function(result, e){
								if(result && result.success){
									win.close();
									exchangetable.getStore().reload();
								}else{
									if(result && result.message){
										Asc.common.Message.showError(result.message);
									}else{
										Asc.common.Message.showError('添加内外网交换配置失败！');
									}
								}
							});
						}
					}
				}
			}, {
				text : '取消',
				handler : function(){
					win.close();
				}
			}]
		});
		//变更组织
		var win = new Ext.Window({
			width : 340,
			height : 320,
			modal : true,
			resizable : false,
			iconCls : 'icon-cross-common-view',
			title : '表列表',
			layout : 'fit',
			items : databasesgrid
		});
		win.show();
		
		// 装载数据
		databasesgrid.getStore().load();
	}

	panel.deleteRecord = function(record){
		if(record.length > 0){
			ApplicationRegisterDirect.deleteSyncTable(record[0].get('id'), 
				function(result, e){
					if(result && result.success){
						exchangetable.getStore().reload();
						Asc.common.Message.showInfo("内外网交换表删除成功！");
					}else{
						if(result && result.message){
							Asc.common.Message.showError(result.message);
						}else{
							Asc.common.Message.showError('内外网交换表删除失败！');
						}
					}
			});
		}
	}
	panel.reStore = function(dbsId){
		panel.dbsId = dbsId;
		exchangetable.getStore().reload();
	}
	//保存记录
	panel.doApply = panel.doSave = function(){
		exchangetable.apply();
	}
	// 显示界面
	panel.add({
		layout : 'border',
		border : false,
		items : [exchangetable]
	});
	panel.doLayout();
	// 装载数据
	exchangetable.getStore().load();
});
</script>