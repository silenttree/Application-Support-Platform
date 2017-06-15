<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var documentKey = panel.object.key;
	var appId = panel.object.appId;
	//下拉排序列表
	var showCombox = Ext.create('Ext.form.field.ComboBox',{
		triggerAction:'all',
		mode:'local',
		width:150,
		editable:false,
		forceSelection: true,
		value:'bystate',
		store:['bystate']
	});
	//添加映射表按钮
	var btnAdd = new Ext.Action({
		text : '添加映射表',
		iconCls : 'icon-sys-add',
		handler : function(){
			//弹出个窗口
			if(!documentKey){
				alert('未指定文档对象键值');
				return;
			}else{
				showWinAuth();//显示创建文档权限映射窗口
			}
		}
	});
	//添加设计对象
	var btnAddDesignObject = new Ext.Action({
		text : '添加设计对象',
		iconCls : 'icon-sys-add',
		disabled : true,
		handler : function(){
			//弹出文档设计窗口
			if(!documentKey){
				Asc.common.Message.showError('未指定文档对象键值');
				return;
			}else{
				loadCkeckTree();//显示文档设计对象窗口
			}
		}
	});
	//删除设计对象按钮
	var btnDeleteDesignObject = new Ext.Action({
		text : '删除',
		disabled : true,
		iconCls : 'icon-sys-delete',
		handler : function(){
			var sel = gridDetail.getSelectionModel().getSelection();
			if(sel.length>0){
				//删除数据，更新每个状态映射表的数据
				var key = sel[0].data.id;
				var obj = {
						appId : panel.object.appId,
						type : sel[0].get('f_class'),
						key : sel[0].get('id')
					};
				//删除危险操作提醒
				//AscApp.ActionManager.runFunction('designer.doDeleteObject', this, [obj, function(obj){
				// 删除对象
				DeveloperAppDirect.deleteDocumentAuthority(appId, documentKey, 'authoritymap', key, function(result, e){
					if(result && result.success){
						//重新加载数据
						gridDetail.getStore().reload();
					}else{
						Asc.common.Message.showError(result.message);
					}
				});
				//}, this]);
			}else{
				Asc.common.Message.showError('请选择要删除的行！');
			}
		}
	});
	/**
		显示window，重新加载checktree
	*/
	var loadCkeckTree = function(){
		//checktree的model
		Ext.define('checktreeModel',{
			extend : 'Ext.data.Model',
	 		fields: [{name: 'type', type: 'string'},
	 		         {name: 'text', type: 'string'},
	 		         {name: 'name',  type: 'string'},
	 		         {name: 'caption', type : 'string'}]
		});
		//checktree的store
		var store = Ext.create('Ext.data.TreeStore', {
			model : 'checktreeModel',
			root: 'data',
			proxy : {
				type : 'direct',
				directFn : DeveloperAppDirect.loadDocCheckTree,
				extraParams : {
					appId : appId,
					documentKey : documentKey,
					//不包含哪些类型的设计对象
					noContain : ['authoritymap','extstate','useridentity','state','menu']
				},
				paramOrder : 'appId documentKey noContain',
				paramsAsHash : true,
				sorters : [{
					property : 'type',
					direction: 'ASC'
				},{
					property : 'key',
					direction: 'ASC'
				}]
			},
			autoLoad : true
	    });
		//带有复选框的选择树
		var checktree = Ext.create('Ext.tree.Panel',{
			rootVisible: false,
			viewConfig :{
				forceFit:true,//当行大小变化时始终填充满
				stripeRows:true //每列是否是斑马线分开
			},
	    	useArrows: true,
	    	border: false,
	    	store : store,
	   		columns : [
			        { xtype: 'treecolumn',text: 'Key',  dataIndex: 'text',width:'40%',flex:1},
			     	{ text: '类型', dataIndex: 'type', width:'15%'},
			        { text: '英文名', dataIndex: 'name', width:'15%'},
			        { text: '中文名', dataIndex: 'caption'}
			    ],
			listeners : {
				//行点击监听事件
		    	itemclick : function(tree, record, item, index, e){
	    			if(record.data.checked != null){
		    			record.set('checked', !record.get('checked'));
		    		}
		    	}
		    }
		});
		//文档设计对象选择窗口
		var win = Ext.create('Ext.window.Window', {
		    title: '文档设计对象选择',
		    height: 400,
		    width: 700,
		    modal : true,
		    maximizable : true,
		    layout: 'fit',
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
					for(var i=0;i<selcheck.length;i++){
						ids.push(selcheck[i].data.text);
					}
					//把选中的设计对象全部都添加到权限映射表中
					DeveloperAppDirect.addDocumentDesignObject(appId, documentKey, ids, function(result,e){
						if(result && result.success){
							//重新加载数据
							gridDetail.getStore().reload();
							win.close();
						}else{
							Asc.common.Message.showError(result.message);
							win.close();
						}
					});
				}
			}]
		}).show();
	};
	//显示添加文档权限映射表窗口
	var showWinAuth = function(){
		var formpanel = new Ext.form.FormPanel({
			labelWidth: 80,
	        bodyStyle:'padding:5px',
			defaults: {
				  anchor: "100%"
			},
	        defaultType: 'textfield',
	        items:[{
				fieldLabel : '文档键值',
				name : 'documentKey',
				minWidth : 100,
				fieldClass : 'x-item-disabled',
				readOnly : true,
				value : documentKey
			},{
				xtype : 'combo',
				fieldLabel : '文档状态',
				name : 'f_states',
	        	minWidth : 100,
				triggerAction: 'all',
	        	editable : false,
	        	forceSelection: true,
	        	valueField: 'f_key',
			    displayField: 'f_caption',
			    store : new Ext.data.DirectStore({
			    	root : 'data',
			    	proxy : {
			    		type : 'direct',
			    		directFn : DeveloperAppDirect.getSubObjectList,
				    	extraParams : {
				    		appId : appId,
				    		key : documentKey,
				    		mclass : 'state'
				    	},
				    	paramOrder : ['appId','key','mclass'],
				    	paramsAsHash : true
			    	},
		    		fields:['f_key', 'f_caption'],
		    		autoLoad :true
			    })
			},{
				xtype : 'combo',
				fieldLabel : '文档身份',
				name : 'f_identities',
				minWidth: 100,
				triggerAction: 'all',
	        	editable: false,
	        	forceSelection: true,
	        	valueField: 'f_key',
			    displayField: 'f_caption',
			    store : new Ext.data.DirectStore({
			      	root : 'data',
			    	proxy : {
			    		type : 'direct',
			    		directFn : DeveloperAppDirect.getSubObjectList,
				    	extraParams : {
				    		appId : appId,
				    		key : documentKey,
				    		mclass : 'useridentity'
				    	},
				    	paramOrder : ['appId','key','mclass'],
				    	paramsAsHash : true
			    	},
		    		fields:['f_key', 'f_caption'],
		    		autoLoad :true
			    })
			},{
				xtype : 'combo',
				fieldLabel : '辅助状态',
				name : 'f_extendstates',
				minWidth: 100,
				triggerAction: 'all',
	        	editable: false,
	        	forceSelection: true,
	        	valueField: 'f_key',
			    displayField: 'f_caption',
			    store : new Ext.data.DirectStore({
			    	root : 'data',
			    	proxy : {
			    		type : 'direct',
			    		directFn : DeveloperAppDirect.getSubObjectList,
				    	extraParams : {
				    		appId : appId,
				    		key : documentKey,
				    		mclass : 'extstate'
				    	},
				    	paramOrder : ['appId','key','mclass'],
				    	paramsAsHash : true
			    	},
		    		fields:['f_key', 'f_caption'],
		    		autoLoad :true
			    })
			}]
		});
		//创建文档权限映射窗口
		var winAuth = Ext.create('Ext.window.Window',{
			title: '创建文档权限映射表窗口',
			titleAlign:'center',
			height: 180,
			width: 300,
			modal : true,
			bodyBorder : false,
			layout:'fit',
			items : [formpanel],
			bbar:['->',{
				xtype : 'button',
				text: '确定',
				iconCls: 'icon-sys-confirm',
				handler : function(){
					//调用direct方法，把增加的文档状态权限添加到后台，然后重新加载树，重新加载grid
					var documentKey = formpanel.getForm().findField('documentKey').getValue();
					var f_states = formpanel.getForm().findField('f_states').getValue();
					var f_extendstates = formpanel.getForm().findField('f_extendstates').getValue();
					var f_identities = formpanel.getForm().findField('f_identities').getValue();
					if(f_states == '' || null == f_states){
						f_states = 'any';
					}
					if(f_extendstates == '' || null == f_extendstates){
						f_extendstates = 'any';
					}
					if(f_identities == '' || null == f_identities){
						f_identities = 'any';
					}
					DeveloperAppDirect.addDocumentAuthorityMap(appId, documentKey, f_states, f_extendstates, f_identities,function(result, e){
						if(result && result.success){
							tree.getStore().reload();
							winAuth.close();
						}else{
							Asc.common.Message.showError(result.message);
							winAuth.close();
						}
					});
					gridDetail.disable();
				}
			},'-',{
				xtype : 'button',
				text: '取消',
				iconCls: 'icon-sys-cancel',
				handler : function(){
					winAuth.close();
				}
			},'->']
		}).show();		
	};
	//左侧的文档权限映射的结构树
	var tree = Ext.create('Ext.tree.Panel',{
    	region : 'west',
        minSize : '15%',
        width : '20%',
        maxSize : '30%',
        height : '100%',
        split : true,
    	autoScroll : true,
    	border : false,
    	store : Ext.create('Ext.data.TreeStore', {
			root: {
				text : '权限映射表',
				id : '',
				expanded : true
			},
			proxy : {
				type : 'direct',
				directFn : DeveloperAppDirect.loadDocumentAuthorityMapTree,
				extraParams : {
					appId : appId,
					documentKey : documentKey
				},
				paramOrder : 'appId documentKey',
				paramsAsHash : true
			},
			autoLoad: true
	    }),
	    listeners : {
	    	//给树添加行点击事件，根据是否是叶子节点，来判断哪些组件是否可用
	    	itemclick : function(tree, record, item, index, e){
	    		if(record.data.leaf){
	    			btnAddDesignObject.enable();
	    			btnDeleteDesignObject.enable();
	    			gridDetail.enable();
	    			gridDetail.getStore().reload();
	    		}else{
	    			btnAddDesignObject.disable();
	    			btnDeleteDesignObject.disable();
	    			gridDetail.disable();
	    		}
	    	}
	    }
	});
	//tree节点增加右键菜单
	tree.on('itemcontextmenu',function(treenode, record, item, index, e){
		if(record.data.leaf){
			e.stopEvent();
			Ext.create('Ext.menu.Menu',{
				  width: 100,
				  items : [{
					  text : '删除',
					  iconCls : 'icon-sys-delete',
					  handler : function(){
						  var authoritymapId = record.data.id;
						  var obj = {
									appId : panel.object.appId,
									type : record.get('f_class'),
									key : record.get('id')
								}
						   //AscApp.ActionManager.runFunction('designer.doDeleteObject', this, [obj, function(obj){
								// 删除对象
							   DeveloperAppDirect.deleteAuthoritymap(appId, documentKey, authoritymapId, function(result,e){
									  if(result && result.success){
										  Asc.common.Message.showInfo("权限映射记录删除成功！");
										  tree.getStore().reload();
										  gridDetail.disable();
										  btnAddDesignObject.disable();
										  btnDeleteDesignObject.disable();
									  }else{
										  Asc.common.Message.showError("权限映射记录删除失败！" + result.message);
									  }
								  });
						   //}, this]);
					  }
				  }]
			}).showAt(e.getXY());
		}
	});
	//grid的刷新数据按钮
	var btnRefresh = new Ext.Action({
		text : '刷新',
		iconCls : 'icon-sys-refresh',
		handler : function(){
			//刷新grid表，重新加载数据
			gridDetail.getStore().reload();
		}
	});
	var gridstore = Ext.create('Ext.data.Store', {
	    fields:['type','f_caption','output','show','edit','extend','forbidden','id'],
	    proxy : {
			type : 'direct',
			directFn : DeveloperAppDirect.loadAuthoritymapGrid,
			extraParams : {
				appId : appId,
				authoritymapId : ''
			},
			paramOrder:['appId','authoritymapId']
		},
		sorters : [{
			property : 'type',
			direction: 'ASC'
		},{
			property : 'Key',
			direction: 'ASC'
		}],
		autoLoad :true,
	 	listeners : {
	 		//通过监听beforeload事件，给参数传递真实的值
			beforeload : function(store, operation){
				var authoritymapId= '';
				if(tree.getSelectionModel().getSelection().length>0){
					authoritymapId = tree.getSelectionModel().getSelection()[0].data.id;
				}
				operation.params = {
						appId : appId,
						authoritymapId : authoritymapId
				}
			}
		}
	});
	//右侧的文档权限映射表的具体数据
	var gridDetail = Ext.create('Ext.grid.Panel',{
		region : 'center',
		height : '100%',
		width : 800,
		border : false,
		autoScroll : true,
		disabled : true,
		store: gridstore,
	    columns: [
			{header: '类型',  dataIndex: 'type'},
	        {header: '中文名', dataIndex: 'f_caption'},
	        {xtype: 'checkcolumn', header: '输出',  dataIndex: 'output',
	        	editor: {
                	xtype: 'checkbox',
                	cls: 'x-grid-checkheader-editor'
              }},
	        {xtype: 'checkcolumn', header: '显示',  dataIndex: 'show',
	        	editor: {
	        		 xtype: 'checkbox',
		             cls: 'x-grid-checkheader-editor'
	        	}},
	        {xtype: 'checkcolumn', header: '编辑',  dataIndex: 'edit',
	        		editor: {
		        		 xtype: 'checkbox',
			             cls: 'x-grid-checkheader-editor'
		        }},
	        {xtype: 'checkcolumn', header: '扩展',  dataIndex: 'extend',
	        	editor: {
	        		 xtype: 'checkbox',
		             cls: 'x-grid-checkheader-editor'
	        	}},
	        {xtype: 'checkcolumn', header: '禁止访问',  dataIndex: 'forbidden',
	        	editor: {
	        		 xtype: 'checkbox',
		             cls: 'x-grid-checkheader-editor'
	        	}},
	        {header: 'Key',  dataIndex: 'id',width:'30%'}
	    ],
		title : '目标权限映射表',
		bbar : [btnRefresh]
	});
	var showPanel = Ext.create('Ext.panel.Panel',{
		layout : 'border',
		width : '95%',
		height : '95%',
		border : false,
		bodyBorder : false,
		tbar : [showCombox, '-', btnAdd, '->',btnAddDesignObject,'-',btnDeleteDesignObject],
		items : [tree,gridDetail]
	});
	//把数据封装成JsonObject格式
	var encodeJsonObject = function(items){
		var obj = {};
		for(var i=0;i<items.length;i++){
			var output = (items[i].data.output==true?'1':'0');
			var show = (items[i].data.show==true?'1':'0');
			var edit = (items[i].data.edit==true?'1':'0');
			var extend = (items[i].data.extend==true?'1':'0');
			var forbidden = (items[i].data.forbidden==true?'1':'0');
			var str = output + show + edit + extend + forbidden;
			obj[items[i].data.id] = str;
		}
		return obj;
	};
	//执行保存
	panel.doApply = function(){
		var newStore = gridDetail.getStore();
		var items = newStore.data.items;
		//把store里面的数据封装成JsonObject格式，可以直接调用DeveloperAppDirect的updateAuthoritiesMap方法，直接设置
		var jsonObject = encodeJsonObject(items);
		if(gridDetail.isDisabled()){
			console.log("不是叶子节点，无法执行保存操作！");
			return;
		}else{
			DeveloperAppDirect.updateAuthoritiesMap(appId, tree.getSelectionModel().getSelection()[0].data.id,jsonObject,function(result,e){
				if(result && result.success){
					//刷新grid表
					gridDetail.getStore().reload();
				}else{
					Asc.common.Message.showError("执行保存操作失败，请重试！");
				}
			});
		}
	};
	panel.add(showPanel);
	panel.doLayout();
});
</script>