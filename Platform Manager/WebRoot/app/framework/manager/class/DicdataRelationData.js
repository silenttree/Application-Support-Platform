AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_dicdata_relation_data',
	// 名称
	caption : '关系数据列表',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_relation_id',
		text : '字典数据关系',
		editor : 'combo',
		editorCfg : {
			disabled : true
		},
		defaultValue: 0,
		store : [],
		description : '字典数据关系名称。'
	},{
		name : 'f_source_data_id',
		text : '源字典数据',
		editor : 'gridcombox',
		defaultValue: 0,
		editorCfg :{
			relationId : 0,
			dicType : 0
		},
		store : [],
		description : '源字典数据。'
	},{
		name : 'f_target_data_id',
		text : '目标字典数据',
		editor : 'gridcombox',
		defaultValue: 0,
		editorCfg :{
			relationId : 0,
			dicType : 1
		},
		store : [],
		description : '目标字典数据。'
	},{
		name : 'f_order',
		text : '排序',
		editor : 'string',
		defaultValue: 0,
		description : '按源字典数据排序。'
	},{
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '备注。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40,hidden:true},
		'f_relation_id':{width:120},
		'f_source_data_id':{width:180},
		'f_target_data_id':{width:180},
		'f_order':{width:50},
		'f_note':{flex:1}
	}
});