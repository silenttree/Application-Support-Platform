AscApp.ClassManager.registerClass({
	// 类型
	type : 'table',
	// 名称
	caption : '数据表',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_i_ds',
		text : '数据源',
		editor : 'designobject',
		editorCfg : {
			type : 'datasource',
			scope : 'app'
		},
		description : '指定数据表对应的数据源对象。'
	}],
	// 编辑器定义
	editors : [
		'designer/properties.editor', 
	{
		title : '字段列表',
		jspUrl : 'designer/dbfield.editor',
		params : {type : 'dbfield'}
	}]
});