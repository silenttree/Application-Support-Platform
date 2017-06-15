<%@page contentType="text/html;charset=UTF-8"%>
panel.addEvents("selectionchange");
var relativeStore = new Ext.data.Store({
	clearRemovedOnLoad : true,
	proxy : {
		type : 'direct',
		directFn : CDRUDirect.loadRelativeDatas,
		paramOrder : ['key'],
		paramsAsHash : true,
		reader : {
			type: 'json',
			root : 'datas',
			totalProperty : 'totals',
			successProperty : 'successed',
			messageProperty : 'message'
		}
	},
	fields: [{
		name: 'text', 
		type: 'string'
	}, {name: 'type', 
		type: 'string'
	}, {name: 'typeDesc', 
		type: 'string'
	}, {name: 'key', 
		type: 'string'
	}, {name: 'note', 
		type: 'string'
	}, {name: 'id', 
		type: 'string'
	}, {name: 'iconCls', 
		type: 'string'
	}, {name: 'order', 
		type: 'int'
	}],
	sorters : [{
        sorterFn: function(o1, o2){
            type1 = (o1.get('key').slice(0, 1) === 'U') ? 4 :
            			(o1.get('key').slice(0, 1) === 'R') ? 3 :
            				(o1.get('key').slice(0, 1) === 'D') ? 2 : 1;
            type2 = (o2.get('key').slice(0, 1) === 'U') ? 4 :
            			(o2.get('key').slice(0, 1) === 'R') ? 3 :
            				(o2.get('key').slice(0, 1) === 'D') ? 2 : 1;
            if(type1 != type2) {
            	return type2 - type1;
            } else {
            	return o1.get('order') - o2.get('order');
            }
            
        }
    }, {
		property : 'text',
		direction: 'ASC'
	}],
	listeners: {
		load: function(store, records, successful) {
			if(successful) {
				store.insert(0, {
					text: '查看所有',
					type: 'ALL',
					typeDesc: '查看',
					key: 'ALL',
					note: '不能维护',
					id: 'ALL_' + wrapPanel.getRelativeKey()
				});
			}
		}
	}
});

var relativeGrid = Ext.create('Ext.grid.Panel', {
	store: relativeStore,
	region: 'center',
	border: false,
	columns: [{
		text: '',
		dataIndex: 'iconCls',
		width: 23,
		resizable: false,
		renderer: function(value, metaData, record, row, col, store, gridView){
			return '<div class="' + value 
				+ ' asc-cdruselector-view-icon-div"></div>';
		}
	},{
		text: '类型',
		width: 45,
		dataIndex: 'typeDesc'
	},{
		text: '名称',
		dataIndex: 'text',
		filterable: true
	}, {
		text: '备注',
		dataIndex: 'note',
		flex: 1
	}],
	tbar: ['->', {
		text : '刷新',
		iconCls : 'icon-sys-refresh',
		handler : function(){
			relativeGrid.getStore().reload();
		}
	}],
	listeners: {
		selectionchange : function(grid, selected, opt) {
			panel.fireEvent('selectionchange',  grid, selected, opt);
		}
	}
});

// 清空选项
panel.clear = function() {
	relativeStore.removeAll();
};
// 重新加载
panel.load = function(key) {
	relativeStore.load({
		params: {
			key: key
		}
	});
};
panel.add(relativeGrid);