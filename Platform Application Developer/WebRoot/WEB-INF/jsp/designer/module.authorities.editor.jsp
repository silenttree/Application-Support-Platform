<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var moduleKey = panel.object.key;
	var appId = panel.object.appId;
	/**
		显示window，重新加载checktree
	*/
	var loadCkeckTree = function(){
		//checktree的model
		Ext.define('checktreeModel',{
			extend : 'Ext.data.Model',
	 		fields : [
				{name: 'type', type: 'string'},
	 		    {name: 'text', type: 'string'},
	 		    {name: 'name',  type: 'string'},
	 		    {name: 'caption', type : 'string'}
	 		]
		});
		//checktree的store
		var store = Ext.create('Ext.data.TreeStore', {
			model : 'checktreeModel',
			root : 'data',
			proxy : {
				type : 'direct',
				directFn : DeveloperAppDirect.loadCheckTreeObjectsAndRefer,
				extraParams : {
					appId : appId,
					moduleKey : moduleKey,
					noContain : ['modulerole','authoritymap','extstate','useridentity','state']
				},
				paramOrder : 'appId moduleKey noContain',
				paramsAsHash : true,
				sorters : [{
					property : 'type',
					direction : 'ASC'
				},{
					property : 'key',
					direction : 'ASC'
				}]
			},
			autoLoad: true
	    });
		//带有复选框的选择树
		var checktree = Ext.create('Ext.tree.Panel',{
			rootVisible : false,
			viewConfig : {
				forceFit : true,//当行大小变化时始终填充满
				stripeRows : true //每列是否是斑马线分开
			},
        	useArrows : true,
        	border : false,
        	store : store,
       		columns : [
   		        { xtype : 'treecolumn',text : 'Key',  dataIndex : 'text', width :'40%', flex : 1},
   		     	{ text : '类型', dataIndex : 'type', width :'15%'},
   		        { text : '英文名', dataIndex : 'name', width :'15%'},
   		        { text : '中文名', dataIndex : 'caption'}
   		    ],
   		 	listeners : {
		    	itemclick : function(tree, record, item, index, e){
		    		if(record.data.checked != null){
		    			record.set('checked', !record.get('checked'));
		    		}
		    	}
		    }
		});
		var win = Ext.create('Ext.window.Window', {
		    title : '模块对象选择',
		    height : 400,
		    width : 700,
		    modal : true,
		    maximizable : true,
		    layout : 'fit',
		    items :[checktree],
		    bbar : ['->',{
					text : '取消',
					iconCls : 'icon-sys-cancel',
					handler : function() {
						win.close();
				}
			}, '-', {
				text : '确定',
				iconCls : 'icon-sys-apply',
				handler : function() {
					var selcheck = checktree.getChecked();
					var ids = [];
					if(selcheck.length == 0){
						Asc.common.Message.showError("未选择设计对象！");
						return;
					}else{
						for(var i = 0;i < selcheck.length;i++){
							ids.push(selcheck[i].data.text);
						}
						DeveloperAppDirect.addAuthObject(appId, moduleKey, ids, function(result, e){
							if(result && result.success){
								//设置维护的参数(result.moduleAuth);
								panelRefresh(appId, moduleKey, result.moduleAuth);
							}else{
								if(result && result.message){
									Asc.common.Message.showError(result.message);
								}else{
									Asc.common.Message.showError('添加模块权限对象[' + panel.object.key, + ']失败！');
								}
							}
						});
						win.close();
					}
				}
			}]
		}).show();
	};
	
	var setCols = function(cols){
		var colPanel = Ext.create('Ext.panel.Panel', {
		    width: '100%',
		    height: '100%',
		    layout: {
		        type: 'table',
		        columns: 4
		    },
		    frame:false
		});
		if(cols.colIds.length > 0){
			for(var i=0;i<cols.colIds.length;i++){
				var checked = false;
				if(setColumnDataIndexs.indexOf(cols.colIds[i]) >= 0){
					checked = true;		
				}
				var checkbox = Ext.create('Ext.form.field.Checkbox',{
					xtype : 'checkboxfield',
					boxLabel : cols.colNames[i],
					inputValue : cols.colIds[i],
					checked: checked,
					name : 'f_cols',
					margin : '5 5 5 5'
				});
				colPanel.add(checkbox);
			}
		}
		var formPanel = Ext.create('Ext.form.Panel', {
// 			layout : 'column',
			width : '100%',
			height : '100%',
			border : false,
			autoScroll: true,
			items : [colPanel]
		});
		
		var f_width = Ext.create('Ext.form.field.Number', {
			name: 'f_width',
		    fieldLabel: '列宽',
		    labelWidth : 60,
		    width : 200,
		    value : 100,
		    labelAlign: 'right',
		    padding : '0 5 0 0',
		    decimalPrecision:0,minValue:100,maxValue:500
		});
		
		var checkboxAll = Ext.create('Ext.form.field.Checkbox',{
			xtype : 'checkboxfield',
			boxLabel : '选择全部',
			inputValue : 'all',
			checked: checked,
			listeners : {
				change: function(checkbox, newValue, oldValue, e){
					var its = colPanel.items.items;
					if(newValue){
						Ext.Array.forEach(its, function(it){
			    			it.setValue(true);
			    		});
					}else{
						Ext.Array.forEach(its, function(it){
			    			it.setValue(false);
			    		});
					}
		    	}
		    }
		});
		
		var win = Ext.create('Ext.window.Window', {
		    title : '选择模块角色',
		    height : 400,
		    width : 700,
		    modal : true,
		    maximizable : true,
		    layout : 'fit',
		    items :[formPanel],
		    bbar : [f_width, '-', checkboxAll, '->',{
					text : '取消',
					iconCls : 'icon-sys-cancel',
					handler : function() {
						win.close();
				}
			}, '-', {
				text : '确定',
				iconCls : 'icon-sys-apply',
				handler : function() {
					if(formPanel.getForm().isValid() && f_width.isValid()){
						var checks = formPanel.getForm().getValues();
						win.close();
						refreshCols(checks.f_cols,f_width.getValue());
					}
				}
			}]
		}).show();
	}
	
	var setColumnDataIndexs = [];
	var setColumns = [];
	
	var refreshCols = function(cols,f_width){
		if(cols != undefined && cols.length > 0){
			setColumns = [];
			setColumnDataIndexs = [];
			for(var i=0;i<columns.length;i++){
				var item = columns[i];
				var have = cols.indexOf(item.dataIndex);
				if(item.dataIndex == 'type' || item.dataIndex == 'key' 
						|| item.dataIndex == 'name' || item.dataIndex == 'caption'
						|| have >= 0){
					if(have >= 0){
						columns[i].width = f_width;
						setColumnDataIndexs.push(columns[i].dataIndex);
					}
					setColumns.push(columns[i]);
				}
			}
			gridpanel.getStore().reload();
			gridpanel.doLayout();
		}
	}
	
	var allColIds = [];
	var allColNames = [];
	
	var toolbar = [{
		text : '添加模块对象',
		iconCls : 'icon-sys-add',
		handler : function(){
			loadCkeckTree();
		}
	},'-',{
		text : '删除',
		iconCls : 'icon-sys-delete',
		handler : function(){
			var sel = gridpanel.getSelectionModel().getSelection();
			if(sel.length > 0){
				//先删除store里面的是数据
				gridpanel.getStore().remove(sel[0]);
				//获得删除后store里面所有数据
				var newStore = gridpanel.getStore();
				var items = newStore.data.items;
				//把store里面的数据封装成JsonObject
				var jsonObject = encodeJsonObject(items);
				//调用更新权限方法
				DeveloperAppDirect.updateAuthorities(appId, moduleKey, jsonObject, function(result, e){
					if(result && result.success){
						gridpanel.getStore().reload();
						gridpanel.doLayout();
					}else{
						Asc.common.Message.showError(result.message);
					}
				});
			}else{
				Asc.common.Message.showError('请选择要删除的行！');
			}
		}
	},'-',{
		text : '选择模块角色',
		iconCls : 'icon-sys-setting',
		handler : function(){
			var cols = {};
			cols.colIds = allColIds;cols.colNames = allColNames;
			setCols(cols);
		}
	}];
	var fields = ['type','key','name','caption'];//固定的列
	var columns = [	{ text: '类型',dataIndex : 'type',
					renderer : function(v, md, record){
						return "<div class='icon-designer-" + record.data.type + "'></div>"  + record.data.type;
					}},
	    	        { text : 'Key',dataIndex : 'key', width:400},
	    	        { text : '英文名',dataIndex : 'name',width:200},
	    	        { text : '中文名',dataIndex : 'caption',width:200}];
	//刷新模块角色权限列表
 	var panelRefresh = function(appId, moduleKey, jsonObject){
		DeveloperAppDirect.updateAuthorities(appId, moduleKey, jsonObject, function(result, e){
			if(result && result.success){
				Asc.common.Message.showInfo('添加权限模块成功！');
				//重新加载grid的数据
				gridpanel.getStore().reload();
				gridpanel.doLayout();
			}else{
				Asc.common.Message.showError(result.message);
			}
		});
	};
	var columnsRenderFn = function(caption, v, m, r){
		 m.tdAttr = 'data-qtip="'+caption+'"';   
		 alert(caption);
         return v;  
	}
	//模块角色权限列表
	var gridpanel = Ext.create('Ext.grid.Panel', {
	    columns : [],
	    height : '95%',
	    width : '95%',
	    border : true,
	    shadow : false,
		tbar : toolbar,
		listeners : {
			afterrender : function(grid){//在grid渲染之后，动态加载其他的列，然后再重新画grid
				DeveloperAppDirect.getModuleRole(appId, moduleKey, function(result, e){
					if(result && result.success){
						allColIds = [];
						allColNames = [];
						if(allColIds.length == 0) {}
						for(var i = 0;i < result.fields.length;i++){//动态加的列
							allColIds.push(result.fields[i]['id'].replace(".","#"));
							allColNames.push(result.fields[i]['f_caption']);
							fields.push(result.fields[i]['id'].replace(".","#"));
							(function(result, i) {
								columns.push({xtype: 'checkcolumn',
						              header: result.fields[i]['f_caption'],
						              dataIndex: result.fields[i]['id'].replace(".","#"),
						              editor : {
						                xtype : 'checkbox',
						                cls : 'x-grid-checkheader-editor'
						              },
						              renderer: function(v, m, r,rowIndex,colIndex,store,view ) {
						            	  m.tdAttr = 'data-qtip="'+result.fields[i]['f_caption']+'"';   
						            	  return (new Ext.grid.column.CheckColumn).renderer(v);
						              }
								});
							})(result, i);
						}
					 	//重新load store
						var gstore = Ext.create('Ext.data.Store', {
						    fields : fields,
							proxy : {
								type : 'direct',
								directFn : DeveloperAppDirect.loadGridModuleAuth,
								extraParams : {
									appId : appId,
									moduleKey : moduleKey
								},
								paramOrder:['appId','moduleKey']
							},
							sorters : [/* {
								property : 'type',
								direction : 'ASC'
							}, */{
								property : 'key',
								direction : 'ASC'
							}],
							autoLoad : true,
							listeners : {
								load : function(g, rs, successful){
									if(successful){
										gridpanel.reconfigure(gstore, setColumns);
									}
								}
							}
						});
					}
				});
			},
			//右键删除菜单
			itemcontextmenu : function(treenode, record, item, index, e){
				var id = record.data.key;
				e.stopEvent();
				Ext.create('Ext.menu.Menu',{
					  width : 100,
					  items : [{
						  text : '删除',
						  iconCls : 'icon-sys-delete',
						  handler : function(){
								//先删除store里面的是数据
								gridpanel.getStore().remove(record);
								//获得删除后store里面所有数据
								var newStore = gridpanel.getStore();
								var items = newStore.data.items;
								//把store里面的数据封装成JsonObject
								var jsonObject = encodeJsonObject(items);
								//调用更新权限方法
								DeveloperAppDirect.updateAuthorities(appId, moduleKey, jsonObject, function(result, e){
									if(result && result.success){
										gridpanel.getStore().reload();
										gridpanel.doLayout();
									}else{
										Asc.common.Message.showError(result.message);
									}
								});
						  	}
					  }]
				}).showAt(e.getXY());
			}

		}
	});
	//把数据封装成JsonObject格式
	var encodeJsonObject = function(items){
		var obj = {};
		for(var i = 0;i < items.length;i++){
			var temp = [];
			//从固定的列之后开始循环查找，该模块角色是否被选中
			for(var j = 4;j < fields.length;j++){
				if(items[i].data.hasOwnProperty(fields[j]) && items[i].data[fields[j]]){
					temp.push(fields[j].replace("#","."));
				}
			}
			obj[items[i].data['key']] = temp;
		}
		return obj;
	};
	//执行保存
	panel.doApply = panel.doSave = function(){
		var newStore = gridpanel.getStore();
		var items = newStore.data.items;
		//把store里面的数据封装成JsonObject格式，可以直接调用DeveloperAppDirect的updateAuthorities方法，直接设置
		var jsonObject = encodeJsonObject(items);
	 	DeveloperAppDirect.updateAuthorities(appId, moduleKey, jsonObject, function(result, e){
			if(result && result.success){
				gridpanel.getStore().reload();
				gridpanel.doLayout();
			}else{
				Asc.common.Message.showError(result.message);
			}
		});
	};
	panel.add(gridpanel);
	panel.doLayout();
});
</script>