AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_log_system',
	// 名称
	caption : '系统日志',
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
		name : 'f_node',
		text : '节点',
		editor : 'string',
		description : '节点。'
	}, {
		name : 'f_source_key',
		text : '资源标识',
		editor : 'string',
		description : '资源标识。'
	}, {
		name : 'f_source_caption',
		text : '资源名称',
		editor : 'string',
		description : '资源名称。'
	}, {
		name : 'f_level',
		text : '级别',
		editor : 'string',
		description : '级别。'
	}, {
		name : 'f_log',
		text : '日志',
		editor : 'string',
		description : '日志。'
	}, {
		name : 'f_detail',
		text : '详细日志',
		editor : 'string',
		description : '详细日志。'
	}, {
		name : 'f_threadid',
		text : '线程ID',
		editor : 'string',
		description : '线程ID。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{hidden:true},
		'f_type':{width:120},
		'f_application_key':{width:100},
		'f_source_key':{width:100},
		'f_source_caption':{width:100},
		'f_level':{width:100},
		'f_log':{width:100},
		'f_detail':{width:100},
		'f_threadid':{width:100}
	}
});