AscApp.ClassManager.registerClass({
	// 类型
	type : 'queryparameter',
	// 名称
	caption : '查询参数',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_sql',
		text : '查询语句',
		editor : 'text',
		description : '查询条件语句，语句中的|value|为参数值。'
	}, {
		name : 'f_nullsql',
		text : '空值查询语句',
		editor : 'text',
		description : '当查询参数值为空时的查询条件语句。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor'],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:150},
		'f_name':{width:150, header:'参数名'},
		'f_caption':{width:120, header:'中文名'},
		'f_sql':{width:200},
		'f_nullsql':{width:200},
		'f_description':{flex:1}
	}
});