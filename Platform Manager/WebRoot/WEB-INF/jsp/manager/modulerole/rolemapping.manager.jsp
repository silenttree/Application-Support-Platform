<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	
	// 机构树
	var orgTreePanel = Ext.create('Asc.extension.JspPanel', {
		title : '机构列表',
		region : 'west',
		split : true,
		collapseMode : 'header',
		collapsible: true,
		border : false,
		minWidth : 180,
		flex : 20,
		jspUrl : 'manager/modulerole/org.tree'
	});
	
	// 应用树
 	var appTreePanel = Ext.create('Asc.extension.JspPanel', {
		title : '应用列表',
		region : 'west',
		collapsible: true,
		split : true,
		collapseMode : 'header',
		border : false,
		minWidth : 180,
		flex : 20,
		jspUrl : 'manager/modulerole/application.tree'
	}); 
	
 	var mrStore = Ext.create('Ext.data.Store', {
		clearRemovedOnLoad : true,
		proxy : {
			type : 'direct',
			directFn : ModuleRoleMappingDirect.load,
			paramOrder : ['appId','orgId'],
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
			name: 'f_application_id', 
			type: 'int'
		}, {
			name: 'f_org_id', 
			type: 'int'
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
			name: 'f_auth_expression', 
			type: 'string'
		}, {
			name: 'f_auth_caption', 
			type: 'string'
		}, {
			name: 'isDeleted', 
			type: 'boolean'
		}],
		sorters : [{
			property : 'f_module_key',
			direction: 'ASC'
		}, {
			property : 'f_mrole_key',
			direction: 'ASC'
		}],
		listeners : {
			'beforeload' : function(s, operation, eOpts){
				return beforeMrmsReload(operation);
			},
			'load': function(s, recs) {
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
 	
 	var beforeMrmsReload = function(operation){
 		var orgId = orgTreePanel.getSelectedId(),
 			appId = appTreePanel.getSelectedId();
 		// 没有选中机构或者应用，不重新加载
 		if(orgId === -1 || appId <= 0) {
 			return false;
 		}
		operation.params = {};
		operation.params.appId = appId;
		operation.params.orgId = orgId;
		return true;
	};
	
	var filerGrid = function() {
		grid.getSelectionModel().deselectAll();
		grid.getStore().clearFilter();
		var moduleQryWord = moduleCombo.getValue() || "";
		var roleQryWord = searchInput.getValue() || "";
		if(roleQryWord.length > 0 || moduleQryWord.length > 0) {
			grid.getStore().filterBy(function(rec){
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
 	var grid = Ext.create('Ext.grid.Panel', {
 		title : '模块角色列表',
		region : 'center',
		border : false,
		selType : 'checkboxmodel',
		selModel : {
			mode : 'SIMPLE'
		},
		sortableColumns : false,
		rowLines : true,
	    viewConfig : {stripeRows: true},
		allowDeselect : true,
	    columns : [{
	    	text : '模块',
	    	dataIndex : 'f_module_caption',
	    	width: 60
	    }, {
	    	text : '模块角色ID',
	    	dataIndex : 'f_mrole_key',
	    	width: 80
	    }, {
	    	text : '模块角色',
	    	dataIndex : 'f_mrole_caption',
	    	renderer : function(value, metaData, record) {
	    		if(record.get('isDeleted')){
	    			return '(已删除)' + value;
	    		}
	    		return value;
	    	},
	    	width: 350
	    }, {
	    	text : '权限',
	    	dataIndex : 'f_auth_caption',
	    	flex : 1
	    }],
	    flex : 60,
		store : mrStore,
		tbar : [{
			text : '授权',
			iconCls : 'icon-manager-applicationentranceauthority',
			handler : function (){
				grid.auth();
			}
		}, '-', {
			text : '清空权限',
			iconCls : 'icon-sys-delete',
			handler : function(){
				grid.clearAuth();
			}
		}, moduleCombo, searchInput, '->', {
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
	
	// 清空权限
	grid.clearAuth = function() {
		var selection = grid.getSelectionModel().getSelection();
		if(selection.length === 0){
			Asc.common.Message.showInfo('请选择一条或多条数据');
			return;
		}
		selection.every(function(record, index) {
			record.set('f_auth_expression', '');
			record.set('f_auth_caption', '');
			record.setDirty();
			return true;
		});
	};
	
	//授权
	grid.auth = function () {
		var authValue,
			selection = grid.getSelectionModel().getSelection();
		console.log(selection);
		if(selection.length === 0){
			Asc.common.Message.showInfo('请选择一条或多条数据');
			return;
		}
		if(selection.length === 1){
			authValue = selection[0].get('f_auth_expression');
		} else {
			authValue = '';
			selection.every(function(record, index){
				if(authValue === ''){
					authValue = record.get('f_auth_expression');
				}else if(authValue !== record.get('f_auth_expression')){
					authValue = '';
					return false;
				}
				return true;
			});
		}
		Ext.create('Asc.common.CdruSelector',{
			selectType: 'cdru',
			singleSelect: false,
			value: authValue,
			range: 'C_' + orgTreePanel.getSelectedId(),
			callback: function(value, rawValue){
				var check = grid.checkSelectModuleRole();
				if(!check){
					Ext.MessageBox.confirm('请确认','确认给选中的模块角色设置权限吗?',
					      	function(btn){
				                if( btn == 'yes'){ 
				                	grid.setAuthValue(selection, value, rawValue);
				    			}
			                }  
			            );
				}else{
					grid.setAuthValue(selection, value, rawValue);
				}
			}
		}).show();
	};
	
	grid.setAuthValue = function(selection, value, rawValue){
		selection.every(function(record, index){
			record.set('f_auth_expression', value);
			record.set('f_auth_caption', rawValue);
			record.setDirty();
			return true;
		});
	}
	
	grid.checkSelectModuleRole = function(){
		var moduleRoles = grid.getSelectionModel().getSelection();
		var check = true;
		var count = moduleRoles.length;
		if(count > 1){
			check = false;
		}
// 		Ext.Array.each(moduleRoles, function(moduleRole, index){
// 			if(index != count - 1){
// 				if(moduleRole.get('f_auth_expression') != moduleRoles[index+1].get('f_auth_expression')){
// 					check = false;
// 					return false;
// 				}
// 			}
// 		});
		return check;
	}
	
	// 保存记录
	panel.doApply = panel.doSave = function() {
		var updateRecs = grid.getStore().getUpdatedRecords();
		if(updateRecs.length > 0){
			var datas = [];
			for(var i in updateRecs){
				var record = updateRecs[i];
				datas.push(record.data);
			}
			ModuleRoleMappingDirect.save(1, datas, function(result, e){
				if(result && result.success){
					Asc.common.Message.showInfo(result.message);
					grid.refresh();
				}else{
					Asc.common.Message.showError('保存对象操作失败！');
				}
			});
		}
	};
	
	orgTreePanel.selectionchange = function(){
		grid.refresh();
	};
	
	appTreePanel.selectionchange = function(){
		grid.refresh();
	};

	// 显示界面
	panel.add({
		layout : 'border',
		border : false,
		items : [appTreePanel, orgTreePanel,  grid]
	});
	panel.doLayout();
});
</script>