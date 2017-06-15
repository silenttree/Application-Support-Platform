<%@page import="com.google.gson.JsonArray"%>
<%@page import="com.asc.framework.importfile.FileImportor"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String contextPath = request.getContextPath();
	long fileImportLogId = Long.valueOf(request
			.getParameter("fileImportLogId"));
	String appKey = request.getParameter("appKey");
	FileImportor fi = FileImportor.load(fileImportLogId);
	JsonArray paramsConfig = fi.getDeleteParamsConfig();
%>
<script>
Ext.onReady(function () {
	var panel = Ext.getCmp('<%=panelid%>');
	var fileImportLogId = <%=fileImportLogId%>;
	var appKey = '<%=appKey%>';
	var paramsConfig = <%=paramsConfig%>;
	var cellEditing = Ext.create('Ext.grid.plugin.CellEditing', {
		clicksToEdit: 1
	});
	var delParamsGrid =  Ext.create('Ext.grid.Panel', {
		hidden : true,
		frame : true,
		border : false,
		height : 95,
		plugins : [cellEditing],
		columns : [{ text: '', width : 30, dataIndex: 'f_is_default', xtype : 'checkcolumn' ,
						editor: {
							xtype: 'checkbox',
							cls: 'x-grid-checkheader-editor'
						}},
				   { text: '参数名',  dataIndex: 'f_caption'},
				   { text: '参数值',  dataIndex: 'f_value',flex : 1, field: {
						   xtype: 'textfield'
					   },renderer : function(v, metaData, record){
						   metaData.style = 'background-color:lightgreen;';
						   return v;
					   }
				   }],
		store : Ext.create('Ext.data.Store', {
			fields:['id', 'f_is_default', 'f_caption', 'f_value','f_sql', 'f_null_sql'],
			data:[],
			proxy: {
				type: 'memory',
				reader: {
					type: 'json'
				}
			}
		})
	});
	delParamsGrid.getStore().loadData(paramsConfig);
	var fileInfoForm = Ext.create('Ext.form.Panel',{
		width: '100%',
		frame : true,
		border : false,
		layout : 'hbox',
		items : [{
			xtype : 'container',
			border : false,
			layout : 'anchor',
			flex : 1,
			padding : 5,
			defaults: {
				anchor : '100%',
				xtype : 'displayfield',
				labelWidth : 60
			},
			items : [{
				fieldLabel : '方案名称',
				name : 'schemaCaption'
			}, {
				fieldLabel : '文件名',
				name : 'clientFileName'
			}, {
				fieldLabel : '文件大小',
				name : 'fileLength'
			}, {
				fieldLabel : '数据量',
				name : 'dataCount'
			}]
		}, {
			xtype : 'container',
			anchor : '100%',
			columns: 1,
			flex : 1,
			border : false,
			frame : true,
			items : [{
				xtype : 'checkboxgroup',
				columns : 3,
				defaults: {
					width: 100
				},
				items :[{
					boxLabel  : '添加',
					inputValue: 'add',
					checked : true,
					name : 'action-add',
				 }, {
					 boxLabel : '更新',
					 inputValue: 'update',
					 checked : true,
					 name : 'action-update',
				 }, {
					 boxLabel: '删除',
					 inputValue: 'delete',
					 checked : false,
					 name: 'action-delete',
					 listeners : {
						 change : function(box, newValue, oldValue, e) {
							 if(newValue) {
								 this.setBoxLabel('删除>筛选范围');
								 delParamsGrid.show();//setVisible(true);
							 } else {
								 this.setBoxLabel('删除');
								 delParamsGrid.hide();//setVisible(false);
							 }
						 }
					 }
				}]
			}, {
				layout : 'anchor',
				border : false,
				items : [delParamsGrid]
			}]
		}]
	});
	
	var selectActionPanel = Ext.create('Ext.panel.Panel', {
		bodyPadding: 10,
		width: '100%',
		frame : true,
		border : false,
		layout : 'fit',
		region : 'center',
		items : [fileInfoForm],
		bbar : ['->',{
			text: '提交操作',
			iconCls : 'icon-sys-confirm',
			handler: function() {
				if(!fileImportLogId) {
					return;
				}
				var appManager = AscApp.getController('AscAppManager');
				var submitOperationFn = appManager.getEngineDirectFn(appKey, 'submitImportOpeation');
				var fieldValues = fileInfoForm.getForm().getFieldValues();
				var isAdd = fieldValues["action-add"];
				var isUpdate = fieldValues["action-update"];
				var isDelete = fieldValues["action-delete"];
				var delParams = {};
				delParamsGrid.getStore().each(function(record){
					if(record.get('f_is_default')) {
						delParams[record.get('id')] = record.get('f_value');
						return true;
					}
				});
				var win = panel.up('window');
				win.body.mask();
				submitOperationFn(fileImportLogId, isAdd, isUpdate, isDelete, delParams, function(result, e) {
					if(e.status && result.success){
						Asc.common.Message.showInfo(result.message);
						win.onSubmitSuccess();
					} else {
						Asc.common.Message.showError("提交操作失败");
					}
					win.body.unmask();
				});
			}
		}]
	});
	
	// 填充文件信息的form
 	fileInfoForm.getForm().setValues({
		"clientFileName" : '<%=fi.getImportFileLog().getF_client_file_name()%>',
		"schemaCaption" : '<%=fi.getFileImportSchema().getF_caption()%>',
		"fileLength" : <%=fi.getImportFileLog().getF_file_length()%>,
		"dataCount" : <%=fi.getImportFileLog().getF_data_count()%>
	});
	panel.add(selectActionPanel);

});
</script>