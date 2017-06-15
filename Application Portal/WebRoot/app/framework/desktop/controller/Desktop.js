Ext.define('Asc.framework.desktop.controller.Desktop', {
	// 指定基类
	extend : 'Ext.app.Controller',
	// 设置别名
	alias : 'widget.AscDesktop',
	// 设置引用类
	requires : [],
	// 名称空间
	$namespace : 'Asc.framework.desktop',
	// 数据模型
	models : ['Navigator'],
	// 数据存储
	stores : ['Navigators'],
	// 视图
	views : ['Viewport'],
	// 设置引用
    refs: [{
		ref: 'AscHeader',
		selector: 'AscDesktopHeader'
	},{
		ref: 'AscViewport',
		selector: 'AscDesktopViewport'
	},{
		ref: 'AscFooter',
		selector: 'AscDesktopFooter'
	},{
		ref: 'AscNavigator',
		selector: 'AscDesktopNavigator'
	},{
		ref: 'AscUserPanel',
		selector: 'AscDesktopUserPanel'
	},{
		ref: 'AscTitle',
		selector: 'AscDesktopTitle'
	},{
		ref: 'AscPortal',
		selector: 'AscDesktopPortal'
	}],
	
	init : function() {
		Asc.common.Message.log('Desktop is loaded & initialise ', this, 'log');
	},
	// 创建桌面界面
	createViewport : function(wsMode){
		this.wsMode = wsMode;
		// 创建桌面对象
		this.getView('Viewport').create();
		// 控制导航菜单
		this.control({
			'AscDesktopHeader splitbutton' : {
				click : this.actionHandler
			},
			'AscDesktopHeader menuitem' : {
				click : this.actionHandler
			}
		});
		
	},
	getAscWorkspace : function(){
		return this.getAscViewport().getWorkspace();
	},
	// 设置应用程序标题
	setAppTitle : function(title){
		this.getAscTitle().update({title : title});
	},
	// 设置导航菜单
	setNavigators : function(navigators){
		this.getAscNavigator().setNavigators(navigators);
	},
	// 设置用户信息
	setUser : function(user){
		this.getAscUserPanel().setUser(user);
	},
	// 设置背景颜色
	setBgcolor : function(bgcolor){
		
	},
	// 设置桌面栏目栏数
	setColumns : function(columns){
		this.getAscPortal().updateColumns(columns);
	},
	// 添加桌面栏目
	addPortlet : function(portlet){
		this.getAscPortal().addPortlet(portlet);
	},
	// 删除桌面栏目
	removePortlet : function(key){
		this.getAscPortal().removePortlet(key);
	},
	
	// 清除除桌面栏目
	clearPortlet : function(){
		this.getAscPortal().clearPortlet();
	},
	
	// 显示桌面栏目
	addPortlets : function(portlets){
		if(!portlets){
			portlets = AscApp.getPreferences().portlets;
		}
		for(n in portlets){
			this.addPortlet(portlets[n]);
		}
	},
	// 添加快捷方式
	addShortcut : function(shortcut){
		this.getAscPortal().addShortcut(shortcut);
	},
	// 删除快捷方式
	removeShortcut : function(key){
		this.getAscPortal().removeShortcut(key);
	},
	// 显示快捷方式
	addShortcuts : function(shortcuts){
		if(!shortcuts){
			shortcuts = AscApp.getPreferences().shortcuts;
		}
		for(n in shortcuts){
			this.addShortcut(shortcuts[n]);
		}
	},

	// 清除快捷方式
	clearShortcuts : function(){
		this.getAscPortal().clearShortcuts();
	},
	// 按钮、菜单处理事件
	actionHandler : function(action){
		if(action.name){
			var group = action.group || 'default';
			AscApp.ActionManager.runFunction(group + '.' + action.name, action, [action]);
		}
	},
	// 设置墙纸
	setWallpaper : function(wallpaper){
		Asc.common.Message.showMask('正在装载墙纸...');
		var wp = new Image();
		wp.src = 'resources/wallpapers/' + wallpaper;
		var portalEl = this.getAscPortal().body;
		var task;
		var verify = function(){
			if(wp.complete){
				task.cancel();
				Asc.common.Message.hideMask();
				Asc.common.Message.showInfo('已完成墙纸装载！');

				portalEl.setStyle('background-image','url(' + wp.src + ')');
			}else{
				task.delay(200);
			}
		};
		task = new Ext.util.DelayedTask(verify, this);
		task.delay(200);
	},
	// 获得模块
	getModule : function(appKey, moduleId){
		// 模块工作空间
		var ws = this.getAscWorkspace();
		// 查找已打开Module对象
		var itemId = appKey + '.' + moduleId;
		return ws.getComponent(itemId);
	},
	// 打开模块（打开后执行fn）
	openModule : function(appKey, moduleId, fn, scope){
		var appManager = AscApp.getController('AscAppManager');
		var app = appManager.getAppModel(appKey);
		if(!Ext.isDefined(app)){
			Asc.common.Message.error('应用[' + appKey + ']不存在！', this);
			return;
		}
		// 模块工作空间
		var ws = this.getAscWorkspace();
		// 查找已打开Module对象
		var itemId = appKey + '.' + moduleId;
		var module = ws.getComponent(itemId);
		if(!Ext.isDefined(module)){
			var moduleManager = AscApp.getController('AscModuleManager');
			module = moduleManager.getModuleCmp(appKey, moduleId, function(module){
				this.openModule(appKey, moduleId, fn, scope);
			}, this);
			if(!Ext.isDefined(module)){
				return;
			}
			module.itemId = itemId;
			ws.loadCmp(module);
		}
		ws.activeCmp(module);
		if(Ext.isFunction(fn)){
			fn.call(scope || this, module);
		}
	},
    // 在桌面打开文档
    openDocument : function(appKey, documentId, dataId, config){
    	// 初始化调用参数
    	if(!config){
    		config = {};
    	}
    	var scope = config.scope || undefined;
    	var fn = config.fn || undefined;
    	// 文档控制器
		var docManager = AscApp.getController('AscDocumentManager');
    	if(!dataId){
    		docManager.getDocumentSeqId(appKey, documentId, function(dataId){
    			this.openDocument(appKey, documentId, dataId, config);
    		}, this);
    		return;
    	}
		// 查找已打开Document对象
    	var id = docManager.getDocumentId(appKey, documentId, dataId);
    	var docWrap = docManager.getDocumentCmp(appKey, documentId, dataId, {
    		fn : function(){
    			this.openDocument(appKey, documentId, dataId, config);
    		},
    		scope : this,
    		userlogId : config.userlogId,
    		iniValues : config.iniValues
    	});
		if(!Ext.isDefined(docWrap)){
			return;
		}
		// 在窗口中显示
		var win;
		if(!docWrap.rendered){
			// 设置文档打开源对象
			docWrap.getDoc().opener = scope || this;
			// 设定文档界面打开源
			docWrap.opener = this;
			// 获得文档定义数据
			var docData = docManager.getDocumentData(appKey, documentId);
			win = Ext.create('Ext.window.Window', Ext.apply({
				title : docData.title + ' - (id=' + dataId + ')',
				width : 550,
				height : 450,
				layout: 'fit',
				closable : true,
				items : docWrap
			}, docData.containerCfg));
			// 初始装载文档
			if(Ext.isFunction(docWrap.getDoc().doLoad)){
				docWrap.getDoc().doLoad();
			}
		}else{
			win = docWrap.up('window');
		}
		win.show();
		// 执行回调
		if(Ext.isFunction(fn)){
			fn.call(scope || this, docWrap.getDoc());
		}
    },
    // 关闭文档窗口（private）
    closeDocument : function(doc){
		var win = doc.up('window');
		win.close();
    }
});