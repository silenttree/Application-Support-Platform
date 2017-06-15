AscApp.ClassManager.registerClass({
	// 类型
	type : 'dbfield',
	// 名称
	caption : '表字段',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '字段排序号。'
	}, {
		name : 'f_db_type',
		text : 'db数据类型',
		editor : 'combosimple',
		editorCfg : {
			editable : true
		},
		store : ['Char', 'Number', 'Datetime', 'Blob', 'Clob'],
		defaultValue : 'Char',
		description : '字段数据库数据类型定义。'
	}, {
		name : 'f_db_type_ext',
		text : '数字精度',
		editor : 'string',
		description : '数字类型数据库字段精度定义，例(10,2)。'
	}, {
		name : 'f_length',
		text : '字符长度',
		editor : 'string',
		defaultValue : 50,
		description : '指定字段的字符长度，当数据类型为char或clob时有效。'
	}, {
		name : 'f_java_type',
		text : 'java数据类型',
		editor : 'combosimple',
		store : ['String', 'Long', 'Float', 'Datetime', 'Blob'],
		defaultValue : 'String',
		description : '字段对应的JAVA数据类型定义。'
	}, {
		name : 'f_keyfield',
		text : '是否主键',
		editor : 'boolean',
		defaultValue : false,
		description : '标识当前字段在数据库表中是否为主键字段。'
	}, {
		name : 'f_allownull',
		text : '允许为空',
		editor : 'boolean',
		defaultValue : true,
		description : '标识当前字段在数据库表中是否允许为空。'
	}, {
		name : 'f_defaultvalue',
		text : '默认值',
		editor : 'string',
		description : '指定字段的默认值。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor'],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:150},
		'f_name':{width:150, header:'英文名'},
		'f_caption':{width:120, header:'中文名'},
		'f_db_type':{width:100},
		'f_db_type_ext':{width:70},
		'f_length':{width:80},
		'f_java_type':{width:100},
		'f_keyfield':{width:80},
		'f_allownull':{width:80},
		'f_defaultvalue':{width:100},
		'f_properties' : {width:120},
		'f_description':{flex:1}
	}
});