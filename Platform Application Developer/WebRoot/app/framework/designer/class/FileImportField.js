AscApp.ClassManager.registerClass({
	// 类型
	type : 'fileimportfield',
	// 名称
	caption : '字段匹配列表',
	//前缀
	prefix : 'fif',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_column_name',
		text : '数据库表的列名',
		editor : 'string',
		description : '对应数据库的数据表的列名。'
	},{
		name : 'f_file_column_index',
		text : '对应文件的列下标。',
		editor : 'number',
		description : '导入对应文件的列下标。'
	},{
		name : 'f_java_type',
		text : 'java类型',
		editor : 'combo',
		store : ['String','Long','Float', 'Datetime', 'Integer', 'Double', 'Boolean'],
		defaultValue : 'String',
		description : '标识导入文件的字段对应的java数据类型。'
	},{
		name : 'f_is_primary_key',
		text : '是否关键字段',
		editor : 'boolean',
		defaultValue : false,
		description : '标识导入文件的字段是否是主键。'
	},{
		name : 'f_is_display',
		text : '是否显示',
		editor : 'boolean',
		defaultValue : false,
		description : '标识导入文件的字段是否是在页面上展示出来。'
	},{
		name : 'f_i_dictionary',
		text : ' 关联转换字典',
		editor : 'designobject',
		editorCfg : {
			type : 'dictionary',
			scope : 'app'
		},
		description : '标识导入文件的字段是否转换字典。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:120},
		'f_name':{width:120, header:'英文名'},
		'f_caption':{width:120, header:'中文名'},
		'f_column_name':{width:120, header:'数据库表的列名'},
		'f_file_column_index':{width:50,header:'列下标'},
		'f_java_type':{width:80, header:'java类型'},
		'f_is_primary_key':{width:120, header:'是否关键字段'},
		'f_is_display':{width:80, header:'是否显示'},
		'f_i_dictionary':{width:120,header:' 关联转换字典'},
		'f_properties':{width:90},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor']
});