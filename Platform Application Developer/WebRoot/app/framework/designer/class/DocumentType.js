AscApp.ClassManager.registerClass({
	// 类型
	type : 'documenttype',
	// 名称
	caption : '文档类型',
	//前缀
	prefix : 'dt',
	// 是否设计对象
	isDesignObject : true,
	//属性定义
	properties : [{
		name : 'f_type',
		text : '文档类型',
		editor : 'number',
		description : '定义文档的类型。'
	},{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '设置文档类型的图标。'
	},{
		name : 'f_action',
		text : '文档对应动作',
		editor : 'combo',
		store : [['None', '无动作'],['Script', '执行脚本'],['PageObject', '打开页面'],['Hyperlink', '超链接']],
		defaultValue : 'None',
		description : '设置文档类型的动作类型。'
	},{
		name : 'f_param',
		text : '参数',
		editor : 'json',
		description : '设置文档类型传入的参数。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor']
});