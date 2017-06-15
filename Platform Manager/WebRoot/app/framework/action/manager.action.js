//创建按钮
AscApp.ActionManager.addAction('editorWindow', {
	text : '窗口',
	iconCls : 'icon-designer-window',
	ignoreParentClicks : true,
	alwaysEnable : true,
	menu : new Ext.menu.Menu({
		id:'mnuEditorWindow',
		ignoreParentClicks : true,
		items : [{
			text : '关闭当前',
			iconCls : 'icon-sys-close',
			group : 'designer',
			name : 'closeEditor'
		},'-','-',{
			text : '关闭所有',
			iconCls : 'icon-sys-closeall',
			group : 'designer',
			name : 'closeAllEditor'
		}]
	})
});