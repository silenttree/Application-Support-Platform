AscApp.ClassManager.registerClass({
	// 类型
	type : 'shortcut',
	// 名称
	caption : '快捷方式',
	//前缀
	prefix : 'sct',
	// 是否设计对象
	isDesignObject : true,
	//属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '菜单图标样式名。'
	}, {
		name : 'f_big_icon',
		text : '桌面显示的大图标',
		editor : 'string',
		description : '桌面上的快捷方式显示的大图标。'
	}, {
		name : 'f_script',
		text : 'JS脚本',
		editor : 'jsarea',
		description : '该快捷方式点击时执行的客户端脚本。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor']
});