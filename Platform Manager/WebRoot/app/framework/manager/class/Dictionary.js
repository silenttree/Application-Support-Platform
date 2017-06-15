AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_dictionary',
	// 名称
	caption : '字典列表',
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
		description : '字典标识（英文符）。'
	}, {
		name : 'f_caption',
		text : '字典名称',
		editor : 'string',
		editorCfg : {
			allowEmpty : true
		},
		description : '字典名称。'
	}, {
		name : 'f_allow_extend',
		text : '多级字典',
		editor : 'combo',
		defaultValue: 0,
		store : [[0, '否'], [1, '是']],
		description : '是否多级字典。'
	},{
		name : 'f_allow_redefine',
		text : '重载',
		editor : 'combo',
		defaultValue: 0,
		store : [[0, '否'], [1, '是']],
		description : '是否是重载字典。'
	},{
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '备注。'
	},{
		name : 'f_propertys',
		text : '扩展属性',
		editor : 'none',
		description : '扩展属性。',
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_key':{width:120},
		'f_caption':{width:100},
		'f_allow_extend':{width:60},
		'f_allow_redefine':{width:40},
		'f_propertys':{flex:1}
	}
});