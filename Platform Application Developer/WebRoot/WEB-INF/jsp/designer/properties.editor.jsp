<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	// 修改标题
	if(!panel.title){
		panel.setTitle('属性编辑');
	}
	// 获得对象属性列表
	var properties = AscApp.ClassManager.getProperties(panel.object.type);
	// 信息提示窗口
	var description = Ext.create('Ext.panel.Panel', {
		region : 'south',
		split : true,
		border : false,
        collapseMode:'mini',
		height : 70,
		bodyStyle:'background-color:lightyellow;padding:8px;font-size:12px',
		html : '<b>属性说明</b>'
	});
	// 创建属性编辑器
	var editor = new Asc.extension.PropertyGrid(properties, {
		region : 'center',
		border : false,
		showPropertyDescription : function(html){
			description.body.update(html);
		}
	});
	// 装载数据
	panel.refresh = function(){
		if(!Ext.isDefined(panel.object.key) || panel.object.key == ''){
			return;
		}
		DeveloperAppDirect.loadObject(panel.object.appId, panel.object.key, function(result, e){
			if(result && result.success){
				editor.setPropertyValues(result.object);
			}else{
				if(result && result.message){
					Asc.common.Message.showError(result.message);
				}else{
					Asc.common.Message.showError('设计对象[' + panel.object.key, + ']装载失败！');
				}
			}
		});
	};
	// 执行保存
	panel.doApply = panel.doSave = function(){
		var values = editor.getPropertyValues(true);
		if(!Ext.isEmpty(values)){
			AscApp.ActionManager.runFunction('designer.doSaveObjects', 
				editor,
				[panel.object.appId, [Ext.apply({id : panel.object.key}, values)], function(){
					panel.refresh();
				}]
			);
		}
	};
	// 装载并显示编辑界面
	panel.add({
		layout : 'border',
		border : false,
		items : [editor, description]
	});
	panel.doLayout();
	// 装载数据
	panel.refresh();
});
</script>