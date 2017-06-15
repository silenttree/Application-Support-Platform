AscApp.ClassManager.registerClass({
	// 类型
	type : 'TableRule',
	// 名称
	caption : '数据表交换配置',
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
		name : 'f_dsn',
		text : '数据源',
		editor : 'string',
		description : '要交换的数据源名称。'
	}, {
		name : 'f_tablename',
		text : '数据表名',
		editor : 'string',
		description : '要交换的数据库表的名称。'
	}, {
		name : 'f_policy',
		text : '数据交换规则',
		editor : 'combo',
		defaultValue: 1,
		store : [[1, '同步'], [0, '不同步']],
		description : '内外网数据交换规则。(同步/不同步)'
	}, {
		name : 'f_scope',
		text : '规则生效范围',
		editor : 'combo',
		defaultValue: 0,
		store : [[0, '所有'], [1, '内网'], [2, '外网']],
		description : '该应用规则的生效范围。(所有/内网/外网)'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40,hidden:true},
		'f_app_id':{width:100},
		'f_app_caption':{width:100},
		'f_dsn':{width:120},
		'f_tablename':{width:100},
		'f_policy':{width:100},
		'f_scope':{width:100}
	}
});