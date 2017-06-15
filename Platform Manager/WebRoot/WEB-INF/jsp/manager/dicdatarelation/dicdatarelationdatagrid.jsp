<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	panel.relationId = 0;
	var type = 't_asc_dicdata_relation_data';
	var typeClass = AscApp.ClassManager.getClass(type);
	var orderFieldName = 'f_source_data_id';
	// 获得对象属性列表
	var properties = AscApp.ClassManager.getProperties(type);
	// 初始化视图列
	var columns = AscApp.ClassManager.getGridColumns(properties, typeClass.propertyColumns);
	//columns[0].dataIndex = orderFieldName;
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
		width : 430,
		minWidth : 200,
		title : '属性编辑窗口',
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/properties.editor',
		params : {type : type}
	});
	// 查询框
	var textfield = new Ext.form.TextField({
		width : 180,
		emptyText : '搜索源字典数据',
		enableKeyEvents : true,
		listeners : {
				'specialkey' : function (field, e) {
					if (e.getKey() == e.ENTER) {
						var query = field.getValue();
						grid.getStore().reload();
						field.setValue();
					}
				}
			}
	});
	// 初始化数据存储对象
	var store = new Ext.data.Store({
		clearRemovedOnLoad : true,
		pageSize : 30,//每页显示几条数据(初始值)
		proxy : {
			type : 'direct',
			directFn : DicdataRelationDirect.getRelationDataList,
			extraParams : {
				relationId : panel.relationId,
				query : textfield.getValue()
			},
			paramOrder : 'relationId page limit query',
			paramsAsHash : true,
			reader : {
				type: 'json',
				root : 'datas',
				totalProperty : 'total',
				successProperty : 'successed',
				messageProperty : 'message'
			}
		},
		fields : fields,
		sorters : [{
			property : orderFieldName,
			direction: 'ASC'
		},{
			property : 'f_order',
			direction: 'ASC'
		}],
		listeners : {
			'beforeload' : function(s, operation, eOpts){
				operation.params = {relationId : panel.relationId,query : textfield.getValue()};
			}
		}
	});

	// 数据列表界面
	var grid = Ext.create('Ext.grid.Panel', {
		region : 'west',
		border : false,
		sortableColumns : false,
	    viewConfig: {stripeRows: false},
		allowDeselect : true,
	    columns : columns,
		store : store,
		width : 650,
		split : true,
		tbar : [textfield,{
			text : '搜索',
			iconCls : 'icon-sys-search',
			handler : function(){
		  		var query = textfield.getValue();
				grid.getStore().reload();
				textfield.setValue();
				console.log('test');
			}
		},'->',{
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
		}, '-', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				grid.getStore().reload();
			}
		}],
		dockedItems : [{
	        xtype: 'AscPagingToolbar',
	        store: store,
	        dock: 'bottom',
	        displayInfo: true
	    }]
	});
	
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
	
	panel.delAll = function(){
		grid.getSelectionModel().deselectAll();
	}

	//下拉框初始化
	panel.reStore = function(relationId){
		DicdataRelationDirect.getDictionaryDataList(relationId,function(result,e){
			if(result&&result.success){
			var array = new Array();
	 		for(var i=0;i<result.datas.length;i++){
	 			array.push([result.datas[i].id,result.datas[i].f_caption]);
	 			}
			properties.f_relation_id.store=array;
			var sary = new Array();
	 		for(var i=0;i<result.source.length;i++){
	 			sary.push([result.source[i].id,result.source[i].f_value]);
	 			}
	 		sary.push(relationId);
	 		//console.log(sary[sary.length-1]);
	 		
			properties.f_source_data_id.store = sary;
			
			var tary = new Array();
			for(var i=0;i<result.target.length;i++){
	 			tary.push([result.target[i].id,result.target[i].f_value]);
	 			}
			properties.f_target_data_id.store = tary;
			properties.f_source_data_id.editorCfg.relationId = relationId;
			properties.f_target_data_id.editorCfg.relationId = relationId;
			panel.relationId = relationId;
			editor.removeAll();
			editor.getLoader().load();
			grid.getStore().reload(panel.relationId);
		}});
	}
	
	// 添加记录
	panel.insertRecord = function(){
		//var values = AscApp.ClassManager.getPropertyDefaultValues(type);
		var values = {'f_relation_id':panel.relationId};
        var records = grid.getSelectionModel().getSelection();
        if(records.length > 0){
        	values = {'f_relation_id':panel.relationId,'f_source_data_id':records[0].get('f_source_data_id')};
        	}
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
			DicdataRelationDirect.saveRelationData({
				relationId : panel.relationId,
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
		items : [grid, editor]
	});
	panel.doLayout();
	
	// 装载数据
	panel.reStore(panel.relationId);
	grid.getStore().load();
	grid.getStore().reload(panel.relationId);
	
});
</script>