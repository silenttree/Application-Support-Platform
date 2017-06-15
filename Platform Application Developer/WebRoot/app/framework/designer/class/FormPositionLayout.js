AscApp.ClassManager.registerClass({
	// 类型
	type : 'formpositionlayout',
	// 名称
	caption : '背景布局',
	//前缀
	prefix : 'fpl',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '菜单图标样式名。'
	},{
		name : 'f_width',
		text : '背景宽度',
		editor : 'number',
		defaultValue : 500,
		description : '指定表单背景图片像素宽度，即界面宽度。'
	},{
		name : 'f_height',
		text : '背景高度',
		editor : 'number',
		defaultValue : 600,
		description : '指定表单背景图片像素高度度，即界面高度。'
	},{
		name : 'f_has_controller',
		text : '页面控制器',
		editor : 'boolean',
		description : '是否定义页面控制器，按照命名规则编写控制器js文件。'
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
		description : '表单布局事件，定义事件代码。',
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
		title : '表单布局',
		jspUrl : 'designer/form.positionlayout.editor'
	},{
		title : '表单操作',
		jspUrl : 'designer/objecttreegrid.editor',
		params : {type : 'button'}
	}]
});