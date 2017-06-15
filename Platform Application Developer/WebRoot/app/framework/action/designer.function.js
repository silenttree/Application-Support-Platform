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