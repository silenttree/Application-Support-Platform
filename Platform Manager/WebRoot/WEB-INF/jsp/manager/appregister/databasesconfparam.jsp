<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
	String key = request.getParameter("key");
	String route = request.getParameter("route");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = '<%=type%>';
	panel.key = '<%=key%>';
	panel.route = '<%=type%>';
	// 获得对象属性列表
	var properties = AscApp.ClassManager.getProperties(type);
	// 脚本
	var script = Ext.create('Ext.panel.Panel', {
		region : 'south',
		split : true,
		border : false,
        collapseMode:'mini',
        width : 400,
        minWidth : 200,
        minHeight : 200,
        title : '脚本',
        items:[{
        	xtype : 'textareafield',
            grow : true,
            width : '100%',
            height : 200,
            border : false,
            name : 'script',
            anchor : '100%',
            value : panel.route
        }]
	});
	// 创建参数列表
	var paramgrid = Ext.create('Ext.grid.Panel', {
		region : 'center',
		height : 100,
		border : false,
		text : '参数',
		columns: [
			{ text: '参数名',  dataIndex: 'caption' },
			{ text: '说明', dataIndex: 'note', flex: 1 }
		],
		store : Ext.create('Ext.data.Store', {
		    fields : ['caption', 'note']
		})
	});
	
	panel.rel = function(){
		paramgrid.getStore().reload();
	}
	panel.getScript = function(){
		var value = script.items.items[0].getValue();
		ApplicationRegisterDirect.addDatasourceScript(panel.key, value,  function(result, e){
			if(result && result.success){
				Asc.common.Message.showInfo('脚本保存成功！');
			}else{
				if(result && result.message){
					Asc.common.Message.showError(result.message);
				}else{
					Asc.common.Message.showError('脚本保存失败！');
				}
			}
		});
	}
	panel.setParam = function(key, params){
		panel.key = key;
		paramgrid.removeAll();
		paramgrid.getStore().add({'caption' : 'test', 'note' : '说明'});
		//paramgrid.getStore().reload();
	}
	panel.setScript = function(value){
		script.items.items[0].setValue(value);
	}
	// 装载并显示编辑界面
	panel.add({
		layout : 'border',
		border : false,
		items : [paramgrid, script]
	});
	panel.doLayout();
	paramgrid.getStore().reload();
});
</script>