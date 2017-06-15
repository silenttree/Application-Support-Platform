AscApp.ClassManager.registerClass({
	// 类型
	type : 'treenode',
	// 名称
	caption : '树状页面节点',
	//前缀
	prefix : 'treenode',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['treenode'],
	// 属性
	properties : [{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '节点排序号。'
	},{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '节点图标样式名。'
	}, {
		name : 'f_group_type',
		text : '分组类型',
		editor : 'combo',
		store : [['None', '无分组'],['StaticDictionary', '静态字典'],['Dictionary', '数据字典'],['Organization', '组织机构'],['QueryData', '检索数据'],['ViewGroup', '视图分组'],['Custom', '自定义']],
		defaultValue : 'None',
		description : '指定菜单下级节点分组类型。'
	}, {
		name : 'f_group_arguments',
		text : '分组参数',
		editor : 'json',
		initialValue :[{name:'dictionaryKey',type:'string',cfg:{},value:''},
			       		{name:'orgCfg',type:'object',cfg:{},value:{},
			       		children:[{name:'listOrg',type:'boolean',cfg:{},value:true},
			       		{name:'listUser',type:'boolean',cfg:{},value:false}]},
			       		{name:'textField',type:'string',cfg:{},value:''},
			       		{name:'valueField',type:'string',cfg:{},value:''},
			       		{name:'isShowCount',type:'boolean',cfg:{},value:true},
			       		{name:'paramName',type:'string',cfg:{},value:''},
			       		{name:'isMultiLevel',type:'boolean',cfg:{},value:true},
			       		{name:'query',type:'designobject',cfg : {type:['query'],scope:'app'},value:{}},
			       		{name:'view',type:'designobject',cfg : {type:['view'],scope:'app'},value:{}}],
		description : '指定分组参数。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_name':{width:100, header:'节点名'},
		'f_caption':{width:100, header:'中文名'},
		'f_icon':{width:100},
		'f_group_type':{width:80},
		'f_group_arguments':{width:120},
		'f_properties':{width:200},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor', {
		title : '下级节点',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'treenode'}
	}]
});