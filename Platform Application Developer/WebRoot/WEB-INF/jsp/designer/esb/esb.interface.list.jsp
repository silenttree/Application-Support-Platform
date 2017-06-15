<%@ page contentType="text/html; charset=utf-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var key = panel.object.key;
	var appId = panel.object.appId;
	var type = panel.object.type;
	Ext.define('esbServiceModel',{
		extend : 'Ext.data.Model',
		fields : [
		   {name :'appEventKey',type:'string'},
		   {name :'params'}
		]
	});
	var store = Ext.create('Ext.data.Store', {
	    model : 'esbServiceModel',
		proxy : {
			type : 'direct',
			directFn : EsbEventDirect.getEventOrServiceList,
			extraParams : {
				appId : appId,
				type : type
			},
			paramOrder : ['appId','type'],
			paramsAsHash : true
		},
		autoLoad : true,
		listeners : {
			beforeload : function(store, operation){
				operation.params = {};
				operation.params.appId = appId;
				operation.params.type = type;
			}
		}
	});
	
	var grid = Ext.create('Ext.grid.Panel', {
	    height : '95%',
	    width : '95%',
	    useArrows : true,
	    border : false,
	    renderTo : Ext.getBody(),
	    store : store,
		viewConfig: {
            stripeRows: true,
            enableTextSelection: true//启用文字选择
    	},
	    columns : [
	        { text: '事件名称', dataIndex: 'appEventKey',width: '30%'},
	        {text: '参数配置',  dataIndex: 'params',flex: 1,
	        	renderer : function(v, md, record){
	        		if(typeof v == 'object'){
	        			if(v instanceof Array){
	        				var str = '';
	        				for(var i=0;i<v.length;i++){
	        					for(var key in v[i]){
	        						str = str + key + ";";
	        					}
	        				}
	        			}
	        			return unescape(str.replace(/\\u/gi, '%u'));//把中文显示的ASCII码转换成中文显示
	        		}else if(typeof v=='string'){
	        			return unescape(v.replace(/\\u/gi, '%u'));
	        		}
        		return v;
        	}}
	    ]
	});
	panel.add(grid);
	panel.doLayout();
});
</script>
