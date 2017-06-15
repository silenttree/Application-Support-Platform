Ext.define('Asc.framework.desktop.store.Navigators', {
	
	extend: 'Ext.data.Store',

	model : 'Asc.framework.desktop.model.Navigator',
	
	proxy : {
		type : 'direct',
		directFn : PortalAppDirect.loadPortalNavigators,
		reader : {
			type: 'json',
			root : 'datas'
		}
	},
	
	remoteFilter : true,
	
	listeners : {
		'load' : function(store, records, successful, eOpts){
			Asc.common.Message.log('Portal navigators is loaded, successful : [' + successful + '];record count : ' + records.length, this, 'log');
		}
	}
});
