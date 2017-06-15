Ext.define('Asc.framework.application.store.Applications', {
	
	extend: 'Ext.data.Store',

	model : 'Asc.framework.application.model.Application',
	
	proxy : {
		type : 'direct',
		directFn : PortalAppDirect.loadApplications,
		reader : {
			type: 'json',
			root : 'datas'
		}
	},
	
	remoteFilter : true,

	autoLoad : true
});
