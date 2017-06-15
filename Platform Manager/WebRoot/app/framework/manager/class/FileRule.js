AscApp.ClassManager.registerClass({
	// 类型
	type : 'FileRule',
	// 名称
	caption : '文件交换配置',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_app_id',
		text : '应用id',
		editor : 'string',
		description : '要交换数据的应用的标识。'
	}, {
		name : 'f_app_caption',
		text : '应用标题',
		editor : 'string',
		description : '应用标题名称。'
	}, {
		name : 'f_policy',
		text : '数据交换规则',
		editor : 'combo',
		defaultValue: 1,
		store : [[1, '同步'], [0, '不同步']],
		description : '内外网数据交换规则。(同步/不同步)'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40,hidden:true},
		'f_app_id':{width:120},
		'f_app_caption':{width:200},
		'f_policy':{width:150}
	}
});