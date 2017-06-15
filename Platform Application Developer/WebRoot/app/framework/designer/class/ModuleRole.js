AscApp.ClassManager.registerClass({
	// 类型
	type : 'modulerole',
	// 名称
	caption : '模块角色',
	//前缀
	prefix : 'mr',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['modulerole'],
	// 属性
	properties : [],
	// 编辑器定义
	editors : ['designer/properties.editor']
});