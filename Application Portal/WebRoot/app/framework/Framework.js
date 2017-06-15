Ext.define('Asc.framework.Framework', {
	// 指定基类
	extend: 'Ext.app.Application',
	// 设置别名
	alias: 'ascapplication',
	// 名称
	name : 'Asc',
	// 应用属性名
	appProperty : 'App',
	// 引用类
	requires : ['Asc.common.Message',
	             'Asc.common.ActionManager',
	             'Asc.framework.desktop.controller.Desktop', 
	             'Asc.framework.application.controller.AppManager'],
	// 应用程序标题
	title : '应用程序框架',
	// 用户信息
	user : undefined,
	// jsp页面请求路径
	jspPageProxy : 'page.do',
	// jsp页面请求路径
	extRoot : 'dependencies/ext-4.2.1',
	// 初始化函数
	init : function(){
	},
	// 启动函数
	launch : function(){
		// 输出日志
		Asc.common.Message.log('Application "' + this.title + '" launch is called ', this, 'log');
		// 屏蔽右键
		Ext.getBody().on('contextmenu', function(e, el) {
			e.preventDefault();
		});
		// 初始化提示管理器
		Ext.tip.QuickTipManager.init();
		// 初始化公共函数动作管理器
		this.ActionManager = Ext.create('Asc.common.ActionManager');
		// 装载桌面
		this.loadController('Asc.framework.desktop.controller.Desktop', 'AscDesktop');
		// 装载系统功能
		this.loadController('Asc.framework.system.controller.System', 'AscSystem');
		// 装载应用管理功能
		this.loadController('Asc.framework.application.controller.AppManager', 'AscAppManager');
		// 装载模块管理功能
		this.loadController('Asc.framework.module.controller.ModuleManager', 'AscModuleManager');
		// 装载页面管理功能
		this.loadController('Asc.framework.page.controller.PageManager', 'AscPageManager');
		// 装载文档管理功能
		this.loadController('Asc.framework.document.controller.DocumentManager', 'AscDocumentManager');
		// 装载流程管理功能
		this.loadController('Asc.framework.workflow.controller.WorkflowManager', 'AscWorkflowManager');
		// 数据字典管理功能
		this.loadController('Asc.framework.dictionary.controller.DictionaryManager', 'AscDictionaryManager');
		// 装载门户数据
		// Ext.defer(this.loadPortalData, 2000, this);
		// this.loadPortalData();
	},
	// 桌面装载完成
	loadPortalData : function(){
		PortalAppDirect.loadPortalData(function(result, e){
			if(result && result.success){
				this.getAscDesktop().createViewport(result.preferences.wsMode || 'AscDesktopWorkspaceMulti');
				this.setTitle(result.title);
				this.setUser(result.user);
				this.setNavigators(result.navigators);
				// Ext.defer(this.initPreferences, 200, this, [result.preferences]);
				this.initPreferences(result.preferences);
				// TODO 显示监控台
				//this.openMonitor();
			}else{
				// TODO 错误处理
			}
		}, this);
	},
	// TODO 显示监控台
	openMonitor : function() {								
		console.log('======================显示监控台===================');
		var dt = this.getAscDesktop();
		dt.openModule('Crip', 'mMonitor')
	},
	// 获得桌面控制器对象
	getAscDesktop : function(){
		return this.getController('AscDesktop');
	},
	// 获得系统功能控制器对象
	getAscSystem : function(){
		return this.getController('AscSystem');
	},
	// 装载ASC控制器
	getAscController: function(className, id) {
		var me = this,
			controllers = me.controllers,
			controller;

		controller = controllers.get(id);

		if (!controller) {
			controller = Ext.create(className, {
				application: me,
				id:		  id
			});

			controllers.add(controller);

			if (me._initialized) {
				controller.doInit(me);
			}
		}

		return controller;
	},
	// 动态装载模块
	loadController : function(controllerClassName, key, fn, scope){
		var controller = this.controllers.get(key);
		if(!controller){
			// 输出日志
			Asc.common.Message.log('动态装载 [' + controllerClassName + ']控制器类', this, 'log');
			Ext.syncRequire(controllerClassName, function(){
				// 装载完毕后初始化模块
				var module = this.getAscController(controllerClassName, key);
				if(Ext.isFunction(fn)){
					fn.call(scope || this, module);
				}
			}, this);
		}else{
			return controller;
		}
	},
	// 设置应用程序标题
	setTitle : function(title){
		this.title = title;
		// 设置界面
		var desktop = this.getAscDesktop();
		desktop.setAppTitle(title);
	},
	// 设置用户信息
	setUser : function(user){
		this.user = user;
		// 设置用户信息
		var desktop = this.getAscDesktop();
		desktop.setUser(user);
	},
	// 设置应用程序标题
	setNavigators : function(navigators){
		// 设置界面
		var desktop = this.getAscDesktop();
		desktop.setNavigators(navigators);
	},
	// 清除桌面栏目
	clearPortlet : function() {
		this.getAscDesktop().clearPortlet();
	},
	// 清除快捷菜单
	clearShortcuts : function() {
		this.getAscDesktop().clearShortcuts();
	},
	
	// 设置用户配置信息
	initPreferences : function(preferences){
		if(preferences){
			this.preferences = preferences;
			// 设置界面
			var desktop = this.getAscDesktop();
			// 清除桌面栏目
			desktop.clearPortlet();
			// 清除快捷菜单
			desktop.clearShortcuts();
			// 设置背景色
			if(preferences.bgcolor){
				desktop.setBgcolor(preferences.bgcolor);
			}
			// 设置背景图片
			if(preferences.wallpaper){
				desktop.setWallpaper(preferences.wallpaper);
			}
			// 设置显示样式
			if(preferences.theme){
				this.setTheme(preferences.theme);
			}
			// 设置桌面栏数
			if(preferences.columns){
				desktop.setColumns(preferences.columns);
			}
			// 设置桌面栏目
			if(preferences.portlets){
				desktop.addPortlets(preferences.portlets);
			}
			// 设置快捷按钮
			if(preferences.shortcuts){
				desktop.addShortcuts(preferences.shortcuts);
			}
		}
	},
	// 获得用户配置信息
	getPreferences : function(){
		return this.preferences;
	},
	// 获得桌面栏目属性
	getPortlet : function(id){
		if(this.preferences && this.preferences.portlets){
			for(var n in this.preferences.portlets){
				if(this.preferences.portlets[n].id === id){
					return this.preferences.portlets[n];
				}
			}
		}
		return null;
	},
	// 添加桌面栏目
	addPortlet : function(portlet){
		/*数据结构
		{
			id : 'Demo.pltTest',
			appKey : 'Demo',
			portletId : 'pltTest'
		}
		 */
		console.log(portlet);
		if(this.preferences && this.preferences.portlets){
			// 桌面栏目不存在的时候添加桌面栏目
			if(!this.getPortlet(portlet.id)) {
				var c = {};
				c = Ext.apply(c, portlet);
				this.preferences.portlets.push(c);
				// 更新界面
				var desktop = this.getAscDesktop();
				desktop.addPortlet(c);
			}
		}
	},
	// TODO 移除桌面栏目
	removePortlet : function(key){
		var index,
			ps = this.preferences.portlets,
			desktop = this.getAscDesktop();
		for(index = ps.length - 1; index >= 0; index--) {
			if(ps[index] && (ps[index].id === key)) {
				ps.splice(index, 1);
				desktop.removePortlet(key);
			}
		}
	},
	// 获得桌面栏目属性
	getShortcut : function(id){
		if(this.preferences && this.preferences.shortcuts){
			for(var n in this.preferences.shortcuts){
				if(this.preferences.shortcuts[n].id === id){
					return this.preferences.shortcuts[n];
				}
			}
		}
		return null;
	},
	// 添加快捷菜单
	addShortcut : function(shortcut){
		/*数据结构
		{
			id : 'Demo.sctTest',
			appKey : 'Demo',
			shortcutId : 'sctTest'
		}
		 */
		if(this.preferences && this.preferences.shortcuts){
			// 快捷按钮不存在的时候添加快捷按钮
			if(!this.getShortcut(shortcut.id)) {
				var c = {};
				c = Ext.apply(c, shortcut);
				this.preferences.shortcuts.push(c);
				// 更新界面
				var desktop = this.getAscDesktop();
				desktop.addShortcut(c);
			}
		}
	},
	// TODO 移除快捷方式
	removeShortcut : function(key){
		var index,
			ss = this.preferences.shortcuts,
			desktop = this.getAscDesktop();
		for(index = ss.length - 1; index >= 0; index--) {
			if(ss[index] && (ss[index].id === key)) {
				ss.splice(index, 1);
				desktop.removeShortcut(key);
			}
		}
	},
	// 设置界面样式
	setTheme : function(theme){
		this.preferences.theme = theme;
		Ext.util.CSS.swapStyleSheet('theme', theme.path);
		Ext.util.CSS.swapStyleSheet('desktop', 'resources/css/desktop.css');
	},
	// 设置界面样式
	setWallpaper : function(wallpaper){
		this.preferences.wallpaper = wallpaper;
		// 更新界面
		var desktop = this.getAscDesktop();
		desktop.setWallpaper(wallpaper);
	},
	
	// 设置背景颜色
	setBackgroundColor: function(color){
		this.preferences.backgroundcolor = color;
		// TODO
	},
	
	//设置任务栏透明度
	setTransparency: function(transparency){
		this.preferences.transparency = transparency;;
		// TODO
	},
	
	//设置前景色
	setFrontColor: function(color){
		this.preferences.frontcolor = color;
		// TODO
	}
});
