AscApp.ClassManager.registerClass({
	// 类型
	type : 'queryform',
	// 名称
	caption : '查询表单',
	//前缀
	prefix : 'qfrm',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['queryfield'],
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '菜单图标样式名。'
	},{
		name : 'f_cols',
		text : '表单列数',
		editor : 'none',
		defaultValue : 1,
		description : '设置查询表单布局的总列数，自动维护。'
	},{
		name : 'f_rows',
		text : '表单行数',
		editor : 'none',
		defaultValue : 0,
		description : '设置查询表单布局的总行数，自动维护。'
	},{
		name : 'f_parameters',
		text : '页面参数',
		editor : 'json',
		defaultValue : true,
		description : '设定页面访问参数。'
	},{
		name : 'f_events',
		text : '事件脚本',
		editor : 'events',
		description : '查询表单事件，定义查询表单事件代码。',
		editorCfg : {
			server : {
				beforeLoad : 'function(TableForm form, JsonObject params, FormContext context)',
				load : 'JsonObject function(TableForm form, JsonObject params, FormContext context)',
				afterLoad : 'function(TableForm form, JsonObject params, FormContext context, JsonObject data)',
				beforeSubmit : 'boolean function(TableForm form, JsonObject oldData, JsonObject submitData, JsonObject errors, FormContext context)',
				submit : 'JsonObject function(TableForm form, JsonObject oldData, JsonObject submitData, FormContext context)',
				afterSubmit : 'function(TableForm form, JsonObject oldData, JsonObject newData, FormContext context)',
				beforeSubmitPost : 'boolean function(TableForm form, JsonObject oldData, Map<String, String> submitData, Map<String, File> files, JsonObject errors, FormContext context)',
				submitPost : 'JsonObject function(TableForm form, JsonObject oldData, Map<String, String> submitData, Map<String, File> files, JsonObject errors, FormContext context)',
				afterSubmitPost : 'function(TableForm form, JsonObject oldData, JsonObject newData, Map<String, File> files, FormContext context)',
				beforeDelete : 'boolean function(TableForm form, JsonObject params, FormContext context)',
				delete : 'function(TableForm form, JsonObject params, FormContext context)',
				afterDelete : 'function(TableForm form, JsonObject params, JsonObject delData, FormContext context)'
			},
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
		title : '查询输入域',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'queryfield'}
	},{
		title : '查询表单布局',
		jspUrl : 'designer/form.tablelayout.editor'
	}]
});