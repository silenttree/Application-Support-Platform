AscApp.ClassManager.registerClass({
	// 类型
	type : 'layout',
	// 名称
	caption : '布局',
	//前缀
	prefix : 'lyo',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '页面图标样式名。'
	}, {
		name : 'f_type',
		text : '布局类型',
		editor : 'combo',
		store : [['Border', 'Border布局'],['Tabs', '多标签布局'],['Column', '列布局'],['Table', '表格布局'],['VBox', '垂直布局'],['HBox', '水平布局'],['Position', '绝对位置布局'],['Anchor', '伸缩面板布局']],
		defaultValue : 'None',
		description : '指定布局页面的布局类型。'
	},{
		name : 'f_layout_config',
		text : '布局配置参数',
		editor : 'json',
		description : '设置布局配置参数。'
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
	editors : ['designer/properties.editor', {
		title : '布局元素',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'layoutitem'}
	}]
});