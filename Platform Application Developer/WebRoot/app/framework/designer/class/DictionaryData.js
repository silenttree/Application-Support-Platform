AscApp.ClassManager.registerClass({
	// 类型
	type : 'dictionarydata',
	// 名称
	caption : '字典数据',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '字典排序号。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_name':{width:150, header:'字典值'},
		'f_caption':{width:120, header:'名称'},
		'f_properties':{flex:1},
		'f_description':{width:200}
	}
});