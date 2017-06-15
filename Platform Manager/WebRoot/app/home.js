// 定义Direct函数源
Ext.direct.Manager.addProvider(Ext.apply(Asc.manager.REMOTING_API,{maxRetries:0}));
// 处理Direct异常
Ext.direct.Manager.on('exception', function(){
	
	//alert('exception');
})
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
             'Asc.extension.TextEditor',
             'Asc.extension.JsonEditor',
             'Asc.extension.DesignObjectEditor',
             'Asc.extension.PagingToolbar',
             'Asc.extension.TreeCombox',
             'Asc.extension.GridCombox']);
var AscApp;
//创建客户端应用框架
Ext.onReady(function(){
	AscApp = Ext.create('Asc.framework.Framework');
});
document.onkeydown = function (e) {
    var code;   
    if (!e){ var e = window.event;}   
    if (e.keyCode){ code = e.keyCode;}
    else if (e.which){ code = e.which;}
    //BackSpace 8;
    if (
      (event.keyCode == 8)
      && ((event.srcElement.type != "text" && event.srcElement.type != "textarea" && event.srcElement.type != "password")
        ||  event.srcElement.readOnly == true
        )
     
     ) {
        
     event.keyCode = 0;        
     event.returnValue = false;    
    }
    return true;
   };