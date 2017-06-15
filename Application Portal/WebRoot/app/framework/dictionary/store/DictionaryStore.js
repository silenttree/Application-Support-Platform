Ext.define('Asc.framework.dictionary.store.DictionaryStore', {
	
	extend: 'Ext.data.Store',

	model : 'Asc.framework.dictionary.model.DictionaryData',
	
	appKey : undefined,
	
	dictionaryKey : undefined,
	
	remoteFilter : true,
	
	constructor : function(appKey, dictionaryKey, cfg){
		this.appKey = appKey;
		this.dictionaryKey = dictionaryKey;
		// 调用Direct方法请求静态字典数据
		var appManager = AscApp.getController('AscAppManager');
		var getDictionaryDatasFn = appManager.getEngineDirectFn(appKey, 'getDictionaryDatas');
		this.proxy = {
			type : 'direct',
			directFn : getDictionaryDatasFn,
			extraParams : {
				dictionaryKey : dictionaryKey
			},
			paramOrder : 'dictionaryKey',
			reader : {
				type: 'json',
				root : 'datas'
			}
		}
		Ext.apply(this, cfg);
		this.callParent();
	},
	renderData : function(v){
		var index = this.findExact('key', v + '');
		if(index > -1){
			return this.getAt(index).get('value');
		}
		return v;
	}
});
