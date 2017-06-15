<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String contextPath = request.getContextPath();
%>
<script>
Ext.onReady(function () {
	var contextPath = '<%=contextPath%>';
	var panel = Ext.getCmp('<%=panelid%>');
	var uiWindow = new Ext.form.Radio({
			boxLabel : 'Window模式',
			hideLabel : true,
			checked : AscApp.getPreferences().uimode === 'window',
			name : 'uimodel'
		});

	var uiWebPage = new Ext.form.Radio({
			boxLabel : 'Web Page模式',
			hideLabel : true,
			checked : AscApp.getPreferences().uimode !== 'window',
			name : 'uimodel'
		});
	
	var wsMode1 = new Ext.form.Radio({
			boxLabel : '单页面',
			checked : AscApp.getPreferences().wsMode !== 'AscDesktopWorkspaceMulti',
			inputValue : 'AscDesktopWorkspace',
			name : 'wsMode'
		});

	var wsMode2 = new Ext.form.Radio({
			boxLabel : '多标签',
			checked : AscApp.getPreferences().wsMode === 'AscDesktopWorkspaceMulti',
			inputValue : 'AscDesktopWorkspaceMulti',
			name : 'wsMode'
		});
	
	var desktopColumns2 = new Ext.form.Radio({
			boxLabel : '2栏',
			checked : AscApp.getPreferences().columns === 2,
			inputValue : 2,
			name : 'columns'
		});

	var desktopColumns3 = new Ext.form.Radio({
			boxLabel : '3栏',
			checked : AscApp.getPreferences().columns !== 2 && AscApp.getPreferences().columns !== 4,
			inputValue : 3,
			name : 'columns'
		});

	var desktopColumns4 = new Ext.form.Radio({
			boxLabel : '4栏',
			checked : AscApp.getPreferences().columns === 4,
			inputValue : 4,
			name : 'columns'
		});

	var store = new Ext.data.DirectStore({
			directFn : PortalAppDirect.loadDeskStyles,
			autoLoad : true,
			paramOrder : [],
			idProperty : 'path',
			fields : [{
					name : 'id',
					mapping : 'id'
				}, {
					name : 'thumbnail',
					mapping : 'thumbnail'
				}, {
					name : 'path',
					mapping : 'path'
				}
			]
		});

	var tpl = Ext.create('Ext.XTemplate',
			'<tpl for=".">',
			'<div class="pref-view-thumb-wrap" id="{id}">',
			'<div class="pref-view-thumb"><img src="{thumbnail}" title="{id}" /></div>',
			'<span>{shortName}</span></div>',
			'</tpl>',
			'<div class="x-clear"></div>');

	var view = Ext.create('Ext.DataView', {
			autoHeight : true,
			anchor : '-20',
			emptyText : '没有可供选择的样式',
			itemSelector : 'div.pref-view-thumb-wrap',
			loadingText : 'loading...',
			singleSelect : true,
			overItemCls : 'x-view-over',
			prepareData : function (data) {
				data.shortName = Ext.util.Format.ellipsis(data.id.replace('ext-theme-', ''), 15);
				return data;
			},
			store : store,
			tpl : tpl
		});

	var viewSelectById = function (id) {
		store.each(function (node) {
			if (node.get('id') === id) {
				view.getSelectionModel().select(node);
				return false;
			}
			return true;
		});
	};

	store.on('load', function (s, records) {
		if (records) {
			var t = AscApp.getPreferences().theme;
			if (t) {
				viewSelectById(t.id);
			}
		}
	}, this);
	
	var columnGroup = Ext.create('Ext.form.RadioGroup',{
			hideLabel : true,
			items : [
				desktopColumns2, 
				desktopColumns3,
				desktopColumns4
			]
		});
	
	var stylePanel = Ext.create('Ext.panel.Panel', {
			padding : '5px',
			border : false,
			items : [{
					xtype : 'fieldset',
					title : '设置界面模式',
					items : [
					     {
					    	hidden : true,
							layout : 'column',
							border : false,
							items : [{
									columnWidth : 0.4,
									border : false,
									layout : 'form',
									items : uiWindow
								}, {
									columnWidth : 0.6,
									border : false,
									layout : 'form',
									items : uiWebPage
								}
							]
						},
						{
							layout : 'column',
							border : false,
							items : [{
									columnWidth : 0.3,
									border : false,
									layout : 'form',
									items : [{
											border : false,
											html : '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;桌面栏目数：'
										}
									]
								}, {
									columnWidth : 0.7,
									border : false,
									layout : 'form',
									items : [{
				                		hideLabel : true,
				                		xtype : 'radiogroup',
				                        items : [desktopColumns2, desktopColumns3, desktopColumns4]
				                	}]
								}
							]
						},
						{
							layout : 'column',
							border : false,
							items : [{
									columnWidth : 0.3,
									border : false,
									layout : 'form',
									items : [{
											border : false,
											html : '工作区显示模式：'
										}
									]
								}, {
									columnWidth : 0.47,
									border : false,
									layout : 'form',
									items : [{
				                		hideLabel : true,
				                		xtype : 'radiogroup',
				                        items : [wsMode1, wsMode2]
				                	}]
								}
							]
						}
					]
				}, {
					xtype : 'panel',
					autoScroll : true,
					height : 300,
					layout : 'fit',
					bodyStyle : 'padding:18px',
					border : true,
					items : view
				}
			]
		});

	panel.fnApply = function() {
		AscApp.getPreferences().columns = desktopColumns2.getValue() ? 2 : desktopColumns4.getValue() ? 4 : 3;
		AscApp.getPreferences().uimode = uiWindow.getValue() ? 'window' : 'webpage';
		AscApp.getPreferences().wsMode = wsMode1.getValue() ? 'AscDesktopWorkspace' : 'AscDesktopWorkspaceMulti';
		console.log(AscApp.getPreferences().wsMode);
		var node,
			nodes = view.getSelectionModel().getSelection();
		if(nodes.length === 0) {
			return false;
		}
		node = nodes[0];
		AscApp.setTheme(node.data);
	};
	
	
	panel.add(stylePanel);
	
	
});

</script>