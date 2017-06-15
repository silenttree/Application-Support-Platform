Ext.define('Asc.framework.page.view.TabLayout', {
	// 指定基类
	extend : 'Ext.tab.Panel',
	// 设置别名
	alias : 'widget.AscPageTabLayout',
	
	border : false,
	// 应用标识
	appKey : undefined,
	// 使用代理
	moduleId : undefined,
	// 视图key
	pageId : undefined,
	// 页面参数
	initParams : undefined,
	
	deferredRender : false,
	// 重载构造函数
	constructor : function(appKey, moduleId, pageData, params, context, cfg){
		var me = this;
		me.appKey = appKey;
		me.moduleId = moduleId;
		me.pageId = pageData.id;
		var itemCmps = [];
		for(var i=0;i<pageData.items.length;i++){
			var item = pageData.items[i];
			var pageManager = AscApp.getController('AscPageManager');
			var itemPage;
			if(Ext.isDefined(item.pageId)){
				itemPage = pageManager.getPageCmp(appKey, moduleId, item.pageId, params, {});
			}else{
				itemPage = Ext.create('Ext.panel.Panel');
			}
			Ext.apply(itemPage, Ext.apply({
				title : item.title,
				iconCls : item.iconCls,
				itemId : item.id}, 
			item.layoutConfig));
			itemCmps.push(itemPage);
			console.log(itemPage);
		}
		// 页面配置
		var config = {
			title : pageData.title,
			iconCls : pageData.iconCls,
			items : itemCmps
		};
		config = Ext.apply(config, pageData.cfg);
		
        me.callParent([config]);
	},
	// 执行刷新
	doRefresh : function(params){
		for(var item in this.items){
			if(Ext.isFunction(item.doRefresh)){
				item.doRefresh(params);
			}
		}
	}
});