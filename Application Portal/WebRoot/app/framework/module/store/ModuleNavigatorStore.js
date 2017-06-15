Ext.define('Asc.framework.module.store.ModuleNavigatorStore', {
	
	extend: 'Ext.data.TreeStore',

	fields: [{name: 'text', type: 'string'},
	         {name: 'type', type: 'string'}, 
	         {name: 'key', type: 'string'}, 
	         {name: 'pageMenu', type: 'string'}, 
	         {name: 'isDefault', type: 'boolean'}, 
	         {name: 'params', type: 'params'}],
	// 重载构造函数
	constructor : function(appKey, moduleId){
		var appManager = AscApp.getController('AscAppManager');
		this.proxy = {
			type : 'direct',
			directFn : appManager.getEngineDirectFn(appKey, 'loadModuleNavigator'),
			extraParams : {
				moduleId : moduleId
			},
			reader : {
				type: 'json',
				root : 'datas',
				messageProperty : 'message'
			}
		};
		this.callParent();
	},
	// 加载事件
	listeners : {
		'beforeload' : {
			fn : function(store, operation, eOpts){
				operation.params.type = operation.node.get('type');
				operation.params.key = operation.node.get('key');
				operation.params.params = store.getNodeParams(operation.node);
			}
		}
	},
	// 获得节点参数（递归实现）
	getNodeParams : function(node){
    	var params = {};
    	if(Ext.isDefined(node) && node != null){
    		if(node != this.getRootNode()){
        		Ext.apply(params, this.getNodeParams(node.parentNode));
    		}
    		Ext.apply(params, node.get('params'));
    	}
    	return params;
    }
});
