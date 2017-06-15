Ext.define('Asc.framework.page.view.View', {
	// 指定基类
	extend : 'Ext.grid.Panel',
	// 设置别名
	alias : 'widget.AscPageView',
	// 设置引用类
	requires : ['Asc.extension.PagingToolbar',
	            'Asc.framework.page.view.QueryPlugin'],
    
	autoScroll : true,
	
	isAutoLoad : true,
	
	border : false,
	// 应用标识
	appKey : undefined,
	// 使用代理
	moduleId : undefined,
	// 视图key
	pageId : undefined,
	// 页面参数
	initParams : undefined,
	// 重载构造函数
	constructor : function(appKey, moduleId, pageData, params, context, cfg){
		var me = this;
		me.appKey = appKey;
		me.moduleId = moduleId;
		me.pageId = pageData.id;
		var config = {};
		// 初始化基础属性
		config.title = pageData.title;
		config.iconCls = pageData.iconCls;
		if(!params){
			params = {};
		}
		config.initParams = Ext.apply(params, pageData.params);
		// 初始化store
		var appManager = AscApp.getController('AscAppManager');
		var getViewResultsFn = appManager.getEngineDirectFn(appKey, 'getViewResults');;
		me.store = Ext.create("Ext.data.Store", {
			clearRemovedOnLoad : true,
			pageSize : pageData.pageSize,
			remoteSort : true,
			proxy : {
				type : 'direct',
				directFn : getViewResultsFn,
				extraParams : {
					moduleId : moduleId,
					pageId : pageData.id,
					params : config.initParams,
					querys : {}
				},
				paramOrder : 'moduleId pageId limit page sort params querys',
				paramsAsHash : true,
				reader : {
					type: 'array',
					root : 'datas',
					totalProperty : 'total',
					messageProperty : 'message'
				},
				simpleSortMode : false
			},
			fields : pageData.fields
		});
		// 初始化columns
		config.columns = this.prepareColumns(pageData.columns);
		// 初始化selmodel
		config.selType = pageData.selType || 'rowmodel';
		config.selModel = pageData.selModel;
		// 初始化viewConfig
		if(Ext.isDefined(pageData.getRowClass)){
			try{
				eval('var fn = ' + pageData.getRowClass);
				if(Ext.isFunction(fn)){
					config.viewConfig = {
						getRowClass : fn
					}
				}
			}catch(e){
				console.error(e.message);
			}
		}
		// 初始化buttons(工具条)
		config.tbar = pageData.buttons;
		// 初始化contextmenu
		this.contextmenu = Ext.create('Ext.menu.Menu', pageData.contextmenu);
		this.contextmenu.grid = this;
		// 初始化plugins
		//1、editable
		//2、orderable
		//3、queryable
		if(pageData.enableVagueQuery || pageData.enableExactQuery){
			config.plugins = [Ext.create('Asc.framework.page.view.QueryPlugin', {
				enableVagueQuery : pageData.enableVagueQuery,
				enableExactQuery : pageData.enableExactQuery,
				queryInputs : this.prepareQueryInputs(pageData.queryInputs)
			})];
		}
		//4、customable
		//5、pagingbar
		if(pageData.pageSize > 0){
			config.dockedItems = [{
		        xtype: 'AscPagingToolbar',
		        store: me.store,
		        dock: 'bottom',
		        displayInfo: true
		    }];
		}
		
		Ext.apply(config, pageData.cfg);
		Ext.apply(config, cfg);
        me.callParent([config]);
        
	},
	// 处理列对象
	prepareColumns : function(cols){
		var columns = [];
		var dictionaryManager = AscApp.getController('AscDictionaryManager');
		for(n in cols){
			var col = cols[n];
			var column = Ext.apply({}, col);
			if(Ext.isDefined(col.renderer)){
				// 自定义渲染函数
				try{
					eval('var fn = ' + col.renderer);
				}catch(e){
					
				}
				if(Ext.isFunction(fn)){
					column.renderer = fn;
				}
			}else if(Ext.isDefined(col.dictionary)){
				// 字典渲染函数
				column.renderer = dictionaryManager.getDictionaryRenderer(this.appKey, col.dictionary);
			}else if(Ext.isDefined(col.sdictionary)){
				// 静态字典渲染函数
				column.renderer = dictionaryManager.getStaticDictionaryRenderer(this.appKey, col.sdictionary);
			}
			columns.push(column);
		}
		return columns;
	},
	// 处理列对象
	prepareQueryInputs : function(queryInputs){
		var inputs = [];
		var dictionaryManager = AscApp.getController('AscDictionaryManager');
		for(n in queryInputs){
			var qi = queryInputs[n];
			var input = Ext.apply({}, qi);
			if(Ext.isDefined(qi.dictionary)){
				Ext.apply(input, {
					xtype : 'combo',
					valueField : 'key',
					displayField : 'value'
				});
				// 字典渲染函数
				input.store = dictionaryManager.getDictionaryStore(this.appKey, qi.dictionary);
			}else if(Ext.isDefined(qi.sdictionary)){
				Ext.apply(input, {
					xtype : 'combo',
					valueField : 'key',
					displayField : 'value'
				});
				// 静态字典渲染函数
				input.xtype = 'combo';
				input.store = dictionaryManager.getStaticDictionaryStore(this.appKey, qi.sdictionary);
			}
			inputs.push(input);
		}
		return inputs;
	},
	// 对象销毁 
	onDestroy : function(){
		// 销毁右键菜单对象
		Ext.destroy(this.contextmenu);
		this.callParent();
	},
	// 执行刷新
	doRefresh : function(params){
		// 判断装载条件
		if(this.isAutoLoad || params || this.isLoaded){
			if(!Ext.isDefined(params)){
				params = Ext.apply({}, this.store.getProxy().extraParams.params);
			}
			var pb = this.getDockedComponent(1);
			if(Ext.isDefined(pb) && pb.getXType() == 'AscPagingToolbar'){
				if(pb.hasParams()){
					this.store.currentPage = 1;
				}
				pb.setParams();
			}
			// 刷新设置持久参数
			Ext.apply(this.store.getProxy().extraParams, {params : params});
			this.store.load();
			this.isLoaded = true;
		}
	},
	// 执行检索
	doQuery : function(queryParams){
		var pb = this.getDockedComponent(1);
		if(Ext.isDefined(pb) && pb.getXType() == 'AscPagingToolbar'){
			pb.setParams({
				querys : queryParams
			});
		}
		this.store.currentPage = 1;
		// 查询使用临时参数
		this.store.load({
			params : {
				querys : queryParams
			}
		});
		this.isLoaded = true;
	}
});