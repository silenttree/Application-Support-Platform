AscApp.ClassManager.registerClass({
	// 类型
	type : 'menu',
	// 名称
	caption : '模块菜单',
	//前缀
	prefix : 'menu',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['menu'],
	// 属性
	properties : [{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '菜单排序号。'
	},{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '菜单图标样式名。'
	}, {
		name : 'f_script',
		text : 'JS脚本',
		editor : 'jsarea',
		description : '该按钮点击时执行的客户端脚本。'
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
	}, {
		name : 'f_action_type',
		text : '动作类型',
		editor : 'combo',
		store : [['None', '无动作'],['Script', '执行脚本'],['PageObject', '打开页面'],['Hyperlink', '超链接'],['ModuleMenu', '打开菜单']],
		defaultValue : 'None',
		description : '指定菜单动作类型'
	}, {
		name : 'f_action_arguments',
		text : '动作目标',
		editor : 'json',
		initialValue : [{name:'url',type:'string',cfg:{},value:''},
		                {name:'page',type:'designobject',cfg : {
		        			type : ['view', 'treepage', 'formtablelayout', 'formpositionlayout', 'urlpage', 'layout'],
		        			scope : 'module'},value:{}},
		                {name:'menu',type:'designobject',cfg : {type:['menu'],scope:'module'},value:{}},
		                {name:'params',type:'object',cfg:{},value:{}}],
		description : '配合动作类型设定目标内容：<br>NONE ： 无效<br>超链接 ： 目标设定为菜单打开的url路径<br>打开页面： 目标为页面标识<br>执行脚本： 目标为可运行的js脚本<br>打开菜单： 目标为菜单标识'
	}, {
		name : 'f_properties',
		text : '扩展属性',
		editor : 'json',
		initialValue :[{name:'isDefault',type:'boolean',cfg:{},value:true}],
		description : '模块菜单的扩展属性，设置该菜单的一些基本默认属性。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_name':{width:100, header:'菜单名'},
		'f_caption':{width:100, header:'中文名'},
		'f_icon':{width:100},
		'f_script':{width:150},
		'f_group_type':{width:80},
		'f_group_arguments':{width:120},
		'f_action_type':{width:80},
		'f_action_arguments':{width:120},
		//'f_events':{width:120},
		'f_properties':{width:200},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor', {
		title : '下级菜单',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'menu'}
	}]
});