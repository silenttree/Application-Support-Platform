AscApp.ClassManager.registerClass({
	// 类型
	type : 'extstate',
	// 名称
	caption : '文档辅助状态',
	//前缀
	prefix : 'est',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_left',
		text : '左操作值',
		editor : 'string',
		description : '子状态的左操作值，|field|或普通string值。'
	}, {
		name : 'f_operator',
		text : '操作符',
		editor : 'combo',
		store : [['Equal','='],['More','>'],['Less','<'],['MoreOrEqual','>='],['LesOrEqual','<='],['Include','包含'],['Custom','自定义']],
		defaultValue : 'equal',
		description : '设定左右操作值匹配计算的操作符。'
	}, {
		name : 'f_right',
		text : '右操作值',
		editor : 'string',
		description : '子状态的右操作值，|field|或普通string值。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:100},
		'f_name':{width:100, header:'英文名'},
		'f_caption':{width:100, header:'中文名'},
		'f_left':{width:200},
		'f_operator':{width:80},
		'f_right':{width:200},
		'f_properties':{width:150},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor']
});