AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_log_manager',
	// 名称
	caption : '运维日志',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_type',
		text : '日志类型',
		editor : 'combo',
		//defaultValue: 0,
		store : [],
		description : '日志类型。'
	}, {
		name : 'f_application_key',
		text : '应用标识',
		editor : 'string',
		description : '应用标识。'
	}, {
		name : 'f_user_id',
		text : '用户ID',
		editor : 'string',
		description : '用户ID。'
	}, {
		name : 'f_user_name',
		text : '用户姓名',
		editor : 'string',
		description : '用户姓名。'
	}, {
		name : 'f_user_host',
		text : '用户IP',
		editor : 'string',
		description : '用户IP。'
	}, {
		name : 'f_log',
		text : '日志',
		editor : 'string',
		description : '日志。'
	}, {
		name : 'f_user_agent',
		text : '用户代理',
		editor : 'string',
		description : '用户代理。'
	}, {
		name : 'f_action',
		text : '资源',
		editor : 'string',
		description : '资源。'
	}, {
		name : 'f_action_param',
		text : '参数',
		editor : 'string',
		description : '参数。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_type':{width:120},
		'f_application_key':{width:100},
		'f_user_id':{width:100},
		'f_user_name':{width:100},
		'f_user_host':{width:100},
		'f_log':{width:100},
		'f_user_agent':{width:100},
		'f_action':{width:100}
	}
});