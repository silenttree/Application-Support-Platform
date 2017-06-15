AscApp.ClassManager.registerClass({
	// 类型
	type : 'treepage',
	// 名称
	caption : '树状页面',
	//前缀
	prefix : 'tree',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '图标样式名。'
	},{
		name : 'f_parameters',
		text : '页面参数',
		editor : 'json',
		defaultValue : true,
		description : '设定页面访问参数。'
	},{
		name : 'f_has_controller',
		text : '页面控制器',
		editor : 'boolean',
		description : '是否定义页面控制器，按照命名规则编写控制器js文件。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor',{
		title : '节点定义',
		jspUrl : 'designer/objecttreegrid.editor',
		params : {type : 'treenode'}
	},{
		title : '右键操作',
		jspUrl : 'designer/objecttreegrid.editor',
		params : {type : 'button'}
	}]
});