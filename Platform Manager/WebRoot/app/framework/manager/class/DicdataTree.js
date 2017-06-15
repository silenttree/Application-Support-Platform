AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_application_entrance_test',
	// 名称
	caption : '应用导航',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_key',
		text : '字典标识',
		editor : 'string',
		description : '字典数据标识。'
	}, {
		name : 'f_value',
		text : '字典数据',
		editor : 'string',
		description : '字典数据名称。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:240},
		'f_value':{flex:1}
	}
});