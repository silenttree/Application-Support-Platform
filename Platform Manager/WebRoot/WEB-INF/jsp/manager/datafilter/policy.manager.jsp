<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script>
Ext.onReady(function(){
	var ids = new Array();
	var panel = Ext.getCmp('<%=panelid%>');
	var win;
	// 模块树
	var moduleFormPanel = Ext.create('Asc.extension.JspPanel',{
		title : '权限录入',
		region : 'center',
		border : false,
		flex : 20,
		jspUrl : 'manager/datafilter/module.form'
	});
	// 应用树
 	var appTreePanel = Ext.create('Asc.extension.JspPanel', {
		title : '应用列表',
		region : 'west',
		split : true,
		collapseMode : 'mini',
		border : false,
		minWidth : 180,
		flex : 20,
		jspUrl : 'manager/datafilter/application.tree'
	}); 
	
 	var mrStore = Ext.create('Ext.data.Store', {
		clearRemovedOnLoad : true,
		remoteSort: false,
		proxy : {
			type : 'direct',
			directFn : DataFilterDirect.listPoliciesByAppid,
			paramOrder : ['appId'],
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
			name: 'id', 
			type: 'int'
		}, {
			name: 'f_app_name', 
			type: 'string'
		}, {
			name: 'f_module_name', 
			type: 'string'
		}, {
			name: 'f_function_name', 
			type: 'string'
		}, {
			name: 'f_object_name', 
			type: 'string'
		}, {
			name: 'f_tablename', 
			type: 'string'
		}, {
			name: 'f_ue_expressions', 
			type: 'string'
		}, {
			name: 'f_ue_captions', 
			type: 'string'
		}, {
			name: 'f_sql_where', 
			type: 'string'
		}, {
			name: 'f_create_time', 
			type: 'String'
		}, {
			name: 'f_creator_id', 
			type: 'int'
		}, {
			name: 'f_creator_name', 
			type: 'string'
		}, {
			name: 'f_note', 
			type: 'string'
		}],
		listeners : {
			'beforeload' : function(s, operation, eOpts){
				return beforeMrmsReload(operation);
			}
		}
	});
 	
 	var beforeMrmsReload = function(operation){
 		appId = appTreePanel.getSelectedId();
 		// 没有选中机构或者应用，不重新加载
 		if(appId <= 0) {
 			return false;
 		}
		operation.params = {};
		operation.params.appId = appId;
		return true;
	};
	
	// 数据列表界面
 	var grid = Ext.create('Ext.grid.Panel', {
 		title : '数据权限列表',
		region : 'center',
		border : false,
		sealedColumns : true,
		selType : 'checkboxmodel',
		//selModel: new Ext.selection.CheckboxModel({checkOnly:true}),
		sortableColumns : false,
		rowLines : true,
	    viewConfig : {stripeRows: true},
		allowDeselect : true,
	    columns : [{
	    	text : '应用名称',
	    	dataIndex : 'f_app_name',
	    	sortable: true,
	    	width:150
	    }, {
	    	text : '模块名称',
	    	dataIndex : 'f_module_name',
	    	sortable: true,
	    	width:150
	    }, {
	    	text : '功能名称',
	    	dataIndex : 'f_function_name',
	    	sortable: true,
	    	width:150
	    }, {
	    	text : '对象名称',
	    	dataIndex : 'f_object_name',
	    	sortable: true,
	    	width:150
	    }, {
	    	text : '权限',
	    	dataIndex : 'f_ue_captions',
	    	sortable: true,
	    	width:150
	    }, {
	    	text : 'SQL条件',
	    	dataIndex : 'f_sql_where',
	    	sortable: true,
	    	width:300
	    }, {
	    	text : '备注',
	    	dataIndex : 'f_note',
	    	sortable: true,
	    	width:150
	    }, {
	    	text : '创建时间',
	    	dataIndex : 'f_create_time',
	    	sortable: true,
	    	width:150
	    }],
	    flex : 60,
		store : mrStore,
		tbar : [{
			text : '创建',
			iconCls : 'icon-sys-add',
			handler : function (){
				grid.createPolicy();
			}
		}, '-', {
			text : '删除',
			iconCls : 'icon-sys-delete',
			handler : function(){
				grid.deletePolicy();
			}
		},'-', {
			text : '打开',
			iconCls : 'icon-sys-open',
			handler : function(){
				grid.openPolicy();
			}
		}, '->', {
			text : '复制',
			iconCls : 'icon-sys-copy',
			handler : function(){
				grid.copyIds();
			}
		},'-',{
			text : '粘贴',
			iconCls : 'icon-sys-copy',
			handler : function(){
				grid.copyRecords();
			}
		},'-',{
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				grid.refresh();
			}
		}]
	});
	
	// 刷新列表
	grid.refresh = function(){
		grid.getStore().removeAll();
		grid.getStore().reload();
		grid.getSelectionModel().deselectAll();
	};
	
	grid.deletePolicy = function(){
		var selection = grid.getSelectionModel().getSelection();
		if(selection.length > 0 && selection.length <2){
			var id = selection[0].data.id;
			Ext.MessageBox.confirm('危险操作提示', '删除数据操作不可恢复，您确定吗？', function(btn){
				if(btn == 'yes'){				
					DataFilterDirect.deletePolicy(id, function(result, e){
						if(result && result.success){
							// 删除后刷新界面
							Asc.common.Message.showInfo('删除成功！');
						}else{
							if(result && result.message){
								Asc.common.Message.showError(result.message);
							}else{
								Asc.common.Message.showError('删除失败！');
							}
						}
						grid.refresh();
					});				
				}
			});
		}else if(selection.length > 1){
			Asc.common.Message.showError('警告，不可以同时删除多条数据！');
		}else{
			Asc.common.Message.showError('请选择删除的数据');
		}
	}
	
	grid.copyIds = function(){
		var selection = grid.getSelectionModel().getSelection();
		if(selection.length === 0){
			Asc.common.Message.showInfo('请选择一条或多条数据');
			return;
		}
		ids = new Array();
		for(var i = 0;i<selection.length;i++){
			ids[i] = selection[i].data.id;
		}
		Asc.common.Message.showInfo('复制成功！');
		return true;
	}
	
	grid.copyRecords = function(){
		if(ids.length != 0){
			DataFilterDirect.copyPolicy(ids, function(result, e){
				if(result && result.success){
					// 粘贴后刷新界面
					Asc.common.Message.showInfo('粘贴成功！');
				}else{
					if(result && result.message){
						Asc.common.Message.showError(result.message);
					}else{
						Asc.common.Message.showError('粘贴失败！');
					}
				}
				grid.refresh();
			});				
			return true;
		} else {
			Asc.common.Message.showError('请先复制数据！');
			return false;
		}
	}
	
	//创建
	grid.createPolicy = function () {
		var treeId = appTreePanel.getSelectedId();
		if(treeId === -1){
			Asc.common.Message.showInfo('请选择一条应用列表数据');
			return false;
		}
		panel.selection = null;
		openModuleForm();
		};
	
	appTreePanel.selectionchange = function(){
		grid.refresh();
	};
	
	var openModuleForm = function(){
		var moduleForm = Ext.create('Asc.extension.JspPanel',{
			itemId: 'moduleGrid',
		    autoScroll: true,
		    width:180,
    		appId: panel.appKey, 
    		jspUrl: "manager/datafilter/module.form",
		    loader: {
    	    	listeners: {
    	    		beforeload: function(jspPanel, res) {
    	    			jspPanel.addEvents('afterpackage');
    	    		}
    	    	}
    	    } 	
		});
		
		var okBtn = Ext.create("Ext.Button", {
			text: "确定",
			handler : function(){
				var mark = moduleForm.getForm().isValid();
				if(!mark){
					return false;
				}
				var data = moduleForm.getForm().getValues();
				var flag = true;
				grid.getStore().reload({callback:function(){
					if(flag){
						flag = false;
						DataFilterDirect.addPolicy(data,function(r){
							if(r.success){
								Ext.Msg.show({
									msg:'提交成功',
									buttons: Ext.Msg.YES,
							     	icon: Ext.Msg.INFO
								});
								grid.refresh();
							} else {
								Ext.Msg.show({
									msg:'提交失败',
									buttons: Ext.Msg.YES,
							     	icon: Ext.Msg.ERROR
								});
							}
						});
					}
					moduleWin.close();
				}});
			}
		});
		
		var cancelBtn = Ext.create("Ext.Button", {
			text: "关闭",
			handler : function(){
				moduleWin.close();
			}
		});
		
		var moduleWin = Ext.widget('window', {
		    title: '模块权限导入',
		    modal:true, 
		    width: 540,
		    height: 440,
		    layout: 'fit',
		    items: moduleForm,
	    	buttons: [okBtn, cancelBtn]
		});
		moduleWin.opener = panel;
		moduleWin.show();
	};
	
	 grid.on("itemdblclick",function(o,record,item,index){
		panel.selection = record.data;
		openModuleForm();
	}); 
	
	grid.openPolicy = function(){
		var selection = grid.getSelectionModel().getSelection();
		if(selection.length != 1) {
			Asc.common.Message.showInfo('请选择一条数据');
			return false;
		}
		panel.selection = selection[0].data;
		openModuleForm();
	}
	
	panel.getAppTree = function() {
		return appTreePanel.tree;
	};
	
	panel.getAppPanel = function() {
		return appTreePanel;
	};
	
	panel.rightGrid = grid;
	// 显示界面
	panel.add({
		layout : 'border',
		border : false,
		items : [appTreePanel, grid]
	});
	panel.doLayout();
});
</script>