//创建按钮
AscApp.ActionManager.addAction('registerAppConn', {
	text : '注册应用连接',
	iconCls : 'icon-designer-registerappconn',
	group : 'connection',
	name : 'registerAppConn'
});
AscApp.ActionManager.addAction('unregisterAppConn', {
	text : '删除应用连接',
	iconCls : 'icon-designer-unregisterappconn',
	group : 'connection',
	name : 'unregisterAppConn'
});
AscApp.ActionManager.addAction('updateDesigns', {
	text : '更新设计',
	iconCls : 'icon-designer-update',
	group : 'connection',
	name : 'updateDesigns'
});
AscApp.ActionManager.addAction('submitDesigns', {
	text : '提交设计',
	iconCls : 'icon-designer-submit',
	group : 'connection',
	name : 'submitDesigns'
});
AscApp.ActionManager.addAction('connection', {
	text : '应用连接',
	iconCls : 'icon-designer-connect',
	ignoreParentClicks : true,
	menu : {
		items : [
			AscApp.ActionManager.getAction('registerAppConn'),
			AscApp.ActionManager.getAction('unregisterAppConn'), '-',
			AscApp.ActionManager.getAction('updateDesigns'),
			AscApp.ActionManager.getAction('submitDesigns')
		]
	}
});