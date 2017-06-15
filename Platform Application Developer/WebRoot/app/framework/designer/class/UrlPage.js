AscApp.ClassManager.registerClass({
	// 类型
	type : 'urlpage',
	// 名称
	caption : 'URL页面',
	//前缀
	prefix : 'up',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '页面图标样式名。'
	},{
		name : 'f_use_proxy',
		text : '使用JSP代理',
		editor : 'boolean',
		defaultValue : true,
		description : '是否使用默认JSP页面代理（JSP页面有效）。'
	},{
		name : 'f_url',
		text : '页面URL路径',
		editor : 'string',
		description : '设置页面的url路径。'
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
	},{
		name : 'f_events',
		text : '事件脚本',
		editor : 'events',
		description : '页面事件，定义URL页面事件代码。',
		editorCfg : {
			client : {
				beforeOpen : 'boolean function(pData, pCmp)',
				afterOpen : 'function(pData, pCmp)',
				active : 'function(pCmp)',
				close : 'function(pCmp)',
				beforeLoad : 'boolean function(pCmp, params)',
				afterDataLoad : 'function(pCmp, params)'
			},
			extJs : ['panel', 'form', 'grid', 'store']
		}
	}],
	// 编辑器定义
	editors : ['designer/properties.editor',{
		title : '页面操作',
		jspUrl : 'designer/objecttreegrid.editor',
		params : {type : 'button'}
	}]
});