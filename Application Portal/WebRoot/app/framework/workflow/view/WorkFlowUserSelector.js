Ext.define('Asc.framework.workflow.view.WorkFlowUserSelector', {
	
	extend : 'Ext.panel.Panel',
	alias : 'widget.workflowuserselector',
	/**
	 * @cfg {string} 智能流转备选的用户表达式字符串,中间用分号分割，如'U_1;U_15;U_16;U_17'
	 */
	intelligentUsers : '',
	
	/**
	 * @cfg {string} 已选中的用户表达式字符串,中间用分号分割，如'U_1'
	 */
	selectedUsers : '',

	/**
	 * @cfg {boolean} 选择多个，但是只有一个人进行办理
	 */
	candidate : false,
	
	/**
	 * @cfg {boolean} 是否单选
	 */
	singleSelect : false,
	
	layout : 'border',
	
	initComponent : function() {
		var me = this,
			userTreeStore, //备选的
			userTree,	//用户树
			selectedStore,
			selectedGrid;
		
		userTreeStore = Ext.create('Ext.data.TreeStore', {
			folderSort : true,
			fields: [{name : 'text', type : 'string'}, 
			         {name : 'type', type : 'string'}, 
			         {name : 'typeDesc', type : 'string'},
			         {name : 'key', type : 'string'}, 
			         {name : 'note', type : 'string'}],
			root: {
				id : '0',
				type : 'root',
				iconCls : '',
				key : '0',
				text : '选择范围',
				expanded : true,
				children : [{
					id : 'I_0',
					type : 'I',
					iconCls : '',
					key : 'I_0',
					text : '智能流转',
					expanded : true,
					children : []
				}, {
					id : 'F_0',
					type : 'F',
					iconCls : '',
					key : 'F_0',
					text : '自由流转',
					expanded : true,
					children : []
				}]
			},
			sorters: [{
		        sorterFn: function(o1, o2){
		        	type1 = (o1.get('key').slice(0, 1) === 'I') ? 5 :
		            			(o1.get('key').slice(0, 1) === 'F') ? 4 :
		            				(o1.get('key').slice(0, 1) === 'C') ? 3 :
		            					(o1.get('key').slice(0, 1) === 'D') ? 2 : 1;
		            type2 = (o2.get('key').slice(0, 1) === 'I') ? 5 :
		            			(o2.get('key').slice(0, 1) === 'F') ? 4 :
		            				(o2.get('key').slice(0, 1) === 'C') ? 3 :
		            					(o2.get('key').slice(0, 1) === 'D') ? 2 : 1;
		            return type2 - type1;
		        }
		    }, {
				property: 'text',
				direction: 'ASC'
			}]
		});
		
		userTree = Ext.create('Ext.tree.Panel', {
			title : '可选办理人',
			store : userTreeStore,
			border : false,
			region : 'center',
			rootVisible : false
		});
		
		var userTreeMask = new Ext.LoadMask(userTree, {msg:"加载中"});
		
		selectedStore = Ext.create('Ext.data.Store', {
			folderSort : true,
			fields: [{name : 'text', type : 'string'},
			         {name : 'key', type : 'string'}]
		});
		
		selectedGrid = Ext.create('Ext.grid.Panel', {
		    title : '已选办理人' + (me.candidate ? '(候选)' : ''),
		    border : false,
		    width : 200,
			split : true,
		    store : selectedStore,
		    hideHeaders : true,
		    region : 'east',
		    columns : [
		        {xtype : 'rownumberer'},
		        { text: '已选用户', dataIndex : 'text', flex : 1}
		    ]
		});
		
		//做测试的时候用的
//		selectedGrid.on('selectionchange', function() {
//			console.log(me.getValue());
//		});
		
		var selectedGridTreeMask = new Ext.LoadMask(selectedGrid, {msg:"加载中"});
		WorkFlowUserSelectorDirect.getUsersByIds(me.selectedUsers, function(result, e){
			if(result && result.success && result.data && result.data.length !== 0){
				selectedStore.add(result.data);
			}
		});
		
		//加载智能流转的数据
		var loadIntelligentUsers = function() {
			WorkFlowUserSelectorDirect.getUsersByIds(me.intelligentUsers, function(result, e){
				if(result && result.success && result.data && result.data.length !== 0){
					var node = userTreeStore.getNodeById('I_0');
					node.appendChild(result.data);
				}
			});
		};
		
		//加载用户的单位
		var loadUserCompany = function() {
			WorkFlowUserSelectorDirect.getDefaultCompany(function(result, e){
				if(result && result.success && result.data){
					var node = userTreeStore.getNodeById('F_0');
					node.appendChild(result.data);
				}
			});
		};
		
		loadIntelligentUsers();
		loadUserCompany();
		
		selectedGrid.on('itemdblclick', function(grid, record){
			deleteUser(record);
		});
		
		userTreeStore.on('beforeexpand', function(node, e) {
			expandNode(node);
		});
		
		userTree.on('itemdblclick', function(tree, record) {
			selectUser(record);
		});
		
		var deleteUser = function(node) {
			selectedStore.remove(node);
			selectedGrid.getView().refresh();
		};
		
		var selectUser = function(node) {
			if(me.singleSelect) {
				selectedStore.removeAll();
			}
			if((node.get('key').indexOf('U_') === 0)) {
				if(selectedStore.findExact('key', node.get('key')) === -1){
					selectedStore.add({key : node.get('key'), text : node.get('text')});
				}
			}
		};
		
		// 展开单位节点
		var expandNode = function(node) {
			//展开的是单位或者机构
			if((node.get('key').indexOf('C_') === 0) || (node.get('key').indexOf('D_') === 0)) {
				userTreeMask.show();
				node.removeAll();
				WorkFlowUserSelectorDirect.loadToselectTreeNodes({id : node.get('key').substr(2)}, function(result, e){
					if(result && result.success && result.data && result.data.length !== 0){
						node.appendChild(result.data);
					}
					userTreeMask.hide();
				});
			} else if(node.get('id') === 'F_0' && !node.hasChildNodes()) {
				loadUserCompany();
			}
		};
		
		
		me.items = [userTree, selectedGrid];
		
		me.selectedStore = selectedStore;
		// 调用父类的initComponent方法
		me.callParent();
	},
	
	getValue : function() {
		var me = this;
		var val = '';
		me.selectedStore.each(function(record){
			val = val + record.get('key') + ';';
		});
		return val;
	}
});