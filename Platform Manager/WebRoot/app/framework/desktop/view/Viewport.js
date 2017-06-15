// 桌面页面布局定义
Ext.define('Asc.framework.desktop.view.Viewport', {
	
	extend: 'Ext.container.Viewport',
	
	alias: 'widget.AscDesktop',
    // 设置引用类
	requires: ['Asc.framework.desktop.view.Header', 
	           'Asc.framework.desktop.view.Navigator', 
	           'Asc.framework.desktop.view.Workspace', 
	           'Asc.framework.desktop.view.Footer'],
	
	layout : 'border',
	
	border : false,
	
	bodyCls : 'asc-desktop-header-body',
	
	defaults : {
		border : false
	},
	
	items : [{
		// 标题区
		region : 'north',
		height : 36,
		xtype : 'AscDesktopHeader'
	}, {
		// 导航区
		region : 'west',
		border : true,
		split: true,
		collapseMode : 'mini',
		padding : '0 0 0 5',
		width : 300,
		minWidth : 100,
		maxWidth : 600,
		xtype : 'AscDesktopNavigator'
	}, {
		// 工作区
		region : 'center',
		border : true,
		padding : '0 5 0 0',
		layout : 'fit',
		xtype : 'AscDesktopWorkspace'
	}, {
		// 版权提示区
		region : 'south',
		height : 23,
		padding : '0 5 0 5',
		xtype : 'AscDesktopFooter'
	}]
});