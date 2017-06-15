<%@page contentType="text/html;charset=UTF-8"%>
	panel.addEvents("applicationchange");
 	var mappingStore = Ext.create('Ext.data.Store', {
		clearRemovedOnLoad : true,
		proxy : {
			type : 'direct',
			directFn : ModuleRoleMappingDirect.loadByCDRUAndAppId,
			paramOrder : ['key','appId', 'isAll'],
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
			name: 'f_application_id', 
			type: 'int'
		}, {
			name: 'f_application_key', 
			type: 'string'
		}, {
			name: 'f_module_key', 
			type: 'string'
		}, {
			name: 'f_module_caption', 
			type: 'string'
		}, {
			name: 'f_mrole_key', 
			type: 'string'
		}, {
			name: 'f_mrole_caption', 
			type: 'string'
		}, {
			name: 'f_org_ids', 
			type: 'string'
		}, {
			name: 'f_org_captions', 
			type: 'string'
		}, {
			name: 'f_is_deleted', 
			type: 'boolean'
		}, {
			name: 'f_is_select',
			type: 'boolean'
		}, {
			name: 'f_owner_captions',
			type: 'string'
		}],
		sorters : [{
			property : 'f_module_key',
			direction: 'ASC'
		}, {
			property : 'f_mrole_key',
			direction: 'ASC'
		}],
		listeners : {
			load: function(s, recs) {
				moduleCombo.getStore().removeAll();
				var modules = [];
				for(var i = 0; i < recs.length; i++) {
					Ext.Array.include(modules, recs[i].get('f_module_caption'));
				}
				for(var i = 0; i < modules.length; i++) {
					moduleCombo.getStore().add({key: modules[i], text: modules[i]});
				}
				filerGrid();
			}
		}
	});
 	
 	var appStore = Ext.create('Ext.data.Store', {
 		fields: ['id', 'text', 'key'],
		proxy : {
			type : 'direct',
			directFn : AppEntDirect.loadApplicationNodes,
			reader : {
				type: 'json',
				root : 'datas',
				successProperty : 'success',
				messageProperty : 'message'
			}
		}
	});
	
	var appCombo = Ext.create('Ext.form.ComboBox', {
		emptyText: '选择应用',
		store: appStore,
		displayField: 'text',
		valueField: 'id',
		listeners: {
			change: function(combo, newVal, oldVal) {
				panel.fireEvent("applicationchange", combo, newVal);
			}
		}
	});
 	
	var filerGrid = function() {
		mappingGrid.getSelectionModel().deselectAll();
		mappingGrid.getStore().clearFilter();
		var moduleQryWord = moduleCombo.getValue() || "";
		var roleQryWord = searchInput.getValue() || "";
		if(roleQryWord.length > 0 || moduleQryWord.length > 0) {
			mappingGrid.getStore().filterBy(function(rec){
				if(moduleQryWord.length > 0 && rec.get('f_module_caption').indexOf(moduleQryWord) === -1) {
					return false;
				}
				if(roleQryWord.length > 0 && rec.get('f_mrole_caption').indexOf(roleQryWord) === -1) {
					return false;
				}
				return true;
			});
		}
	};
	
	var moduleCombo = Ext.create("Ext.form.field.ComboBox", {
		emptyText: '选择或输入模块',
		queryMode: 'local',
		store: Ext.create("Ext.data.Store", {
			fields: ["key", "text"],
			data: []
		}),
		valueField: 'key',
		displayField: 'text',
		listeners: {
			change: filerGrid
		}
	});
	
	var searchInput = Ext.create("Ext.form.field.Text", {
		emptyText: '输入模块角色关键字',
		listeners: {
			change: filerGrid
		}
	});
 	
	// 数据列表界面
 	var mappingGrid = Ext.create('Ext.grid.Panel', {
 		title : '模块角色列表',
		region : 'center',
		border : false,
		sortableColumns : false,
		rowLines : true,
	    viewConfig : {stripeRows: true},
		allowDeselect : true,
	    columns : [{
            xtype: 'checkcolumn',
            header: '授权',
            dataIndex: 'f_is_select',
            width: 40,
            stopSelection: false,
            listeners: {
            	beforecheckchange: function(cc, rowIdx, checked, eOpts) {
            		if(wrapPanel.isAll()) {
            			Asc.common.Message.showInfo("查看状态下不能修改权限");
            			return false;
            		}
            		return true;
            	}, 
            	checkchange: function(cc, rowIdx, checked, eOpts) {
            		var rec = mappingGrid.getStore().getAt(rowIdx);
            		if(checked) {
            			if(rec.get("f_org_ids") == null || rec.get("f_org_ids") == "") {
                			var manageUnit = manageUnitWindow.getValues();
                			if(manageUnit) {
                				rec.set("f_org_ids", manageUnit.orgIds);
                    			rec.set("f_org_captions", manageUnit.orgCaptions);
                			}
            			}
            			if(rec.get("f_org_ids") == null || rec.get("f_org_ids") == "") {
            				panel.currentRec = rec;
        					manageUnitWindow.show();
        					manageUnitWindow.select(panel.currentRec.get('f_org_ids'));
            			}
            		} else {
        				rec.set("f_org_ids", "");
            			rec.set("f_org_captions", "");
        			}
            	}
            }
        }, {
	    	text : '模块',
	    	dataIndex : 'f_module_caption',
	    	sortable: true,
	    	width: 60
	    }, {
	    	text : '模块角色ID',
	    	dataIndex : 'f_mrole_key',
	    	sortable: true,
	    	width: 80
	    }, {
	    	text : '模块角色',
	    	dataIndex : 'f_mrole_caption',
	    	sortable: true,
	    	renderer : function(value, metaData, record) {
	    		if(record.get('f_is_deleted')){
	    			return '(已删除)' + value;
	    		}
	    		return value;
	    	},
	    	width: 350
	    }, {
	    	text : '权限管理单位',
	    	dataIndex : 'f_org_captions',
	    	width: 300,
	    	renderer: function(val, md, rec) {
	    		if(rec.get("f_is_select")) {
	    			if(!val || val == "") {
	    				return '<span style="cursor:hand;text-decoration:underline;color:red">(空)</span>';
	    			} else {
	    				return '<span style="cursor:hand;text-decoration:underline">' + val + '</span>';
	    			}
	    		} else {
	    			return val;
	    		}
	    	}
	    }, {
	    	text : '权限所有者',
	    	dataIndex : 'f_owner_captions',
	    	width: 300
	    }],
		store : mappingStore,
		tbar : [appCombo, moduleCombo, searchInput, '->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				mappingGrid.getStore().reload();
			}
		}],
		listeners: {
			cellclick: function(grid, td, cellIdx, record, tr, rowIdx, e, eOpts) {
				if((cellIdx === 4) && !wrapPanel.isAll() && record.get("f_is_select")) {
					panel.currentRec = record;
					manageUnitWindow.show();
					manageUnitWindow.select(panel.currentRec.get('f_org_ids'));
				}
			},
			destroy: function(grid) {
				// 销毁的时候顺便销毁窗口
				manageUnitWindow.destroy();
			}
		}
	});
 	
	// 维护单位的grid
	var manageUnitGrid = Ext.create("Ext.grid.Panel", {
		selType: 'checkboxmodel',
		selModel: {
			mode: 'SIMPLE'
		},
		border: false,
		store: Ext.create("Ext.data.Store", {
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
					if(!successful) {
						store.removeAll();
						return;
					} 
					// 只留下单位或者部门
					for(var i = records.length - 1; i >= 0; i--) {
						var rec = records[i];
						if(rec.get('key').slice(0, 1) !== 'C' && rec.get('key').slice(0, 1) !== 'D') {
							store.removeAt(i);
						}
					}
				}
			}
		}),
		columns: [{
			text: '',
			dataIndex: 'iconCls',
			width: 23,
			resizable: false,
			renderer: function(value, metaData, record, row, col, store, gridView){
				return '<div class="' + value + ' asc-cdruselector-view-icon-div"></div>';
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
		}]
	});
	
	var manageUnitWindow = Ext.create("Ext.window.Window", {
		title: "选择权限管理单位",
		width: 300,
		height: 300,
		closeAction: 'hide',
		layout: 'fit',
		modal: true,
		items: [manageUnitGrid],
		buttonAlign: 'center',
		buttons: [{
			xtype: 'button',
			text: '确定',
			handler: function() {
				panel.setManageUnit(manageUnitWindow.getValues());
				manageUnitWindow.close();
			}
		}, {
			xtype: 'button',
			text: '关闭',
			handler: function() {
				manageUnitWindow.close();
			}
		}]
	});
	
	// 根据机构的IDs字符串选中权限管理单位列表
	manageUnitWindow.select = function(orgIds) {
		manageUnitGrid.getSelectionModel().deselectAll();
		var recs = manageUnitGrid.getStore().queryBy(function(rec) {
			var orgIds2 = ";" + orgIds;
			if(orgIds2.indexOf(";" + rec.get("key").substring(2) + ";") > -1) {
				return true;
			} else {
				return false;
			}
		});
		recs.each(function(item) {
			manageUnitGrid.getSelectionModel().select(item, true);
			return true;
		});
	};
	
	manageUnitWindow.getValues = function() {
		var recs = manageUnitGrid.getSelectionModel().getSelection();
		if(recs.length === 0) {
			return null;
		}
		var result = {orgIds: "", orgCaptions: ""};
		for(var i = 0 ; i < recs.length; i++) {
			result.orgIds += recs[i].get("key").substring(2) + ";";
			result.orgCaptions += recs[i].get("text") + ";";
		}
		return result;
	};
	

	
	panel.loadManageUnit = function(key) {
		manageUnitGrid.getStore().load({
			params: {
				key: key
			}
		});
	};
	
	panel.clearManageUnit = function() {
		manageUnitGrid.getStore().removeAll();
	};
	
	panel.setManageUnit = function(manageUnit) {
		if(manageUnit) {
			panel.currentRec.set("f_org_ids", manageUnit.orgIds);
			panel.currentRec.set("f_org_captions", manageUnit.orgCaptions);
		} else {
			panel.currentRec.set("f_org_ids", "");
			panel.currentRec.set("f_org_captions", "");
		}
		console.log(manageUnit);
	};
 	
 	panel.loadMappingGrid = function (relativeKey, appId, isAll) {
 		if(!relativeKey || !appId) {
 	 		mappingStore.removeAll();
 		}
 		mappingStore.load({
 			params: {
 				key: relativeKey,
 				appId: appId,
 				isAll: isAll
 			}
 		});
 	};
 	
 	panel.clearMappingGrid = function(){
 		mappingStore.removeAll();
 	};
 	
 	panel.loadManagerUnitRec = function(recs) {
 		manageUnitGrid.getStore().removeAll();
 		manageUnitGrid.getStore().insert(0, recs);
 	};
 	
 	// 保存记录
	panel.doApply = panel.doSave = function() {
		var updateRecs = mappingGrid.getStore().getUpdatedRecords();
		if(updateRecs.length > 0){
			var datas = [];
			for(var i in updateRecs){
				if(updateRecs[i].get("f_is_select") && (!updateRecs[i].get("f_org_ids") || updateRecs[i].get("f_org_ids") == "")) {
					console.log(updateRecs[i]);
					Asc.common.Message.showError('需要选中权限管理单位');
					return; 
				}
				var relativeKey = wrapPanel.getRelativeKey();
				var data = Ext.clone(updateRecs[i].data);
				data.f_relative_key = wrapPanel.getRelativeKey();
				datas.push(data);
			}
			ModuleRoleMappingDirect.save2(1, datas, function(result, e){
				if(result && result.success){
					Asc.common.Message.showInfo(result.message);
					mappingGrid.getStore().reload();
				}else{
					Asc.common.Message.showError('保存对象操作失败！');
				}
			});
		}
	};
 	panel.add(mappingGrid);