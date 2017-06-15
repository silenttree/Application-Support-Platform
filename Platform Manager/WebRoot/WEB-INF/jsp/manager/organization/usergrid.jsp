<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_user';
	var typeClass = AscApp.ClassManager.getClass(type);
	var orderFieldName = 'f_state';
	panel.orgId = 0;
	panel.userId = 0;
	panel.tree = null;
	// 获得对象属性列表
	var properties = AscApp.ClassManager.getProperties(type);
	
	// 初始化视图列
	var columns = AscApp.ClassManager.getGridColumns(properties, typeClass.propertyColumns);
	columns[0].dataIndex = orderFieldName;
	// 初始化存储字段
	var fields = AscApp.ClassManager.getStoreFields(properties);
	// 初始化数据存储对象
	var store = new Ext.data.Store({
		clearRemovedOnLoad : true,
		proxy : {
			type : 'direct',
			directFn : OrganizationDirect.loadUsers,
			//directFn : OrganizationDirect.getOrgUser,
			extraParams : {
				orgId : 0
			},
			paramOrder : 'orgId',
			paramsAsHash : true,
			reader : {
				type: 'json',
				root : 'datas',
				totalProperty : 'totals',
				successProperty : 'successed',
				messageProperty : 'message'
			}
		},
		fields : fields,
		sorters : [{
			property : orderFieldName,
			direction: 'ASC'
		}],
		listeners : {
			'beforeload' : function(s, operation, eOpts){
				operation.params = {orgId : panel.orgId};
			}
		}
	});
	
	var findUser = new Ext.data.Store({
		clearRemovedOnLoad : true,
		proxy : {
			type : 'direct',
			directFn : OrganizationDirect.findUser,
			reader : {
				type: 'json',
				root : 'datas',
				totalProperty : 'totals',
				successProperty : 'successed',
				messageProperty : 'message'
			}
		},
		fields : [{name: 'id'},
		          {name: 'text'},
		          {name: 'leaf'},
		          {name: 'iconCls'},
		          {name: 'path'},
		          {name: 'f_dept_id'},
		          {name: 'f_caption'},
		          {name: 'f_name'},
		          {name: 'f_gender'},
		          {name: 'f_type'},
		          {name: 'f_state'},
		          {name: 'f_email'},
		          {name: 'f_company_caption'},
		          {name: 'f_dept_caption'},
		          {name: 'orgs'},
		          {name: 'roles'}]
	});
	// 属性编辑窗口
	var editor = Ext.create('Asc.extension.JspPanel', {
		disabled : true,
		layout : 'fit',
		region : 'east',
		split : true,
		border : false,
        collapseMode:'mini',
		width : 500,
		minWidth : 300,
		title : '属性编辑窗口',
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/properties.editor',
		params : {type : type},
		tbar : [{
			id : 'ps',
			text : '密码重置',
			handler : function(){
				panel.retPassword();
			}
		}]
	});
	// 数据列表界面
	var grid = Ext.create('Ext.grid.Panel', {
		region : 'center',
		border : false,
		width : 450,
		sortableColumns : false,
	    viewConfig: {stripeRows: false},
		allowDeselect : true,
	    columns : columns,
		split : true,
		store : store,
		tbar : [{
			id : 'add',
			text : '添加',
			iconCls : 'icon-sys-add',
			handler : function(){
				panel.insertRecord();
			}
		}, '-',{
			id : 'del',
			text : '删除',
			iconCls : 'icon-sys-delete',
			handler : function(){
				panel.deleteRecord();
			}
		}, '-', {
			id : 'ch',
			text : '变更机构',
			handler : function(){
				var record = grid.getSelectionModel().getSelection()[0];
				if(record){
					panel.showMovedUser(record);
				}
			}
		}, '-', {
			xtype: 'combobox',
            store: findUser,
            displayField: 'text',
            width: '30%',
            anchor: '100%',
            applyTo: 'search',
            minChars: 2,
            emptyText : '搜索用户姓名',
            listeners: {
            	'select': function(combo, records){
            		//展开用户所在机构的上级
            		panel.tree.expandPath(records[0].data.path);
            		//选中用户所在机构
            		panel.tree.selectPath(records[0].data.path + "/" + records[0].data.f_dept_id);
            		//选中用户
            		grid.getSelectionModel().select(records);
            		grid.fireEvent('selectionchange',grid,grid.getSelectionModel().getSelection());
            		panel.orgId = records[0].data.f_dept_id;
            	}
            },
            listConfig: {
                loadingText: 'Searching...',
                emptyText: 'No matching posts found.',
                getInnerTpl: function() {
                    return '{text}';
                }
            },
            pageSize: 10
		}, '->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				grid.getSelectionModel().deselectAll();
				grid.getStore().reload();
			}
		}]
	});
	
	// 选中校验
	grid.on('beforeselect', function(g, record, index){
		// 被删除的记录不能选中
		if(record.isDeleted){
			editor.clearRecord();
			return false;
		}
	})
	// 选中
	grid.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			panel.userId = selected[0].data.id;
			if(panel.userId>0){
				if(panel.orgId != -1){
					//浏览已有用户时
					OrganizationDirect.getUserDeptList(panel.userId,function(result,e){
						if(result&&result.success){
							var array = [];
							selected[0].data.orgs = '';
					 		for(var i=0;i<result.depts.length;i++){
					 			array.push([result.depts[i].id,result.depts[i].caption]);
					 			selected[0].data.orgs += result.depts[i].caption+';'
					 		}
					 		//动态加载store
					 		var nProperty = Ext.apply({}, properties['f_dept_id']);
					 		nProperty.store = array;
					 		var propertyEditor = editor.items.get(0).items.get(0);
					 		propertyEditor.sourceConfig['f_dept_id'].editor = propertyEditor.getPropertyEditor(nProperty);
					 		properties.f_dept_id.store = array;
							OrganizationDirect.findUserRoles(panel.userId,function(result,e){
								if(result&&result.success){
									selected[0].data.roles = '';
									for(var i=0;i<result.datas.length;i++){
										selected[0].data.roles += result.datas[i].f_org_caption+'-'+result.datas[i].f_caption+';'
									}
								}
								if(selected[0].data.f_name != ''){
									//登陆名不可更改
									var name = Ext.apply({}, properties['f_name']);
									name.editor = 'none';
									propertyEditor.sourceConfig['f_name'].editor = propertyEditor.getPropertyEditor(name);
								}
								editor.loadRecord(selected[0]);
							});
						}
					});	
				}else{
					//选中已删除用户时
					var deptId = selected[0].data.f_dept_id;
					var deptCaption = selected[0].data.f_dept_caption;
					var array = [];
					array.push([deptId,deptCaption]);
					var nProperty = Ext.apply({}, properties['f_dept_id']);
					nProperty.store = array;
					var propertyEditor = editor.items.get(0).items.get(0);
					propertyEditor.sourceConfig['f_dept_id'].editor = propertyEditor.getPropertyEditor(nProperty);
					editor.loadRecord(selected[0]);
					//已删除用户不可编辑只能查看
					propertyEditor.on('beforeedit', function(e){ 
						if(panel.orgId == -1){
							e.cancel = true;  
	       					return false;  
	       				}
    				}); 
				}
			}else{
				//添加新用户时
				OrganizationDirect.getDeptCaption(panel.orgId,function(result,e){
					if(result&&result.success){
						var array = [];
				 		array.push([panel.orgId,result.text]);
				 		//动态加载store
				 		var nProperty = Ext.apply({}, properties['f_dept_id']);
				 		nProperty.store = array;
				 		var propertyEditor = editor.items.get(0).items.get(0);
				 		propertyEditor.sourceConfig['f_dept_id'].editor = propertyEditor.getPropertyEditor(nProperty);
				 		properties.f_dept_id.store = array;
				 		//登陆名始终可以修改
						var name = Ext.apply({}, properties['f_name']);
						name.editor = 'string';
						propertyEditor.sourceConfig['f_name'].editor = propertyEditor.getPropertyEditor(name);
				 		editor.loadRecord(selected[0]);
					}
				});
			}
		}else{
			editor.clearRecord();
		}
	})

	panel.delAll = function(){
		grid.getSelectionModel().deselectAll();
	}
	
	panel.reStore = function(orgId, tree){
		panel.tree = tree;
		panel.orgId = orgId;
		grid.getStore().reload();
	}
	// 添加记录
	panel.insertRecord = function(){
		var values = AscApp.ClassManager.getPropertyDefaultValues(type);
		//values[orderFieldName] = grid.getStore().getCount() + 1;
		var records = store.add(values);
		grid.getSelectionModel().select(records);
	}
	// 删除记录
	panel.deleteRecord = function(){
		var records = grid.getSelectionModel().getSelection();
		if(records.length > 0){
			// 设置删除标记
			if(records[0].get('id') > 0){
				// 已存在的记录标记删除
				if(records[0].get('f_state')==3){
					Ext.MessageBox.confirm('操作提示','确定要删除用户 '+records[0].get('f_caption')+' 吗？',
						function(btn){
							if(btn=='yes'){
								OrganizationDirect.deleteUser(records[0].get('id'), function(result, e){
									if(result && result.success){
										Asc.common.Message.showInfo('用户删除操作成功。');
										editor.clearRecord();
										grid.getStore().reload();
									}else{
										if(result && result.message){
											Asc.common.Message.showError(result.message);
										}else{
											Asc.common.Message.showError('用户删除操作失败！');
										}
									}
								});					
							}
					},this);
				}else{
					records[0].isDeleted = true;
					records[0].setDirty();
				}
			}else{
				// 新添加的记录直接删除
				grid.getStore().remove(records);
			}
			// 取消选中
			grid.getSelectionModel().deselectAll();
		}
	}
	// 刷新列表
	panel.refresh = function(){
		grid.getStore().reload();
		grid.getSelectionModel().deselectAll();
	}
	
	//机构变更
	panel.showMovedUser = function(record){
		var treepanel = Ext.create('Ext.tree.Panel', {
			border : false,
			height : 250,
			width : 350,
			autoScroll : true,
			store : new Ext.data.TreeStore({
				root : {
					id : 0,
					iconCls : 'icon-manager-root',
					text : '组织机构',
					expanded : true
				},
				proxy : {
					type : 'direct',
					directFn : OrganizationDirect.loadOrganizationNodes,
					reader : {
						type: 'json',
						root : 'datas',
						totalProperty : 'totals',
						successProperty : 'successed',
						messageProperty : 'message'
					}
				}
			}),
			bbar :['->',{
				text: '取消',
				handler : function(){
					win.close();
				}
			},{
				text : '确定',
				iconCls : 'icon-cross-btn-save',
				handler : function(){
					var selected = treepanel.getSelectionModel().getSelection();
					if(selected.length > 0){
						var orgId = selected[0].data.id;
						var userId = record.data.id;
						OrganizationDirect.updateUserOrg(userId.toString(), orgId.toString(), function(result, e){
							if(result && result.success){
								win.close();
								panel.refresh();
								Asc.common.Message.showInfo('变更机构操作成功。');
							}else{
								if(result && result.message){
									Asc.common.Message.showError(result.message);
								}else{
									Asc.common.Message.showError('机构变更操作失败！');
								}
							}
						});
					}
				}
			}]
		});
		//变更组织
		var win = new Ext.Window({
			width : 350,
			height : 200,
			modal : true,
			resizable : false,
			iconCls : 'icon-cross-common-view',
			title : '选择变更机构',
			layout : 'fit',
			items : treepanel
		});
		win.show();
	}
	//密码重置
	panel.retPassword = function(){
		Ext.MessageBox.confirm('操作提示','确定要重置密码吗？',
			function(btn){
				if(btn=='yes'){
					OrganizationDirect.retPassword(panel.userId, function(result, e){
						if(result && result.success){
							Asc.common.Message.showInfo('密码重置操作成功。');
							editor.clearRecord();
							grid.getStore().reload();
						}else{
							if(result && result.message){
								Asc.common.Message.showError(result.message);
							}else{
								Asc.common.Message.showError('密码重置操作失败！');
							}
						}
					});					
				}
		},this);
	}

	// 保存记录
	panel.apply = function(callback){
		var insertRecs = grid.getStore().getNewRecords();
		var updateRecs = grid.getStore().getUpdatedRecords();
		if(insertRecs.length + updateRecs.length > 0){
			var deletes = [];
			var updates = [];
			for(var i in insertRecs){
				var record = insertRecs[i];
				updates.push(record.getData());
			}
			for(var i in updateRecs){
				var record = updateRecs[i];
				if(record.isDeleted){
					deletes.push({id : record.get('id')});
				}else{
					updates.push(Ext.apply({id : record.get('id')},record.getChanges()));
				}
			}
			OrganizationDirect.saveUsers(panel.orgId, {
				updates : updates,
				deletes : deletes
			}, function(result, e){
				if(result && result.success){
					if(result.sObjects && result.sObjects.length > 0){
						var str = '';
						for(var n in result.sObjects){
							str = str + '<br>[' + result.sObjects[n].id + ']';
						}
						Asc.common.Message.showInfo('共[' + result.sObjects.length + ']个对象提交成功：' + str);
					}
					
					if(result.fObjects && result.fObjects.length > 0){
						var str = '';
						for(var n in result.fObjects){
							str += '<br>';
							if(result.fObjects[n].id) {
								str += '[' + result.fObjects[n].id + ']';
							}
							if(result.fObjects[n].msg) {
								str += result.fObjects[n].msg;
							}
						}
						Asc.common.Message.showError('共[' + result.fObjects.length + ']个对象提交失败：' + str);
					} else {
						panel.refresh();
					}
					// 调用回调函数
					if(Ext.isFunction(callback)) {
						callback();
					}
				}else{
					if(result && result.message){
						Asc.common.Message.showError(result.message);
					}else{
						Asc.common.Message.showError('保存对象操作失败！');
					}
				}
			});
		}
	}
	
	// 装载并显示组织树
	panel.add({
		layout : 'border',
		border : false,
		items : [grid, editor]
	});
	panel.doLayout();
});
</script>