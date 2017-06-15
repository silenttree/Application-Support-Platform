<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
%>
<script language='javascript'>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	Ext.define('esbflowModel',{
		extend : 'Ext.data.Model',
 		fields : [
 		    {name: 'text', type: 'string'},
 		    {name: 'id',  type: 'string'}
 		]
	});
	var store = Ext.create('Ext.data.TreeStore', {
		model : 'esbflowModel',
		root : {
			id : 0,
			//iconCls : 'icon-manager-organizationfolder',
			text : '所有业务流设计对象',
			expanded : true
		},
		proxy : {
			type : 'direct',
			directFn : EsbFlowDirect.getAllEsbFlowDesignObject,
			reader : {
				type: 'json',
				root : 'datas',
				totalProperty : 'totals',
				successProperty : 'successed',
				messageProperty : 'message'
			}
		},
        autoLoad: true
	});
	//业务流设计对象对应的业务流实例
	var flowLogGrid = Ext.create('Asc.extension.JspPanel', {
		disabled : true,
		layout : 'fit',
		region : 'east',
		header: false,
		split : true,
		border : false,
		width : '80%',
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/esb/esb.flow.instance',
		//params : {type : type},
		object : {f_flow_id : ''}
	});
	//业务流设计对象树
	var treepanel = Ext.create('Ext.tree.Panel',{
		layout : 'fit',
		minWidth : '20%',
		region : 'center',
		split : true,
		border : false,
		useArrows: true,
		store : store
	});
	// 选中对象树节点
	treepanel.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			//panel.appkey = selected[0].get('id');
			flowLogGrid.enable();
			//节点的id传递过去
			flowLogGrid.object.f_flow_id = selected[0].get('id');
			flowLogGrid.refresh(flowLogGrid.object.f_flow_id);
		}
	});
	panel.add({
		layout : 'border',
		border : false,
		items : [treepanel, flowLogGrid]
	});
	panel.doLayout();
});
</script>