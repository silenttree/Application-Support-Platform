// 打开桌面首页
AscApp.ActionManager.addFunction('sys.openPortal', function(){
	var desktop = AscApp.getController('AscDesktop');
	desktop.getAscWorkspace().loadPortal();
});
//装载桌面配置
AscApp.ActionManager.addFunction('sys.loadPreferences', function(){
	PortalAppDirect.loadPreferences(key, function(result, e){
		if(result && result.success){
			AscApp.setPreferences(result.preferences);
			Asc.common.Message.showInfo('装载桌面完毕！');
		}else{
			Asc.common.Message.showError('装载保存失败！');
		}
	});
});
// 添加桌面栏目（测试）
AscApp.ActionManager.addFunction('sys.addPortlet', function(){
	var desktop = AscApp.getController('AscDesktop');
	desktop.getAscPortal().addPortlet({
		key : 'test'
	});
});
// 删除桌面按钮（测试）
AscApp.ActionManager.addFunction('sys.removeShortcut', function(){
	AscApp.removeShortcut('Desktop.test2');
});
// 保存桌面配置
AscApp.ActionManager.addFunction('sys.savePreferences', function(){
	PortalAppDirect.savePreferences(this.modelKey || 'self', AscApp.getPreferences(), function(result, e){
		if(result && result.success){
			Asc.common.Message.showInfo('桌面保存完毕！');
		}else{
			Asc.common.Message.showError('桌面保存失败！');
		}
	});
});