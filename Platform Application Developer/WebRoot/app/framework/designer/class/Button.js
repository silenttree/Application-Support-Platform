AscApp.ClassManager.registerClass({
	// 类型
	type : 'button',
	// 名称
	caption : '按钮',
	//前缀
	prefix : 'btn',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '视图按钮排序号。'
	}, {
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '按钮图标样式名。'
	}, {
		name : 'f_xtype',
		text : '按钮类名',
		editor : 'combo',
		editorCfg : {
			editable : true
		},
		store : ['button','text','splitbutton','uploadbutton', 'separator', 'fill'],
		defaultValue : 'button',
		description : '指定按钮对象xtype，用于生成按钮界面对象的类引用名。'
	}, {
		name : 'f_script',
		text : 'JS脚本',
		editor : 'jsarea',
		description : '该按钮点击时执行的客户端脚本。'
	}, {
		name : 'f_properties',
		text : '扩展属性',
		editor : 'json',
		initialValue : [{name:'isDefault',type:'boolean',cfg:{},value:true}],
		description : '按钮的扩展属性，设置该按钮的一些基本默认属性。'
	}, {
		name : 'f_events',
		text : '事件脚本',
		editor : 'event',
		description : '按钮事件脚本。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:100},
		'f_name':{width:100, header:'英文名'},
		'f_caption':{width:100, header:'中文名'},
		'f_icon':{width:120},
		'f_xtype':{width:80},
		'f_script':{width:200},
		'f_events':{width:120},
		'f_properties':{width:120},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor']
});