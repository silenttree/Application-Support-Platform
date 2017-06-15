Ext.define('Asc.framework.system.view.Preferences', {
	// 指定基类
	extend : 'Ext.panel.Panel',
	// 设置别名
	alias : 'widget.AscPreferences',
	
	border : false,
	
	layout : 'border',
	
	items : [{
		region : 'west',
		xtype : 'AscPreferencesTree',
		split : true,
		width : 180
	}, {
		region : 'center',
		disabled : true,
		xtype : 'AscPreferencesEditor'
	}]
});