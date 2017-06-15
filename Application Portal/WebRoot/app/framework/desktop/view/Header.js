// 页面上部标题区界面
Ext.define('Asc.framework.desktop.view.Header', {
	
	extend: 'Ext.panel.Panel',
	
	alias: 'widget.AscDesktopHeader',
    // 设置引用类
	requires: ['Asc.framework.desktop.view.Navigator',
	           'Asc.framework.desktop.view.UserPanel',
	           'Asc.framework.desktop.view.Title'],
	       	
	bodyCls : 'asc-desktop-header-body',
    
	autoScroll : true,
	
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
		xtype : 'AscDesktopTitle'
	}, {
		// 应用程序导航
		flex: 5,
		xtype : 'AscDesktopNavigator'
	}, {
		// 用户信息
		xtype : 'AscDesktopUserPanel'
	}]
});