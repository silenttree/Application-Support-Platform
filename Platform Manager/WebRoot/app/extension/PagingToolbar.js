Ext.define('Asc.extension.PagingToolbar', {
	// 指定基类
	extend : 'Ext.toolbar.Paging',
	// 设置别名
	alias : 'widget.AscPagingToolbar',
	
	beforePageText : '第',
	
	afterPageText : '页，共 {0} 页',
	
	displayMsg : '显示第 {0} 到 {1} 条，共 {2} 条', //只要当displayInfo为true时才有效，用来显示有数据时的提示信息，{0},{1},{2}会自动被替换成对应的数据  
	
	emptyMsg : '没有匹配的数据',//没有数据时显示信息  
	
	initComponent : function(){
		var me = this;
		me.items = ['每页显示', {
			xtype: 'combo',
			store : {
				fields : ['size', 'text'],
				data:{'items':[
					{ 'size': 10,  "text":"10"},
					{ 'size': 20,  "text":"20"},
					{ 'size': 30,  "text":"30"},
					{ 'size': 50,  "text":"50"},
					{ 'size': 100,  "text":"100"},
					{ 'size': 200,  "text":"200"},
					{ 'size': 500,  "text":"500"},
					{ 'size': 0,  "text":"全部"}
				]},
				proxy: {
					type: 'memory',
					reader: {
						type: 'json',
						root: 'items'
					}
				}
			},
			//store : [10, 20, 30, 50, 100, 200, 500],
			width : 50,
			editable : false,
			displayField : 'text',
			valueField : 'size',
			value : this.store.pageSize,
			itemId: 'pageSelector',
			listeners : {
				'select' : function(){
					me.store.currentPage = 1;
					me.store.pageSize = this.getValue();
					me.store.load();
				}
			}
		}, '条'];
		me.store.on('beforeload', function(s, op){
			if(!Ext.isDefined(op.params) || op.params == null){
				op.params = Ext.apply({}, me.params);
			}else{
				Ext.apply(op.params, me.params);
			}
		});
		me.callParent();
	},
	setParams : function(params){
		this.params = params;
	},
	hasParams : function(){
		return Ext.isDefined(this.params);
	},
	// 重载页面数据解析
    getPageData : function(){
        var store = this.store,
            totalCount = store.getTotalCount();
        var size =  store.pageSize == 0 ? totalCount : store.pageSize;
        return {
            total : totalCount,
            currentPage : store.currentPage,
            pageCount: Math.ceil(totalCount / size),
            fromRecord: ((store.currentPage - 1) * size) + 1,
            toRecord: Math.min(store.currentPage * size, totalCount)
        };
    }
});