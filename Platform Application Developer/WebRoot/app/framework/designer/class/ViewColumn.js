AscApp.ClassManager.registerClass({
	// 类型
	type : 'viewfield',
	// 名称
	caption : '视图字段',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '视图按钮排序号。'
	}, {
		name : 'f_asname',
		text : '字段别名',
		editor : 'string',
		description : 'SQL语句中的别名。'
	}, {
		name : 'f_type',
		text : '列类型',
		editor : 'combosimple',
		store : ['None','String','Integer','Float','Date','Datetime','Icon','File','Dictionary','JsonObject','Custom'],
		defaultValue : 'String',
		description : '视图列类型。'
	}, {
		name : 'f_renderer',
		text : '渲染脚本',
		editor : 'textarea',
		description : '字段渲染脚本函数，自定义字段显示。'
	}, {
		name : 'f_query',
		text : '查询方式',
		editor : 'combosimple',
		store : ['None','String','Dictionary','Date','Number'],
		defaultValue : 'None',
		description : '设置列查询方式。'
	}, {
		name : 'f_orderable',
		text : '支持排序',
		editor : 'boolean',
		defaultValue : true,
		description : '设置列是否支持排序。'
	}, {
		name : 'f_width',
		text : '列宽',
		editor : 'number',
		defaultValue : 120,
		description : '设置列显示宽度。'
	},{
		name : 'f_properties',
		text : '扩展属性',
		editor : 'json',
		initialValue :[{name:'dictionary',type:'string',cfg:{},value:''},
		            {name:'sdictionary',type:'string',cfg:{},value:''},
		       		{name:'cfg',type:'object',cfg:{},value:{},
		       		children:[{name:'xtype',type:'string',cfg:{},value:''},
		       		{name:'decimalPrecision',type:'number',cfg:{},value:0}
		       		]}],
		description : '列定义的扩展属性，设置该列的一些基本默认属性。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor'],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:100},
		'f_name':{width:100, header:'英文名'},
		'f_caption':{width:100, header:'中文名'},
		'f_asname':{width:100},
		'f_type':{width:80},
		'f_renderer':{width:150},
		'f_query':{width:100},
		'f_orderable':{width:80},
		'f_width':{width:80},
		'f_properties':{width:200},
		'f_description':{flex:1}
	}
});