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
	window.open('/developer/dependencies/ext-4.2.1/docs/index.html');
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