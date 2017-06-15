AscApp.ClassManager.registerClass({
	// 类型
	type : 'state',
	// 名称
	caption : '文档状态',
	//前缀
	prefix : 'st',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_isdefault',
		text : '默认状态',
		editor : 'boolean',
		defaultValue : false,
		description : '标识当前状态是否默认状态，创建时设定。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:120},
		'f_name':{width:120, header:'英文名'},
		'f_caption':{width:120, header:'中文名'},
		'f_isdefault':{width:80, header:'默认状态'},
		'f_properties':{width:120},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor']
});