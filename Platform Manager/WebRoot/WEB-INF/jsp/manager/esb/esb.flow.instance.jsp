<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script language='javascript'>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	
	var fields = ['id','f_flow_id','f_flow_version','f_caption','f_start_time','f_end_time','f_state'];
	
	var columns = [{header : 'id',  dataIndex : 'id', hidden : true},
					{header : '业务流设计对象id', dataIndex : 'f_flow_id', hidden : true},
					{header : '业务流名字', dataIndex : 'f_caption',flex : 1},
					{header : '版本', dataIndex : 'f_flow_version'},
					{header : '运行状态', dataIndex : 'f_state'},
					{header : '开始时间', dataIndex : 'f_start_time',flex : 1},
					{header : '结束时间', dataIndex : 'f_end_time',flex : 1}];
	
	var store = new Ext.data.Store({
	 	clearRemovedOnLoad : true,
		fields : fields,
		pageSize : 30,//每页显示几条数据(初始值)
		proxy : {
			type : 'direct',
			directFn : EsbFlowDirect.getSfFlowLogsByFlowId,
			extraParams : {
				flowId : panel.object.f_flow_id
			},
			paramOrder : ['flowId',  'page', 'limit'],
			paramsAsHash : true
		},
		sorters : [{
			property : 'id',
			direction : 'DESC'
		}],
		autoLoad : false
	});
	// 数据列表界面
	var grid = Ext.create('Ext.grid.Panel', {
		region : 'center',
		layout : 'fit',
		border : false,
	    stripeRows:true, //斑马线效果  
        loadMask:true, //显示遮罩和提示功能,即加载Loading…… 
		sortableColumns : false,
		allowDeselect : true,
		split : true,
	    columns : columns,
	    width : '75%',
		store : store,
		tbar : ['->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				 if(Ext.isDefined(panel.object.f_flow_id) && panel.object.f_flow_id != ''){
					store.load({'flowId':panel.object.f_flow_id});
				} 
			}
		}],
		//根据不同的信息，显示不同的字体颜色样式
		viewConfig: {
		    getRowClass: function(record, rowIndex, rowParams, datas){
		/*     	var state = record.get('f_state');
				switch(state){
				case 0:
					return 'log-level-fatal-info';
				case 3:
					return 'log-level-error-info';
				case 4:
					return 'log-level-warn-info';
				case 6:
				case 7:
				default:
					return '';
				} */
		    }
		},
		dockedItems : [{
	        xtype: 'AscPagingToolbar',
	        store: store,
	        dock: 'bottom',
	        displayInfo: true
	    }]
	});
	store.on('beforeload', function(s, operation){
		store.removeAll();
		operation.params = {};
		operation.params.flowId = panel.object.f_flow_id;
	});
	//业务流节点记录
	var NodeLogsGrid = Ext.create('Asc.extension.JspPanel', {
		disabled : true,
		layout : 'fit',
		region : 'east',
		header: false,
		split : true,
		border : false,
		width : '25%',
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/esb/esb.flow.view',
		object : {f_flowlog_id : ''}
	});
	
	//加载新数据
	panel.refresh = function(flowId){
		if(Ext.isDefined(flowId) && flowId != ''){
			panel.object.f_flow_id = flowId;
			store.load({'flowId':panel.object.f_flow_id});
		}else{
			grid.getSelectionModel().deselectAll();
		}
	};
	// 选中grid的行对象
	grid.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			NodeLogsGrid.enable();
			//节点的id传递过去
			NodeLogsGrid.object.f_flowlog_id = selected[0].get('id');
			NodeLogsGrid.refresh(NodeLogsGrid.object.f_flowlog_id);
		}else{
			NodeLogsGrid.refresh();
			NodeLogsGrid.disable();
		}
	});
	panel.add({
		layout : 'border',
		border : false,
		items : [grid, NodeLogsGrid]
	});
	panel.doLayout();
});
</script>