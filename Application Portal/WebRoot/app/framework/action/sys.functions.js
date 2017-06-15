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
	window.open('/developer');
});
//打开管理工具
AscApp.ActionManager.addFunction('sys.manager', function(){
	window.open('/manager');
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