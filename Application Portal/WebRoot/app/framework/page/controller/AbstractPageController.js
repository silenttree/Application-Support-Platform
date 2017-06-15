Ext.define('Asc.framework.page.controller.AbstractPageController', {
	
	extend : 'Ext.app.Controller',
	
	appKey : undefined,
	
	moduleId : undefined,
	
	pageId : undefined,

	// 初始化函数
	init : function(){
		Asc.common.Message.log(this.self.getName() + ' is loaded & initialise ', this, 'log');
	},
	// 控制器被初始化后调用
	afterInit : Ext.emptyFn,
	// 获得主页面对象
	getPage : function(itemCmp){
		return itemCmp.up('[pageId=' + this.pageId + ']');
	},
	// 允许根据key，pageId，页面标题查找对象
	getPageItem : function(pageCmp, itemKey){
		return pageCmp.down('[itemId=' + itemKey + '],[pageId=' + itemKey + '],[title=' + itemKey + ']');
	}
});