AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_datasource',
	// 名称
	caption : '数据库实例配置',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_caption',
		text : '数据源名称',
		editor : 'string',
		description : '数据源名称。'
	}, {
		name : 'f_databases_caption',
		text : '数据库名称',
		editor : 'string',
		description : '数据库名称。'
	}, {
		name : 'f_type',
		text : '数据库类型',
		editor : 'string',
		description : '数据库类型。'
	}, {
		name : 'f_ip',
		text : 'IP',
		editor : 'string',
		description : '数据库IP地址。'
	}, {
		name : 'f_port',
		text : '端口',
		editor : 'number',
		description : '数据库端口。'
	}, {
		name : 'f_url',
		text : 'URL',
		editor : 'string',
		description : '数据库访问地址。'
	}, {
		name : 'f_location',
		text : '地址',
		editor : 'string',
		description : '数据库地址。'
	}, {
		name : 'f_user',
		text : '用户名',
		editor : 'string',
		description : '数据库用户名。'
	}, {
		name : 'f_password',
		text : '密码',
		editor : 'string',
		description : '数据库密码。'
	}, {
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '备注。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_caption':{width:100},
		'f_databases_caption':{width:100},
		'f_type':{width:100},
		'f_ip':{width:100},
		'f_port':{width:100},
		'f_url':{width:100},
		'f_location':{width:100},
		'f_user':{
			width:100,
			editor: new Ext.form.field.Text({
                typeAhead: true
            })
		},
		'f_password':{
			width:100,
			editor: new Ext.form.field.Text({
                typeAhead: false
            })
		},
		'f_note':{flex:1}
	}
});