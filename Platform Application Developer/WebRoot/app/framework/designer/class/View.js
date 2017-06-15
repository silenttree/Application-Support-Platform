AscApp.ClassManager.registerClass({
	// 类型
	type : 'view',
	// 名称
	caption : '视图页面',
	//前缀
	prefix : 'v',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '菜单图标样式名。'
	},{
		name : 'f_i_dbquery',
		text : '数据查询',
		editor : 'designobject',
		editorCfg : {
			type : 'query',
			scope : 'app'
		},
		description : '设置绑定到视图的数据库查询对象。'
	},{
		name : 'f_type',
		text : '视图类型',
		editor : 'combo',
		store : [['Normal', '普通视图'],['WeekView', '周视图'],['MonthView', '月视图'],['Custom', '自定义']],
		defaultValue : 'Normal',
		description : '指定视图界面类型，自定义视图可指定目标文件。'
	},{
		name : 'f_target',
		text : '视图目标',
		editor : 'string',
		description : '视图界面对应的解析程序URL路径，当视图类型为自定义时生效'
	},{
		name : 'f_i_form',
		text : '编辑表单',
		editor : 'designobject',
		editorCfg : {
			type : 'form',
			scope : 'module'
		},
		description : '指定行编辑对应的编辑表单，当视图为行编辑时有效。'
	},{
		name : 'f_i_documenttype',
		text : '文档类型',
		editor : 'designobject',
		editorCfg : {
			type : 'documenttype',
			scope : 'app'
		},
		description : '设置绑定到视图的数据库查询对象。'
	},{
		name : 'f_selectmode',
		text : '行选择类型',
		editor : 'combo',
		store : [['SingleSelect', '单行选择'],['MultiSelect', '多行选择']],
		defaultValue : 'SingleSelect',
		description : '视图行选择类型，指定是否为多行选择。'
	},{
		name : 'f_pagesize',
		text : '每页显示记录数',
		editor : 'number',
		defaultValue : 30,
		description : '设定列表每页显示记录数量，0为显示所有，不分页。'
	},{
		name : 'f_querytype',
		text : '查询类型',
		editor : 'combo',
		store : [['None', 'NONE'],['Column', '字段查询'],['Form', '表单查询']],
		defaultValue : 'Column',
		description : '设定查询类型，字段查询为默认生成查询界面，表单查询则由外部查询表单提供查询界面。'
	},{
		name : 'f_vague_query',
		text : '模糊查询',
		editor : 'boolean',
		defaultValue : true,
		description : '设定是否支持模糊查询，在所有字符字段中模糊检索。'
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
	}, {
		name : 'f_properties',
		text : '扩展属性',
		editor : 'json',
		initialValue :[{name:'cfg',type:'object',cfg:{},value:{}}],
		description : '视图的扩展属性，设置该视图的一些基本默认属性。'
	}, {
		name : 'f_events',
		text : '事件脚本',
		editor : 'events',
		description : '视图事件，定义视图事件代码。',
		editorCfg : {
			server : {
				BeforeDataLoad : 'function(View view, JsonObject params, ViewContext context, User user)',
				DataLoad : 'JsonArray function(View view, JsonObject params, ViewContext context, User user)',
				AfterDataLoad : 'function(View view, JsonObject params, JsonArray results, ViewContext context, User user)',
				ColumnDataParse : 'JsonPrimitive function(ViewColumn column, JsonPrimitive data, ViewContext context, User user)'
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
		title : '列定义',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'viewfield'}
	},{
		title : '视图操作',
		jspUrl : 'designer/objecttreegrid.editor',
		params : {type : 'button'}
	},{
		title : '查询条件',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'viewqueryparameter'}
	}]
});