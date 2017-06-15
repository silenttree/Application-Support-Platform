Ext.define('Asc.framework.dictionary.controller.DictionaryManager', {
	// 指定基类
	extend : 'Ext.app.Controller',
	// 设置别名
	alias : 'widget.AscDictionaryManager',
	// 设置引用类
	requires : ['Asc.framework.dictionary.model.StaticDictionaryData',
	            'Asc.framework.dictionary.store.StaticDictionaryStore',
	            'Asc.framework.dictionary.model.DictionaryData',
	            'Asc.framework.dictionary.store.DictionaryStore'],
	// 名称空间
	$namespace : 'Asc.framework.dictionary',
	// 静态字典存储器
	sDicStores : {},
	// 字典存储器
	dicStores : {},
	
	init : function() {
		Asc.common.Message.log('Dictionary Manager is loaded & initialise ', this, 'log');
	},
	// 获得动态字典Store
	getDictionaryStore : function(appKey, dictionaryKey, cfg){
		if(Ext.isDefined(this.sDicStores[dictionaryKey])){
			return this.dicStores[appKey.dictionaryKey];
		}else{
			var store = new Asc.framework.dictionary.store.DictionaryStore(appKey, dictionaryKey, cfg);
			this.dicStores[appKey.dictionaryKey] = store;
			store.load();
			return store;
		}
	},
	// 获得静态字典Store
	getStaticDictionaryStore : function(appKey, dictionaryKey, cfg){
		var dicId = appKey + '.' + dictionaryKey;
		if(Ext.isDefined(this.sDicStores[dicId])){
			return this.sDicStores[dicId];
		}else{
			var store = new Asc.framework.dictionary.store.StaticDictionaryStore(appKey, dictionaryKey, cfg);
			this.sDicStores[dicId] = store;
			store.load();
			return store;
		}
	},
	getDictionaryRenderer : function(appKey, dictionaryKey){
		var dicId = appKey + '.' + dictionaryKey;
		var store = this.getDictionaryStore(appKey, dictionaryKey);
		if(Ext.isDefined(store)){
			return store.renderData.bind(store);
		}else{
			return Ext.emptyFn;
		}
	},
	getStaticDictionaryRenderer : function(appKey, dictionaryKey){
		var dicId = appKey + '.' + dictionaryKey;
		var store = this.getStaticDictionaryStore(appKey, dictionaryKey);
		if(Ext.isDefined(store)){
			return store.renderData.bind(store);
		}else{
			return Ext.emptyFn;
		}
	}
});