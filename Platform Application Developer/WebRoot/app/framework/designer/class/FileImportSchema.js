AscApp.ClassManager.registerClass({
	// 类型
	type : 'fileimportschema',
	// 名称
	caption : '文件导入对象',
	//前缀
	prefix : 'fis',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_table_name',
		text : '表名',
		editor : 'string',
		description : '对应到数据库的表名。'
	}, {
		name : 'f_begin_row_index',
		text : '开始行数',
		editor : 'number',
		defaultValue : 1,
		description : '导入的文件，从第几行开始导入。'
	},{
		name : 'f_default_operations',
		text : '默认操作',
		editor : 'json',
		defaultValue : {"add":true,"update":true,"delete":false},
		/*initialValue :[{name:'add',type:'boolean',cfg:{},value:true},
		               {name:'update',type:'boolean',cfg:{},value:true},
		               {name:'delete',type:'boolean',cfg:{},value:false}],*/
		description : '设置默认操作，add增加，update更新，delete删除。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor',{
		title : '删除参数',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'fileimportdelparam'}
	},{
		title : '字段匹配列表',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'fileimportfield'}
	}]
});