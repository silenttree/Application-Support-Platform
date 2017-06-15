<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = '<%=type%>';
	var typeClass = AscApp.ClassManager.getClass(type);
	// 修改标题
	if(!panel.title){
		panel.setTitle(typeClass.text + '列表');
	}
	// 获得对象属性列表
	var properties = AscApp.ClassManager.getProperties(type);
	// 创建界面
	var editor = new Asc.extension.ObjectGridEditor(panel.object, type, properties, typeClass.propertyColumns, {
		border : false,
		tbar : [{
			text : '添加',
			iconCls : 'icon-sys-add',
			handler : function(){
				AscApp.ActionManager.runFunction('designer.createDesignObject', 
					editor,
					[panel.object.appId, panel.object.key, type, function(){
						panel.refresh();
						AscApp.getAscDesigner().reloadNode();
					}]
				);
			}
		}, {
			text : '修改键值',
			iconCls : 'icon-sys-rename'
		}, {
			text : '删除',
			iconCls : 'icon-sys-delete',
			handler : function(){
				var records = editor.getSelectionModel().getSelection();
				if(records.length > 0){
					var record = records[0];
					var obj = {
						appId : panel.object.appId,
						type : type,
						key : record.get('id')
					}
					AscApp.ActionManager.runFunction('designer.doDeleteObject', this, [obj, function(obj){
						// 删除后刷新界面
						panel.refresh();
					}, this]);
				}
			}
		}, '-',{
			text : '复制',
			iconCls : 'icon-sys-copy'
		}, {
			text : '粘贴',
			iconCls : 'icon-sys-paste'
		}, '-',{
			text : '上移',
			iconCls : 'icon-sys-moveup',
			disabled : !Ext.isDefined(properties['f_order']),
			handler : function(){
				var sm = editor.getSelectionModel();
				var store = editor.getStore();
				var records = editor.getSelectionModel().getSelection();
				if(records.length > 0){
					var record = records[0];
					var index = store.indexOf(record);
					if(index > 0){
						recordUp = store.getAt(index - 1);
						record.set('f_order', index);
						recordUp.set('f_order', index + 1);
					}
				}
				store.sort();
			}
		}, {
			text : '下移',
			iconCls : 'icon-sys-movedown',
			disabled : !Ext.isDefined(properties['f_order']),
			handler : function(){
				var sm = editor.getSelectionModel();
				var store = editor.getStore();
				var records = editor.getSelectionModel().getSelection();
				if(records.length > 0){
					var record = records[0];
					var index = store.indexOf(record);
					if(index < store.getCount() - 1){
						recordDown = store.getAt(index + 1);
						record.set('f_order', index + 2);
						recordDown.set('f_order', index + 1);
					}
				}
				store.sort();
			}
		},'->',{
			text : '引入输入域',
			iconCls : 'icon-sys-importfield',
			handler : function(){
				showInputField();
			}
		}, {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				AscApp.getAscDesigner().reloadNode();
				panel.refresh();
			}
		}]
	});
	//显示输入域
	var showInputField = function(){
		var gstore = Ext.create('Ext.data.Store', {
		    fields: ['id','f_key','f_name','f_caption'],
			proxy : {
				type : 'direct',
				directFn : DeveloperAppDirect.loadDbFields,
				extraParams : {
					appId : panel.object.appId,
					id : panel.object.key,
					type : 'dbfield'
				},
				paramOrder:['appId','id','dbfield']
			},
			sorters : [{
				property : 'id',
				direction : 'ASC'
			},{
				property : 'f_key',
				direction : 'ASC'
			}],
			autoLoad : true,
			listeners : {
				load : function(st, records, successful){
					if(records.length == 0){
						Asc.common.Message.showError('未配置数据表或者配置的数据表中没有字段！');
					}
				}
			}
		});
		var grid = Ext.create('Ext.grid.Panel',{
			selType: 'checkboxmodel',
			selModel: {
				mode : 'SIMPLE'
			},
			store : gstore,
			columns: [
				{header : 'id',  dataIndex : 'id', hidden : true},
				{header : 'Key', dataIndex : 'f_key', flex : 1},
				{header : '英文名', dataIndex : 'f_name', flex : 1},
				{header : '中文名', dataIndex : 'f_caption'}
			]       
		});
		var win = Ext.create('Ext.window.Window',{
			width : 650,
			height : 400,
			modal : true,
			items : [grid],
			autoScroll : true,
			bbar:['->',{
				text : '取消',
				iconCls : 'icon-sys-cancel',
				handler : function(){
					win.close();
				}
			},{
				text : '确定',
				iconCls : 'icon-sys-confirm',
				handler : function(){
					var records = grid.getSelectionModel().getSelection();
					var datas = [];
					if(records.length>0){
						for(var i = 0;i < records.length;i++){
							datas.push(records[i].raw);
						}
						DeveloperAppDirect.importFields(panel.object.appId, panel.object.key, "inputfield", datas, function(result,e){
							if(result && result.success){
								Asc.common.Message.showInfo('引入输入域成功!');
								editor.getStore().reload();
							}else{
								Asc.common.Message.showError('引入输入域失败，' + result.message);
							}
						});
					}
					win.close();
				}
			}]
		}).show();
	};
	// 执行保存
	panel.doApply = panel.doSave = function(){
		var records = editor.getStore().getModifiedRecords();
		if(records.length > 0){
			var datas = [];
			for(var i in records){
				var record = records[i];
				datas.push(Ext.apply({id : record.get('id')},record.getChanges()));
			}
			AscApp.ActionManager.runFunction('designer.doSaveObjects', 
				editor,
				[panel.object.appId, datas, function(){
					panel.refresh();
				}]
			);
		}
	}
	// 刷新，装载数据
	panel.refresh = function(){
		editor.getStore().reload();
	}
	// 显示界面
	panel.add(editor);
	panel.doLayout();
	// 装载数据
	editor.getStore().load();
});
</script>