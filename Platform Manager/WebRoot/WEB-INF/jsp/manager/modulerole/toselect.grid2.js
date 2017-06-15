<%@page contentType="text/html;charset=UTF-8"%>
	panel.addEvents('selectionchange');
	var toSelectStore = new Ext.data.Store({
		clearRemovedOnLoad : true,
		proxy : {
			type : 'direct',
			directFn : CDRUDirect.loadToSelectDatas,
			paramOrder : ['key','selectType','range'],
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
		}],
		sorters : [{
	        sorterFn: function(o1, o2){
	        	var selectedRange = wrapPanel.getSelectRange();
	        	if(selectedRange) {
	        		if(o1.get('key') === selectedRange) {
	        			return -1;
	        		} else if(o2.get('key') === selectedRange) {
	        			return 1;
	        		}
	        	}
	            type1 = (o1.get('key').slice(0, 1) === 'C') ? 4 :
	            			(o1.get('key').slice(0, 1) === 'D') ? 3 :
	            				(o1.get('key').slice(0, 1) === 'R') ? 2 : 1;
	            type2 = (o2.get('key').slice(0, 1) === 'C') ? 4 :
	            			(o2.get('key').slice(0, 1) === 'D') ? 3 :
	            				(o2.get('key').slice(0, 1) === 'R') ? 2 : 1;
	            return type2 - type1;
	        }
	    }, {
			property : 'text',
			direction: 'ASC'
		}],
		listeners : {
			'beforeload' : function(s, operation, eOpts){
				// TODO 
			}
		}
	});
	
	var searchTextField = new Ext.form.TextField({
		width : 130,
		disabled : false,
		emptyText : '输入查询关键字按回车',
		enableKeyEvents : true,
		listeners : {
			specialkey : function (field, e) {
				if (e.getKey() === e.ENTER) {
					// me.searchToSelect();
					this.focus(true);
				}
			}
		}
	});
	
	var toSelectGrid = Ext.create('Ext.grid.Panel', {
		// title: '备选列表',
		store: toSelectStore,
		region: 'center',
		//flex: 60,
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
		tbar: [searchTextField,'->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				toSelectGrid.getStore().reload();
			}
		}],
		listeners: {
			selectionchange : function(grid, selected, opt) {
				panel.fireEvent('selectionchange', grid, selected, opt);
			}
		}
	});
	
	
	// 清空选项
	panel.clear = function() {
		toSelectStore.removeAll();
	};
	// 重新加载
	panel.load = function(key) {
		console.log({
			key: key,
			selectType: wrapPanel.selectType,
			range: wrapPanel.range
		});
		toSelectStore.load({
			params: {
				key: key,
				selectType: wrapPanel.selectType,
				range: wrapPanel.range
			}
		});
	};
	panel.add(toSelectGrid);