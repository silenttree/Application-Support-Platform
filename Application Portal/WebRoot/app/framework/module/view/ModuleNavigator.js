Ext.define('Asc.framework.module.view.ModuleNavigator', {
	
	extend: 'Ext.tree.Panel',
	
	alias: 'widget.AscModuleNavigator',
	
	requires: ['Asc.framework.module.store.ModuleNavigatorStore'],
	
	autoScroll : true,
	
	//bodyPadding : '0 5 0 5',
	
	initComponent: function() {
		var me = this;
		var moduleManager = AscApp.getController('AscModuleManager');
		var module = moduleManager.getModuleData(this.appKey, this.moduleId);
		var icon = module.iconCls || 'icon-app-' + this.appKey.toLowerCase() + '-' + this.moduleId.toLowerCase();
		me.root = {
			iconCls : icon,
			type : 'None',
			key : this.moduleId,
			text : '功能导航',
			expanded : true
		}
		me.store = new Asc.framework.module.store.ModuleNavigatorStore(this.appKey, this.moduleId);
		me.tbar = [{
			text : module.title,
			iconCls : icon
		},'->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				var nodes = this.getSelectionModel().getSelection();
				if(nodes.length > 0){
					this.getStore().load({
						node : nodes[0]
					});
				}
			},
			scope : me
		}]
		me.callParent();
	}
});