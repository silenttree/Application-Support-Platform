/*
 * 自定义视图
 */
Ext.define('Asc.common.CustomView',{
	extend : 'Ext.AbstractPlugin',
	alias: 'plugin.customview',
	
	grid : '',

	columns : [],

	sorts : [],

	init : function(grid){
		var me = this;
		me.grid = grid;
		me.columns = grid.columns;
		grid.on('itemcontextmenu', function ( view, record, item, index, e, eOpts) {
			e.preventDefault();  //屏蔽默认右键菜单            
			Ext.create('Ext.menu.Menu', {
				items: [{
				        	text: '视图配置',
				        	disabled: false,
				        	handler: function() {
				        		me.showCustomView();
				        	}
						}]
			}).showAt(e.getXY());
		});
	},

	showCustomView: function(){
		var store = new Ext.data.ArrayStore({
			idIndex : 0,
			idProperty : 'id',
			fields: [
			         'dataIndex',
			         {
			        	 name: 'hidden',
			        	 type: 'bool'
			         }, {
			        	 name: 'locked',
			        	 type : 'bool'
			         }, {
			        	 name: 'caption', 
			        	 type : 'string'
			         }, 
			         'width', 
			         'sortorder',
			         'dir',
			         'sortable',
			         'order'
			],
			sorters : {
				property : 'order',
			    direction : 'asc'
			}
		});

		//添加列信息
		var cm = this.columns;
		for(var i=0;i<cm.length;i++){
			var data = {
					dataIndex : cm[i].dataIndex,
					hidden : !cm[i].hidden,
					locked : cm[i].locked,
					caption : cm[i].text,
					width : cm[i].width,
					order : i,
					id : i + 1
			}
			store.add(data);
		}

		this.initSorts(store);

		//列宽度编辑 事件
		var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
			clicksToEdit: 2,
			listeners: {
				'edit': function(editor, e){
					this.columns[e.rowIdx].setWidth(parseInt(e.value));
				},
				scope: this
			}
		});

		var gridPanel = Ext.create('Ext.grid.Panel', {
			store: store,
			enableColumnMove : false,
			tbar: ['->',{
				xtype: 'button',
				text: '上移',
				handler: function(){
					var record = gridPanel.getSelectionModel().getSelection();
					if(record[0]){
						var order = record[0].get('order');
						var index = store.indexOf(record[0]);
						if(index > 0){
							var recordPre = store.getAt(index - 1);
							var orderPre = recordPre.get('order');
							record[0].set('order', orderPre);
							recordPre.set('order', order);
							store.sort('order', 'ASC');
							this.setColumnMove(order, orderPre, record);
						}
					}
				},
				scope : this
			},{
				xtype: 'button',
				text: '下移',
				handler: function(){
					var record = gridPanel.getSelectionModel().getSelection();
					if(record){
						var order = record[0].get('order');
						var index = store.indexOf(record[0]);
						if(index < store.getCount() - 1){
							var recordPre = store.getAt(index + 1);
							var orderPre = recordPre.get('order');
							record[0].set('order', orderPre);
							recordPre.set('order', order);
							store.sort('order', 'ASC');
							this.setColumnMove(order, orderPre, record);
						}
					}
				},
				scope : this
			}],
			columns: [
			          {
			        	  header: '显示',  
			        	  dataIndex: 'hidden',
			        	  width: 40,
			        	  renderer : function(v, md){
			        		  if(v){
			        			  md.css = 'customview-grid-check-col';
			        		  }else{
			        			  md.css = 'customview-grid-check-col-on';
			        		  }
			        		  return '&nbsp;';
			        	  }
			          },
			          {
			        	  header: '锁定', 
			        	  dataIndex: 'locked', 
			        	  width: 40,
			        	  renderer : function(v, md){
			        		  if(v){
			        			  md.css = 'customview-grid-check-col';
			        		  }else{
			        			  md.css = 'customview-grid-check-col-on';
			        		  }
			        		  return '&nbsp;';
			        	  }
			          },
			          { 
			        	  header: '列名称', 
			        	  dataIndex: 'caption', 
			        	  width: 100,
			          },
			          { 
			        	  header: '列宽', 
			        	  dataIndex: 'width', 
			        	  width: 50,
			        	  editor: {
			        		  xtype:'textfield'
			        	  },
			        	  renderer : function(v){
			        		  if(v == -1){
			        			  return '自动';
			        		  }else{
			        			  return v;
			        		  }
			        	  }
			          },
			          { 
			        	  header: '排序', 
			        	  dataIndex: 'sortorder', 
			        	  width: 40, 
			        	  renderer : function(v, md, record){
			        		  if(v){
			        			  if(record.get('dir') == 'ASC'){
			        				  md.css = 'customview-grid-sort-asc';
			        			  }else{
			        				  md.css = 'customview-grid-sort-desc';
			        			  }
			        		  }
			        		  return v;
			        	  }
			          }],
			          height: 390,
			          width: 340,
			          viewConfig: {
			        	  markDirty: false
			          },
			          plugins:[cellEditing]
		});
		
		gridPanel.on('cellmousedown', function(th, td, cellIndex, record, tr, rowIndex, e){
			if(cellIndex == 0){
				//设置列显隐功能
				this.setHidden(rowIndex, record);
			}else if(cellIndex == 1){
				//设置列锁定功能
				this.setLocked(th, rowIndex, record);
			}else if(cellIndex == 4){
				//设置列排序功能
				this.setOrder(record);
			}
		},this);

		var win = Ext.create('Ext.window.Window', {
			title: '配置自定义视图',
			height: 400,
			modal : true,
			width: 350,
			layout: 'fit',
			items: gridPanel,
			buttons : [{
				text : '保存设置',
				iconCls : 'icon-cross-btn-save',
				handler : function(){
					var columns = [];
					for(var i=0;i<store.getCount();i++){
						columns.push(store.getAt(i).data);
					}
					this.submitConfig({columns : columns}, function(){
						win.close();
					});
				},
				scope: this
			}]
		}).show();
	},

	//设置列显隐
	setHidden : function(rowIndex, record){
		record.set('hidden', !record.data.hidden);
		this.columns[record.data.id].setVisible(record.data.hidden);
	},

	initSorts : function(store){
		for(var i=0;i<this.sorts.length;i++){
			var record = store.getAt(store.find('dataIndex', this.sorts[i].id));
			record.set('dir', this.sorts[i].dir);
			record.set('sortorder', i + 1);
		}
	},

	//设置列移动
	setColumnMove : function(index, newIndex, record){
		//锁定列
		var lchCt = this.grid.lockedGrid.headerCt;
		//未锁定列
		var nohCt = this.grid.normalGrid.headerCt;
		var item;
		//算出未锁定列开始的index
		var con = 0;
		for(var i = 0;  i < this.columns.length; i++){
			col = this.columns[i];
			if(col.locked){
				con = con + 1;
			}
		}
		//判断将转移到的列是否是锁定列
		if(this.columns[newIndex].locked){
			//判断被转移列是否是锁定列
			if(this.columns[index].locked){
				lchCt.move(index, newIndex);
			}else{
				record[0].set('locked', true);
				this.grid.lock(this.columns[index]);
				lchCt.move(index, newIndex);
			}
		}else{
			if(record[0].get('locked')){
				record[0].set('locked', false);
				this.grid.unlock(this.columns[record[0].get('id') - 1]);
				if(con == 1){
					nohCt.move(index, newIndex);
				}else{
					nohCt.move(0, 1);
				}
			}else{
				if(con == 1){
					nohCt.move(index - 1, newIndex - 1);
				}else{
					nohCt.move(index - con - 1, newIndex - con - 1);
				}
			}
		}
		this.grid.getView().refresh();
	},

	//设置列排序
	setOrder : function(record){
		if(record.get('sortorder')){
			if(record.get('dir') ==  'ASC'){
				record.data.dir = 'DESC';
				this.sorts[record.get('sortorder') - 1].dir = 'DESC';
			}else if(record.get('sortorder') < this.sorts.length){
				record.set('dir', 'ASC');
			}else{
				record.set('sortorder', undefined);
				record.set('dir', undefined);
				this.sorts.pop();
			}
		}else{
			record.set('sortorder', this.sorts.length + 1);
			record.set('dir', 'ASC');
			this.sorts.push({id : record.get('dataIndex'), dir : 'ASC'});
		}
		record.commit();
	},

	//设置列锁定
	setLocked : function(th, rowIndex, record){
		record.set('locked', !record.data.locked);
		if(record.data.locked){
			this.grid.lock(this.columns[record.get('id')-1]);
		}else{
			this.grid.unlock(this.columns[record.get('id')-1]);
		}
	},

	//提交配置
	submitConfig : function(config, fn){
		Ext.apply(config, {sorts : this.grid.sorts, size : this.grid.getSize()});
		fn.call();
	}
});