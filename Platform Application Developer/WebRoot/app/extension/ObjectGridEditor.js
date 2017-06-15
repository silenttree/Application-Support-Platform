Ext.define('Asc.extension.ObjectGridEditor', {
	// 指定基类
	extend : 'Ext.grid.Panel',
	// 设置别名
	alias : 'widget.AscObjectGridEditor',
	
	sortableColumns : false,
    
    viewConfig: {
        stripeRows: true
    },
	// 重载构造函数
	constructor : function(parentObject, type, properties, propertyColumns, cfg){
		// 编辑器
		this.plugins  = [Ext.create('Ext.grid.plugin.CellEditing', {})];
		// 初始化字段属性
		var fields = [];
		for(var n in properties){
			var p = properties[n];
			fields.push({name : p.name});
		}
		// 初始化列属性
		var columns = [{
			xtype: 'rownumberer',
			dataIndex : Ext.isDefined(properties['f_order']) ? 'f_order' : undefined
		}];
		for(var n in propertyColumns){
			var col = Ext.apply({
				dataIndex : properties[n].name,
				header : properties[n].text
			}, propertyColumns[n]);
			var editor = AscApp.ClassManager.getPropertyEditor(properties[n]);
			if(Ext.isDefined(editor)){
				col.editor = editor;
			}
			var renderer = AscApp.ClassManager.getPropertyRenderer(properties[n]);
			if(Ext.isDefined(renderer)){
				col.renderer = renderer;
			}
			columns.push(col);
		}
		// 初始化存储
		var store = new Ext.data.Store({
			clearRemovedOnLoad : true,
			proxy : {
				type : 'direct',
				directFn : DeveloperAppDirect.loadObjects,
				extraParams : {
					appId : parentObject.appId,
					parentId : parentObject.key,
					type : type
				},
				paramOrder : 'appId parentId type',
				paramsAsHash : true,
				reader : {
					type: 'json',
					root : 'datas',
					messageProperty : 'message'
				}
			},
			sorters : [{
				property : Ext.isDefined(properties['f_order']) ? 'f_order' : 'f_key',
				direction: 'ASC'
			}],
			fields : fields
		});
		var config = Ext.apply({
			store : store,
			columns : columns
		}, cfg);
        this.callParent([config]);
	},
    initComponent : function(){
    	this.callParent();
    }
});