<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script language='javascript'>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var fields = ['id','f_node_id','f_node_caption','f_start_time','f_end_time','f_state','f_flowlog_id','f_create_time','f_update_time','f_params','f_exception_type','f_exception_detail'];
	// 数据访问
	var store = new Ext.data.Store({
		clearRemovedOnLoad : true,
		fields : fields,
		proxy : {
    		type : 'direct',
    		directFn : EsbFlowDirect.getNodeLogsByFlowLogId,
	    	extraParams : {
	    		flowLogId : panel.object.f_flowlog_id
	    	},
			paramOrder:['flowLogId'],
	    	paramsAsHash : true,
	    	reader : {
	    		type : 'json',
	    		root : 'results',
	    		idProperty : 'ID',
	    		totalProperty : 'totals'
	    	}
    	},
		autoLoad : false
	});
	store.on('beforeload', function(s, operation){
		store.removeAll();
		operation.params = {};
		operation.params.flowLogId = panel.object.f_flowlog_id;
	});
	var grid = new Ext.grid.Panel({
		layout : 'fit',
		title : '节点办理记录',
		region : 'east',
		border:false,
		split : true,
		collapsible : false,
        collapseMode:'mini',
        lineBreak : true,
    	enableHdMenu : false,
    	hideHeaders : true,
    	viewConfig: {
            stripeRows: true,
            enableTextSelection: true//启用文字选择
    	},
		store : store,
		columns : [{
			dataIndex : 'id',
			header : '办理记录',
			width : '100%',
			renderer : function(value, metaData, record, rowIndex, colIndex, store){
				var output = '<H2 style="color:red;">' + record.get('f_node_caption') + '</H2>';
				var state = record.get('f_state');
				output = output + '<B>状态:</B>'
				if(state == 'processing'){
					output = output + '<BR/>&nbsp;&nbsp;正在执行...';
					metaData.tdAttr = 'style="background-color:lightgreen;"';
				}else if(state == 'completed'){
					output = output + '<BR/>&nbsp;&nbsp;执行结束';
				}else if(state == 'created'){
					output = output + '<BR/>&nbsp;&nbsp;创建完毕';
				}
				output = output + '<BR/>&nbsp;&nbsp;开始于&nbsp;' + record.get('f_start_time') + 
					((record.get('f_end_time')==null ||record.get('f_end_time')=='')?'':( '<BR/>&nbsp;&nbsp;结束于&nbsp;' + record.get('f_end_time')));
				var params = record.get('f_params');
				if(params.length != 0){
					var pstr = '';
					for(var i = 0;i < params.length;i++){
						pstr = pstr + '<BR/>&nbsp;&nbsp;' + params[i]['f_name'] + ' : ' + params[i]['f_value'];
					}
					output =  output + '<BR/><BR/><B>参数列表:</B>' +  pstr;
				}
				if(record.get('f_exception_type') == 1) {
					output =  output + '<BR/><BR/><B>异常信息</B>:<BR/>' + record.get('f_exception_detail');
				}
				output = output + '<BR/>';
				return '<div>' + output  + '</div>';
			}
		}],
		autoExpandColumn: 'id',
		tbar : ['->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				 if(Ext.isDefined(panel.object.f_flowlog_id) && panel.object.f_flowlog_id != ''){
					store.load({'flowLogId':panel.object.f_flowlog_id});
				} 
			}
		}]
	});
	//加载新数据
	panel.refresh = function(flowLogId){
		if(Ext.isDefined(flowLogId) && flowLogId != ''){
			panel.object.f_flowlog_id = flowLogId;
			store.load({'flowLogId':panel.object.f_flowlog_id});
		}else{
			grid.getSelectionModel().deselectAll();
			store.removeAll();
		}
	};
	panel.add({
		layout : 'fit',
		border : false,
		items : [grid]
	});
	panel.doLayout();
});
</script>