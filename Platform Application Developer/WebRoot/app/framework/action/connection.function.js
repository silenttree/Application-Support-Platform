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