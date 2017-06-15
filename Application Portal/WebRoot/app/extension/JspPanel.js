Ext.define('Asc.extension.JspPanel', {
	// 指定基类
	extend : 'Ext.panel.Panel',
	// 设置别名
	alias : 'widget.AscJspPanel',
	
	layout : 'fit',
    
	//autoScroll : false,
	
	border : false,
	// 应用标识
	appId : undefined,
	// 使用代理
	useProxy : true,
	// 页面请求相对地址
	jspUrl : undefined,
	// 页面请求相对地址
	url : undefined,
	// 页面参数
	params : undefined,
	// 重载构造函数
	constructor : function(config){
		var me = this;
		// 获得ID
		me.getId();
		var useProxy = this.useProxy;
		if(Ext.isDefined(config.useProxy)){
			useProxy = config.useProxy;
		}
		var url = useProxy ? AscApp.jspPageProxy : (config.jspUrl || config.url);
		// 获得应用路径
		if(config.appId){
			var appManager = AscApp.getController('AscAppManager');
			// 根据AppId计算url
			url = appManager.getAppUrl(config.appId) + '/' + url;
		}
		// 构造请求参数
		var params = config.params || {};
		if(useProxy){
			Ext.applyIf(params, {
				url : config.jspUrl
			});
		}
		// 设置默认参数panelid
		Ext.applyIf(params, {
			panelid : this.getId()
		});
		// 构造自动装载对象
		var autoLoad = config.autoLoad || {};
		Ext.applyIf(autoLoad, {
			url : url,
			loadMask : true,
			params : params,
			scripts : true
		});
		config.autoLoad = autoLoad;
        me.callParent([config]);
	}
});