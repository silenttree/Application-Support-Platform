AscApp.ClassManager.registerClass({
	// 类型
	type : 'datagroup',
	// 名称
	caption : '数据分组',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['datagroup'],
	// 编辑器定义
	editors : ['designer/properties.editor']
});