AscApp.ClassManager.registerClass({
	// 类型
	type : 'serviceflow',
	// 名称
	caption : 'ESB业务流',
	//前缀
	prefix : 'sf',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['sfnode'],
	//属性定义
	properties : [{
		name : 'f_version',
		text : '业务流版本',
		editor : 'string',
		defaultValue : '1.0',
		description : '定义业务流的版本。'
	},{
		name : 'f_events',
		text : '事件脚本',
		editor : 'textarea',
		description : '设置业务流的事件脚本。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor']
});