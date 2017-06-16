Ext.define('Asc.framework.page.view.Layout', {
	// 指定基类
	extend : 'Ext.panel.Panel',
	// 设置别名
	alias : 'widget.AscPageLayout',
    
	autoScroll : false,
	
	border : false,
	// 应用标识
	appKey : undefined,
	// 使用代理
	moduleId : undefined,
	// 视图key
	pageId : undefined,
	// 页面参数
	initParams : undefined,
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
				itemId : item.key, 
				title : item.title, 
				border : true,
				header : false}, 
			item.layoutConfig));
			itemCmps.push(itemPage);
		}
		var layout = pageData.layout.toLowerCase();
		switch(pageData.layout.toLowerCase()){
		case 'vbox':
		case 'hbox':
		case 'table':
			layout = Ext.apply({
				type : pageData.layout.toLowerCase()
			}, pageData.layoutConfig);
			break;
		default:
			break;
		}
		// 页面配置
		var config = {
			title : pageData.title,
			iconCls : pageData.iconCls,
			layout : layout,
			items : itemCmps
		};
		config = Ext.apply(config, pageData.cfg);
		// 帮助文档的tool
		if(pageData.helpDocKey && pageData.helpDocKey.trim() !== "") {
			if(!config.tools) {
				config.tools = [];
			}
			config.tools.push({
				type: 'help',
				handler: function() {
					AscApp.getAscDesktop().openDocWin(pageData.helpDocKey.trim());
				}
			});
		}
        me.callParent([config]);
	},
	// 执行刷新
	doRefresh : function(params){
		// zhsq 2016-02-17 修改遍历items的方式
		this.items.each(function(item, idx, len){
			if(Ext.isFunction(item.doRefresh)){
				item.doRefresh(params);
				return true;
			}
		});
		/*for(var n in this.items){
			var item = this.items[n];
			if(Ext.isFunction(item.doRefresh)){
				item.doRefresh(params);
			}
		}*/
	},
	initComponent : function(){
		var me = this;
		me.callParent();
	}
});