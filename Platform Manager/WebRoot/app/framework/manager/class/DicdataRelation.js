AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_dicdata_relation',
	// 名称
	caption : '字典数据关系列表',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_key',
		text : '关系标识',
		editor : 'string',
		description : '关系标识（英文符）。'
	}, {
		name : 'f_caption',
		text : '关系描述',
		editor : 'string',
		editorCfg : {
			allowEmpty : true
		},
		description : '关系描述。'
	}, {
		name : 'f_source_dic_id',
		text : '源字典标识',
		editor : 'combo',
		defaultValue: 0,
		store : [],
		description : '源字典标识。'
	},{
		name : 'f_target_dic_id',
		text : '目的字典标识',
		editor : 'combo',
		defaultValue: 0,
		store : [],
		description : '目的字典标识。'
	},{
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '备注。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_key':{width:120},
		'f_caption':{width:120},
		'f_source_dic_id':{width:120},
		'f_target_dic_id':{width:120},
		'f_note':{width:120}
	}
});