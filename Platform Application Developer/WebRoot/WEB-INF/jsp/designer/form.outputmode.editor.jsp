<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var formTableLayoutId = panel.object.key;
	var appId = panel.object.appId;
	var gridStore = Ext.create('Ext.data.Store',{
		fields:['key','caption','output','show','edit','extend','forbidden','id','order'],
		proxy : {
			type : 'direct',
			directFn : DeveloperAppDirect.loadTableFields,
			extraParams : {
				appId : appId,
				formId : formTableLayoutId,
				type : panel.object.type
			},
			paramOrder:['appId','formId','type']
		},
		sorters : [{
			property : 'order',
			direction : 'ASC'
		},{
			property : 'Key',
			direction : 'ASC'
		}],
		autoLoad :true,
		listeners : {
			beforeload : function(store, operation){
				operation.params = {};
				operation.params.appId = panel.object.appId;
				operation.params.formId = panel.object.key;
				operation.params.type = panel.object.type;
			}
		}
	});
	var gridPanel = Ext.create('Ext.grid.Panel',{
		border : false,
		autoScroll : true,
		store: gridStore,
	    columns: [
			{header: 'Key',  dataIndex: 'key', hidden: true},
	        {header: '中文名', dataIndex: 'caption', flex:1},
	        {xtype: 'checkcolumn', header: '输出',  dataIndex: 'output',
	        	editor: {
                	xtype: 'checkbox',
                	cls: 'x-grid-checkheader-editor'
              },width:'30px'},
	        {xtype: 'checkcolumn', header: '显示',  dataIndex: 'show',
	        	editor: {
	        		 xtype: 'checkbox',
		             cls: 'x-grid-checkheader-editor'
	        	},width:'20px'},
	        {xtype: 'checkcolumn', header: '编辑',  dataIndex: 'edit',
	        		editor: {
		        		 xtype: 'checkbox',
			             cls: 'x-grid-checkheader-editor'
		        },width:'20px'},
	        {xtype: 'checkcolumn', header: '扩展',  dataIndex: 'extend',
	        	editor: {
	        		 xtype: 'checkbox',
		             cls: 'x-grid-checkheader-editor'
	        	},width:'20px'},
	        {xtype: 'checkcolumn', header: '禁止访问',  dataIndex: 'forbidden',
	        	editor: {
	        		 xtype: 'checkbox',
		             cls: 'x-grid-checkheader-editor'
	        	},width:'50px'},
	        {header: 'id',  dataIndex: 'id', hidden: true},
	        {header: '排序',  dataIndex: 'order', hidden: true}
	    ]
	});
	var encodeJsonObject = function(items){
		var obj = {};
		for(var i=0;i<items.length;i++){
			var output = (items[i].data.output == true?'1':'0');
			var show = (items[i].data.show == true?'1':'0');
			var edit = (items[i].data.edit == true?'1':'0');
			var extend = (items[i].data.extend == true?'1':'0');
			var forbidden = (items[i].data.forbidden == true?'1':'0');
			var str = output + show + edit + extend + forbidden;
			if(str != '00000'){
				obj[items[i].data.id] = str;
			}
		}
		return obj;
	};
	// 获得输出模式数据
	panel.getOutputModel = function(){
		var modifyStore = gridPanel.getStore();
		var items = modifyStore.data.items;
		//把store里面的数据封装成JsonObject格式，可以直接调用DeveloperAppDirect的updateAuthoritiesMap方法，直接设置
		return encodeJsonObject(items);
	}
	// 刷新
	panel.refresh = function(){
		gridPanel.getStore().reload();
	}
	panel.add(gridPanel);
	panel.doLayout();
});
</script>
