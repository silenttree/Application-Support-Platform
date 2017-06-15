AscApp.ClassManager.registerClass({
	// 类型
	type : 'layoutitem',
	// 名称
	caption : '布局元素',
	//前缀
	prefix : 'lyoi',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '视图按钮排序号。'
	},{
		name : 'f_i_page',
		text : '页面对象',
		editor : 'designobject',
		editorCfg : {
			type : ['view', 'treepage', 'formtablelayout', 'formpositionlayout', 'urlpage', 'layout'],
			scope : 'module'
		},
		description : '设置布局页面对象。'
	},{
		name : 'f_layout_config',
		text : '布局配置参数',
		editor : 'json',
		initialValue :[{name:'region',type:'string',cfg:{},value:''},
		       		{name:'layout',type:'string',cfg:{},value:''},
		       		{name:'split',type:'boolean',cfg:{},value:true},
		       		{name:'collapseMode',type:'string',cfg:{},value:'mini'},
		       		{name:'autoScroll',type:'boolean',cfg:{},value:true}],
		description : '设置布局配置参数。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:100},
		'f_name':{width:100, header:'英文名'},
		'f_caption':{width:100, header:'中文名'},
		'f_i_page':{width:150},
		'f_layout_config':{width:150},
		'f_properties':{width:120},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor']
});