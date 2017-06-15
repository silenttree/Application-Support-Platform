<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_database_sync';
	var typeClass = AscApp.ClassManager.getClass(type);
	var orderFieldName = 'id';
	panel.dbsId = 0;
	panel.key = '';
	// 获得对象属性列表
	var properties = AscApp.ClassManager.getProperties(type);
	// 初始化视图列
	var columns = AscApp.ClassManager.getGridColumns(properties, typeClass.propertyColumns);
	// 初始化存储字段
	var fields = AscApp.ClassManager.getStoreFields(properties);
	
	// 属性编辑窗口
	var editor = Ext.create('Asc.extension.JspPanel', {
		layout : 'fit',
		split : true,
		border : false,
        collapseMode:'mini',
		width : 400,
		header: false,
		minWidth : 200,
		title : '属性编辑窗口',
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/properties.editor',
		params : {type : type}
	});
	
	// 交换表
	var exchangetable = Ext.create('Asc.extension.JspPanel', {
		layout : 'fit',
		split : true,
		border : false,
        collapseMode:'mini',
        title : '交换表列表',
		header: false,
		width : 400,
		minWidth : 200,
		iconCls : 'icon-manager-table',
		jspUrl : 'manager/appregister/databasesexchangetable',
        listeners: {
            activate: function(tab) {
            	tab.loader.params.dbsid = panel.dbsId;
                tab.loader.load();
    			//exchangetable.reStore(panel.dbsId);
            },
            deactivate : function(tab){
            	tab.removeAll();
            }
        }
	});
	
	var tabPanel = Ext.create('Ext.tab.Panel', {
		disabled : true,
		region : 'center',
	    width: 400,
	    items: [editor, exchangetable]
	});
	
	// 初始化数据存储对象
	var store = new Ext.data.Store({
		clearRemovedOnLoad : true,
		proxy : {
			type : 'direct',
			directFn : ApplicationRegisterDirect.loadDatabasessync,
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
		}]
	});
	
	// 内外网交换
	var grid = Ext.create('Ext.grid.Panel', {
		region : 'west',
		border : false,
		sortableColumns : false,
	    viewConfig: {stripeRows: false},
		allowDeselect : true,
	    columns : columns,
		store : store,
		width : 600,
		//plugins: [Ext.create('Ext.grid.plugin.CellEditing', {clicksToEdit: 2})],
		split : true,
		tbar : [{
			text : '添加',
			iconCls : 'icon-sys-add',
			handler : function(){
				panel.showDatabasesconf();
			}
		}, '-',{
			text : '删除',
			iconCls : 'icon-sys-delete',
			handler : function(){
				panel.deleteRecord();
			}
		}, '->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				grid.getStore().reload();
			}
		}]
	});
	
	//添加数据库实例配置
	panel.showDatabasesconf = function(record){
		var type = 't_asc_database';
		var typeClass = AscApp.ClassManager.getClass(type);
		// 获得对象属性列表
		var properties = AscApp.ClassManager.getProperties(type);
		// 初始化存储字段
		var fields = AscApp.ClassManager.getStoreFields(properties);
		// 初始化视图列
		var columns = AscApp.ClassManager.getGridColumns(properties, typeClass.propertyColumns);
		// 初始化数据存储对象
		var store = new Ext.data.Store({
			clearRemovedOnLoad : true,
			proxy : {
				type : 'direct',
				directFn : ApplicationRegisterDirect.getDatabaseList,
				extraParams : {
					location : ''
				},
				paramOrder : 'location',
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
			}]
		});
		
		var databasesgrid = Ext.create('Ext.grid.Panel', {
			border : false,
			sortableColumns : false,
		    viewConfig: {stripeRows: false},
			allowDeselect : true,
		    columns : columns,
		    selModel : new Ext.selection.CheckboxModel,
			store : store,
			bbar :['->', {
				text : '确定',
				handler : function(){
					var selected = databasesgrid.getSelectionModel().getSelection().length;
					if(selected == 2){
						var selectOne = databasesgrid.getSelectionModel().getSelection()[0].data;
						var selectTwo = databasesgrid.getSelectionModel().getSelection()[1].data;
						ApplicationRegisterDirect.addDatabasesExchange(selectOne, selectTwo, function(result, e){
							if(result && result.success){
								win.close();
								grid.getStore().reload();
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
			}, {
				text : '取消',
				handler : function(){
					win.close();
				}
			}]
		});
		//变更组织
		var win = new Ext.Window({
			width : 500,
			height : 350,
			modal : true,
			resizable : false,
			iconCls : 'icon-cross-common-view',
			title : '数据库实例配置',
			layout : 'fit',
			items : databasesgrid
		});
		win.show();
		
		// 装载数据
		databasesgrid.getStore().load();
	}

	// 选中校验
	grid.on('beforeselect', function(g, record, index){
		// 被删除的记录不能选中
		if(record.isDeleted){
			tabPanel.disable();
			editor.clearRecord();
			return false;
		}
	})
	// 选中
	grid.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			panel.dbsId = selected[0].get('id');
			tabPanel.enable();
			editor.loadRecord(selected[0]);
			if(tabPanel.activeTab.title != '属性编辑窗口'){
				exchangetable.reStore(panel.dbsId);
			}
		}else{
			tabPanel.disable();
			editor.clearRecord();
		}
	})
	
	// 刷新列表
	panel.refresh = function(){
		grid.getStore().reload();
		grid.getSelectionModel().deselectAll();
	}
	
	// 删除记录
	panel.deleteRecord = function(){
		var records = grid.getSelectionModel().getSelection();
		if(records.length > 0){
			// 设置删除标记
			if(records[0].get('id') > 0){
				// 已存在的记录标记删除
				records[0].isDeleted = true;
				records[0].setDirty();
			}else{
				// 新添加的记录直接删除
				grid.getStore().remove(records);
			}
			// 取消选中
			grid.getSelectionModel().deselectAll();
		}
	}
	
	//保存记录
	panel.doApply = panel.doSave = function(){
		var insertRecs = grid.getStore().getNewRecords();
		var updateRecs = grid.getStore().getUpdatedRecords();
		if(insertRecs.length + updateRecs.length > 0){
			var deletes = [];
			var updates = [];
			for(var i in insertRecs){
				var record = insertRecs[i];
				updates.push(record.getData());
			}
			for(var i in updateRecs){
				var record = updateRecs[i];
				if(record.isDeleted){
					deletes.push({id : record.get('id')});
				}else{
					updates.push(Ext.apply({id : record.get('id')},record.getChanges()));
				}
			}
			ApplicationRegisterDirect.saveDatabasessync(panel.dbsId, {
				updates : updates,
				deletes : deletes
			}, function(result, e){
				if(result && result.success){
					if(result.sObjects && result.sObjects.length > 0){
						var str = '';
						for(var n in result.sObjects){
							str = str + '<br>[' + result.sObjects[n].id + ']';
						}
						Asc.common.Message.showInfo('共[' + result.sObjects.length + ']个对象提交成功：' + str);
					}
					if(result.fObjects && result.fObjects.length > 0){
						var str = '';
						for(var n in result.fObjects){
							str = str + '<br>[' + result.fObjects[n].id + ']';
						}
						Asc.common.Message.showError('共[' + result.fObjects.length + ']个对象提交失败：' + str);
					}
					panel.refresh();
				}else{
					if(result && result.message){
						Asc.common.Message.showError(result.message);
					}else{
						Asc.common.Message.showError('保存对象操作失败！');
					}
				}
			});
		}
	}
	// 显示界面
	panel.add({
		layout : 'border',
		border : false,
		items : [grid, tabPanel]
	});
	panel.doLayout();
	
	grid.getStore().load();
});
</script>