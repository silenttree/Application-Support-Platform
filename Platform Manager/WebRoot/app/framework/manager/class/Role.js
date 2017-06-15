AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_role',
	// 名称
	caption : '角色列表',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_type',
		text : '角色类型',
		editor : 'combo',
		store : [[0, '普通角色'], [1, '自定义']],
		description : '角色类型'
	}, {
		name : 'f_caption',
		text : '角色名称',
		editor : 'string',
		description : '角色名称。'
	}, {
		name : 'f_key',
		text : '角色标识',
		editor : 'string',
		editorCfg : {
			allowEmpty : true
		},
		description : '角色标识。'
	}, {
		name : 'f_config',
		text : '角色配置',
		editor : 'string',
		editorCfg : {
			allowEmpty : true
		},
		description : '用于自定义角色配置可解析参数'
	}, {
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '说明信息。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_type':{width:80},
		'f_key':{width:120},
		'f_caption':{width:100},
		'f_config':{width:200},
		'f_note':{flex:1}
	}
});