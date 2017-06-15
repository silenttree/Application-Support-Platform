AscApp.ClassManager.registerClass({
	// 类型
	type : 'appconn',
	// 名称
	caption : '应用连接',
	// 是否设计对象
	isDesignObject : false,
	// 属性定义
	properties : [{
		name : 'appId',
		text : '标识',
		editor : 'none',
		description : '应用连接标识'
	}, {
		name : 'appCaption',
		text : '名称',
		editor : 'string',
		description : '应用连接名称，用于显示。'
	},{
		name : 'appType',
		text : '链接类型',
		editor : 'combo',
		store : [['app', '应用链接'],['esb', 'ESB业务流链接']],
		defaultValue : 'app',
		description : '应用连接类型，根据链接类型，显示不同的结构树。'
	},{
		name : 'appHost',
		text : '访问地址',
		editor : 'string',
		defaultValue : 'http://localhost:8080/demo',
		description : '应用连接访问地址。'
	}, {
		name : 'serviceUrl',
		text : '服务路径',
		editor : 'string',
		defaultValue : '/services/DesignObjectAccessService?wsdl',
		description : '数字编辑框。'
	}, {
		name : 'note',
		text : '备注',
		editor : 'text',
		description : '应用连接备注信息'
	}],
	// 编辑器定义
	editors : ['designer/appconnection.editor']
});