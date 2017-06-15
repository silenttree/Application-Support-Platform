AscApp.ClassManager.registerClass({
	// 类型
	type : 'queryfield',
	// 名称
	caption : '查询字段',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '输入字段排序号。'
	}, {
		name : 'f_inputtype',
		text : '输入类型',
		editor : 'combosimple',
		editorCfg : {
			editable : true
		},
		store : ['none','display','boolean','number','text','textarea','combo','check','radio','file','image','html'],
		defaultValue : 'none',
		description : '指定输入域类型。'
	}, {
		name : 'f_defaultvalue',
		text : '默认值',
		editor : 'string',
		description : '设置字段默认值。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:100},
		'f_name':{width:100, header:'英文名'},
		'f_caption':{width:100, header:'中文名'},
		'f_inputtype':{width:120},
		'f_defaultvalue':{width:120},
		'f_properties':{width:150},
		'f_description':{flex:1}
	}
});