<%@page contentType="text/html;charset=UTF-8"%>
<%
	String docPanelId = request.getParameter("docPanelId");
	String docId = docPanelId.substring(0, docPanelId.lastIndexOf("."));
	String panelid = request.getParameter("panelid");
	String contextPath = request.getContextPath();
	String dataId = request.getParameter("id");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	// 获得appId
	var appKey = panel.appKey;
	// 获得DocumentId
	var docId = '<%=docId%>';
	// 获得DocumentDataId
	var dataId = <%=dataId%>;
	// 获得FlowId
	var docManager = AscApp.getController('AscDocumentManager');
	var docData = docManager.getDocumentData(appKey, docId);
	var flowId = docData.flowId;
	// 获得流程版本
	var version = 0;

	// Creates the graph and loads the default stylesheet
	var graph = new mxGraph();

	// 流程跟踪页面
	var workflowPanel = Ext.create('Ext.panel.Panel',{
		region : 'center',
		border:false,
		layout:'fit'
	});
	var store = Ext.create('Ext.data.ArrayStore', {
		model : 'Asc.framework.workflow.model.NodeProcessLog',
		proxy: {
			type: 'memory',
			reader: {
				type: 'json',
				idProperty: 'ID',
				root: 'processlogs'
			}
		}
	});
	var attributePanel =  Ext.create('Ext.grid.Panel', {
		region : 'east',
		width : 200,
		minWidth : 150,
		maxWidth : 450,
		border : false,
		split : true,
		collapsible : false,
		collapseMode : 'mini',
	    store: store,
		columns : [{
			dataIndex : 'F_USER_NAME',
			header : '办理记录',
			flex :1,
			renderer : function(value, metaData, record, rowIndex, colIndex, store, view){
				var output = "<B>" + record.get("F_NODE_CAPTION") + "</B>";
				output = output + "<BR>" + record.get("F_USER_NAME");
				switch(record.get("F_PROCESS_TYPE")){
				case 0:
					output = output + " <B>主办</B>";
					break;
				case 1:
					output = output + " 协办";
					break;
				case 2:
					output = output + " 读者";
					break;
				}

				switch(record.get("F_STATE")){
				case 0:
					break;
				case 1:
					output = output + "<BR>" + record.get("F_START_TIME") + " 开始";
					output = output + "<BR>正在办理...";
					metaData.tdAttr = "style='background-color:lightgreen;'";
					break;
				case 2:
					output = output + "<BR>" + record.get("F_START_TIME") + " 开始";
					output = output + "<BR>" + record.get("F_END_TIME") + " 结束";
					break;
				case 3:
					output = output + "<BR>已取消";
					break;
				}
				output = output + "<BR><BR>" + record.get("F_OPINION");
				return output;
			}
		}]
	});
	var ui = {
		layout : 'border',
		border : false,
		items : [workflowPanel, attributePanel]
	};
	// 计算节点显示类型
	function getNodeDisplayType(nodelogs, id, isCompute){
		for(var i=0;i<nodelogs.length;i++){
			var nodelog = nodelogs[nodelogs.length - i - 1];
			if(nodelog.F_NODE_ID == id){
				if(nodelog.F_STATE == 3 || nodelog.F_STATE == 5){
					return isCompute ? "nodeComputePassed" : "nodeDone";
				}else if(nodelog.F_STATE == 2){
					return "nodeDoing";
				}
			}
		}
		return isCompute ? "nodeComputeUnpassed" : "nodeUndo";
	}
	// 转换节点类型字典
	function getNodeTypeTranslate(type){
		switch (type){
		case 'StartNode':
			return 'nodeStart';
		case 'EndNode':
			return 'nodeEnd';
		case 'StartNode':
			return 'nodeStart';
		case 'ForkNode':
			return 'nodeFork';
		case 'JoinNode':
			return 'nodeJoin';
		case 'ScriptNode':
			return 'nodeScript';
		case 'ProcessNode':
			return 'nodeProcess';
		case 'SubFlowNode':
			return 'nodeSubFlow';
		}
		return type;
	}

	// 刷新
	panel.refresh = function(){
		graph.getModel().clear();
		var appManager = AscApp.getController('AscAppManager');
		var loadFlowInstanceDataFn = appManager.getEngineDirectFn(appKey, 'loadFlowInstanceData');
		loadFlowInstanceDataFn(docId, dataId, function(result, e){
			if(result && result.success){
				var flowObjects = result.flow;
				var nodelogs = result.nodelogs;
				graph.getModel().beginUpdate();
				try{
					var parent = graph.getDefaultParent();
					for(var i=0;i<flowObjects.nodes.length;i++){
						var node = flowObjects.nodes[i];
						
						var w = 50;
						var h = 50;
						var type = getNodeTypeTranslate(node.type);
						if(type == 'node'){
							w = 120;
							h = 30;
							type = getNodeDisplayType(nodelogs, node.id);
						}else if(type == 'nodeFork'){
							w = 125;
							h = 30;
							type = getNodeDisplayType(nodelogs, node.id);
						}else if(type == 'nodeSubFlow'){
							w = 100;
							h = 35;
							type = getNodeDisplayType(nodelogs, node.id);
						}else if(type == 'nodeJoin'){
							w = 120;
							h = 40;
							type = getNodeDisplayType(nodelogs, node.id);
						}else if(type == 'nodeProcess'){
							w = 110;
							h = 30;
							type = getNodeDisplayType(nodelogs, node.id);
						}else if(type == 'nodeScript'){
							w = 135;
							h = 35;
							type = getNodeDisplayType(nodelogs, node.id, true);
						}
						var x =  node.x * 180 + 90 + 20 - w/2;
						var y =  node.y * 100 + 50 + 20 - h/2;
						
						var cell = graph.insertVertex(parent, node.id, node.caption, x, y, w, h, type);
					}
					for(var i=0;i<flowObjects.routes.length;i++){
						var route = flowObjects.routes[i];
						var source = graph.getModel().getCell(route.from);
						var target = graph.getModel().getCell(route.to);
						var edge = graph.insertEdge(parent, route.id, route.caption, source, target, 'route');
						if(route.style){
							// 设置线条位置
							var geo = edge.getGeometry();
							geo.points = [new mxPoint(route.style.point.x, route.style.point.y)];
							// 设置线条文本位置
							if(route.style.labelPoint && route.style.labelPoint != null){
								geo.offest = {x:route.style.labelPoint.x, y:route.style.labelPoint.y}
							}
							edge.setGeometry(geo);
							// 设置线条样式
							graph.setCellStyles(mxConstants.STYLE_ELBOW, route.style.elbow, [edge]);
						}
					}
				}finally{
					// Updates the display
					graph.getModel().endUpdate();
				}
				store.loadData(result.processlogs);
				//alert(store.getCount());
			}
		});
	};
	

	// 初始化
	panel.initWorkflowPanel = function(){
		if(panel.isGraphInit){
			return;
		}
		panel.isGraphInit = true;
		// 初始化
		// Makes the connection are smaller
		mxConstants.DEFAULT_HOTSPOT = 0.3;
		// Loads the default stylesheet into the graph
		var style = mxUtils.load('dependencies/mxgraph/mixky.flowstyle.xml').getDocumentElement();
		var dec = new mxCodec(style.ownerDocument);
		dec.decode(style, graph.getStylesheet());
		//Mixky.lib.initFlowStylesheet(graph.getStylesheet());
		
		// Initializes the graph as the DOM for the panel has now been created	
		workflowPanel.body.dom.style.overflow = 'auto';
		graph.init(workflowPanel.body.dom);
		graph.autoExtend = false;
		graph.setCellsLocked(true);
		graph.setConnectable(false);
		// 不允许编辑对象大小
		graph.setCellsResizable(false);
		graph.setAllowDanglingEdges(false);
		// 设置路由名称位置不可编辑
		graph.edgeLabelsMovable = false;
		// 设置图标样式
		graph.container.style.cursor = 'default';
		// 设置单选模式
		graph.getSelectionModel().setSingleSelection(true);
		// 移动对象
		graph.graphHandler.setMoveEnabled(false);
		// 编辑对象文本
		graph.setCellsEditable(false);
		
		panel.refresh();
	};
	
	store.load();

	workflowPanel.on('afterrender', function(){
		panel.initWorkflowPanel();
	});
	// 显示界面
	panel.add(ui);
	panel.doLayout();
});
</script>