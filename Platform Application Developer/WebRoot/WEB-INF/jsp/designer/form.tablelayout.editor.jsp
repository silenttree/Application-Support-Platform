<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	// 布局编辑器
	var layoutEditor = Ext.create('Asc.extension.JspPanel',{
		region : 'center',
		layout : 'fit',
		xtype : 'AscJspPanel',
		jspUrl : 'designer/form.tablelayout.editor.layout',
		layout : 'fit',
		autoScroll : false
	});
	layoutEditor.object = panel.object;
	// 输出模式编辑器
	var fieldEditor = Ext.create('Asc.extension.JspPanel',{
		title : '表单域输出模式',
		region : 'west',
		width : 400,
		split : true,
		collapseMode : 'mini',
		layout : 'fit',
		autoScroll : true,
		jspUrl : 'designer/form.outputmode.editor'
	});
	fieldEditor.object = panel.object;
	// 保存数据
	panel.doApply = panel.doSave = function(){
		// 获得输出模式定义数据
		var outputMode = fieldEditor.getOutputModel();
		// 获得布局数据
		var layout = layoutEditor.getLayoutValues();
		// 组织数据
		var data = {
			id : panel.object.key,
			f_output_mode : outputMode,
			f_layout : layout.layout,
			f_cols : layout.cols,
			f_rows : layout.rows
		}
		// 执行保存
		AscApp.ActionManager.runFunction('designer.doSaveObjects', 
			panel,
			[panel.object.appId, [data], function(){
				panel.refresh();
			}]
		);
	}
	// 刷新
	panel.refresh = function(){
		layoutEditor.refresh();
		fieldEditor.refresh();
	}
	// 输出界面
	panel.add({
		layout : 'border',
		border : false,
		items : [layoutEditor, fieldEditor]
	});
	panel.doLayout();
});
</script>