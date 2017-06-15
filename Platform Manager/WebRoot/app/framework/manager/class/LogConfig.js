AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_log_config',
	// 名称
	caption : '日志配置',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_application_id',
		text : '应用标识',
		editor : 'none',
		description : '应用标识。'
	}, {
		name : 'f_resource_id',
		text : '资源标识',
		editor : 'string',
		description : '资源标识。'
	}, {
		name : 'f_type',
		text : '日志类型',
		editor : 'combo',
		//defaultValue: 0,
		store : [],
		description : '日志类型。'
	}, {
		name : 'f_action',
		text : '动作名称',
		editor : 'textarea',
		description : '动作名称（如direct类方法名，文档动作save、delete等）。'
	}, {
		name : 'f_targets',
		text : '目标级别',
		editor : 'json',
		description : '格式如{debug:xxx,info:yyy,...}的json字符串,一共包括五个级别（debug、info、warn、error、fatal）。'
	}, {
		name : 'f_has_userinfo',
		text : '用户信息',
		editor : 'combo',
		defaultValue: 1,
		store : [[0, '否'], [1, '是']],
		description : '用户信息。'
	}, {
		name : 'f_has_requestinfo',
		text : '请求信息',
		editor : 'combo',
		defaultValue: 1,
		store : [[0, '否'], [1, '是']],
		description : '请求信息。'
	}, {
		name : 'f_has_detail',
		text : '详细信息',
		editor : 'combo',
		defaultValue: 1,
		store : [[0, '否'], [1, '是']],
		description : '详细信息。'
	}, {
		name : 'f_state',
		text : '状态',
		editor : 'combo',
		defaultValue: 0,
		store : [[0, '启用'], [1, '禁用']],
		description : '定义的日志规则是否启用。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{hidden:true},
		'f_application_id':{width:120},
		'f_resource_id':{width:120},
		'f_type':{width:80},
		'f_action':{width:180},
		'f_targets':{width:180},
		'f_has_userinfo':{width:60},
		'f_has_requestinfo':{width:60},
		'f_has_detail':{width:60},
		'f_state':{width:60}
	}
});