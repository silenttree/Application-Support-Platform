AscApp.ClassManager.registerClass({
	// 类型
	type : 'panel',
	// 名称
	caption : '文档标签',
	//前缀
	prefix : 'p',
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
		text : '标签类型',
		editor : 'combosimple',
		store : ['Form','View','Custom'],
		defaultValue : 'Form',
		description : '指定文档标签类型。'
	},{
		//name : 'f_i_page',
		name : 'f_i_page',
		text : '标签页面',
		editor : 'designobject',
		editorCfg : {
			//returnType : 'string',
			type : ['view', 'formtablelayout', 'formpositionlayout', 'urlpage', 'layout'],
			scope : 'module'
		},
		description : '指定标签对应的页面对象。'
	},{
		name : 'f_parameters',
		text : '页面参数',
		editor : 'json',
		defaultValue : true,
		description : '设定页面访问参数。'
	},{
		name : 'f_properties',
		text : '扩展属性',
		editor : 'json',
		initialValue :[{name:'cfg',type:'object',cfg:{},value:{}},
		               {name:'idParamName',type:'string',cfg:{},value:''}],
		description : '文档标签的扩展属性，设置该文档标签的一些基本默认属性。'
	},{
		name : 'f_events',
		text : '事件脚本',
		editor : 'events',
		description : '页面事件，定义标签页面事件代码。',
		editorCfg : {
			server : {
				beforeLoad : 'function()',
				load : 'function()',
				afterLoad : 'function()',
				beforeSubmit : 'function()',
				submit : 'function()',
				afterSubmit : 'function()',
			},
			client : {
				beforeOpen : 'function()',
				afterOpen : 'function()',
				active : 'function()',
				close : 'function()'
			},
			extJs : ['panel']
		}
	},{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '标签页的排序号。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:100},
		'f_name':{width:100, header:'英文名'},
		'f_caption':{width:100, header:'中文名'},
		'f_icon':{width:100},
		'f_type':{width:70},
		'f_i_page':{width:120},
		'f_parameters':{width:200},
		'f_events':{width:150},
		'f_properties':{width:150},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor']
});