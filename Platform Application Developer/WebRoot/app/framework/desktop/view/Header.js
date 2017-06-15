// 页面上部标题区界面
Ext.define('Asc.framework.desktop.view.Header', {
	
	extend: 'Ext.panel.Panel',
	
	alias: 'widget.AscDesktopHeader',
    // 设置引用类
	requires: ['Asc.framework.desktop.view.DeveloperToolbar',
	           'Asc.framework.desktop.view.Title'],
	       	
	bodyCls : 'asc-desktop-header-body',
	
	border : false,
	
	defaults : {
		border : false
	},
	
	layout : {
        type: 'hbox',
        align:'stretch'
    },
    
	items : [{
		// 应用程序标识,
		flex: 3,
		xtype : 'AscDesktopTitle'
	}, {
		// 开发工具菜单
		xtype : 'AscDesktopDeveloperToolbar'
	}]
});