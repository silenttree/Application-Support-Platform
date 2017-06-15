Ext.define('Asc.framework.desktop.store.NavigatorStore', {
	
	extend: 'Ext.data.TreeStore',

	fields: [{name: 'text', type: 'string'}, 
	         {name: 'type', type: 'string'}, 
	         {name: 'appId', type: 'string'}, 
	         {name: 'key', type: 'string'}, 
	         {name: 'url', type: 'string'}],
	
	root : {
		type : 'root',
		iconCls : 'icon-designer-root',
		key : 'root',
		text : '功能导航',
		expanded : true
	},
	
	proxy : {
		type : 'direct',
		directFn : DeveloperAppDirect.loadNavigators,
		extraParams : {
			type : '',
			key : ''
		},
		reader : {
			type: 'json',
			root : 'datas',
			messageProperty : 'message'
		}
	},
	listeners : {
		'beforeload' : function(store, operation, eOpts){
			operation.params.type = operation.node.get('type');
			operation.params.appId = operation.node.get('appId');
			operation.params.key = operation.node.get('key');
		}
	}
});
