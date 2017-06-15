AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_app_ent_auth',
	// 名称
	caption : '应用导航',
	// 属性定义
	properties : [{
		xtype : 'treecolumn',
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
		description : '应用导航标识，不可重复。'
	}, {
		name : 'f_order',
		text : '序号',
		editor : 'none',
		defaultValue : 0,
		description : '应用导航序号。'
	}, {
		name : 'f_level',
		text : '级别',
		editor : 'none',
		defaultValue : 0,
		description : '应用导航级别。'
	}, {
		name : 'f_caption',
		text : '名称',
		editor : 'string',
		defaultValue : '导航名称',
		description : '应用导航名称。'
	}, {
		name : 'f_application_key',
		text : '应用标识',
		editor : 'string',
		defaultValue : 'app1',
		description : '相关应用标识'
	}, {
		name : 'f_module_key',
		text : '模块标识',
		editor : 'string',
		defaultValue : 'app1',
		description : '相关模块标识'
	}, {
		name : 'f_disabled',
		text : '是否可用',
		editor : 'combo',
		store : [[0, '否'], [1, '是']],
		defaultValue : 1,
		description : '应用导航是否可用。'
	}, {
		name : 'f_icon',
		text : '图标地址',
		editor : 'string',
		defaultValue : '',
		description : '图标地址。'
	}, {
		name : 'f_note',
		text : '备注',
		editor : 'none',
		defaultValue : '',
		description : '说明信息。'
	}, {
		name : 'f_auth_expression',
		text : '权限表达式',
		editor : 'none',
		defaultValue : '',
		description : '权限表达式。'
	}, {
		name : 'f_auth_display',
		text : '权限',
		editor : 'none',
		defaultValue : '',
		description : '权限。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:200},
		'f_caption':{width:120},
		'f_auth_display' : {flex:1}
	}
});