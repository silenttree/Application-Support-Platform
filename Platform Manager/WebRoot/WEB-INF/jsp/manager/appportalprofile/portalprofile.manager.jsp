<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_app_portalprofile';
	var typeClass = AscApp.ClassManager.getClass(type);
	// 获得对象属性列表
	var properties = AscApp.ClassManager.getProperties(type);
	// 初始化视图列
	var columns = AscApp.ClassManager.getGridColumns(properties, typeClass.propertyColumns);
	// 初始化存储字段
	var fields = AscApp.ClassManager.getStoreFields(properties);
	
	var orderFieldName = 'f_order';
	// 初始化数据存储对象
	var store = new Ext.data.Store({
		clearRemovedOnLoad : true,
		autoLoad : false,
		root : {
	        expanded: true,
	        children: []
	    },
		proxy : {
			type : 'direct',
			directFn : PortalProfileDirect.findAll,
			reader : {
				type: 'json',
				root : 'datas',
				successProperty : 'successed',
				messageProperty : 'message'
			}
		},
		sorters : [{
			property : orderFieldName
		}],
		fields : fields
	});

	
	// 数据列表界面
	var grid = Ext.create('Ext.grid.Panel', {
		region : 'center',
		border : false,
		sortableColumns : false,
        useArrows: true,
        rowLines : true,
	    viewConfig: {stripeRows: true},
		allowDeselect : true,
	    columns : columns,
	    flex : 70,
		store : store,
		tbar : [{
			text : '添加',
			iconCls : 'icon-sys-add',
			handler : function(){
				grid.insertRecord();
			}
		}, '-', {
			text : '删除',
			iconCls : 'icon-sys-delete',
			handler : function(){
				grid.deleteRecord();
			}
		}, '-', {
			text : '上移',
			iconCls : 'icon-sys-moveup',
			handler : function(){
				grid.moveRecord(-1);
			}
		}, '-', {
			text : '下移',
			iconCls : 'icon-sys-moveDown',
			handler : function(){
				grid.moveRecord(1);
			}
		}, '-', {
			text : '授权',
			iconCls : 'icon-manager-applicationentranceauthority',
			handler : function (){
				grid.auth();
			}
		} ,'->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				grid.getStore().reload();
			}
		}]
	});
	
	// 属性编辑窗口
	var editor = Ext.create('Asc.extension.JspPanel', {
		disabled : true,
		layout : 'fit',
		region : 'east',
		split : true,
		border : false,
        collapseMode:'mini',
        flex : 30,
		minWidth : 200,
		title : '属性编辑窗口',
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/properties.editor',
		params : {type : type}
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
		if(selected.length > 0){
			editor.loadRecord(selected[0]);
		}else{
			editor.clearRecord();
		}
	})
	
	// 添加记录
	grid.insertRecord = function(){
		var values = AscApp.ClassManager.getPropertyDefaultValues(type);
		values[orderFieldName] = grid.getStore().getCount() + 1;
		var records = store.add(values);
		store.sort();
		grid.getSelectionModel().select(records);
	};
	
	// 删除记录
	grid.deleteRecord = function(){
		var records = grid.getSelectionModel().getSelection();
		if(records.length == 0){
			return;
		}
		var recToDel = records[0];
		grid.deleteNode(recToDel);
		grid.getSelectionModel().deselectAll();
	};
	
	//删除记录
	grid.deleteNode = function(node){
		var index = grid.getStore().indexOf(node),
			i;
		if(node.get('id') > 0){
			// 已存在的记录标记删除
			node.isDeleted = true;
			node.setDirty();
			index = index + 1;
		}else{
			// 新添加的记录直接删除
			grid.getStore().remove(node);
		}
	};

	
	//移动，上移，下移
	grid.moveRecord = function(step){
		var store,
			records,
			selectedRec,
			recordTo,
			index,
			toIndex,
			order,
			orderTo;
		records = grid.getSelectionModel().getSelection();
		if(records.length === 0){
			return true;
		}
		selectedRec = records[0];
		store = grid.getStore();
		//当前下标
		index = store.indexOf(selectedRec);
		//超出范围不做操作
		toIndex = index + step;
		if(toIndex >= grid.getStore().getCount() || toIndex < 0){
			return true;
		}
		recordTo = store.getAt(toIndex);
		order = selectedRec.get(orderFieldName);
		orderTo = recordTo.get(orderFieldName);
		recordTo.set(orderFieldName, order);
		selectedRec.set(orderFieldName, orderTo);
		store.sort();
		grid.getSelectionModel().select(selectedRec);
		editor.loadRecord(selectedRec);
	}
	
	// 刷新列表
	grid.refresh = function(){
		grid.getStore().reload();
		grid.getSelectionModel().deselectAll();
	};
	
	// 保存记录
	panel.doApply = panel.doSave = function(){
		var idx,
			order;
		//重新编序号
		for(idx = 0, order = 1; idx < grid.getStore().getCount(); idx++, order++){
			if(grid.getStore().getAt(idx).isDeleted){
				order--;
			}else if(grid.getStore().getAt(idx).get(orderFieldName) != (order)){
				grid.getStore().getAt(idx).set(orderFieldName, order);
			}
		}
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
			PortalProfileDirect.save({
				updates : updates,
				deletes : deletes
			}, function(result, e){
				if(result && result.success){
					Asc.common.Message.showInfo('对象保存成功');
					grid.refresh();
				}else{
					if(result && result.message){
						Asc.common.Message.showError(result.message);
					}else{
						Asc.common.Message.showError('保存对象操作失败！');
					}
				}
			});
		}
	};
	
	//授权
	grid.auth = function () {
		var authValue,
			selection = grid.getSelectionModel().getSelection();
		if(selection.length === 0){
			Asc.common.Message.showInfo('请选择一条或多条数据');
			return;
		}
		if(selection.length === 1){
			authValue = selection[0].get('f_scope_expression');
		} else {
			authValue = '';
			selection.every(function(record, index, me){
				if(authValue === ''){
					authValue = record.get('f_scope_expression');
				}else if(authValue !== record.get('f_scope_expression')){
					authValue = '';
					return false;
				}
				return true;
			});
		}
		Ext.create('Asc.common.CdruSelector',{
			selectType: 'cdru',
			singleSelect: false,
			value: authValue,
			callback: function(value, rawValue){
				selection.every(function(record, index, me){
					record.set('f_scope_expression', value);
					record.set('f_scope_display', rawValue);
					record.setDirty();
					editor.loadRecord(record);
					return true;
				});
			}
		}).show();
	};

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