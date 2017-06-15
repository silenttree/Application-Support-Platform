AscApp.ClassManager.registerClass({
	// 类型
	type : 'relation',
	// 名称
	caption : '路由关系',
	//前缀
	prefix : 'rtl',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	//childrenType : ['route'],
	properties : [{
    	name:'f_type', 
    	text:'关系类型', 
    	editor:'combo',  
    	editorCfg:{
    		editable:false
    	},
    	store:[['1','流程启动者'],['2','目标节点历史办理人'],['3','同部门人员'],['4','直接部门领导'],['5','所有上级领导'],['6','上一节点办理人'],['7','所有已办理人员'],['8','直接下属'],['9','所有下属'],['10','分管领导'],['11','流程管理员'],['12','流程读者'],['0','自定义类型']],
    	defaultValue : 'none',
    	description:'路由关系类型。'
    },{
    	name:'f_source', 
    	text:'关系源', 
    	editor:'combo',  
    	editorCfg:{
    		editable:false
    	},
    	store:[['0','按当前办理人'],['1','按流程启动者'],['2','按上一主办人']],
    	defaultValue : 'none',
    	description:'关系计算相关类型，0：按流程启动者计算关系；1：按当前办理人计算关系；2：按上一办理人计算关系。'
    },{
    	name:'f_arguments', 
    	text:'解析参数', 
    	editor:'string', 
    	description:'定义路由关系解析参数，自定类型有效。'
    },{
    	name:'f_merge_type', 
    	text:'结果计算', 
    	editor:'combo', 
    	editorCfg:{
    		editable:false
    	},
    	store:[['0','交集'],['1','并集']],
    	defaultValue : 'none',
    	description:'各计算关系之间的结果合并方式，intersection：交集；outersection：并集。'
    }],
	// 编辑器定义
	editors : ['designer/properties.editor',{
		title : '路由关系',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'relation'}
	}],
	propertyColumns : {
		'f_key':{width:100, header:'Key'},
		'f_name':{width:150, header:'英文名'},
		'f_caption':{width:150, header:'中文名'},
		'f_source':{width:100, header:'关系源'},
		'f_type':{width:180, header:'关系类型'},
		'f_merge_type':{width:80, header:'结果计算'},
		'f_arguments':{width:150, header:'解析参数'}
	}
});