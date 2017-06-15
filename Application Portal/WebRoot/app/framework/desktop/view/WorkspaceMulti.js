// 页面下部的页脚区界面
Ext.define('Asc.framework.desktop.view.WorkspaceMulti', {
	
	extend: 'Ext.tab.Panel',
	
	alias: 'widget.AscDesktopWorkspaceMulti',
	
	requires: ['Asc.framework.desktop.view.Portal'],
	
	border : true,
	
	padding : '0 5 0 5',
	
	activeTab: 0,

	layout : 'fit',
	
	items : [{
		xtype : 'AscDesktopPortal',
		iconCls : 'icon-portal-desktop',
		closable : false,
		title : '桌面'
	}],
	
	// 装载门户桌面
	loadPortal : function(){
		this.setActiveTab(0);
	},
	// 装载模块
	loadCmp : function(cmp){
		this.add(cmp);
		this.doLayout();
	},
	// 激活界面
	activeCmp : function(cmp){
		this.setActiveTab(cmp);
	}
});