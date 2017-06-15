AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_application_instance',
	// 名称
	caption : '应用实例',
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
		defaultValue : 'appnode',
		description : '应用系统标识，不可重复。'
	}, {
		name : 'f_serialnumber',
		text : '编号',
		editor : 'none',
		defaultValue : 0,
		description : '应用实例节点编号。'
	}, {
		name : 'f_ip',
		text : '主机地址',
		editor : 'string',
		description : '应用主机IP地址。'
	}, {
		name : 'f_port',
		text : '端口号',
		editor : 'number',
		defaultValue : 80,
		description : '应用端口号。'
	}, {
		name : 'f_app_path',
		text : '应用路径',
		editor : 'string',
		description : '应用发布路径。'
	}, {
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '说明信息。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_key':{width:100},
		'f_ip':{width:120},
		'f_port':{width:60},
		'f_app_path':{width:120},
		'f_note':{flex:1}
	}
});