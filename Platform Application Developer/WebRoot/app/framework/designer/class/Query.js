AscApp.ClassManager.registerClass({
	// 类型
	type : 'query',
	// 名称
	caption : '数据检索',
	//前缀
	prefix : 'q',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['datagroup'],
	// 属性定义
	properties : [{
		name : 'f_i_ds',
		text : '数据源',
		editor : 'designobject',
		editorCfg : {
			type : 'datasource'
		},
		description : '指定查询对应的数据源对象。'
	}, {
		name : 'f_from',
		text : 'from语句',
		editor : 'text',
		description : '查询SQL的from子句。'
	}, {
		name : 'f_where',
		text : 'where语句',
		editor : 'text',
		description : '查询SQL的where子句。'
	}, {
		name : 'f_groupby',
		text : 'groupby语句',
		editor : 'text',
		description : '查询SQL的groupby子句。'
	}, {
		name : 'f_orderby',
		text : 'orderby语句',
		editor : 'text',
		description : '查询SQL的orderby子句。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor', 
	{
		title : '查询参数',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'queryparameter'}
	}]
});