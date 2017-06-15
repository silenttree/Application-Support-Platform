	Ext.define('Asc.extension.editor.CdruSelectorPanel',{
		extend: 'Ext.panel.Panel',
		alias: 'widget.cdruselectorpanel',
		
	    /**
		 * @cfg {string} 单位范围表达式 空表示全局范围
		 */
		range: '',
		
		/**
		 * @cfg {string} 选择类型表达式 包含c表示要选单位 包含d表示要选部门 包含r表示要选角色 包含u表示要选用户
		 */
		selectType: 'cdru',
		
		/**
		 * @cfg {boolean} 是否单选
		 */
		singleSelect: false,
		
		/**
		 * @cfg {string} 已选中的数据表达式
		 */
		value: '',
		
		/**
		 * @cfg {function} 回调函数
		 */
		
		callback: Ext.emptyFn,
		
		closeFn: Ext.emptyFn,
			
		border: false,
		
		layout: 'border',
		
		initComponent: function() {
			var me = this,
				cdrTree, // 选择范围树
				toSelectStore, // 备选类表的store
				toSelectGrid, // 备选列表
				selectedStore, // 已选中的dataStore
				selectedView, // 已选中的dataView
				toSelectPanel, // 备选panel，里面放选择范围树和备选列表
				cdrStore, // 选择范围树的Store
				searchTextField, //查询框
				toSelectGridMenu, //备选列表上的右键菜单
				selectedViewMenu, //已选类表上的右键菜单
				itemTpl; // 已选中的viewTpl
			
			toSelectGridMenu = Ext.create('Ext.menu.Menu',{
				items: [{
					text: '选中本条',
					handler: function(){
						me.selectRecord(toSelectGrid.getSelectionModel().getSelection()[0]);
					},
					scope: me
				}, {
					text: '选中所有',
					handler: me.selectAll,
					scope: me
				}]
			});
			
			selectedViewMenu = Ext.create('Ext.menu.Menu',{
				items: [{
					text: '删除选中',
					handler: function(){
						me.removeSelected(selectedView.getSelectionModel().getSelection());
					},
					scope: me
				}, {
					text: '删除所有',
					handler: me.removeAllSelected,
					scope: me
				}]
			});
			
			cdrStore = Ext.create('Ext.data.TreeStore', {
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
						range: me.range,
						key : '0'
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
					'beforeload': function(store, operation, eOpts){
						me.beforeCdrTreeReload(store, operation);
					}
				}
			});
			
			cdrTree = Ext.create('Ext.tree.Panel', {
				// title: '选择范围树',
				store: cdrStore,
				border: false,
				region: 'west',
				split: true,
				rootVisible: false,
				//flex: 40,
				width: 200,
				tbar: ['->', {
					text : '刷新',
					iconCls : 'icon-sys-refresh',
					handler : function(){
						cdrTree.getStore().reload();
						me.clearToSelect();
					}
				}]
			});
			
			cdrTree.on('select', function (tree, record) {
				toSelectStore.reload();
			});
			
			toSelectStore = new Ext.data.Store({
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
						return me.beforeToSelectReload(operation);
					}
				}
			});
			
			searchTextField = new Ext.form.TextField({
				width : 130,
				disabled : false,
				emptyText : '输入查询关键字按回车',
				enableKeyEvents : true,
				listeners : {
					specialkey : function (field, e) {
						if (e.getKey() === e.ENTER) {
							me.searchToSelect();
							this.focus(true);
						}
					}
				}
			});
			
			toSelectGrid = Ext.create('Ext.grid.Panel', {
				// title: '备选列表',
				store: toSelectStore,
				region: 'center',
				// flex: 60,
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
				}]
			});
			
			//双击选中
			toSelectGrid.on('itemdblclick', function (grid, record, item, index, e, eOpts) {
				me.selectRecord(record);
			});
			
			//显示右键菜单
			toSelectGrid.on('itemcontextmenu', function (grid, record, item, index, e, eOpts) {
				toSelectGridMenu.showAt(e.getXY());
			});

			selectedStore = Ext.create('Ext.data.Store', {
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
				sortOnLoad: true,
				autoLoad: true,
				
				sorters : [{
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
					property : 'text',
					direction: 'ASC'
				}],
				proxy : {
					type: 'direct',
					directFn: CDRUDirect.loadSelectedDatas,
					extraParams: {
						'value': me.value
					},
					paramOrder : ['value'],
					paramsAsHash : true,
					reader : {
						type: 'json',
						root : 'datas',
						totalProperty : 'totals',
						successProperty : 'successed',
						messageProperty : 'message'
					}
				},
				listeners : {
					'beforeload' : function(s, operation, eOpts){
						return me.beforeSelectedReload(operation);
					}
				}
			});
			
			itemTpl = new Ext.XTemplate(
					'<span data-qtip="{typeDesc}-{note}" onselectstart="return false" class="asc-cdruselector-view-items">',
						'<div class="{iconCls} asc-cdruselector-view-icon-div"></div>',
						'{text}',
					'</span>'
			);

			selectedView = Ext.create('Ext.view.View', {
				store: selectedStore,
				border: false,
				itemTpl: itemTpl,
				overflowY: 'auto',
				trackOver: true,
				multiSelect: true,
				itemSelector: 'span.asc-cdruselector-view-items',
				selectedItemCls: 'asc-cdruselector-view-items-selected'
			});
			
			selectedView.on('itemdblclick', function (view, record, item, index, e, eOpts) {
				me.removeSelected(record);
			});
			
			selectedView.on('itemcontextmenu', function (grid, record, item, index, e, eOpts) {
				selectedViewMenu.showAt(e.getXY());
			});

			toSelectPanel = Ext.create('Ext.panel.Panel', {
				layout: 'border',
				region: 'center',
				//flex: 60,
				items: [cdrTree, toSelectGrid]
			});
			me.items = [
				toSelectPanel, 
				{
					xtype: 'panel',
					title: '已选列表',
					region: 'south',
					height: 150,
					//flex: 40,
					split: true,
					layout: 'fit',
					items: [selectedView],
					bbar: ['->', {
						text: '确定',
						handler: me.okClick,
						scope: me
					}, '-', {
						text: '取消',
						handler: me.cancelClick,
						scope: me
					}]
				}
			];
			
			me.on('beforedestroy', function(me, opt) {
				Ext.destroy(toSelectGridMenu);
				Ext.destroy(selectedViewMenu);
				return true;
			});
			
			me.searchTextField = searchTextField;
			me.toSelectStore = toSelectStore;
			me.selectedStore = selectedStore;
			me.cdrTree = cdrTree;
			me.toSelectGrid = toSelectGrid;
			// 调用父类的initComponent方法
			me.callParent();
		},
		
		/**
		 * 确定按钮点击事件
		 */
		okClick: function(){
			var me = this,
				selectedItems,
				index,
				value,
				rawValue;
			selectedItems = me.selectedStore.data.items;
			value = '';
			rawValue = '';
			if(selectedItems && selectedItems.length > 0){
				for(index = 0; index < selectedItems.length; index++){
					value += selectedItems[index].get('key') + ';';
					rawValue += selectedItems[index].get('text') + ';';
				}
			}
			me.callback(value,rawValue);
			me.closeFn();
		},
		
		//取消按钮点击事件
		cancelClick: function() {
			var me = this;
			me.closeFn();
		},
		
		//取消选中一条数据
		removeSelected: function(record) {
			this.selectedStore.remove(record);
		},
		
		//选中所有备选类表中的数据
		selectAll: function(){
			var me = this;
			me.toSelectStore.each(function(rec) {
				me.selectRecord(rec);
			});
		},
		
		//删除所有的已选数据
		removeAllSelected: function(){
			var me = this;
			me.selectedStore.removeAll();
		},
		
		//选中一条数据
		selectRecord: function(record) {
			var me = this,
				selectedItems,
				i;
			if(me.singleSelect){
				me.selectedStore.removeAll();
			}else{
				selectedItems = me.selectedStore.data.items;
				for(i = 0; i < selectedItems.length; i++){
					if(me.selectedStore.getAt(i).get('key') ===  record.get('key')){
						return;
					}
				}
			}
			me.selectedStore.add(record);
			if(!me.singleSelect){
				me.selectedStore.sort();
			}
		},
		
		//载入选择范围树前处理参数
		beforeCdrTreeReload: function(store, operation){
			var me = this;
			operation.params.range = me.range;
			operation.params.selectType = me.selectType;
			operation.params.key = operation.node.get('key');
		},
		
		//载入备选数据前前处理参数
		beforeToSelectReload: function(operation){
			var me = this;
			operation.params = {};
			if(me.cdrTree.getSelectionModel().getSelection().length > 0){
				operation.params.key = (me.cdrTree.getSelectionModel().getSelection())[0].get('key');
				operation.params.selectType = me.selectType;
				operation.params.range = me.range;
			}else{
				return false;
			}
			return true;
		},
		
		beforeSelectedReload: function(operation) {
			var me = this;
			console.log(me.value);
			operation.params = {};
			operation.params.value = me.value;
			return true;
		},
		
		//前台查询
		searchToSelect: function () {
			var me = this,
				searchVal,
				selection,
				index,
				i,
				storeCount;
			searchVal = me.searchTextField.getValue();
			if(searchVal === ''){
				return;
			}
			storeCount = me.toSelectStore.getCount();
			if(storeCount === 0){
				return;
			}
			selection = me.toSelectGrid.getSelectionModel().getSelection();
			if(selection.length === 0){
				index = 0;
			}else{
				index = me.toSelectStore.indexOf(selection[0]);
			}
			for(i = index + 1; i < storeCount; i++) {
				if(me.toSelectStore.getAt(i).get('typeDesc').indexOf(searchVal) !== -1){
					me.toSelectGrid.getSelectionModel().select(me.toSelectStore.getAt(i));
					return;
				}
				if(me.toSelectStore.getAt(i).get('text').indexOf(searchVal) !== -1){
					me.toSelectGrid.getSelectionModel().select(me.toSelectStore.getAt(i));
					return;
				}
				if(me.toSelectStore.getAt(i).get('note').indexOf(searchVal) !== -1){
					me.toSelectGrid.getSelectionModel().select(me.toSelectStore.getAt(i));
					return;
				}
			}
			
			for(i = 0; i <= index; i++) {
				if(me.toSelectStore.getAt(i).get('typeDesc').indexOf(searchVal) !== -1){
					me.toSelectGrid.getSelectionModel().select(me.toSelectStore.getAt(i));
					return;
				}
				if(me.toSelectStore.getAt(i).get('text').indexOf(searchVal) !== -1){
					me.toSelectGrid.getSelectionModel().select(me.toSelectStore.getAt(i));
					return;
				}
				if(me.toSelectStore.getAt(i).get('note').indexOf(searchVal) !== -1){
					me.toSelectGrid.getSelectionModel().select(me.toSelectStore.getAt(i));
					return;
				}
			}
			
		},
		
		//清空备选数据
		clearToSelect: function() {
			this.toSelectStore.removeAll();
		},
		
		setValue: function(val) {
			this.value = val;
			console.log('setValue: ' + val);
			if(!val || '' === val) {
				this.selectedStore.removeAll();
			} else {				
				this.selectedStore.reload();
			}
		}
	});