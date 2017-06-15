<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_application';
	var typeClass = AscApp.ClassManager.getClass(type);
	// 获得对象属性列表
	var properties = AscApp.ClassManager.getProperties(type);
	// 初始化视图列
	var columns = AscApp.ClassManager.getGridColumns(properties, typeClass.propertyColumns);
	// 初始化存储字段
	var fields = AscApp.ClassManager.getStoreFields(properties);
	// 初始化数据存储对象
	var store = new Ext.data.Store({
		clearRemovedOnLoad : true,
	   	proxy: {
	        type: 'ajax',
	        url : 'log/app.data.jsp',
	        reader: {
		    	idProperty : 'id',
		        type: 'json',
		        root: 'datas'
		    }
	    },
		fields : fields
	});
	//启动按钮
	var startAction = new Ext.Action({
		text : '启动',
		iconCls : 'icon-manager-applicationstaterun',
		disabled : true,
		handler : function(){
			changeState(2);
		}
	});
	//停止按钮
	var stopAction = new Ext.Action({
		text : '停止',
		iconCls : 'icon-manager-applicationstatestop',
		disabled : true,
		handler : function(){
			changeState(1);
		}
	});
	//暂停按钮
	var pauseAction = new Ext.Action({
		text : '挂起',
		iconCls : 'icon-manager-applicationstatepause',
		disabled : true,
		handler : function(){
			changeState(3);
		}
	});
	//暂停按钮
	var resumeAction = new Ext.Action({
		text : '继续',
		iconCls : 'icon-manager-applicationstateresume',
		disabled : true,
		handler : function(){
			changeState(5);
		}
	});
	//刷新按钮
	var refreshAction = new Ext.Action({
		text : '刷新',
		iconCls : 'icon-sys-refresh',
		handler : function(){
			grid.getStore().reload();
		}
	});
	// 数据列表界面
	var grid = Ext.create('Ext.grid.Panel', {
		layout : 'fit',
		border : false,
		sortableColumns : false,
	    viewConfig: {stripeRows: false},
		allowDeselect : true,
	    columns : columns,
		store : store,
		tbar : ['->',startAction,pauseAction,stopAction,resumeAction,refreshAction]
	});
	//改变应用系统的运行状态
	var changeState = function(newState){
		var selectRs = grid.getSelectionModel().getSelection();
		if(selectRs.length > 0){
			var record = selectRs[0];
			Ext.MessageBox.confirm('操作提示', '应用系统：”' + record.get('f_caption') + '“ 由状态“' + getStateInfo(record.get('f_state')) + '”变成状态“' + getStateInfo(newState) + '”，您确定吗？', function(btn){
				if(btn == 'yes'){
						Ext.getBody().mask('正在更新状态，请稍候...');
						ApplicationRegisterDirect.updateAppState(record.get('id'), record.get('f_domain'), newState, function(result, e){
						if(result && result.success){
							Asc.common.Message.showInfo("更新状态成功 ！");
							grid.getStore().reload();
							//grid.getSelectionModel().select(record);
							record.set('f_state',newState);
							grid.fireEvent('select', grid, record, record.index);
						}else{
							Asc.common.Message.showError('更新状态失败，' + result.message);
						}
						Ext.getBody().unmask();
					});
				}
			});
		}else{
			Asc.common.Message.showInfo('未选中应用系统对象！');
		}
	};
	
	//应用系统的运行状态信息
	var getStateInfo = function(state){
		switch(state){
		case 0:
			return '正在初始化';
			break;
		case 1:
			return '停止';
			break;
		case 2:
			return '正在运行';
			break;
		case 3:
			return '已挂起';
			break;
		case 4:
			return '未知状态';
			break;
		case 5:
			return '继续运行';
			break;
		default:
			return '';
			break;
		}
	};
	// 选中
	grid.on('beforeselect', function(g, record, index){
		// 被删除的记录不能选中
		if(record.isDeleted){
			return false;
		}
	});
	// 选中改变
	grid.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			var state = selected[0].get('f_state');
			if(state == 0){
				startAction.enable();
				pauseAction.disable();
				stopAction.enable();
				resumeAction.disable();
			}else if(state == 1){
				startAction.enable();
				pauseAction.disable();
				stopAction.disable();
				resumeAction.disable();
			}else if(state == 2){
				startAction.disable();
				pauseAction.enable();
				stopAction.enable();
				resumeAction.disable();
			}else if(state == 3){
				startAction.disable();
				pauseAction.disable();
				stopAction.enable();
				resumeAction.enable();
			}else if(state == 5){
				startAction.disable();
				pauseAction.enable();
				stopAction.enable();
				resumeAction.disable();
			}else{
				startAction.enable();
				pauseAction.disable();
				stopAction.enable();
				resumeAction.disable();
			}
			//grid.reconfigure();
			panel.doLayout();
		}
	});
	//选中事件
	grid.on('select', function(g, record, index){
		var state = record.get('f_state');
		if(state == 0){
			startAction.enable();
			pauseAction.disable();
			stopAction.enable();
			resumeAction.disable();
		}else if(state == 1){
			startAction.enable();
			pauseAction.disable();
			stopAction.disable();
			resumeAction.disable();
		}else if(state == 2){
			startAction.disable();
			pauseAction.enable();
			stopAction.enable();
			resumeAction.disable();
		}else if(state == 3){
			startAction.disable();
			pauseAction.disable();
			stopAction.enable();
			resumeAction.enable();
		}else if(state == 5){
			startAction.disable();
			pauseAction.enable();
			stopAction.enable();
			resumeAction.disable();
		}else{
			startAction.enable();
			pauseAction.disable();
			stopAction.enable();
			resumeAction.disable();
		}
		//grid.reconfigure();
		panel.doLayout();
	});
	// 刷新列表
	panel.refresh = function(){
		grid.getStore().reload();
		grid.getSelectionModel().deselectAll();
	};
	// 显示界面
	panel.add({
		layout : 'fit',
		border : false,
		items : [grid]
	});
	panel.doLayout();
	// 装载数据
	grid.getStore().load();
});
</script>