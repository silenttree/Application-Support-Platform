
//=================================================================
//	�ļ�����cfs.function.js
//=================================================================

// 输入支付密码
AscApp.ActionManager.addFunction('cfs.inputPayPassword', function(showAgain, callback, scope){
	Ext.require('Ext.ux.IFrame', function(){
		var win = Ext.create('Ext.Window', {
			title : '请输入支付密码',
			width : 360,
			height : 180,
			resizable : false,
			modal: true,
			layout : 'fit',
			items : {
				xtype : 'uxiframe', 
				padding : 5, 
				src : AscApp.jspPageProxy + "?url=cfs/icardpay.ras" + (showAgain ? '.again' : '')
			},
			buttons : [{
				text : '确定',
				handler : function(){
					var iframe = win.items.get(0);
					var domIframe = iframe.getFrame();
					var doc = domIframe.contentWindow;
					var result = doc.getRsaPassword();
					if(result.successed){
						callback.call(scope || win, result.message);
						win.close();
					}else{
						Asc.common.Message.showInfo(result.message);
					}
				}
			}, {
				text : '取消',
				handler : function(){
					win.close();
				}
			}]
		})
		win.show();
	}); 
});

Ext.form.field.ComboBox.prototype.findRecordByValue = function(value){
	return this.findRecord(this.valueField + '', value + '');
}
//打开监控平台工具
AscApp.ActionManager.addFunction('sys.monitor', function(){
	window.open('http://10.3.4.15/portal/monitor.jsp');
});
//=================================================================
//	�ļ�����desktop.actions.js
//=================================================================

//=================================================================
//	�ļ�����desktop.functions.js
//=================================================================
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
//=================================================================
//	�ļ�����sys.actions.js
//=================================================================

//=================================================================
//	�ļ�����sys.functions.js
//=================================================================
// 打开个性化配置中心
AscApp.ActionManager.addFunction('sys.showPreferences', function(){
	var win = Ext.create('Ext.window.Window', {
		title : '个性化配置中心',
		iconCls : 'icon-sys-preferences',
		width : 680,
		height : 500,
		modal : true,
		border : false,
		layout : 'fit',
		resizable: false,
		maximizable : false,
		minimizable : false,
		items : {
			xtype : 'AscPreferences'
		}
	});
	win.show();
});

//修改密码
AscApp.ActionManager.addFunction('sys.changePassword', function(){ 
	Ext.require('Asc.framework.desktop.view.PasswordChanger'); 
	Ext.create('Asc.framework.desktop.view.PasswordChanger');
});

//帮助主题
AscApp.ActionManager.addFunction('sys.helpDoc', function(){ 
	Ext.require('Asc.framework.desktop.view.HelpDoc'); 
	Ext.create('Asc.framework.desktop.view.HelpDoc', {
		docPath : '/帮助主题/门户桌面/常用模块'
	});
});

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
	window.open('http://localhost:8080/developer');
});
//打开管理工具
AscApp.ActionManager.addFunction('sys.manager', function(){
	window.open('http://10.3.4.15/manager');
});
//生成图标文件
AscApp.ActionManager.addFunction('sys.buildIcons', function(){
	PortalAppDirect.buildIconCssFile(function(result, e){
		if(result && result.success){
			Asc.common.Message.showInfo('图标样式文件生成完毕！');
		}else{
			Asc.common.Message.showError('图标样式文件生成失败！');
		}
	});
});
//打开文件导入操作窗口
AscApp.ActionManager.addFunction('sys.importFile', function(appKey, fileImportLogId){ 
	Ext.require('Asc.extension.editor.ImportWindow'); 
	Ext.create('Asc.extension.editor.ImportWindow', {"appKey" : appKey, "fileImportLogId" : fileImportLogId}).show();
});
//关于产品
AscApp.ActionManager.addFunction('sys.showAbout', function(){
	var win = Ext.create('Ext.window.Window', {
		title : '关于ASC平台',
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
			layout : 'fit',
			jspUrl : 'framework/system/about'
		}
	});
	win.show();
});
//生成文件调用
AscApp.ActionManager.addFunction('sys.buildFiles', function(type, title){
	PortalAppDirect.buildFiles(type, function(result, e){
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
// 生成功能函数文件
AscApp.ActionManager.addFunction('sys.buildActionsJs', function(){
	AscApp.ActionManager.runFunction('sys.buildFiles', this, ['action', '功能函数定义']);
});
//=================================================================
//	�ļ�����test.function.js
//=================================================================

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