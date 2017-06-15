/*
 * 属性扩展
 */
Ext.define('Asc.common.ExtPropertyData',{
	extend : 'Ext.AbstractPlugin',
	alias: 'plugin.extpropertydata',
	
	type: '',
	fields: [],
	dataid: 0,
	editor: undefined,
	
	init: function(grid) {
		var me = this;
		this.grid = grid;
		//添加事件
		grid.getStore().on('beforeload', function(s, operation, eOpts){
			me.dataid = operation.params.dataId;
		});
		
		grid.getStore().on('load', function(th, node, records, successful){
			var typeClass = AscApp.ClassManager.getClass(me.type);
			// 获得对象属性列表
			var properties = AscApp.ClassManager.getPropertie(me.type);
			// 初始化视图列
			var columns = AscApp.ClassManager.getGridColumns(properties, typeClass.propertyColumns);
			columns[2].xtype = 'treecolumn';
			me.fields = grid.getStore().model.getFields();
			//查找扩展属性
			ExtpropertyDirect.loadExtproperty(me.dataid, me.type, function(result, e){
				if(result && result.success){
					if(result.datas.length > 0){
						for(var n in result.datas){
							var data = result.datas[n];
							//添加fields
							me.fields.push({
								name: data.f_field_name
							});
							//添加视图列
							columns.push({
								header: data.f_field_caption,
								dataIndex: data.f_field_name
							});
							//添加属性到属性视图
							properties[data.f_field_name] = {
									description : data.f_field_caption,
									editor : data.f_editor_type,
									name : data.f_field_name,
									text : data.f_field_caption,
									editorCfg : data.f_config
							};
						}
					}
					//重新获取fields
					grid.getStore().model.setFields(me.fields);
					//重新加载视图
					grid.reconfigure(grid.getStore(), columns);
					//重新加载editor
					me.editor.removeAll();
					me.editor.getLoader().load();
					//添加扩展属性数据
					me.addExtData(records);
				}
			});
		});
	},

	//查找扩展属性数据
	addExtData : function(records){
		ExtpropertyDirect.findExtpropertyData(this.type, this.dataid, function(result, e){
			if(result && result.success){
				this.addData(records, result);
			}
		}, this);
	},
	
	//添加扩展属性数据
	addData : function(records, result){
		//循环扩展数据
		for(var m in result.datas){
			data = result.datas[m];
			//根据扩展数据dataid 获取行数据
			var record = this.grid.getStore().getNodeById(data.id);
			if(typeof(record) != "undefined"){
				record.set(data.f_field_name, data.f_value);
				record.commit();
			}
		}
	}
});