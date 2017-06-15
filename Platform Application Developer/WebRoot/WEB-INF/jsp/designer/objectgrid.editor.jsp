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
		},'->', {
			text : '引入流程身份',
			iconCls : 'icon-sys-importfield',
			hidden : !(type=='useridentity'),
			handler : function(){
				//在文档身份标签里面显示引入流程身份功能按钮
				DeveloperAppDirect.importIdentify(panel.object.appId, panel.object.key, function(result, e){
					if(result && result.success){
						Asc.common.Message.showInfo('引入流程身份成功！');
						panel.refresh();
					}else{
						Asc.common.Message.showError('引入流程身份失败！' + result.message);
					}
				});
			}
		},{
			text : '引入流程节点',
			iconCls : 'icon-sys-importfield',
			hidden : !(type=='state'),
			handler : function(){
				//在文档状态标签里面显示引入流程节点功能按钮，能引入流程节点
				DeveloperAppDirect.importNodes(panel.object.appId, panel.object.key, function(result, e){
					if(result && result.success){
						Asc.common.Message.showInfo('引入流程节点成功！');
						panel.refresh();
					}else{
						Asc.common.Message.showError('引入流程身份失败！' + result.message);
					}
				});
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