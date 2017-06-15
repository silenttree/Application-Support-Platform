/*
 * 属性扩展
 */
Ext.define('Asc.common.ExtProperty',{
	extend : 'Ext.window.Window',
	alias: 'widget.extproperty',
	
	title: '属性扩展',
	height: 430,
	border: false,
	width: 700,
	layout: 'border',
	resizable: false,
	modal : true,
	
	callback: Ext.emptyFn,
	tablename: '',
	columns: [],
	dataid: 0,
	
	initComponent: function() {
		var me = this,
		extpropertygrid,
		extpropertystore,
		editor;
		var type = 't_asc_ext_property';
		var orderFieldName = 'f_order';
		var typeClass = AscApp.ClassManager.getClass(type);
		// 获得对象属性列表
		var properties = AscApp.ClassManager.getProperties(type);
		// 初始化视图列
		var columns = AscApp.ClassManager.getGridColumns(properties, typeClass.propertyColumns);
		// 初始化存储字段
		var fields = AscApp.ClassManager.getStoreFields(properties);
		
		// 属性编辑窗口
		editor = Ext.create('Asc.extension.JspPanel', {
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
		
		// 初始化数据存储对象
		extpropertystore = new Ext.data.Store({
			clearRemovedOnLoad : true,
			proxy : {
				type : 'direct',
				directFn : ExtpropertyDirect.loadExtproperty,
				extraParams : {
					dataId : 0,
					tablename : ''
				},
				paramOrder : 'dataId tablename',
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
					operation.params = {dataId : me.dataid, tablename : me.tablename};
				}
			}
		});
		
		extpropertygrid = Ext.create('Ext.grid.Panel', {
			region : 'west',
			text : '属性扩展',
			width : 400,
			height : 270,
			columns : columns,
			store : extpropertystore,
			//plugins: [Ext.create('Ext.grid.plugin.CellEditing', {clicksToEdit: 2})],
			tbar : [{
				text : '添加',
				iconCls : 'icon-sys-add',
				handler : function(){
					extpropertygrid.insertRecord();
				}
			}, '-',{
				text : '删除',
				iconCls : 'icon-sys-delete',
				handler : function(){
					extpropertygrid.deleteRecord();
				}
			}, '-',{
				text : '上移',
				iconCls : 'icon-sys-moveup',
				handler : function(){
					var sm = extpropertygrid.getSelectionModel();
					var store = extpropertygrid.getStore();
					var records = extpropertygrid.getSelectionModel().getSelection();
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
					var sm = extpropertygrid.getSelectionModel();
					var store = extpropertygrid.getStore();
					var records = extpropertygrid.getSelectionModel().getSelection();
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
			}, '->',{
				text : '刷新',
				iconCls : 'icon-sys-refresh',
				handler : function(){
					extpropertygrid.getSelectionModel().deselectAll();
					extpropertygrid.getStore().reload();
				}
			}]
		});
		
		// 添加记录
		extpropertygrid.insertRecord = function(){
			var values = AscApp.ClassManager.getPropertyDefaultValues(type);
			values['f_table_name'] = me.tablename;
			values[orderFieldName] = extpropertygrid.getStore().getCount() + 1;
			var records = extpropertystore.add(values);
			extpropertygrid.getSelectionModel().select(records);
		};
		// 删除记录
		extpropertygrid.deleteRecord = function(){
			var records = extpropertygrid.getSelectionModel().getSelection();
			if(records.length > 0){
				// 设置删除标记
				if(records[0].get('id') > 0){
					// 已存在的记录标记删除
					records[0].isDeleted = true;
					records[0].setDirty();
				}else{
					// 新添加的记录直接删除
					extpropertygrid.getStore().remove(records);
				}
				// 取消选中
				extpropertygrid.getSelectionModel().deselectAll();
			}
		};
		// 选中校验
		extpropertygrid.on('beforeselect', function(g, record, index){
			// 被删除的记录不能选中
			if(record.isDeleted){
				editor.clearRecord();
				return false;
			}
		})
		// 选中
		extpropertygrid.on('selectionchange', function(g, selected){
			if(selected.length > 0){
				editor.loadRecord(selected[0]);
			}else{
				editor.clearRecord();
			}
		})
		// 保存记录
		extpropertygrid.apply = function(){
			var insertRecs = extpropertygrid.getStore().getNewRecords();
			var updateRecs = extpropertygrid.getStore().getUpdatedRecords();
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
				ExtpropertyDirect.saveProperty(me.dataid, {
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
						extpropertygrid.getSelectionModel().deselectAll();
						extpropertygrid.getStore().reload();
						me.callback(result);
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
		me.bbar = [{
			text: '确定',
			handler: function(){
				extpropertygrid.apply();
			},
			scope: me
		}, '-', {
			text: '取消',
			handler: function(){
				me.close();
			},
			scope: me
		}];
		me.items = [extpropertygrid, editor];
		me.callParent();
		extpropertygrid.getStore().load();
	}
});