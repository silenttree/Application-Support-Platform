AscApp.ClassManager.registerClass({
	// 类型
	type : 'portlet',
	// 名称
	caption : '门户栏目',
	//前缀
	prefix : 'ptl',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '设置栏目图标样式。'
	},{
		name : 'f_i_page',
		text : '对应页面',
		editor : 'designobject',
		editorCfg : {
			type : ['view', 'formtablelayout', 'formpositionlayout', 'urlpage', 'layout']
		},
		description : '对应页面，选择页面对象作为桌面栏目对应的默认页面。'
	},{
		name : 'f_refresh_interval',
		text : '刷新间隔（秒）',
		editor : 'number',
		defaultValue : 30,
		description : '栏目自动刷新的时间间隔。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor']
});