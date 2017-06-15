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
			name : 'closeEditor',
			handler : function(){
				var designer = AscApp.getAscDesigner();
				var title = AscApp.Context.activeContainer.title;
				if(title == '首页'){
					return false;
				}else{
					AscApp.getAscDesigner().closeEditor();
				}
			}
		},'-','-',{
			text : '关闭所有',
			iconCls : 'icon-sys-closeall',
			group : 'designer',
			name : 'closeAllEditor',
			handler : function(){
				var items = AscApp.getAscDesigner().getAscWorkspace().items.items;
				for(var i = items.length-1;i>0;i--){
					AscApp.getAscDesigner().closeEditor();
				}
			}
		}]
	})
});
/*AscApp.ActionManager.addAction('extendAction', {
	text : '扩展功能',
	iconCls : 'icon-sys-extend',
	ignoreParentClicks : true,
	menu : new Ext.menu.Menu({
		id:'mnuExtendAction',
		ignoreParentClicks : true
	})
});*/
/*AscApp.ActionManager.addAction('templateAction', {
	text : '模板',
	iconCls : 'icon-designer-template',
	ignoreParentClicks : true,
	menu : [{
		text : '创建'
	},{
		text : '引用'
	}]
});*/
AscApp.ActionManager.addAction('openEditor', {
	text : '打开',
	iconCls : 'icon-sys-open',
	group : 'designer',
	name : 'openEditor'
});
AscApp.ActionManager.addAction('addObject', {
	text : '创建..',
	iconCls : 'icon-sys-add',
	ignoreParentClicks : true,
	menu : new Ext.menu.Menu({
		id:'mnuAddDesignObject',
		ignoreParentClicks : true
	})
});
AscApp.ActionManager.addAction('renameObject', {
	text : '修改键值',
	iconCls : 'icon-sys-rename',
	group : 'sys',
	name : 'renameObject'
});
AscApp.ActionManager.addAction('delObject', {
	text : '删除',
	iconCls : 'icon-sys-delete',
	group : 'designer',
	name : 'delObject'
});
AscApp.ActionManager.addAction('copyObject', {
	text : '复制',
	iconCls : 'icon-sys-copy',
	group : 'sys',
	name : 'copyObject'
});
AscApp.ActionManager.addAction('pasteObject', {
	text : '粘贴',
	iconCls : 'icon-sys-paste',
	group : 'sys',
	name : 'copyObject'
});
AscApp.ActionManager.addAction('reloadNavigatorSelectedNode', {
	text : '刷新',
	iconCls : 'icon-sys-refresh',
	group : 'designer',
	name : 'reloadNavigatorSelectedNode'
});