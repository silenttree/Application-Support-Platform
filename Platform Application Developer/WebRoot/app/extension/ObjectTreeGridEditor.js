Ext.define('Asc.extension.ObjectTreeGridEditor', {
	// 指定基类
	extend : 'Ext.tree.Panel',
	// 设置别名
	alias : 'widget.AscObjectTreeGridEditor',
	
	rootVisible: false,
	
	useArrows: true,
	
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
		},{
            xtype: 'treecolumn', //this is so we know which column will show the tree
            text: 'f_key',
            width : 120,
            dataIndex: 'f_key'
		}];
		for(var n in propertyColumns){
			if(properties[n].name == 'id'){
				continue;
			}
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
		var store = new Ext.data.TreeStore({
			clearRemovedOnLoad : true,
			proxy : {
				type : 'direct',
				directFn : DeveloperAppDirect.loadTreeObjects,
				extraParams : {
					appId : parentObject.appId,
					parentId : parentObject.key,
					type : type
				},
				paramOrder : 'appId parentId type',
				paramsAsHash : true
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