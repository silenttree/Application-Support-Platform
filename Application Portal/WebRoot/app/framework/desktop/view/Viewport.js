// 桌面页面布局定义
Ext.define('Asc.framework.desktop.view.Viewport', {
	
	extend: 'Ext.container.Viewport',
	
	alias: 'widget.AscDesktopViewport',
    // 设置引用类
	requires: ['Asc.framework.desktop.view.Header', 
	           'Asc.framework.desktop.view.Workspace', 
	           'Asc.framework.desktop.view.WorkspaceMulti', 
	           'Asc.framework.desktop.view.Footer'],
	
	layout : 'border',
	
	border : false,
	
	bodyCls : 'asc-desktop-header-body',
	
	defaults : {
		border : false
	},
	
	initComponent : function(){
		this.items = [{
			// 标题区
			region : 'north',
			height : 36,
			xtype : 'AscDesktopHeader'
		}, {
			// 工作区
			region : 'center',
			border : true,
			xtype : AscApp.getAscDesktop().wsMode
		}, {
			// 版权提示区
			region : 'south',
			height : 23,
			xtype : 'AscDesktopFooter'
		}];
		this.callParent();
	},
	
	getWorkspace : function(){
		return this.down('[region=center]');
	}
});