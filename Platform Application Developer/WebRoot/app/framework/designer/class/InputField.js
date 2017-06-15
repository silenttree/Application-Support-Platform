AscApp.ClassManager.registerClass({
	// 类型
	type : 'inputfield',
	// 名称
	caption : '输入域',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '输入字段排序号。'
	}, {
		name : 'f_i_dbfield',
		text : '数据字段',
		editor : 'designobject',
		editorCfg : {
			type : ['dbfield'],
			scope : 'parent.f_i_table'
		},
		renderer : function(v){
			if(v != null && typeof v == 'object'){
				return v.data;
			}
			return v;
		},
		description : '按钮图标样式名。'
	}, {
		name : 'f_inputtype',
		text : '输入类型',
		editor : 'combosimple',
		editorCfg : {
			editable : true
		},
		store : ['none','display','boolean','date', 'time', 
		         'datetime', 'number','text','textarea','combo',
		         'combolist','combotree','check','radio',
		         'organization','file','image','html', 'radiocombo'],
		defaultValue : 'none',
		description : '指定输入域类型。'
	}, {
		name : 'f_defaultvalue',
		text : '默认值',
		editor : 'combosimple',
		editorCfg : {
			editable : true
		},
		store : ['|userid|','|userlogin|','|username|','|companynane|', '|companyid|', '|deptid|', '|deptname|','|date|','|time|'],
		defaultValue : 'none',
		//editor : 'string',
		description : '设置字段默认值。'
	}, {
		name : 'f_allownull',
		text : '允许为空',
		editor : 'boolean',
		defaultValue : true,
		description : '字段是否允许输入为空。'
	}, {
		name : 'f_maxlength',
		text : '字段长度',
		editor : 'number',
		defaultValue : 0,
		description : '设置字段允许输入最大长度。'
	}, {
		name : 'f_validator',
		text : '合法性验证',
		editor : 'text',
		description : '设置字段合法性验证函数，提交时自动调用。'
	},{
		name : 'f_properties',
		text : '扩展属性',
		editor : 'json',
		initialValue :[{name:'displayCfg',type:'object',cfg:{},value:{}},
		       		{name:'editCfg',type:'object',cfg:{},value:{}},
		       		{name:'store',type:'array',cfg:{},value:[]},
		       		{name:'directFn',type:'string',cfg:{},value:''},
		       		{name:'dictionary',type:'string',cfg:{},value:''},
		       		{name:'sdictionary',type:'string',cfg:{},value:''},
		       		{name:'jspUrl',type:'string',cfg:{},value:''},
		       		{name:'renderer',type:'boolean',cfg:{},value:true},
		       		{name:'valueField',type:'string',cfg:{},value:''},
		       		{name:'isDefault',type:'boolean',cfg:{},value:true}],
		description : '输入域的扩展属性，设置该输入域的一些基本默认属性。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:100},
		'f_name':{width:100, header:'英文名'},
		'f_caption':{width:100, header:'中文名'},
		'f_i_dbfield':{width:120},
		'f_inputtype':{width:100},
		'f_allownull':{width:80},
		'f_maxlength':{width:80},
		'f_defaultvalue':{width:120},
		'f_validator':{width:150},
		'f_properties':{width:120},
		'f_description':{flex:1}
	}
});