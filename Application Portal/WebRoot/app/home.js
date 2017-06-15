
// 定义全局应用变量
var AscApp;
// 定义Direct函数源
Ext.direct.Manager.addProvider(Ext.apply(Asc.portal.REMOTING_API, {
	enableBuffer : false,
	maxRetries : 0
}));
// 设置动态装载参数
Ext.Loader.setConfig({
	enabled: true,
	paths : {
		'Ext.ux' : 'resources/ux',
		'Asc' : 'app'
	}
});
/*
// 装载初始化类
Ext.require(['Asc.framework.Framework',
			'Asc.extension.JspPanel',
			'Asc.extension.UploadButton']);
*/
//创建客户端应用框架
Ext.onReady(function(){
	AscApp = Ext.create('Asc.framework.Framework');
});