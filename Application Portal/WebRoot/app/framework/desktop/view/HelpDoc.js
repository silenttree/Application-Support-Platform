// 帮助页面
Ext.define('Asc.framework.desktop.view.HelpDoc', {
	extend : 'Ext.window.Window',
	alias : 'widget.AscHelpDoc',
	title : '帮助文档',
	key : '',
	width: 600,
    height: 300,
	iconCls : 'icon-sys-book',
	maximizable : true,
	layout: 'border',
	autoShow : true,
	items : [],
	docPath : '/',
	
	//在该方法中动态组装window中的组件元素
	initComponent : function(){
		var me = this;
		me.callParent(arguments);
		
  		//帮助主题树的data model
	 	Ext.define('HelpJsonModel',{
	 		extend : 'Ext.data.Model',
	 		fields : [
	 		         {name : 'text', type : 'string'},
	 		         {name : 'url', type : 'string'},
	 		         {name : 'folder', type : 'string'}
	 		        ]
	 	});
	 	
	 	//树过滤器
	 	Ext.define('TreeFilter', {
	 		filterByText: function(text) {
	 			this.filterBy(text, 'text');
	 		},
	 	    /**
	 	     * 根据字符串过滤所有的节点，将不符合条件的节点进行隐藏.
	 	     * @param 查询字符串.
	 	     * @param 要查询的列.
	 	     */
	 		filterBy: function(text, by) {
	 			this.clearFilter();
	 			var view = this.getView(),
	 				me = this,
	 				nodesAndParents = [];
	 	 
	 	        // 找到匹配的节点并展开.
	 	        // 添加匹配的节点和他们的父节点到nodesAndParents数组.
	 			this.getRootNode().cascadeBy(function(tree, view) {
	 				var currNode = this;

	 				if (currNode && currNode.data[by] && currNode.data[by].toString().toLowerCase().indexOf(text.toLowerCase()) > -1) {
	 					me.expandPath(currNode.getPath());

	 					while (currNode.parentNode) {
	 						nodesAndParents.push(currNode.id);
	 						currNode = currNode.parentNode;
	 					}
	 				}
	 			}, null, [me, view]);
	 	 
	 			// 将不在nodesAndParents数组中的节点隐藏
	 			this.getRootNode().cascadeBy(function(tree, view) {
	 				var uiNode = view.getNodeByRecord(this);

	 				if (uiNode && !Ext.Array.contains(nodesAndParents, this.id) && !this.isRoot()) {
	 					Ext.get(uiNode).setDisplayed('none');
	 				}
	 			}, null, [me, view]);
	 		},

	 	    clearFilter: function() {
	 	    	var view = this.getView();

	 	    	this.getRootNode().cascadeBy(function(tree, view) {
	 	    		var uiNode = view.getNodeByRecord(this);
	 	 
	 	    		if (uiNode) {
	 	    			Ext.get(uiNode).setDisplayed('table-row');
	 	    		}
	 	    	}, null, [this, view]);
	 	    }
	 	});
	 	
	 	//定义可以过滤的树
	 	Ext.define('FilterTreePanel',{
	 		extend:'Ext.tree.Panel',
	 		mixins:['TreeFilter']
	 	});
		
	 	//只有根节点的treeStore
		var treeStore = Ext.create('Ext.data.TreeStore', {
	 		model:'HelpJsonModel',
	 		root: {
	 			expanded: false,
	 			text : '帮助主题',
	 			iconCls : 'icon-sys-books',
	 			url : 'index.html',
	 			autoLoad : false,
			 	children:[]
	 		},
	 		
	 		folderSort: true
	 	});
		
		//过滤树的查询框
		var searchField = new Ext.form.TextField({
			width : '100%',
			emptyText : '输入主题相关词',
			enableKeyEvents : true,
			listeners : {
				change : function(tf, newValue, oldValue){
					tree.filterByText(newValue);
				}
			}
		});
		
		//帮助主题树
		var tree = Ext.create('FilterTreePanel', {
			store: treeStore,
			title : '帮助文档目录',
			region : 'west',
			split : true,
			animCollapse : true,
			collapsible : true,
			hideCollapseTool : true,
			tbar : [searchField],
			flex : 30
		});
		
  		//树节点选中事件，根据选中的节点不同设置不同的操作元素的状态
  		tree.on('select',function(rowModel, record, index){
  			if(!Ext.isEmpty(record.data.url)){  				
  				Ext.fly('docDataIframe').dom.contentWindow.location = me.getUrlByNode(record);
  			}
  			me.replaceBreadCrumb(record);
  			me.updateDataIndexPanel(record);
  		});
		
  		me.tree = tree;
  		me.add(tree);
  
  		var dataPanel = Ext.create('Ext.panel.Panel',{
  			layout : 'border',
  			region : 'center',
  			flex : 70,
  			tbar : []
  		});
  		
  		//显示帮助页面的iframe的容器
  		var dataIframePanel = Ext.create('Ext.panel.Panel',{
  			region : 'center',
  			flex : 50,
  			html : '<iframe id="docDataIframe" src="app/docs/index.html" width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>'
  		});
  		
  		//显示某个节点下的子节点的panel
  		var dataIndexPanel = Ext.create('Ext.panel.Panel',{
  			region : 'north',
  			layout : 'fit',
  			flex : 50
  		});
  		
  		//显示某个节点下的子节点的grid
		var indexGrid = Ext.create('Ext.grid.Panel', {
	  		hideHeaders : true,
  	  		columns: [{
  	  		    header : '标题',
  	  		    dataIndex : 'text',
  	  		    flex : 1
  	  		}],
  	  		listeners : {
  	  			itemclick : function( grid, record, item, index, e, eOpts ){
  	  				me.tree.getSelectionModel().select(record);
  	  			}
  	  		}
  	  	});
		
		dataIndexPanel.add(indexGrid);
		
  		me.dataIframePanel = dataIframePanel;
  		me.dataIndexGrid = indexGrid;
  		me.dataIndexPanel = dataIndexPanel;
  		dataPanel.add(dataIframePanel);
  		dataPanel.add(dataIndexPanel);
  		me.dataPanel = dataPanel;
  		me.add(dataPanel);
  		
  		//加载树节点
  		me.reloadTree();
	},
	
	//显示某个节点的子节点列表
	updateDataIndexPanel : function(node){
		var me = this;
		var indexStore = Ext.create('Ext.data.Store',{
			fields : ['text','url'],
			data : node.childNodes,
	  		proxy: {
  	  			type: 'memory',
  	  			reader: {
  	  				type: 'json',
  	  			}
  	  		}
		});
		
		me.dataIndexGrid.setTitle(node.data.text);
		me.dataIndexGrid.reconfigure(indexStore);
		
		if(!Ext.isEmpty(node.data.url)){
			me.dataIframePanel.setVisible(true);
	  		me.dataIndexPanel.setVisible(false);
		}else{
			me.dataIframePanel.setVisible(false);
	  		me.dataIndexPanel.setVisible(true);
		}
	},
	
	/**
	 * 根据帮助主题树的节点重置breadcrumb
	 */
	replaceBreadCrumb : function(node){
		var me = this;
		//获取顶部工具条
		var topToolBars = me.dataPanel.getDockedItems('toolbar[dock="top"]');
		if(topToolBars && topToolBars.length > 0){
			var topToolBar = topToolBars[0];
			
			//删除所有的breadcrumb
			var breadcrumbButton;
			while(breadcrumbButton = topToolBar.child('[breadcrumb=true]')){
				topToolBar.remove(breadcrumbButton);
			}
			
			//添加新的breadcrumb
			var nodeToAdd = node;
			while(nodeToAdd){
				var bc;
				if(nodeToAdd.data.leaf){
					bc = Ext.create('Ext.Button',{
						text : nodeToAdd.data.text,
						breadcrumb : true,
						treeNode : nodeToAdd,
						iconCls : nodeToAdd.data.iconCls,
						handler : function(){
							me.tree.expandPath(this.treeNode.getPath('text'),'text');
							me.tree.getSelectionModel().select(this.treeNode);
						}
					});
				}else{ //不是叶子节点的情况
					var menuItems = [];
					if(nodeToAdd){
						nodeToAdd.eachChild(function(cn){
							menuItems.push({
								text : cn.data.text,
								iconCls : cn.data.iconCls,
								treeNode : cn,
								handler : function(){
									me.tree.expandPath(cn.getPath('text'),'text');
									me.tree.getSelectionModel().select(cn);
								}
							});
						});
					}
					bc = Ext.create('Ext.button.Split',{
						text : nodeToAdd.data.text,
						breadcrumb : true,
						treeNode : nodeToAdd,
						iconCls : nodeToAdd.data.iconCls,
						menu : {
							items : menuItems
						},
						handler : function(){
							me.tree.expandPath(this.treeNode.getPath('text'),'text');
							me.tree.getSelectionModel().select(this.treeNode);
						}
					});
				}
				topToolBar.add(0,bc);
				nodeToAdd = nodeToAdd.parentNode;
			}
		}
	},
	
	/**
	 * 根据帮助主题树的节点获取相应的帮助文档url
	 */
	getUrlByNode : function(node){
		var baseUrl = 'app/docs/';
		if(node.isRoot()){
			return baseUrl + 'index.html';
		}
		var url = node.data.url;
		if(node.data.folder){
			url = node.data.folder + "/" + url;
		}
		var pNode = node.parentNode;
		while(!pNode.isRoot()){
			if(pNode.data.folder){
				url = pNode.data.folder + "/" + url;
			}
			pNode = pNode.parentNode;
		}
		return baseUrl + url;
	},
	
	/**
	 * 载入帮助主题树
	 */
	reloadTree:function(){
		var me = this;
		var rootNode = me.tree.getRootNode();
		rootNode.removeAll();
		PortalAppDirect.getDocTree(me.key,function(result,e){
			if(result && result.success){
				rootNode.appendChild(result.treeNodes);
				rootNode.expand();
				if(me.docPath) {

		  			console.log(me.docPath);
					me.tree.selectPath(me.docPath,'text');
					//me.tree.selectPath('/帮助主题/门户桌面','text');
					
				} else {		
					me.tree.getSelectionModel().select(rootNode);
				}
			}
		});
	},
	
	toPath: function(docPath){
		me.selectPath = docPath;
		me.tree.selectPath(docPath,'text');
	}
});
