Ext.define('Asc.framework.page.view.Tree', {
	// 指定基类
	extend : 'Ext.tree.Panel',
	// 设置别名
	alias : 'widget.AscPageTree',
	// 设置引用类
	requires : ['Asc.framework.page.store.TreePageStore'],
    
	autoScroll : true,
	
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
		// 根节点
		me.root = {
			iconCls : pageData.iconCls,
			type : 'None',
			key : this.pageId,
			text : pageData.title,
			params : Ext.apply(params, pageData.params)
		}
		// 存储
		me.store = new Asc.framework.page.store.TreePageStore(this.appKey, this.moduleId, this.pageId);
		// 初始化contextmenu
		if(Ext.isDefined(pageData.contextmenu)){
			this.contextmenu = Ext.create('Ext.menu.Menu', pageData.contextmenu);
			this.contextmenu.tree = this;
		}
		var config = {
			title : pageData.title
		}
		var config = Ext.apply(config, pageData.cfg);
        me.callParent([config]);
	},
	onDestroy : function(){
		// 销毁右键菜单对象
		if(Ext.isDefined(this.contextmenu)){
			Ext.destroy(this.contextmenu);
		}
		this.callParent();
	},
	// 执行刷新
	doRefresh : function(params){
		var node = this.getRootNode();
		if(Ext.isDefined(params)){
			node.set('params', params);
		}
		// 展开根节点
		if(node.isExpanded()){
			this.doReload();
		}else{
			node.expand();
		}
	},
	// 重新装载节点
	doReload : function(node){
		if(!node){
			var nodes = this.getSelectionModel().getSelection();
			if(nodes.length > 0){
				node = nodes[0];
			}else{
				node = this.getRootNode();
			}
		}
		this.getStore().load({node : node});
	}
});