Ext.define('Asc.framework.system.store.PreferencesTreeStore', {
	
	extend: 'Ext.data.TreeStore',

	fields: [{name: 'text', type: 'string'}, {name: 'url', type: 'string'}],
	
	proxy : {
		type : 'ajax',
		url : 'page.do?url=framework/system/preferences.tree'
	}
});
