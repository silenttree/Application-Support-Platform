
//打开管理工具
AscApp.ActionManager.addFunction('test.openModule', function(){
	var desktop = AscApp.getController('AscDesktop');
	desktop.openModule('Demo', 'mTest');
});

//清空缓存
AscApp.ActionManager.addFunction('test.clearCache', function(){
	var appManager = AscApp.getController('AscAppManager');
	var appStore = appManager.getStore('Applications');
	var msg = '';
	var sppCnt = appStore.getCount() + 1;
	var clearedCnt = 0;
	PortalAppDirect.clearCache('', function(result, e){
		clearedCnt += 1;
		if(result && result.success){
			msg += '门户应用缓存清空完毕！<br/>';
		}else{
			msg += '<font color="#FF0000">门户应用缓存清除失败！</font><br/>';
		}
		if(clearedCnt === sppCnt){
			Asc.common.Message.showInfo(msg);
		}
	});
	appStore.each(function(rec){
		if(rec.get('f_is_init')){
			var clearCacheFn = appManager.getEngineDirectFn(rec.get('f_key'), 'clearCache');
			clearCacheFn('', function(result, e){
				clearedCnt += 1;
				if(result && result.success){
					msg = rec.get('f_caption') + '应用缓存清空完毕！<br/>';
				}else{
					msg = '<font color="#FF0000">' + rec.get('f_caption') + '应用缓存清除失败！</font><br/>';
				}
				//if(clearedCnt === sppCnt){
					Asc.common.Message.showInfo(msg);
				//}
			});
		}
	});
});