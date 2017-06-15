AscApp.ClassManager.registerClass({
	// 类型
	type : 'sfroute',
	// 名称
	caption : '路由',
	//前缀
	prefix : 'sfroute',
	// 是否设计对象
	isDesignObject : true,
	//属性定义
	properties : [{
		name : 'f_i_dest_node',
		text : '目标节点',
		editor : 'designobject',
		editorCfg : {
			type : ['sfnode'],
			scope : 'esbflow'
		},
		description : '定义该路由的目标节点。'
	},{
		name : 'f_type',
		text : '路由类型',
		editor : 'combo',
		store : [['CommonRoute', '一般路由'],['ErrorRoute', '异常路由']],
		description : '定义路由的类型。'
	},{
		name : 'f_events',
		text : '事件脚本',
		editor : 'textarea',
		description : '设置业务流的事件脚本。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor']
});