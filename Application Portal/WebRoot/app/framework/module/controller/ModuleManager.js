Ext.define('Asc.framework.module.controller.ModuleManager', {
	// 指定基类
	extend : 'Ext.app.Controller',
	// 设置别名
	alias : 'widget.AscModuleManager',
	// 设置引用类
	requires : ['Asc.framework.module.view.Module'],
	// 名称空间
	$namespace : 'Asc.framework.module',
	// 模块定义
	modules : {},
	// 初始化函数
	init : function() {
		Asc.common.Message.log('ModuleManager is loaded & initialise ', this, 'log');
		// 控制导航菜单
		this.control({
			'AscModuleNavigator' : {
				// 双击节点执行打开操作
				itemdblclick : {
					fn : this.openMenuHandler,
					scope : this
				},
				load : {
					fn : this.loadMenuHandler,
					scope : this
				}
			}
		});
	},
	// 装载模块
	loadModuleData : function(appKey, moduleId, fn, scope){
		var appManager = AscApp.getController('AscAppManager');
		var app = appManager.getAppModel(appKey);
		if(!Ext.isDefined(app)){
			Asc.common.Message.error('应用[' + appKey + ']不存在！', this);
			return;
		}
		var moduleData = this.getModuleData(appKey, moduleId);
		if(!Ext.isDefined(moduleData)){
			// 调用Direct方法请求模块数据
			var getModuleFn = appManager.getEngineDirectFn(appKey, 'getModule');;
			getModuleFn(moduleId, function(result, e){
				if(result && result.success){
					// 装载模块数据
					this.modules[appKey][moduleId] = result.data;
					if(Ext.isDefined(fn)){
						fn.call(scope || this);
					}
					Asc.common.Message.log('装载模块[' + appKey + '.' + moduleId + ']数据执行完毕！');
				}else{
					Asc.common.Message.showError('装载模块[' + appKey + '.' + moduleId + ']失败！');
				}
			}, this);
		}else{
			return moduleData;
		}
	},
	// 获得模块数据定义
	getModuleData : function(appKey, moduleId){
		if(Ext.isDefined(this.modules[appKey])){
			return this.modules[appKey][moduleId];
		}else{
			this.modules[appKey] = {};
		}
	},
	// 获得界面对象（异步装载时执行fn）
	getModuleCmp : function(appKey, moduleId, fn, scope){
		var appManager = AscApp.getController('AscAppManager');
		var app = appManager.getAppModel(appKey);
		if(!Ext.isDefined(app)){
			Asc.common.Message.error('应用[' + appKey + ']不存在！', this);
			return;
		}
		var moduleData = this.getModuleData(appKey, moduleId);
		if(!Ext.isDefined(moduleData)){
			// 请求模块数据，异步装载界面
			this.loadModuleData(appKey, moduleId, function(){
				if(Ext.isFunction(fn)){
					fn.call(scope || this, module);
				}
			}, this);
			return;
		}
		// 创建模块界面对象
		var module;
		switch(moduleData.type){
		case 'JspPage' :
			var pageManager = AscApp.getController('AscPageManager');
			module = pageManager.getPageCmp(appKey, moduleId, moduleData.startPage, {}, {}, fn, scope);
			break;
		default : 
			module = Ext.create('Asc.framework.module.view.Module', {
				appKey : appKey,
				moduleId : moduleId,
				title : moduleData.title,
				iconCls : moduleData.iconCls || 'icon-app-' + appKey.toLowerCase() + '-' + moduleId.toLowerCase(),
				closable : true,
				border : false
			});
			break;
		}
		return module;
	},
	// 模块菜单装载
	loadMenuHandler : function(store, node, records, successful, eOpts ){
		// 处理默认打开节点
		for(n in node.childNodes){
			var child = node.childNodes[n];
			// 查找到默认装载节点
			if(child.get('isDefault')){
				var tree = node.getOwnerTree();
				// 选中节点
				tree.getSelectionModel().select([child]);
				// 打开节点
				this.openMenuHandler(tree.getView(), child);
				return;
			}
		}
	},
	// 打开菜单节点
	openMenuHandler : function(view, record){
		// 导航菜单
		var tree = view.up('AscModuleNavigator');
		// 模块
		var module = view.up('AscModule');
		// 模块应用标识
		var appKey = module.appKey;
		// 模块标识
		var moduleId = module.moduleId;
		// 模块数据
		var moduleData = this.getModuleData(appKey, moduleId);
		// 菜单标识
		var menuId = record.get('pageMenu');
		if(Ext.isDefined(moduleData.menus[menuId])){
			var menu = moduleData.menus[menuId];
			this.openMenu(module, tree, menu, record);
			Asc.common.Message.log('Module Navigator [appKey : ' + appKey + '][moduleId : ' + moduleId + '][menuId : ' + menuId + '] is handler');
		}else{
			Asc.common.Message.log('模块菜单[appKey : ' + appKey + '][moduleId : ' + moduleId + '][menuId : ' + menuId + '] 未定义合法动作！');
		}
	},
	// 执行打开菜单动作
	openMenu : function(module, tree, menu, node){
		// 模块数据
		var moduleData = this.getModuleData(module.appKey, module.moduleId);
		// 根据菜单定义执行动作
		var params = tree.getStore().getNodeParams(node);
		switch(menu.action){
		case 'Script':			// 执行脚本
			try{
				eval('fn = ' + menu.script);
				if(Ext.isFunction(fn)){
					// 调用函数方法
					fn.call(this, tree, node);
				}
			}catch(err){
				Asc.common.Message.showError('菜单【' + node.get('text') + '】定义执行函数非法！');
			}
			break;
		case 'Hyperlink':		// 超链接
			window.open(menu.url);
			break;
		case 'PageObject':		// 打开页面
			this.openPage(node, 'page-' + menu.id, module.appKey, module.moduleId, menu.page, params);
			break;
		case 'ModuleMenu':		// 模块菜单
			if(Ext.isDefined(moduleData.menus[menu.menu])){
				var target = moduleData.menus[menu.menu];
				this.openMenu(module, tree, target, node);
			}else{
				// 打开父节点
				alert(menu.id);
			}
			break;
		case 'None':			// 无动作
		default:
			break;
		}
	},
	// 打开并装载页面（界面装载后执行fn）
	openPage : function(source, itemId, appKey, moduleId, pageId, params, fn, scope){
		var desktop = AscApp.getController('AscDesktop');
		// 打开模块
		desktop.openModule(appKey, moduleId, function(module){
			// 打开页面
			module.openPage(source, itemId, pageId, params, fn, scope);
		}, this);
	}
});