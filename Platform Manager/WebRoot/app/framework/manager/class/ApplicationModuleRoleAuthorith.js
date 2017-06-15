AscApp.ClassManager.registerClass({
	// 类型
	type : 'ApplicationModuleRoleAuthorith', 
	// 名称
	caption : '模块角色权限',
	// 编辑器定义
	editors : [{
		title : '模块角色权限',
		jspUrl : 'manager/modulerole/rolemapping.manager',
		iconCls : 'icon-manager-ApplicationModuleRoleAuthorith'
	}, {
		title : 'CDRU配模块角色',
		jspUrl : 'manager/modulerole/rolemapping.manager2',
		iconCls : 'icon-manager-ApplicationModuleRoleAuthorith'
	}]
});