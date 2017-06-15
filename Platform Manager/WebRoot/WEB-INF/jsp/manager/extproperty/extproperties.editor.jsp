<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = '<%=type%>';
	// 获得对象属性列表
	var properties = AscApp.ClassManager.getProperties(type);
	// 信息提示窗口
	var description = Ext.create('Ext.panel.Panel', {
		region : 'south',
		split : true,
		border : false,
        collapseMode:'mini',
		height : 150,
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
	// 属性被修改
	editor.on('propertychange', function(source, recordId, value, oldValue){
		editor.record.set(recordId, value);		
		editor.record.dirty = true;
	})
	// 清空编辑窗口
	panel.clearRecord = function(){
		editor.findPlugin('cellediting').cancelEdit();
		editor.getSelectionModel().deselectAll();
		panel.disable();
		editor.record = undefined;
		editor.setPropertyValues({});
	}
	
	// 装载数据
	panel.loadRecord = function(record){
		editor.findPlugin('cellediting').cancelEdit();
		editor.record = record;
		editor.setPropertyValues(record.getData());
		panel.enable();
	}
	
	panel.loadProperties = function(properties){
		panel.properties = properties;
	}
	// 装载并显示编辑界面
	panel.add({
		layout : 'border',
		border : false,
		items : [editor, description]
	});
	panel.doLayout();
});
</script>