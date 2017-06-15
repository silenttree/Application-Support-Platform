<%@page contentType="text/html;charset=UTF-8"%>

	panel.addEvents('selectionchange');
	var cdrStore = Ext.create('Ext.data.TreeStore', {
		folderSort: true,
		fields: [{name: 'text', type: 'string'}, 
		         {name: 'type', type: 'string'}, 
		         {name: 'typeDesc', type: 'string'},
		         {name: 'key', type: 'string'}, 
		         {name: 'note', type: 'string'}],
		root: {
			id: '0',
			type : 'root',
			iconCls : '',
			key : '0',
			text : '选择范围',
			expanded : true
		},
		sorters: [{
	        sorterFn: function(o1, o2){
	            type1 = (o1.get('key').slice(0, 1) === 'C') ? 4 :
	            			(o1.get('key').slice(0, 1) === 'D') ? 3 :
	            				(o1.get('key').slice(0, 1) === 'R') ? 2 : 1;
	            type2 = (o2.get('key').slice(0, 1) === 'C') ? 4 :
	            			(o2.get('key').slice(0, 1) === 'D') ? 3 :
	            				(o2.get('key').slice(0, 1) === 'R') ? 2 : 1;
	            return type2 - type1;
	        }
	    }, {
			property: 'text',
			direction: 'ASC'
		}],
		
		proxy: {
			type: 'direct',
			directFn: CDRUDirect.loadRangeTree,
			extraParams : {
				range: wrapPanel.range,
				key: wrapPanel.selectType
			},
			reader: {
				type: 'json',
				root : 'datas',
				totalProperty : 'totals',
				successProperty : 'successed',
				messageProperty : 'message'
			}
		},
		listeners: {
			'beforeload' : function(store, operation, eOpts){
				operation.params.range = wrapPanel.range;
				operation.params.selectType = wrapPanel.selectType;
				operation.params.key = operation.node.get('key');
			}
		}
	});
	
	var cdrTree = Ext.create('Ext.tree.Panel', {
		store: cdrStore,
		border: false,
		region: 'west',
		split: true,
		rootVisible: false,
		width: 200,
		tbar: ['->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				cdrTree.getStore().reload();
				// TODO me.clearToSelect();
			}
		}]
	});

	// 选中事件
	cdrTree.on('selectionchange', function (tree, selected, opt) {
		console.log('selectionchange');
		console.log(selected);
		panel.fireEvent('selectionchange', tree, selected, opt);
	});
	
	panel.add(cdrTree);
