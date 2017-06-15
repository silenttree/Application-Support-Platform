Ext.define('Asc.extension.PropertyGrid', {
	// 指定基类
	extend : 'Ext.grid.property.Grid',
	// 设置别名
	alias : 'widget.AscPropertyGrid',
	
	sortableColumns : false,
	
	viewConfig: {
		stripeRows: false
	},
	// 重载构造函数
	constructor : function(properties, cfg){
		// 初始化编辑窗口属性信息
		var source = {};
		var sourceConfig = {};
		for(var n in properties){
			var p = properties[n];
			if(p.editor == 'hidden'){
				continue;
			}
			sourceConfig[n] = {
				displayName : p.text + (p.allowBlank === false ? '<font color=red><B>&nbsp;*&nbsp;</B></font>' : '')
			},
			source[n] = '';
			sourceConfig[n].editor = this.getPropertyEditor(p);
			sourceConfig[n].renderer = this.getPropertyRenderer(p);
			if(p.renderer){
				sourceConfig[n].renderer = p.renderer;
			}
		}
		var config = Ext.apply({
			properties : properties,
			sourceConfig : sourceConfig,
			source : source
		}, cfg);
		this.callParent([config]);
	},
	initComponent : function(){
		this.callParent();
		// 显示提示信息
		this.selModel.on('select', function(sm, record, rowIndex, colIndex){
			var name = record.get('name');
			var p = this.properties[name];
			var readonly = '';
			if(p.editor == 'readonly' || p.editor == 'none' ){
				readonly = '，<font color=red>只读</font>';
			}
			this.showPropertyDescription('<B>' + p.text + ' (' + p.name + ')</B>' + readonly + '<BR><BR>' + p.description);
		}, this);
	},	
	getPropertyEditor : function(p){
		var editor = AscApp.ClassManager.getPropertyEditor(p);
		if(Ext.isDefined(editor)){
			return new Ext.grid.CellEditor({
				field : editor
			})
		}
	},
	getPropertyRenderer : function(p){
		return AscApp.ClassManager.getPropertyRenderer(p);
	},
	getPropertyValues : function(onlyChanges){
		var values = {};
		this.store.each(function(record){
			if(!onlyChanges || record.isModified('value')){
				values[record.get('name')] = record.get('value');
			}
		})
		return values;
	},

	clearPropertyValues : function(){
		this.store.each(function(record){
			record.set('value', '');
		});
		this.store.commitChanges();
	},
	
	setPropertyValues : function(values){
		for(var n in values){
			var record = this.store.getById(n);
			if(record && record != null){
				record.set('value', values[n]);
			}
		}
		this.store.commitChanges();
	},
	getPropertyValue : function(name){
		var record = this.store.getById(name);
		if(record && record != null){
			return record.get('value');
		}
		
	},
	// 显示属性说明
	showPropertyDescription : Ext.emptyFn
});