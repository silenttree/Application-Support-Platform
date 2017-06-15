<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	//String type = request.getParameter("type");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_log_config';
	panel.appkey = '';
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
			type : 'direct',
			directFn : LogDirect.loadLogConfig,
			extraParams : {
				appkey : ''
			},
			paramOrder : 'appkey',
			paramsAsHash : true,
			reader : {
				type: 'json',
				root : 'datas',
				messageProperty : 'message'
			}
		},
		fields : fields,
		listeners : {
			'beforeload' : function(s, operation, eOpts){
				operation.params = {appkey : panel.appkey};
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
		width : 350,
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
	    width : 570,
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
		},'->', {
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
			editor.clearRecord();
			return false;
		}
	})
	// 选中
	grid.on('selectionchange', function(g, selected){
		editor.clearRecord();
		if(selected.length > 0){
			editor.loadRecord(selected[0]);
		}
	})
	// 添加记录
	panel.insertRecord = function(){
		var values = AscApp.ClassManager.getPropertyDefaultValues(type);
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
	
	panel.reStore = function(appkey){
		panel.appkey = appkey;
		if(panel.appkey == null){
			properties.f_type.store = [[0, '会话日志'], [1, '用户日志']];
		}else{
			properties.f_type.store = [[2, '应用状态'], [3, '模块'], [4, '文档'], [5, '表单'], [6, '视图'], [7, 'direct']];
		}
		editor.removeAll();
		editor.getLoader().load();
		console.log(editor);
		grid.getStore().reload();
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
			LogDirect.saveLogConf(panel.appkey, {
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
		items : [grid, editor]
	});
	panel.doLayout();
	// 装载数据
	grid.getStore().load();
});
</script>