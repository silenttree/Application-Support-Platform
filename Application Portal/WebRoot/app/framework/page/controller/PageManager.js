Ext.define('Asc.framework.page.controller.PageManager', {
	// 指定基类
	extend : 'Ext.app.Controller',
	// 设置别名
	alias : 'widget.AscPageManager',
	// 设置引用类
	requires : ['Asc.framework.page.view.View', 
	            'Asc.framework.page.view.Tree', 
	            'Asc.framework.page.view.TableForm', 
	            'Asc.framework.page.view.TabLayout', 
	            'Asc.framework.page.view.Layout'],
	// 名称空间
	$namespace : 'Asc.framework.page',
	// 页面定义
	pages : {},
	// 按钮脚本函数
	handlers : {},
	// 初始化函数
	init : function() {
		Asc.common.Message.log('PageManager is loaded & initialise ', this, 'log');
		// 页面控制界面元素
		this.control({
			'AscPageView' : {
				itemcontextmenu : this.viewContextmenu,
				itemdblclick : this.viewDblClick
			},
			'AscPageView [type=pagebutton]' : {
				click : this.buttonHandler
			},
			'menu[type=pagemenu] menuitem' : {
				click : this.buttonHandler
			}
		});
	},
	// 获得页面定义数据
	getPageData : function(appKey, moduleId, pageId){
		if(!Ext.isDefined(this.pages[appKey])){
			this.pages[appKey] = {};
		}
		if(!Ext.isDefined(this.pages[appKey][moduleId])){
			this.pages[appKey][moduleId] = {};
		}
		return this.pages[appKey][moduleId][pageId];
	},
	// 设置页面数据
	setPageData : function(appKey, moduleId, pageId, data){
		this.pages[appKey][moduleId][pageId] = data;
		// 处理按钮
		if(Ext.isDefined(data.buttons)){
			for(n in data.buttons){
				var btn = data.buttons[n];
				btn.appKey = appKey;
				if(btn.xtype != 'AscUploadButton'){
					btn.type = 'pagebutton';
					if(Ext.isDefined(btn.menu)){
						btn.menu.type = 'pagemenu';
					}
				}
				// 初始化事件脚本
				if(Ext.isDefined(btn.listeners)){
					for(var l in btn.listeners){
						eval('btn.listeners[l] = ' + btn.listeners[l]);
					}
				}
			}
		}
		// 处理按钮
		if(Ext.isDefined(data.contextmenu)){
			data.contextmenu.type = 'pagemenu';
		}
		// 处理按钮脚本
		if(Ext.isDefined(data.scripts)){
			for(var n in data.scripts){
				try{
					eval('var fn = ' + data.scripts[n]);
					if(Ext.isFunction(fn)){
						this.handlers[n] = fn;
					}else{
						Asc.common.Message.log('按钮[' + n + ']定义非法，不是函数体！');
					}
				}catch(err){
					Asc.common.Message.log('按钮[' + n + ']定义非法，不是函数体！');
				}
			}
			delete data.scripts;
		}
		// 装载页面控制器
		if(data.hasController){
			AscApp.loadController('Asc.app.' + appKey.toLowerCase() + '.' + pageId, 
				appKey.toLowerCase() + '.' + pageId, 
				function(controller){
				controller.appKey = appKey;
				controller.moduleId = moduleId;
				controller.pageId = pageId;
				controller.afterInit();
			}, this);
		}
	},
	// 装载页面定义数据
	loadPageData : function(appKey, moduleId, pageId, fn, scope){
		var data = this.getPageData(appKey, moduleId, pageId);
		if(!Ext.isDefined(data)){
			// 调用Direct方法请求页面数据
			var appManager = AscApp.getController('AscAppManager');
			var getPageFn = appManager.getEngineDirectFn(appKey, 'getPage');
			getPageFn(moduleId, pageId, function(result, e){
				if(result && result.success){
					for(var pId in result.data){
						// 装载页面数据
						this.setPageData(appKey, moduleId, pId, result.data[pId]);
					}
					if(Ext.isFunction(fn)){
						fn.call(scope || this);
					}
					Asc.common.Message.log('装载页面[' + appKey + '.' + pageId + ']数据执行完毕！');
					console.log(result.data);
				}else{
					Asc.common.Message.showError('装载页面[' + appKey + '.' + pageId + ']失败！');
				}
			}, this);
		}else{
			return data;
		}
	},
	// 获得界面对象（异步装载时执行fn）
	getPageCmp : function(appKey, moduleId, pageId, params, context, fn, scope){
		// 装载页面数据
		var data = this.getPageData(appKey, moduleId, pageId);
		if(!Ext.isDefined(data)){
			this.loadPageData(appKey, moduleId, pageId, function(){
				if(Ext.isFunction(fn)){
					fn.call(scope || this);
				}
			}, this);
		}else{
			var page;
			switch(data.type){
			case 'urlpage':
				page = this.getUrlPage(appKey, moduleId, pageId, params, context);
				break;
			case 'view':
				page = this.getViewPage(appKey, moduleId, pageId, params, context);
				break;
			case 'treepage':
				page = this.getTreePage(appKey, moduleId, pageId, params, context);
				break;
			case 'formtablelayout':
				page = this.getTableFormPage(appKey, moduleId, pageId, params, context);
				break;
			case 'formpositionlayout':
				page = this.getFormPositionPage(appKey, moduleId, pageId, params, context);
				break;
			case 'queryform':
				page = this.getQuerFormPage(appKey, moduleId, pageId, params, context);
				break;
			case 'layout':
				if(!data.items || data.items.length == 0){
					Asc.common.Message.error('布局页面[' + pageId + ']子页面数量为0，无法布局');
					return;
				}
				// 检查子页面装载
				// TODO 批量装载子页面
				for(var i=0;i<data.items.length;i++){
					var item = data.items[i];
					if(Ext.isDefined(item.pageId)){
						var itemData = this.getPageData(appKey, moduleId, item.pageId);
						if(!Ext.isDefined(itemData)){
							this.loadPageData(appKey, moduleId, item.pageId, function(){
								if(Ext.isFunction(fn)){
									fn.call(scope || this);
								}
							}, this);
							return;
						}
					}
				}
				page = this.getLayoutPage(appKey, moduleId, pageId, params, context);
				break;
			default :
				break;
			}
			page.appKey = appKey;
			page.moduleId = moduleId;
			page.pageId = pageId;
			page.type = data.type;
			return page;
		}
	},
	// 装载URL页面
	getUrlPage : function(appKey, moduleId, pageId, params, context){
		var pageData = this.getPageData(appKey, moduleId, pageId);
		var config = Ext.apply({
			title : pageData.title,
			iconCls : pageData.iconCls,
			appId : appKey,
			moduleId : moduleId,
			pageId : pageId,
			jspUrl : pageData.url,
			useProxy : pageData.useProxy,
			params : Ext.apply({}, params, pageData.params),
			itemId : appKey + '.' + pageId,
			context : context
		}, pageData.cfg);
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
		var page = Ext.create('Asc.extension.JspPanel', config);
		return page;
	},
	getViewPage : function(appKey, moduleId, pageId, params, context){
		var pageData = this.getPageData(appKey, moduleId, pageId);
		var page = Ext.create('Asc.framework.page.view.View', appKey, moduleId, pageData, params, context, {
			itemId : appKey + '.' + pageId
		});
		return page;
	},
	getTreePage : function(appKey, moduleId, pageId, params, context){
		var pageData = this.getPageData(appKey, moduleId, pageId);
		var page = Ext.create('Asc.framework.page.view.Tree', appKey, moduleId, pageData, params, context, {
			itemId : appKey + '.' + pageId
		});
		return page;
	},
	getTableFormPage : function(appKey, moduleId, pageId, params, context){
		var pageData = this.getPageData(appKey, moduleId, pageId);
		var page = Ext.create('Asc.framework.page.view.TableForm', appKey, moduleId, pageData, params, context, {
			itemId : appKey + '.' + pageId
		});
		return page;
	},
	getLayoutPage : function(appKey, moduleId, pageId, params, context){
		var pageData = this.getPageData(appKey, moduleId, pageId);
		var page;
		if(pageData.layout == 'Tabs'){
			page = Ext.create('Asc.framework.page.view.TabLayout', appKey, moduleId, pageData, params, context, {
				itemId : appKey + '.' + pageId
			});
		}else{
			page = Ext.create('Asc.framework.page.view.Layout', appKey, moduleId, pageData, params, context, {
				itemId : appKey + '.' + pageId
			});
		}
		return page;
	},
	// 处理菜单被右键点击
	viewContextmenu : function(view, record, item, index, e, eOpts){
		var grid = view.up('grid');
		if(Ext.isDefined(grid) && Ext.isDefined(grid.contextmenu)){
			//grid.contextmenu.grid = grid;
			grid.contextmenu.showAt(e.getXY());
		}
	},
	// 处理双击执行默认事件
	viewDblClick : function(view, record, item, index, e, eOpts){
		var grid = view.up('grid');
		// 查找默认按钮
		var tbar = grid.getDockedItems('toolbar[dock="top"]')[0];
		var btn = tbar.down('[isDefault=true]');
		if(Ext.isDefined(btn) && btn != null){
			this.buttonHandler(btn);
		}
	},
	// 处理按钮被点击
	buttonHandler : function(btn){
		if(Ext.isFunction(this.handlers[btn.name])){
			var grid = btn.up('grid');
			if(!Ext.isDefined(grid)){
				var menu = btn.up('[type=pagemenu]');
				grid = menu.grid;
			}
			if(Ext.isDefined(grid)){
				var records = grid.getSelectionModel().getSelection(); 
				this.handlers[btn.name].call(btn, grid, records);
				return;
			}
			var tree = btn.up('treepanel');
			if(!Ext.isDefined(grid)){
				var menu = btn.up('[type=pagemenu]');
				tree = menu.tree;
			}
			if(Ext.isDefined(tree)){
				var records = tree.getSelectionModel().getSelection(); 
				this.handlers[btn.name].call(btn, tree, records);
				return;
			}
		}else{
			Asc.common.Message.log('按钮[' + btn.name + ']未定义操作函数！');
		}
	}
});