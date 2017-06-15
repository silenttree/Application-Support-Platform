<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
%>
<script>
Ext.onReady(function(){
	Ext.require('Asc.common.ExtProperty');
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_org';
	var typeClass = AscApp.ClassManager.getClass(type);
	var orderFieldName = 'f_order';
	panel.orgId = 0;
	panel.tree;
	// 获得对象属性列表
	var properties = AscApp.ClassManager.getProperties(type);
	// 初始化视图列
	var columns = AscApp.ClassManager.getGridColumns(properties, typeClass.propertyColumns);
	columns[0].dataIndex = orderFieldName;
	// 初始化存储字段
	var fields = AscApp.ClassManager.getStoreFields(properties);
	
	// 属性编辑窗口
	var editor = Ext.create('Asc.extension.JspPanel', {
		disabled : true,
		layout : 'fit',
		region : 'center',
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
	
	// 初始化数据存储对象
	var store = new Ext.data.Store({
		clearRemovedOnLoad : true,
		proxy : {
			type : 'direct',
			directFn : OrganizationDirect.loadOrganization,
			extraParams : {
				orgId : 0
			},
			paramOrder : 'orgId',
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
				operation.params = {orgId : panel.orgId};
			}
		}
	});
	// 数据列表界面
	var grid = Ext.create('Ext.grid.Panel', {
		region : 'west',
		border : false,
		width : 450,
		sortableColumns : false,
	    viewConfig: {stripeRows: false},
		allowDeselect : true,
		disabled : false,
	    columns : columns,
		split : true,
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
		}, '-',{
			text : '转移机构',
			handler : function(){
				var record = grid.getSelectionModel().getSelection()[0];
				if(record){
					panel.showMovedOrg(record);
				}
			}
		}, '->',{
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				grid.getStore().reload();
			}
		}]
	});
	
	//机构转移
	panel.showMovedOrg = function(record){
		var treepanel = Ext.create('Ext.tree.Panel', {
			border : false,
			height : 250,
			width : 350,
			autoScroll : true,
			store : new Ext.data.TreeStore({
				root : {
					id : 0,
					iconCls : 'icon-manager-root',
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
			bbar :['->',{
				text: '取消',
				handler : function(){
					win.close();
				}
			},{
				text : '确定',
				iconCls : 'icon-cross-btn-save',
				handler : function(){
					var selected = treepanel.getSelectionModel().getSelection();
					if(selected.length > 0){
						var targetId = selected[0].data.id;
						var orgId = record.data.id;
						OrganizationDirect.moveOrg(orgId, targetId, function(result, e){
							if(result && result.success){
								win.close();
								panel.tree.getRootNode().removeAll(false);
								panel.tree.getStore().load();
								grid.getStore().reload();
								Asc.common.Message.showInfo('机构转移操作成功。');
							}else{
								if(result && result.message){
									Asc.common.Message.showError(result.message);
								}else{
									Asc.common.Message.showError('机构转移操作失败！');
								}
							}
						});
					}
				}
			}]
		});
		//变更组织
		var win = new Ext.Window({
			width : 350,
			height : 200,
			modal : true,
			resizable : false,
			iconCls : 'icon-cross-common-view',
			title : '选择目标机构',
			layout : 'fit',
			items : treepanel
		});
		win.show();
	}
	
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

	panel.reStore = function(orgId, tree){
		panel.orgId = orgId;
		panel.tree = tree;
		grid.getStore().reload();
	}
	
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
	
	panel.delAll = function(){
		grid.getSelectionModel().deselectAll();
	}
	// 刷新列表
	panel.refresh = function(){
		grid.getStore().reload();
		grid.getSelectionModel().deselectAll();
	}
	
	// 保存记录
	panel.apply = function(callback){
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
			//alert("saveOrg panel.orgId:" + panel.orgId);
			OrganizationDirect.saveOrg(panel.orgId, {
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
							str += '<br>';
							if(result.fObjects[n].id) {
								str += '[' + result.fObjects[n].id + ']';
							}
							if(result.fObjects[n].msg) {
								str += result.fObjects[n].msg;
							}
						}
						Asc.common.Message.showError('共[' + result.fObjects.length + ']个对象提交失败：' + str);
					} else {
						panel.refresh();
					}
					// 调用回调函数
					if(Ext.isFunction(callback)) {
						callback();
					}
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
		items : [grid, editor]
	});
	panel.doLayout();
});
</script>