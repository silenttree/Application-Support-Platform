<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script>

Ext.onReady(function(){
	Ext.require('Asc.common.CdruSelector');
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_app_ent_auth';
	var typeClass = AscApp.ClassManager.getClass(type);
	// 获得对象属性列表
	var properties = AscApp.ClassManager.getProperties(type);
	// 初始化视图列
	var columns = AscApp.ClassManager.getGridColumns(properties, typeClass.propertyColumns);
	columns[1].xtype = 'treecolumn';
	// 初始化存储字段
	var fields = AscApp.ClassManager.getStoreFields(properties);
	
	var orderFieldName = 'f_order';
	// 初始化数据存储对象
	var store = new Ext.data.TreeStore({
		clearRemovedOnLoad : true,
		autoLoad : false,
		root : {
	        expanded: true,
	        children: []
	    },
		proxy : {
			type : 'direct',
			directFn : AppEntDirect.loadAppEntranceTree
		},
		sorters : [{
			property : orderFieldName
		}],
		fields : fields
	});

	
	// 数据列表界面
	var grid = Ext.create('Ext.tree.Panel', {
		region : 'center',
		rowLines : true,
		border : false,
		sortableColumns : false,
        useArrows: true,
        rootVisible: false,
	    viewConfig: {stripeRows: true},
		allowDeselect : true,
		selType: 'checkboxmodel',
	    columns : columns,
	    flex : 40,
		store : store,
		tbar : [{
			text : '授权',
			iconCls : 'icon-manager-applicationentranceauthority',
			handler : function (){
				grid.auth();
			}
		},'->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				grid.getStore().reload();
			}
		}]
	});
	
	// 刷新列表
	grid.refresh = function(){
		grid.getStore().reload();
		grid.getSelectionModel().deselectAll();
	};
	// 保存记录
	panel.doApply = panel.doSave = function(){
		var updateRecs = grid.getStore().getUpdatedRecords();
		if(updateRecs.length > 0){
			var updates = [];
			for(var i in updateRecs){
				var record = updateRecs[i];
				updates.push(Ext.apply({id : record.get('id')},record.getChanges()));
			}
			AppEntDirect.saveAppEntAuths({
				updates : updates,
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
							str = str + '<br>[' + result.fObjects[n].id + ']';
						}
						Asc.common.Message.showError('共[' + result.fObjects.length + ']个对象提交失败：' + str);
					}
					grid.refresh();
				}else{
					if(result && result.message){
						Asc.common.Message.showError(result.message);
					}else{
						Asc.common.Message.showError('保存对象操作失败！');
					}
				}
			});
		}
	};

	// 显示界面
	panel.add({
		layout : 'border',
		border : false,
		items : [grid]
	});
	panel.doLayout();
	// 装载数据
	grid.getStore().load();
	
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
			selection.every(function(record, index, me){
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
				selection.every(function(record, index, me){
					record.set('f_auth_expression', value);
					record.set('f_auth_display', rawValue);
					return true;
				});
			}
		}).show();
	};
	

});
</script>