AscApp.ClassManager.registerClass({
	// 类型
	type : 'fileimportdelparam',
	// 名称
	caption : '删除参数',
	//前缀
	prefix : 'fidp',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_sql',
		text : '删除的sql语句',
		editor : 'text',
		description : '删除的sql语句（delete XXX from XXX）。'
	},{
		name : 'f_null_sql',
		text : '空值的删除sql语句',
		editor : 'text',
		description : '空值删除的sql语句（delete XXX from XXX where XXX is null）。'
	},{
		name : 'f_is_default',
		text : '默认参数',
		editor : 'boolean',
		defaultValue : false,
		description : '标识参数是否是默认参数，创建时设定。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:120},
		'f_name':{width:120, header:'英文名'},
		'f_caption':{width:120, header:'中文名'},
		'f_sql':{width:120, header:'删除的sql语句'},
		'f_null_sql':{width:120,header:'空值sql语句'},
		'f_is_default':{width:80, header:'默认参数'},
		'f_properties':{width:120},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor']
});