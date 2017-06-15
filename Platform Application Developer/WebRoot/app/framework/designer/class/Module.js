AscApp.ClassManager.registerClass({
	// 类型
	type : 'module',
	// 名称
	caption : '模块',
	//前缀
	prefix : 'm',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '设置模块图标样式。'
	},{
		name : 'f_type',
		text : '界面类型',
		editor : 'combo',
		store : [['默认', 'default'],['JspPage', 'JSP页面']],
		defaultValue : 'default',
		description : '选择模块界面类型，默认或打开JSP页面。'
	},{
		name : 'f_i_startpage',
		text : '起始页面',
		editor : 'designobject',
		editorCfg : {
			type : ['view', 'formtablelayout', 'formpositionlayout', 'urlpage', 'layout']
		},
		description : '起始页面，选择页面对象作为模块打开时的默认页面。'
	},{
		name : 'f_events',
		text : '事件脚本',
		editor : 'events',
		description : '模块事件，定义模块事件代码。',
		editorCfg : {
			client : {
				beforeOpen : 'boolean function(mData, mCmp)',
				afterOpen : 'function(mData, mCmp)',
				active : 'function(mCmp)',
				close : 'function(mCmp)',
			},
			extJs : ['modulePanel', 'moduleMenuTree', 'moduleWorkspace']
		}
	}],
	// 编辑器定义
	editors : ['designer/properties.editor']
});