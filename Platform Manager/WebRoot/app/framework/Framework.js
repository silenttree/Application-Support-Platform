Ext.define('Asc.framework.Framework', {
	// 指定基类
	extend: 'Ext.app.Application',
	// 设置别名
	alias: 'ascapplication',
	// 设置引用类
	requires : ['Asc.common.ActionManager',
	            'Asc.common.ClassManager'],
	// 名称
	name : 'Asc',
	// 应用属性名
	appProperty : 'App',
	// 应用程序标题
	title : 'ASC Manager 运维管理平台',
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
		// 装载Extjs4扩展
		Ext.Loader.loadScript({
			url : 'app/common/Extjs4.override.js'
		});
		// 屏蔽右键
		Ext.getBody().on('contextmenu', function(e, el) {
			e.preventDefault();
		});
		// 初始化公共函数动作管理器
		this.ActionManager = Ext.create('Asc.common.ActionManager');
		// 初始化剪贴板
		this.Context = Ext.create('Asc.common.Context');
		// 初始化设计工具节点类管理器
		this.ClassManager = Ext.create('Asc.common.ClassManager');
		// 装载桌面
		this.loadModule('Asc.framework.desktop.controller.Desktop', 'AscDesktop');
		// 装载开发工具控制器
		this.loadModule('Asc.framework.manager.controller.Manager', 'AscManager');
	},
	// 桌面装载完成
	onDesktopReady : function(){

	},
	// 获得桌面控制器对象
	getAscDesktop : function(){
		return this.getController('AscDesktop');
	},
	// 获得管理平台控制器对象
	getAscManager : function(){
		return this.getController('AscManager');
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
	loadModule : function(controllerClassName, key){
		var controller = this.controllers.get(key);
		if(!controller){
			// 输出日志
			Asc.common.Message.log('动态装载 [' + controllerClassName + ']控制器类', this, 'log');
			Ext.require(controllerClassName, function(){
				// 装载完毕后初始化模块
				var module = this.getAscController(controllerClassName, key);
			}, this);
		}else{
			return controller;
		}
	}
});
