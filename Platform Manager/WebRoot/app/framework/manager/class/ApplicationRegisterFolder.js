AscApp.ClassManager.registerClass({
	// 类型
	type : 'ApplicationsFolder',
	// 名称
	caption : '应用注册',
	// 编辑器定义
	editors : [{
		title : '应用配置',
		jspUrl : 'manager/appregister/application.manager',
		iconCls : 'icon-manager-application'
	},{
		title : '集群节点',
		jspUrl : 'manager/appregister/applicationinstance.manager',
		iconCls : 'icon-manager-applicationinstance'
	}]
});