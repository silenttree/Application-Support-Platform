AscApp.ClassManager.registerClass({
	// 类型
	type : 'ApplicationWorkflowRole', 
	// 名称
	caption : '流程角色权限',
	// 编辑器定义
	editors : [{
		title : '流程角色映射',
		jspUrl : 'manager/workflowrole/rolemapping.manager',
		iconCls : 'icon-manager-applicationmodulerolemapping'
	}]
});