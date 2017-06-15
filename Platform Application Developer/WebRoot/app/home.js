// 定义Direct函数源
Ext.direct.Manager.addProvider(Ext.apply(Asc.developer.REMOTING_API,{maxRetries:0}));
Ext.direct.Manager.on('exception', function(){
	
	//alert('exception');
});
// 设置动态装载参数
Ext.Loader.setConfig({
	enabled: true,
	paths : {
		'Ext.ux' : 'resources/ux',
		'Asc' : 'app'
	}
});
// 装载初始化类
Ext.require(['Asc.common.Message',
             'Asc.common.Context',
             'Asc.common.ActionManager',
             'Asc.framework.Framework',
             'Asc.extension.JspPanel',
             'Asc.extension.PropertyGrid',
             'Asc.extension.ObjectGridEditor',
             'Asc.extension.ObjectTreeGridEditor',
             'Asc.extension.TextEditor',
             'Asc.extension.DesignObjectEditor',
             'Asc.extension.EventsEditor',
             'Asc.extension.JsonEditor',
             'Asc.extension.CheckCombEditor',
             'Asc.extension.JsEditorPanel',
             'Asc.extension.JsEditor']);
var AscApp;
//创建客户端应用框架
Ext.onReady(function(){
	AscApp = Ext.create('Asc.framework.Framework');
});