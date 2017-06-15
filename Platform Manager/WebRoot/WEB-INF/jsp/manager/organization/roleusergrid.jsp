<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_user';
	var typeClass = AscApp.ClassManager.getClass(type);
	var orderFieldName = 'f_order';
	panel.orgId = 0;
	panel.roleId = 0;
	// 获得对象属性列表
	var properties = AscApp.ClassManager.getProperties(type);
	// 初始化视图列
	var columns = AscApp.ClassManager.getGridColumns(properties, typeClass.propertyColumns);
	columns[0].dataIndex = orderFieldName;
	// 初始化存储字段
	var fields = AscApp.ClassManager.getStoreFields(properties);
	// 初始化数据存储对象
	var store = new Ext.data.Store({
		clearRemovedOnLoad : true,
		proxy : {
			type : 'direct',
			directFn : OrganizationDirect.findRoleUsers,
			extraParams : {
				orgId : 0,
				roleId : 0
			},
			paramOrder : 'roleId orgId',
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
			'beforeload' : function(store, operation, eOpts){
				operation.params = {orgId : panel.orgId, roleId : panel.roleId};
			}
		}
	});
	// 数据列表界面
	var grid = Ext.create('Ext.grid.Panel', {
		region : 'east',
		border : false,
		//split : true,
		sortableColumns : false,
	    viewConfig: {stripeRows: false},
		allowDeselect : true,
	    columns : columns,
		store : store,
		width : '100%',
		tbar : [{
			text : '添加',
			iconCls : 'icon-sys-add',
			handler : function(){
				Ext.create('Asc.common.CdruSelector',{
					selectType: 'u',
					singleSelect: false,
					value: "",
					callback: function(value, rawValue){
						if(value){
							OrganizationDirect.saveSelectorRoleUsers(value, panel.orgId, panel.roleId, function(result, e){
								if(result && result.success){
									grid.getStore().reload();
								}else{
									if(result && result.message){
										Asc.common.Message.showError(result.message);
									}else{
										Asc.common.Message.showError('添加机构用户操作失败！');
									}
								}
							});
						}
					}
				}).show();
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
		}],
	    dockedItems: [{
	        xtype: 'pagingtoolbar',
	        store: store,   // same store GridPanel is using
	        dock: 'bottom',
	        displayInfo: true
	    }]
	});
	
	panel.delAll = function(){
		grid.getSelectionModel().deselectAll();
	}
	
	panel.reStore = function(orgId, roleId){
		panel.orgId = orgId;
		panel.roleId = roleId;
		grid.getStore().reload();
	}
	
	// 选中校验
	grid.on('beforeselect', function(g, record, index){
		// 被删除的记录不能选中
		if(record.isDeleted){
			//editor.clearRecord();
			return false;
		}
	})
	// 选中
	grid.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			//editor.loadRecord(selected[0]);
		}else{
			//editor.clearRecord();
		}
	})
	
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
	panel.desAll = function(){
		grid.getSelectionModel().deselectAll();
	}
	// 刷新列表
	panel.refresh = function(){
		grid.getStore().reload();
		grid.getSelectionModel().deselectAll();
	}
	// 保存记录
	panel.apply = function(){
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
			OrganizationDirect.saveRoleUsers(panel.roleId, {
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
	
	// 装载并显示组织树
	panel.add({
		layout : 'border',
		border : false,
		items : [grid]
	});
	panel.doLayout();
});
</script>