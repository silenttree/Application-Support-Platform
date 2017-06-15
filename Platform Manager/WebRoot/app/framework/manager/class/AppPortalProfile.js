AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_app_portalprofile',
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
		text : '标识',
		editor : 'string',
		editorCfg : {
			allowEmpty : true
		},
		defaultValue : 'appent',
		description : '门户模板标识，不可重复。'
	}, {
		name : 'f_order',
		text : '序号',
		editor : 'none',
		defaultValue : 0,
		description : '门户模板序号。'
	}, {
		name : 'f_caption',
		text : '名称',
		editor : 'string',
		defaultValue : '门户模板',
		description : '门户模板名称。'
	}, {
		name : 'f_scope_expression',
		text : '权限表达式',
		editor : 'hidden',
		defaultValue : '',
		description : '权限表达式。'
	}, {
		name : 'f_scope_display',
		text : '权限',
		editor : 'none',
		defaultValue : '',
		description : '权限。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:100},
		'f_caption':{width:120},
		'f_scope_display' : {width:200,flex:1}
	}
});