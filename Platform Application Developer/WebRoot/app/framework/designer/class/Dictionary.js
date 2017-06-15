AscApp.ClassManager.registerClass({
	// 类型
	type : 'dictionary',
	// 名称
	caption : '静态数据字典',
	//前缀
	prefix : 'dic',
	// 是否设计对象
	isDesignObject : true,
	// 编辑器定义
	editors : [
		'designer/properties.editor', 
	{
		title : '字典数据列表',
		jspUrl : 'designer/objecttreegrid.editor',
		params : {type : 'dictionarydata'}
	}]
});