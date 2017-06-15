<%@page import="com.asc.manager.log.handler.LogConfigHandler"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	//String managerUrl = LogConfigHandler.instance().getManagerUrl();
	//String casUrl = LogConfigHandler.instance().getCasUrl();
	String managerUrl = "/manager/log/managerlog";
	String casUrl = "/manager/log/appuserlog";
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = panel.object.type;
	var url = '';
	if(type == 'ApplicationSysLog'){
		type = 'SystemLog';
		url = panel.object.url;
	}else if(type == 'ApplicationManagerLog'){
		type = 'MgrActivityLog';
		url = '<%=managerUrl%>';
	}else if(type == 'ApplicationUserLog'){
		type = 'UserActivityLog';
		url = panel.object.url;
	}else if(type == 'ApplicationCasLog'){
		type = 'CasLog';
		url = '<%=casUrl%>';
	}
	var typeClass = AscApp.ClassManager.getClass(type);
	// 获得对象属性列表
	var properties = AscApp.ClassManager.getProperties(type);
	// 初始化视图列
	var columns = AscApp.ClassManager.getGridColumns(properties, typeClass.propertyColumns);
	// 初始化存储字段
	var fields = AscApp.ClassManager.getStoreFields(properties);
	//日志详细信息
	var description = panel.description = Ext.create('Ext.panel.Panel', {
		disabled : true,
		split : true,
		layout : 'fit',
		border : false,
        collapseMode : 'mini',
		height : 150,
		bodyStyle : 'background-color:lightyellow;padding:8px;font-size:12px',
		html : '<b>详细日志信息</b>'
	});
	var textfield = new Ext.form.TextField({
		width : 150,
		emptyText : '搜索关键字',
		enableKeyEvents: true,//添加键盘事件，这项必须有并且为true（默认为false），否则键盘事件不可用
		listeners : {
			specialkey : function(field,e){
				if(e.getKey() == e.ENTER){
					//触发创建按钮事件
					
				}
			} 
		}
	});
	// 初始化数据存储对象
	var store = new Ext.data.Store({
	 	clearRemovedOnLoad : true,
		fields : fields,
		pageSize : 30,//每页显示几条数据(初始值)
		proxy : {
			type : 'direct',
			directFn : LogDirect.loadLogsByType,
			extraParams : {
				type : type,
				url : url
			},
			paramOrder : ['type', 'url',  'page', 'limit'],
			paramsAsHash : true,
			reader: {
		    	idProperty : 'id',
		        type: 'json',
		        root: 'datas',
		        totalProperty : 'total'
		    }
		},
		sorters : [{
			property : 'id',
			direction : 'DESC'
		}],
		autoLoad : false
	});
	// 数据列表界面
	var grid = Ext.create('Ext.grid.Panel', {
		//region : 'center',
		border : false,
	    stripeRows:true, //斑马线效果  
        loadMask:true, //显示遮罩和提示功能,即加载Loading…… 
		sortableColumns : false,
		allowDeselect : true,
	    columns : columns,
	    width : 500,
		store : store,
		tbar : ['->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				if(Ext.isDefined(panel.address) && panel.address != ''){
					store.load();
				}
			}
		}],
		//根据不同的错误信息，显示不同的字体颜色样式
		viewConfig: {
		    getRowClass: function(record, rowIndex, rowParams, s){
		    	var level = record.get('f_level');
				switch(level){
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
				}
		    }
		},
		dockedItems : [{
	        dock: 'bottom',
	        border : false,
	        items: [description, {
		        xtype: 'AscPagingToolbar',
		        id : 'asc-page-tool',
		        store: store,
		        displayInfo: true
		    }]
		}]
	});
	// 选中之前
	grid.on('beforeselect', function(g, record, index){
		description.setDisabled(true);
		description.body.update('');
	});
	// 选中
	grid.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			description.setDisabled(false);
			description.body.update('<b>日志：</b><BR><BR>' + selected[0].get('f_log') + '<BR><BR><b>详细信息：</b><BR><BR>' + selected[0].get('f_detail'));	
		}else{
			description.setDisabled(true);
			description.body.update('');
		}
	});
	
	store.on('beforeload', function(s, operation){
		store.removeAll();
		url = panel.address;
		operation.params = {'url':url,'type':type};
	});
	
	// 刷新列表，重新装载数据
	panel.refresh = function(address){
		if(Ext.isDefined(address) && address != ''){
			panel.address = address;
			store.currentPage = 1;
			store.load();
		}else{
			grid.getSelectionModel().deselectAll();
		}
	};
	
	// 显示界面
	panel.add({
		layout : 'fit',
		border : false,
		items : [grid]
	});
	panel.refresh(url);
	panel.doLayout();
});
</script>