<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_application_instance';
	var typeClass = AscApp.ClassManager.getClass(type);
	var orderFieldName = 'f_serialnumber';
	panel.applicationId = 0;
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
			directFn : ApplicationRegisterDirect.loadApplicationInstances,
			extraParams : {
				applicationId : 0
			},
			paramOrder : 'applicationId',
			paramsAsHash : true,
			reader : {
				type: 'json',
				root : 'datas',
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
				operation.params = {applicationId : panel.applicationId};
			}
		}
	});
	// 属性编辑窗口
	var editor = Ext.create('Asc.extension.JspPanel', {
		disabled : true,
		layout : 'fit',
		region : 'east',
		split : true,
		border : false,
        collapseMode:'mini',
		width : 400,
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
		disabled : true,
	    columns : columns,
		store : store,
		tbar : [{
			text : '添加',
			iconCls : 'icon-sys-add',
			handler : function(){
				panel.insertRecord();
			}
		}, '-',{
			text : '删除',
			iconCls : 'icon-sys-delete',
			handler : function(){
				panel.deleteRecord();
			}
		}, '-',{
			text : '上移',
			iconCls : 'icon-sys-moveup',
			handler : function(){
				var sm = grid.getSelectionModel();
				var store = grid.getStore();
				var records = grid.getSelectionModel().getSelection();
				if(records.length > 0){
					var record = records[0];
					var index = store.indexOf(record);
					if(index > 0){
						recordUp = store.getAt(index - 1);
						record.set(orderFieldName, index);
						recordUp.set(orderFieldName, index + 1);
					}
				}
				store.sort();
			}
		}, {
			text : '下移',
			iconCls : 'icon-sys-movedown',
			handler : function(){
				var sm = grid.getSelectionModel();
				var store = grid.getStore();
				var records = grid.getSelectionModel().getSelection();
				if(records.length > 0){
					var record = records[0];
					var index = store.indexOf(record);
					if(index < store.getCount() - 1){
						recordDown = store.getAt(index + 1);
						record.set(orderFieldName, index + 2);
						recordDown.set(orderFieldName, index + 1);
					}
				}
				store.sort();
			}
		},'->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				grid.getStore().reload();
			}
		}]
	});
	var tree = Ext.create('Ext.tree.Panel', {
		region : 'west',
		width : 230,
		minWidth : 150,
		split : true,
		border : false,
        collapseMode:'mini',
		store : Ext.create('Ext.data.TreeStore', {
			root : {
				id : 0,
				iconCls : 'icon-manager-root',
				text : '集群应用',
				expanded : true
			},
			proxy : {
				type : 'direct',
				directFn : ApplicationRegisterDirect.loadClusterApplicationNodes,
				reader : {
					type: 'json',
					root : 'datas',
					messageProperty : 'message'
				}
			}
		}),
		tbar : ['->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				grid.getSelectionModel().deselectAll();
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
			grid.getStore().removeAll();
			grid.disable();
			editor.clearRecord();
			return false;
		}
	})
	// 选中应用对象树节点
	tree.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			panel.applicationId = selected[0].get('id');
			grid.enable();
			grid.getStore().reload();
		}
	})
	// 选中校验
	grid.on('beforeselect', function(g, record, index){
		// 被删除的记录不能选中
		if(record.isDeleted){
			editor.clearRecord();
			return false;
		}
	})
	// 选中
	grid.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			editor.loadRecord(selected[0]);
		}else{
			editor.clearRecord();
		}
	})
	// 添加记录
	panel.insertRecord = function(){
		var values = AscApp.ClassManager.getPropertyDefaultValues(type);
		values[orderFieldName] = grid.getStore().getCount() + 1;
		var records = store.add(values);
		grid.getSelectionModel().select(records);
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
	// 刷新列表
	panel.refresh = function(){
		grid.getStore().reload();
		grid.getSelectionModel().deselectAll();
	}
	// 保存记录
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
			ApplicationRegisterDirect.saveApplicationInstances(panel.applicationId, {
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
		items : [tree, grid, editor]
	});
	panel.doLayout();
});
</script>