
//=================================================================
//	�ļ�����connection.action.js
//=================================================================
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
//=================================================================
//	�ļ�����connection.function.js
//=================================================================
// 执行创建对象操作
AscApp.ActionManager.addFunction('connection.registerAppConn', function(scope){
	var obj = AscApp.Context.getActivateObject();
	// 获得默认值
	var values = AscApp.ClassManager.getPropertyDefaultValues('appconn');
	Ext.Msg.prompt('注册应用连接', '请输入连接标识', function(buttonId, text){
		if(buttonId == 'ok'){
			// 创建对象
			DeveloperAppDirect.saveAppConnection(text, values, function(result, e){
				if(result && result.success){
					var nodeId = AscApp.ClassManager.getNodeId(text, 'appconn', '');
					// 刷新父节点
					AscApp.getAscDesigner().reloadNode(undefined, function(records, operation, success){
						// 装载完毕后选中、装载节点
						AscApp.getAscDesigner().selectNavigatorNode(nodeId);
						AscApp.getAscDesigner().openEditor();
					});
				}else{
					if(result && result.message){
						Asc.common.Message.showError(result.message);
					}else{
						Asc.common.Message.showError('创建应用连接【' + text + '】失败！');
					}
				}
			});
		}
	});
});
// 执行删除设计对象
AscApp.ActionManager.addFunction('connection.unregisterAppConn', function(){
	var obj = AscApp.Context.getActivateObject();
	if(!obj){
		Asc.common.Message.showError('未指定删除连接');
	}else{
		Ext.MessageBox.confirm('危险操作提示', '删除应用连接[' + obj.appId + ']，该操作不可恢复，您确定吗？', function(btn){
			if(btn == 'yes'){
				DeveloperAppDirect.deleteAppConnection(obj.appId, function(result, e){
					if(result && result.success){
						// 删除后刷新界面
						var nodeId = AscApp.ClassManager.getNodeId(obj.appId, 'appconn', '');
						AscApp.getAscDesigner().afterDeleteNode(nodeId);
					}else{
						if(result && result.message){
							Asc.common.Message.showError(result.message);
						}else{
							Asc.common.Message.showError('删除应用连接【' + obj.appId + '】失败！');
						}
					}
				});
			}
		})
	}
});
//=================================================================
//	�ļ�����designer.action.js
//=================================================================
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
//=================================================================
//	�ļ�����designer.function.js
//=================================================================
// 生成文件调用
AscApp.ActionManager.addFunction('sys.buildFiles', function(type, title){
	DeveloperAppDirect.buildFiles(type, function(result, e){
		if(result && result.success){
			Asc.common.Message.showInfo(title + '文件生成完毕！');
		}else{
			if(result && result.message){
				Asc.common.Message.showError(result.message);
			}else{
				Asc.common.Message.showError(title + '文件生成失败！');
			}
		}
	});
});
// 添加设计对象菜单
AscApp.ActionManager.addFunction('designer.addObject', function(){
	var obj = AscApp.Context.getActivateObject();
	AscApp.ActionManager.runFunction('designer.createDesignObject', this, [obj.appId, obj.key, this.type, function(appId, type, objectId){
		// 装载完毕后选中、装载节点
		var nodeId = AscApp.ClassManager.getNodeId(appId, type, objectId);
		AscApp.getAscDesigner().selectNavigatorNode(nodeId);
		AscApp.getAscDesigner().openEditor();
	}]);
});
// 显示创建对象窗口
AscApp.ActionManager.addFunction('designer.createDesignObject', function(appId, parentKey, type, fn, scope){
	AscApp.getAscDesigner().showCreateDesignObjectWindow(appId, parentKey, type, fn, scope);
});
// 执行创建对象操作
AscApp.ActionManager.addFunction('designer.doCreateDesignObject', function(appId, parentKey, type, key, fn, scope){
	// 获得默认值
	var values = AscApp.ClassManager.getPropertyDefaultValues(type);
	// 创建对象
	DeveloperAppDirect.createObject(appId, parentKey, type, key, values, function(result, e){
		if(result && result.success){
			AscApp.getAscDesigner().closeCreateDesignObjectWindow();
			var objectId = result.objectId;
			// 刷新父节点
			AscApp.getAscDesigner().reloadNode(undefined, function(records, operation, success){
				if(fn){
					fn.call(scope || this, appId, type, objectId);
				}
			});
		}else{
			if(result && result.message){
				Asc.common.Message.showError(result.message);
			}else{
				Asc.common.Message.showError('创建对象【' + type + '-' + parentKey + '.' + key + '】失败！');
			}
		}
	});
});
// 删除对象操作(右键菜单)
AscApp.ActionManager.addFunction('designer.delObject', function(){
	var obj = AscApp.Context.getActivateObject();
	AscApp.ActionManager.runFunction('designer.doDeleteObject', this, [obj, function(obj){
		// 删除后刷新界面
		var nodeId = AscApp.ClassManager.getNodeId(obj.appId, obj.type, obj.key);
		AscApp.getAscDesigner().afterDeleteNode(nodeId);
	}, this]);
});
// 执行删除设计对象
AscApp.ActionManager.addFunction('designer.doDeleteObject', function(obj, fn, scope){
	if(!obj){
		Asc.common.Message.showError('未指定删除对象');
	}else{
		Ext.MessageBox.confirm('危险操作提示', '删除对象[' + obj.key + ']，该操作不可恢复，您确定吗？', function(btn){
			if(btn == 'yes'){
				DeveloperAppDirect.deleteObject(obj.appId, obj.key, function(result, e){
					if(result && result.success){
						fn.call(scope || this, obj);
					}else{
						if(result && result.message){
							Asc.common.Message.showError(result.message);
						}else{
							Asc.common.Message.showError('删除对象【' + obj.key + '】失败！');
						}
					}
				});
			}
		})
	}
});
// 执行保存设计对象
AscApp.ActionManager.addFunction('designer.doSaveObjects', function(appId, datas, fn, scope){
	DeveloperAppDirect.saveObjects(appId, datas, function(result, e){
		if(result && result.success){
			if(result.sObjects && result.sObjects.length > 0){
				var str = '';
				for(var n in result.sObjects){
					str = str + '<br>[' + result.sObjects[n].id + ']';
				}
				Asc.common.Message.showInfo('共[' + result.sObjects.length + ']个对象保存成功：' + str);
			}
			if(result.fObjects && result.fObjects.length > 0){
				var str = '';
				for(var n in result.fObjects){
					str = str + '<br>[' + result.fObjects[n].id + ']';
				}
				Asc.common.Message.showError('共[' + result.fObjects.length + ']个对象保存失败：' + str);
			}
			if(Ext.isDefined(fn)){
				fn.call(scope || this, appId, result.sObjects, result.fObjects);
			}
		}else{
			if(result && result.message){
				Asc.common.Message.showError(result.message);
			}else{
				Asc.common.Message.showError('保存对象操作失败！');
			}
		}
	});
});
// 生成类文件
AscApp.ActionManager.addFunction('sys.buildClassesJs', function(){
	AscApp.ActionManager.runFunction('sys.buildFiles', this, ['class', '类定义']);
});
// 生成功能函数文件
AscApp.ActionManager.addFunction('sys.buildActionsJs', function(){
	AscApp.ActionManager.runFunction('sys.buildFiles', this, ['action', '功能函数定义']);
});
//添加设计对象菜单
AscApp.ActionManager.addFunction('designer.reloadNavigatorSelectedNode', function(){
	AscApp.getAscDesigner().reloadNode();
});
// 打开对象编辑器
AscApp.ActionManager.addFunction('designer.openEditor', function(){
	AscApp.getAscDesigner().openEditor();
});
// 关闭对象编辑器
AscApp.ActionManager.addFunction('designer.closeEditor', function(){
	AscApp.getAscDesigner().closeEditor();
});
// 对象编辑器应用操作
AscApp.ActionManager.addFunction('designer.applyEditor', function(){
	AscApp.getAscDesigner().applyEditor();
});
// 对象编辑器刷新操作
AscApp.ActionManager.addFunction('designer.reloadEditor', function(){
	AscApp.getAscDesigner().reloadEditor();
});
//=================================================================
//	�ļ�����sys.action.js
//=================================================================

//=================================================================
//	�ļ�����sys.function.js
//=================================================================
//退出应用系统
AscApp.ActionManager.addFunction('sys.logout', function(){
	Ext.MessageBox.confirm('退出警告','退出门户，该操作将放弃所有未保存数据，您确定吗？',function(btn){
		if(btn == 'yes'){
			window.location = "logout.do";
		}
	});
});
//打开开发工具
AscApp.ActionManager.addFunction('sys.developer', function(){
	window.open('/developer');
});
//打开管理工具
AscApp.ActionManager.addFunction('sys.manager', function(){
	window.open('/manager');
});
//打开Ext帮助
AscApp.ActionManager.addFunction('sys.extjsDoc', function(){
	window.open('http://extjs-doc-cn.github.io/ext4api/');
});
// 生成图标文件
AscApp.ActionManager.addFunction('sys.buildIcons', function(){
	CommonAppDirect.buildIconCssFile(function(result, e){
		if(result && result.success){
			Asc.common.Message.showInfo('图标样式文件生成完毕！');
		}else{
			Asc.common.Message.showError('图标样式文件生成失败！');
		}
	});
});
// 关于产品
AscApp.ActionManager.addFunction('sys.showAbout', function(){
	var win = Ext.create('Ext.window.Window', {
		title : '关于产品',
		iconCls : 'icon-sys-logo',
		width : 380,
		height : 260,
		modal : true,
		layout : 'fit',
		resizable: false,
		maximizable : false,
		minimizable : false,
		items : {
			xtype : 'AscJspPanel',
			jspUrl : 'framework/system/about'
		}
	});
	win.show();
});