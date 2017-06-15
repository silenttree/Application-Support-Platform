AscApp.ClassManager.registerClass({
	// 类型
	type : 'sfnode',
	// 名称
	caption : '节点',
	//前缀
	prefix : 'sfnode',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['sfroute'],
	//属性定义
	properties : [{
		name : 'f_type',
		text : '节点类型',
		editor : 'combo',
		store : [['StartNode', '开始节点'],['MessageNode', '消息节点'],
		         ['ServiceNode', '服务节点'],['ScriptNode', '脚本判断节点'],
		         ['EndNode', '结束节点']],
		//defaultValue : 'ProcessNode',
		description : '业务流节点的类型，根据类型可以找出对应的节点图形。'
	},{
		name : 'f_app_key',
		text : '应用标识',
		editor : 'string',
		description : '业务流节点的应用标识，需要填写。'
	},{
		name : 'f_message_key',
		text : '消息标识',
		editor : 'string',
		description : '业务流节点的消息标识，需要填写。'
	},{
		name : 'f_service_key',
		text : '服务标识',
		editor : 'String',
		description : '业务流节点的服务标识，需要填写。'
	},{
		name : 'f_params',
		text : '参数映射',
		editor : 'json',
		description : '节点类型为服务调用节点的时候起作用，用来映射向服务传递的参数。'
	},{
		name : 'f_i_message_src_node',
		text : '消息来源节点',
		editor : 'designobject',
		editorCfg : {
			type : ['sfnode'],
			scope : 'esbflownode'
		},
		description : '只定义该消息节点的消息来源节点。'
	},{
		name : 'f_script',
		text : '节点计算脚本',
		editor : 'textarea',
		description : '节点计算脚本代码'
	},{
		name : 'f_events',
		text : '节点事件',
		editor : 'textarea',
		description : '节点事件的定义。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor']
});