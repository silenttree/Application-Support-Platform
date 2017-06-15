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
	models : [],
	// 数据存储
	stores : ['NavigatorStore'],
	// 视图
	views : ['Viewport'],
	// 设置引用
    refs: [{
		ref: 'AscHeader',
		selector: 'AscDesktopHeader'
	},{
		ref: 'AscWorkspace',
		selector: 'AscDesktopWorkspace'
	},{
		ref: 'AscFooter',
		selector: 'AscDesktopFooter'
	},{
		ref: 'AscTitle',
		selector: 'AscDesktopTitle'
	},{
		ref : 'AscNavigator',
		selector: 'AscDesktopNavigator'
	}],
	
	init : function() {
		Asc.common.Message.log('Desktop is loaded & initialise ', this, 'log');
		// 控制导航菜单
		this.control({
			'AscDesktopManagerToolbar button' : {
				click : AscApp.ActionManager.handler
			},
			'AscDesktopManagerToolbar menuitem' : {
				click : AscApp.ActionManager.handler
			},
			'#navigatorContextMenu menuitem' : {
				click : AscApp.ActionManager.handler
			}
		});
		// 创建桌面对象
		this.getView('Viewport').create();
		// 回调事件
		AscApp.onDesktopReady();
	}
});