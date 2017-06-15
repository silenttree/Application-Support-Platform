<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String contextPath = request.getContextPath();
%>
<script>
	Ext.onReady(function(){
		
		var contextPath = '<%=contextPath%>';
		var panel = Ext.getCmp('<%=panelid%>');
		
		var store = new Ext.data.DirectStore({
			directFn : PortalAppDirect.loadPortalViewList,
			autoLoad: true,
			paramOrder:[],
			root : 'datas',
			totalProperty : 'total',
			idProperty : 'path',
			fields:[
			    {name:'id', mapping:'id'},
			    {name:'view', mapping:'view'}
			]
		});
		var tpl = new Ext.XTemplate(
			'<tpl for=".">',
				'<div class="pref-view-thumb-wrap" id="{id}">',
					'<div class="pref-view-thumb"><img width=103 src="' + contextPath + '{view}" title="{id}" /></div>',
				'<span>{shortName}</span></div>',
			'</tpl>',
			'<div class="x-clear"></div>'
		);
		
		var view = new Ext.DataView({
			emptyText : '没有门户显示的配置选项',
			itemSelector :'div.pref-view-thumb-wrap',
			loadingText : 'loading...',
			singleSelect : true,
			border : false,
			trackOver: true,
			overItemCls : 'x-view-over',
			prepareData : function(data){
				data.shortName = Ext.util.Format.ellipsis(data.id, 17);
				return data;
			},
			store : store,
			tpl : tpl
		});
		
		
		var portViewPanel = new Ext.Panel({
			border : false,
			header : false,
			layout : 'fit',
	        iconCls : 'icon-sys-background',
	        items : [ view ]
		});
		
		panel.fnApply = function() {
			
		};
		
		panel.add(portViewPanel);
		panel.doLayout();

	});
</script>