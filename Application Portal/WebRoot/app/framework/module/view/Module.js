// 页面上部标题区界面
Ext.define('Asc.framework.module.view.Module', {
	
	extend: 'Ext.panel.Panel',
	
	alias: 'widget.AscModule',
    // 设置引用类
	requires: ['Asc.framework.module.view.ModuleNavigator',
	           'Asc.framework.module.view.ModuleWorkspace'],
	
	layout : 'border',
	
	appKey : '',
	
	moduleId : '',
	
	border : false,
	
	defaults : {
		border : true
	},
	
    initComponent: function() {
        var me = this;
        me.items = [{
        	appKey : this.appKey,
        	moduleId : this.moduleId,
        	xtype : 'AscModuleNavigator',
        	region : 'west',
    		split: true,
    		collapseMode : 'mini',
        	width : 250,
    		minWidth : 100,
    		maxWidth : 600
        },{
        	appKey : this.appKey,
        	moduleId : this.moduleId,
        	xtype : 'AscModuleWorkspace',
        	region : 'center'
        }]
        me.callParent(arguments);
    },
    // 模块工作空间
    getWorkspace : function(){
    	return this.down('AscModuleWorkspace');
    },
    // 获得页面对象
    getPageById : function(pageId){
    	return this.down('[pageId=' + pageId + ']');
    },
    // 在模块中打开页面
    openPage : function(source, itemId, pageId, params, fn, scope){
		// 模块工作空间
		var ws = this.getWorkspace();
		// 查找已打开Page对象
		var page = ws.getComponent(itemId);
		// 判断是否已打开
		if(!Ext.isDefined(page)){
			// 获得界面对象
			var pageManager = AscApp.getController('AscPageManager');
			page = pageManager.getPageCmp(this.appKey, this.moduleId, pageId, params, {}, function(){
				this.openPage(source, itemId, pageId, params, fn, scope);
			}, this);
			if(!Ext.isDefined(page)){
				return;
			}
			// 设置调用参数
			page.source = source;
			page.itemId = itemId;
			// 添加到工作空间
			page.closable = true;
			ws.add(page);
			ws.doLayout();
			if(Ext.isFunction(page.doRefresh) && page.isAutoLoad){
				page.doRefresh(params);
			}
		}
		// 根据参数刷新页面（调用接口函数）
		if(source != page.source){
			if(Ext.isFunction(page.doRefresh)){
				page.doRefresh(params);
			}
			page.source = source;
		}
		// 激活页面;
		ws.setActiveTab(page);
		if(Ext.isFunction(fn)){
			fn.call(scope || this, page);
		}
    },
    // 在模块中打开文档
    openDocument : function(documentId, dataId, cfg){
    	// 初始化调用参数
    	if(!cfg){
    		cfg = {};
    	}
    	// 模块控制器
		var docManager = AscApp.getController('AscDocumentManager');
		// 模块工作空间
		var ws = this.getWorkspace();
    	if(!dataId){
    		docManager.getDocumentSeqId(this.appKey, documentId, function(dataId){
    			this.openDocument(documentId, dataId, cfg);
    		}, this);
    		return;
    	}
		// 查找已打开Document对象
    	var id = docManager.getDocumentId(this.appKey, documentId, dataId);
    	var docWrap = docManager.getDocumentCmp(this.appKey, documentId, dataId, {
    		fn : function(){
    			this.openDocument(documentId, dataId, cfg);
    		},
    		scope : this,
    		userlogId : cfg.userlogId,
    		iniValues : cfg.iniValues
    	});
		if(!Ext.isDefined(docWrap)){
			return;
		};
		if(!docWrap.rendered){
			// 设定文档对象打开源
			docWrap.getDoc().opener = cfg.scope || this;
			// 设定文档打开源
			docWrap.opener = this;
			// 获得文档定义数据
			var docData = docManager.getDocumentData(this.appKey, documentId);
			// 设置打开参数
			Ext.apply(docWrap, {
				title : docData.title + ' - (id=' + dataId + ')',
				iconCls : docData.iconCls,
				closable : true,
				moduleId : this.moduleId
			});
			// 初始装载文档
			if(Ext.isFunction(docWrap.getDoc().doLoad)){
				docWrap.getDoc().doLoad();
			}
			// 添加到工作空间
			ws.add(docWrap);
			ws.doLayout();
		}
		// 激活页面;
		ws.setActiveTab(docWrap);
		// 执行回调
		if(Ext.isFunction(cfg.fn)){
			cfg.fn.call(cfg.scope || this, docWrap.getDoc());
		}
    },
    // 关闭文档窗口（private）
    closeDocument : function(doc){
		var ws = this.getWorkspace();
		ws.remove(doc);
		ws.doLayout();
    }
});