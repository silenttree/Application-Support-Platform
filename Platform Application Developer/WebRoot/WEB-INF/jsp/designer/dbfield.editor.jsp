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
	var tableId = panel.object.key;//当前表的id（也是当前表设计对象的key）
	var appId = panel.object.appId;
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
			text : '引入字段',
			iconCls : 'icon-sys-importfield',
			handler : function(){
				DeveloperAppDirect.buildTableFields(appId, tableId, function(result, e){
					if(result && result.success){
						//AscApp.getAscDesigner().reloadNode();
						panel.refresh();
					}else{
						Asc.common.Message.showError(result.message);
					}
				});
			}
		},{
			text : '在数据库中创建',
			iconCls : 'icon-sys-createtable',
			handler : function(){
				var maxlength = 0;
				editor.getStore().each(function(record){
					var len = record.get('f_key').length;
					maxlength = len > maxlength?len:maxlength;
				});
				var formatKey = function(key){
					var rs = key + ' ';
					for(var i = 0; i < (maxlength - key.length); i++){
						rs = rs + ' ';
					}
					return rs;
				};
				var alterPrimaryKey = 'ALTER TABLE ' + panel.object.key + ' ADD CONSTRAINT PK_' 
				+ panel.object.key + ' PRIMARY KEY('; 
				var createTableSql = 'CREATE TABLE ' + panel.object.key +'(\n';
				var flag = false;//根据flag判断是否存在主键
				//拼接sql语句，并格式化
				editor.getStore().each(function(record){
					if(record.data.f_keyfield){
						alterPrimaryKey = alterPrimaryKey + record.data.f_key + ',';
						flag = true;
					}
					if(record.data.f_db_type == 'Number'){
						createTableSql = createTableSql  + '    ' + formatKey(record.get('f_key'))
						+ 'NUMBER' + record.data.f_db_type_ext 
						+ (record.data.f_allownull?'':' NOT NULL') + ',\n';
					}else if(record.data.f_db_type == 'Char'){
						createTableSql = createTableSql + '    ' + formatKey(record.get('f_key'))
						+ 'NVARCHAR2(' + (record.data.f_length==''?'50':record.data.f_length) + ')' 
						+ (record.data.f_allownull?'':' NOT NULL') + ',\n';
					}else if(record.data.f_db_type == 'Datetime'){
						createTableSql = createTableSql + '    ' + formatKey(record.get('f_key'))
						+ 'DATE' + (record.data.f_allownull?'':' NOT NULL') + ',\n';
					}else if(record.data.f_db_type == 'Blob'){
						createTableSql = createTableSql + '    ' + formatKey(record.get('f_key'))
						+ 'BLOB' + (record.data.f_allownull?'':' NOT NULL') + ',\n';
					}else if(record.data.f_db_type == 'Clob'){
						createTableSql = createTableSql + '    ' + formatKey(record.get('f_key'))
						+ 'CLOB' + (record.data.f_allownull?'':' NOT NULL') + ',\n';
					}
				});
				createTableSql = createTableSql.substring(0,createTableSql.length-2) + '\n);'; 
				if(flag){
					createTableSql = createTableSql + '\n' + alterPrimaryKey.substring(0,alterPrimaryKey.length-1) + ');'
				}
				var sqlWindow = Ext.create('Ext.window.Window',{
					width : 600,
					height : 450,
					title : '创建数据库语句',
					autoScroll : true,
					modal : true,
					bodyStyle : {
					    padding : '10 30 10 30',
					    background : 'white'
					},
					html : '<pre style="font-size:14px;">' + createTableSql + '</pre>',
					bbar : ['->',{
						text : '取消',
						iconCls : 'icon-sys-cancel',
						handler : function(){
							sqlWindow.close();
						}
					},{
						text : '确认创建',
						iconCls : 'icon-sys-confirm',
						handler : function(){
							//执行sql语句创建
							DeveloperAppDirect.createTable(panel.object.appId, panel.object.key, createTableSql, function(result,e){
								if(result && result.success){
									Asc.common.Message.showInfo('创建数据表成功！');
								}else{
									Asc.common.Message.showError(result.message);
								}
							});
							sqlWindow.close();
						}
					}]
				}).show();
			}
		},{
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				AscApp.getAscDesigner().reloadNode();
				panel.refresh();
			}
		}]
	});
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