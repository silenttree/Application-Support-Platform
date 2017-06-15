<%@page import="com.mixky.toolkit.JsonObjectTool"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var moduleKey = panel.object.key;
	var appId = panel.object.appId;
	var tbar = [{
		text : '添加外部引用',
		iconCls : 'icon-sys-add',
		handler : function(){
			showWindow();
		}
	},'-',{
		text : '删除',
		iconCls : 'icon-sys-delete',
		handler : function(){
			var sel = gridpanel.getSelectionModel().getSelection();
			if(sel.length>0){
				var id = sel[0].data.key;
				DeveloperAppDirect.deleteReference(appId, moduleKey, id, function(result,e){
					if(result && result.success){
						Asc.common.Message.showInfo("删除外部引用对象成功！");
						//重新加外部引用列表
						gridpanel.getStore().reload();
					}else{
						Asc.common.Message.showError("删除外部引用对象失败！失败原因：" + result.message);
					}
				});
			}else{
				Asc.common.Message.showError('请选择要删除的行！');
			}
		}
	}];
	var showWindow = function(){
		//checktree的store
 		 var store = Ext.create('Ext.data.TreeStore', {
			fields : ['moduleId','type','text','name','caption'],
			root: 'data',
			proxy : {
				type : 'direct',
				directFn : DeveloperAppDirect.loadReferenceCheckObjects,
				extraParams : {
					appId : appId,
					isContain : ['query','page','layout','form','view','urlpage'],
 					moduleKey : moduleKey
				},
				paramOrder : 'appId isContain moduleKey',
				paramsAsHash : true
			},
			autoLoad: true
	    });
		//带有复选框的选择树
		var checktree = Ext.create('Ext.tree.Panel',{
			rootVisible: false,
			viewConfig :{
				forceFit:true,//当行大小变化时始终填充满
				stripeRows:true //每列是否是斑马线分开
			},
        	useArrows: true,
        	border: false,
        	store : store,
       		columns : [
				{ text: 'moduleId',  dataIndex: 'moduleId', hidden: true},
   		        { xtype: 'treecolumn',text: '模块',  dataIndex: 'moduleId'},
   		     	{ text: '类型', dataIndex: 'type', width:'15%'},
   		     	{ text: 'Key', dataIndex: 'text', width:'40%',flex:1},//id
   		        { text: '英文名', dataIndex: 'name', width:'15%'},
   		        { text: '中文名', dataIndex: 'caption',width: '15%'}
   		    ],
   		 	listeners : {
		    	itemclick : function(tree, record, item, index, e){
		    		if(record.data.check!=null){
			    		record.set('checked',!record.get('checked'));
		    		}
		    	}
		    }
		});
		var win = Ext.create('Ext.window.Window',{
			title: '外部引用对象选择',
		    height: 400,
		    width: 700,
		    modal : true,
		    maximizable : true,
		    layout: 'fit',
		    items : [checktree],
		    bbar : ['->',{
				text : '取消',
				iconCls : 'icon-sys-cancel',
				handler : function() {
					win.close();
			}
		}, '-', {
				text : '确定',
				iconCls : 'icon-sys-apply',
				handler : function() {
					var selcheck = checktree.getChecked();
					var ids = [];
					if(selcheck.length==0){
						Asc.common.Message.showError("未选择设计对象！");
						return;
					}else{
						for(var i=0;i<selcheck.length;i++){
							ids.push(selcheck[i].data.text);
						}
						console.log(moduleKey);
						DeveloperAppDirect.addReferences(appId, moduleKey, ids,function(result,e){
							if(result && result.success){
								Asc.common.Message.showInfo("添加外部引用对象成功！");
								//重新加外部引用列表
								gridpanel.getStore().reload();
							}else{
								Asc.common.Message.showError("添加外部引用对象失败！失败原因：" + result.message);
							}
						});
						win.close();
					}
				}
			}]
		}).show();
	}
	var gridStore = Ext.create('Ext.data.Store',{
		fields : ['moduleId','moduleName','type','key','caption','name'],
		proxy :{
			type : 'direct',
			directFn : DeveloperAppDirect.loadGridReferences,
			extraParams : {
				appId : appId,
				moduleKey : moduleKey
			},
			paramOrder : 'appId moduleKey',
			paramsAsHash : true
		},
		autoLoad : true
	});
	var gridpanel = Ext.create('Ext.grid.Panel',{
		width : '95%',
		height : '95%',
		columns: [{header: '模块Id',dataIndex : 'moduleId',hidden : true},
		          {header: '所在模块',dataIndex : 'moduleName'},
		          {header: '类型',dataIndex : 'type'},
	        	  {header: 'Key',dataIndex : 'key',flex:1},
	        	  {header: '中文名',dataIndex : 'caption'},
	        	  {header: '英文名',dataIndex : 'name'}],
		border : true,
		shadow : false,
		store : gridStore,
		tbar : tbar,
		listeners : {
			//右键删除菜单
			itemcontextmenu : function(treenode, record, item, index, e){
				var id = record.data.key;
				e.stopEvent();
				Ext.create('Ext.menu.Menu',{
					  width: 100,
					  items : [{
						  text : '删除',
						  iconCls : 'icon-sys-delete',
						  handler : function(){
							  var id = record.data.key;
							  DeveloperAppDirect.deleteReference(appId, moduleKey, id, function(result,e){
									if(result && result.success){
										Asc.common.Message.showInfo("删除外部引用对象成功！");
										//重新加外部引用列表
										gridpanel.getStore().reload();
									}else{
										Asc.common.Message.showError("删除外部引用对象失败！失败原因：" + result.message);
									}
							  });
						  }
					  }]
				}).showAt(e.getXY());
			}
		}
	});
	panel.add(gridpanel);
	panel.doLayout();
});
</script>
