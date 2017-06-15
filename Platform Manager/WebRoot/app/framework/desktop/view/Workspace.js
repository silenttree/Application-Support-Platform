// 页面下部的页脚区界面
Ext.define('Asc.framework.desktop.view.Workspace', {
	
	extend: 'Ext.tab.Panel',
	
	alias: 'widget.AscDesktopWorkspace',
	
	requires: [],
	
	border : false,
	
	defaults : {
		closable : true
	},
	
	enableTabScroll: true,
	
	items : [{
		title : '首页',
		border : false,
		closable : false
	}]
});