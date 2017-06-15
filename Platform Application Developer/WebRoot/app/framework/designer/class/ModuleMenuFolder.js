AscApp.ClassManager.registerClass({
	// 类型
	type : 'ModuleMenuFolder',
	// 名称
	caption : '模块菜单列表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['menu'],
	// 编辑器定义
	editors : [{
		title : '模块菜单',
		jspUrl : 'designer/objecttreegrid.editor',
		params : {type : 'menu'}
	}]
});