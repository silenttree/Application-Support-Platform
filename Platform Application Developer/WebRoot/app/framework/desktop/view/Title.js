// 页面下部的页脚区界面
Ext.define('Asc.framework.desktop.view.Title', {
	
	extend: 'Ext.panel.Panel',
	
	alias: 'widget.AscDesktopTitle',
	
	bodyPadding : '4 80 0 10',
	
	cls : 'asc-desktop-header-body',
	
	bodyCls : 'asc-desktop-title-body',
	
	data : {
		title : '应用模块快速开发工具'
	},
	
	tpl : '{title}'
});