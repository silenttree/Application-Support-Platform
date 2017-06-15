// 设计工具导航
Ext.define('Asc.framework.desktop.view.Navigator', {
	
	extend: 'Ext.tree.Panel',
	
	alias: 'widget.AscDesktopNavigator',
	
	requires: [],
	
	autoScroll : true,
	
	store : 'NavigatorStore',
	
	listeners : {
		render : function(){
			// 选中根节点
			this.getSelectionModel().select(this.getStore().getRootNode());
		}
	}
});