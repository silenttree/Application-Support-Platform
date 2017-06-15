AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_log',
	// 名称
	caption : '日志配置',
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
		name : 'f_action_type',
		text : '资源类型',
		editor : 'combo',
		//defaultValue: 0,
		store : [],
		description : '资源类型。'
	}, {
		name : 'f_application_key',
		text : '应用标识',
		editor : 'string',
		description : '应用标识。'
	}, {
		name : 'f_resource_key',
		text : '资源标识',
		editor : 'string',
		description : '资源标识。'
	}, {
		name : 'f_result',
		text : '结果',
		editor : 'string',
		description : '结果。'
	}, {
		name : 'f_nodenumber',
		text : '节点编号',
		editor : 'string',
		description : '节点编号。'
	}, {
		name : 'f_thread_id',
		text : '线程ID',
		editor : 'string',
		description : '线程ID。'
	}, {
		name : 'f_user_id',
		text : '用户ID',
		editor : 'string',
		description : '用户ID。'
	}, {
		name : 'f_user_name',
		text : '用户登录名',
		editor : 'string',
		description : '用户登录名。'
	}, {
		name : 'f_user_caption',
		text : '用户姓名',
		editor : 'string',
		description : '用户姓名。'
	}, {
		name : 'f_user_ip',
		text : '用户IP',
		editor : 'string',
		description : '用户IP。'
	}, {
		name : 'f_useragent',
		text : 'f_useragent',
		editor : 'string',
		description : 'f_useragent。'
	}, {
		name : 'f_detail_before',
		text : '操作前',
		editor : 'string',
		description : '操作前。'
	}, {
		name : 'f_detail_after',
		text : '操作后',
		editor : 'string',
		description : '操作后。'
	}, {
		name : 'f_create_time',
		text : '操作时间',
		editor : 'string',
		description : '操作时间。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_type':{width:120},
		'f_action_type':{width:120},
		'f_application_key':{width:100},
		'f_resource_key':{width:100},
		'f_result':{width:100},
		'f_nodenumber':{width:100},
		'f_thread_id':{width:100},
		'f_user_id':{width:100},
		'f_user_name':{width:100},
		'f_user_caption':{width:100},
		'f_user_ip':{width:100},
		'f_useragent':{width:100},
		'f_detail_before':{width:100},
		'f_detail_after':{width:100},
		'f_user_ip':{width:100}
	}
});