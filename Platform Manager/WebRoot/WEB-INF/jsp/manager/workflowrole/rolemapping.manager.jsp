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
		collapseMode : 'mini',
		minWidth : 180,
		flex : 20,
		jspUrl : 'manager/workflowrole/org.tree'
	});
	
	// 应用树
 	var appTreePanel = Ext.create('Asc.extension.JspPanel', {
		title : '应用列表',
		region : 'west',
		split : true,
		collapseMode :'mini',
		minWidth : 180,
		flex : 20,
		jspUrl : 'manager/workflowrole/application.tree'
	}); 
	
 	var mrStore = Ext.create('Ext.data.Store', {
		clearRemovedOnLoad : true,
		proxy : {
			type : 'direct',
			directFn : WorkFlowRoleMappingDirect.load,
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
			name: 'f_flow_key', 
			type: 'string'
		}, {
			name: 'f_flow_caption', 
			type: 'string'
		}, {
			name: 'f_wrole_key', 
			type: 'string'
		}, {
			name: 'f_wrole_caption', 
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
			property : 'f_flow_key',
			direction: 'ASC'
		}, {
			property : 'f_wrole_key',
			direction: 'ASC'
		}],
		listeners : {
			'beforeload' : function(s, operation, eOpts){
				return beforeMrmsReload(operation);
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
	
	// 数据列表界面
 	var grid = Ext.create('Ext.grid.Panel', {
 		title : '流程角色列表',
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
	    	text : '流程',
	    	dataIndex : 'f_flow_caption'
	    }, {
	    	text : '流程角色ID',
	    	dataIndex : 'f_wrole_key'
	    }, {
	    	text : '流程角色',
	    	dataIndex : 'f_wrole_caption',
	    	renderer : function(value, metaData, record) {
	    		if(record.get('isDeleted')){
	    			return '(已删除)' + value;
	    		}
	    		return value;
	    	}
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
		}, '->', {
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
			callback: function(value, rawValue){
				selection.every(function(record, index){
					record.set('f_auth_expression', value);
					record.set('f_auth_caption', rawValue);
					record.setDirty();
					return true;
				});
			}
		}).show();
	};
	
	// 保存记录
	panel.doApply = panel.doSave = function() {
		var updateRecs = grid.getStore().getUpdatedRecords();
		if(updateRecs.length > 0){
			var datas = [];
			for(var i in updateRecs){
				var record = updateRecs[i];
				datas.push(record.data);
			}
			WorkFlowRoleMappingDirect.save(1, datas, function(result, e){
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
		items : [appTreePanel, orgTreePanel, grid]
	});
	panel.doLayout();
});
</script>