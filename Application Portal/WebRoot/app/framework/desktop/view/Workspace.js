// 页面下部的页脚区界面
Ext.define('Asc.framework.desktop.view.Workspace', {
	
	extend: 'Ext.panel.Panel',
	
	alias: 'widget.AscDesktopWorkspace',
	
	requires: ['Asc.framework.desktop.view.Portal'],
	
	border : true,
	
	padding : '0 5 0 5',

	layout : 'fit',
	
	items : [{
		xtype : 'AscDesktopPortal'
	}],
	
	// 装载门户桌面
	loadPortal : function(){
		this.removeAll();
		var portal = Ext.create('Asc.framework.desktop.view.Portal');
		this.add(portal);
		this.doLayout();
		AscApp.initPreferences(AscApp.preferences);
	},
	// 装载界面
	loadCmp : function(cmp){
		this.removeAll();
		cmp.closable = false;
		cmp.header = false;
		this.add(cmp);
		this.doLayout();
	},
	// 激活界面
	activeCmp : function(cmp){
		
	}
});