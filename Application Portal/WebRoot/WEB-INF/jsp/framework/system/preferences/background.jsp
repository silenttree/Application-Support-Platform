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
			directFn : PortalAppDirect.loadWallpapers,
			autoLoad: true,
			paramOrder:[],
			root : 'datas',
			totalProperty : 'total',
			idProperty : 'path',
			fields:[
			    {name:'id', mapping:'id'},
			    {name:'thumbnail', mapping:'thumbnail'},
			    {name:'path', mapping:'path'},
			    {name:'delflag', mapping:'delflag'}
			]
		});
		var tpl = new Ext.XTemplate(
			'<tpl for=".">',
				'<div class="pref-view-thumb-wrap" id="{id}">',
					'<div class="pref-view-thumb"><img width=103 src="' + contextPath + '{thumbnail}" title="{id}" /></div>',
				'<span>{shortName}</span></div>',
			'</tpl>',
			'<div class="x-clear"></div>'
		);
		
		var view = new Ext.DataView({
			emptyText : '没有可供选择的墙纸',
			itemSelector :'div.pref-view-thumb-wrap',
			loadingText : 'loading...',
			singleSelect : true,
			trackOver: true,
			overItemCls : 'x-view-over',
			prepareData : function(data){
				data.shortName = Ext.util.Format.ellipsis(data.id, 17);
				return data;
			},
			store : store,
			tpl : tpl
		});
		
		var viewSelectById = function(id) {
			store.each(function(node) {
				if(node.get('id') === id) {
					view.getSelectionModel().select(node);
					return false;
				}
				return true;
			});
		};

		store.on('load', function(s, records){
			if(records){
				var wp = AscApp.getPreferences().wallpaper;
				if(wp){
					viewSelectById(wp);
				}
			}
		}, this);
		
		var wpTile = Ext.create('Ext.form.Radio',{
			inputValue: 'tile',
			checked : AscApp.getPreferences().wallpaperposition === 'tile',
			name : 'wallpaperposition',
			boxLabel : '平铺方式',
			x : 15,
			y : 90
		});
				
		var wpCenter = Ext.create('Ext.form.Radio', {
			inputValue: 'center',
			checked: AscApp.getPreferences().wallpaperposition === 'center',
			name : 'wallpaperposition',
			boxLabel : '居中显示',
			x: 110,
			y: 90
		});
		
		wpTile.on('change', function(rdi, value) {
			if(value){
				AscApp.getPreferences().wallpaperposition = 'tile';
			}
		});
		
		wpCenter.on('change', function(rdi, value) {
			if(value){
				AscApp.getPreferences().wallpaperposition = 'center';
			}
		});
		
		//设置任务栏透明度
		var transparencySlider = Ext.create('Ext.Slider', {
			minValue : 0, 
			maxValue : 100, 
			width : 100, 
			value : AscApp.getPreferences().transparency,
			x : 200, 
			y : 40
		});
		
		//设置任务栏透明度
		var transparencyField =  Ext.create('Ext.form.NumberField', {
			cls : 'x-field-percent', 
			enableKeyEvents : true, 
			maxValue : 100, 
			minValue : 0, 
			width : 45, 
			value : AscApp.getPreferences().transparency,
			x : 200, 
			y : 70
		});
		
		//TODO 设置任务栏透明度的函数没有写呢
		var transparencyUpdateHandler = new Ext.util.DelayedTask(AscApp.setTransparency, AscApp);
		
		var transparencyHandler = function (){
			var v = transparencySlider.getValue();
			transparencyField.setValue(v);
			transparencyUpdateHandler.delay(100, null, null, [v]); // delayed task prevents IE bog
		};
		
		// 透明度滑条事件
		transparencySlider.on({
			'change': transparencyHandler, 
			'drag': transparencyHandler
		});
		
		// 透明度输入框事件
		transparencyField.on({
			'keyup': {
				fn: function(field){
					var v = field.getValue();
					if(v !== '' && !isNaN(v) && v >= field.minValue && v <= field.maxValue){
						transparencySlider.setValue(v);
					}
				}, 
				buffer: 350
			}
		});
		
		//TODO 颜色选择框没有实现
	    var onChangeBackgroundColor = function() {
	    	var dialog = new Ext.ux.ColorDialog({
				border: false, 
				closeAction: 'close', 
				listeners: {
					'select': { fn: onBackgroundColorSelect, scope: this, buffer: 350 }
				}, 
				resizable: false, 
				title: 'Color Picker'
			});
			dialog.show(AscApp.getPreferences().backgroundcolor);
	    };
	    
	    var onBackgroundColorSelect = function(p, hex) {
	    	AscApp.setBackgroundColor(hex);
		};
		
	    var onChangeFrontColor = function() {
	    	var dialog = new Ext.ux.ColorDialog({
				border: false, 
				closeAction: 'close', 
				listeners: {
					'select': { fn: onFrontColorSelect, scope: this, buffer: 350 }
				}, 
				resizable: false, 
				title: 'Color Picker'
			});
			dialog.show(AscApp.getPreferences().frontcolor);
	    }
		
	    var onFrontColorSelect = function(p, hex) {
			AscApp.setFrontColor(hex);
		};
		
		var formPanel = new Ext.FormPanel({
			border : false,
			height : 140,
			layout : 'absolute',
			items : [{
				border: false,
				items: {border: false, html:'选择墙纸显示方式：'},
				x: 15,
				y: 15
			},{
				border: false,
				items: {border: false, html: '<img border=0 src="' + contextPath + '/resources/images/app/wallpaper-tile.png" width="64" height="44" border="0" alt="" />'},
				x: 15,
				y: 40
			}, wpTile,{
				border: false,
				items: {border: false, html: '<img border=0 src="' + contextPath + '/resources/images/app/wallpaper-center.png" width="64" height="44" border="0" alt="" />'},
				x: 110,
				y: 40
			}, wpCenter, {
				border: false,
				items: {border: false, html:'设置任务栏透明度：'},
				x: 200,
				y: 15
			}, transparencySlider, transparencyField, {
				border: false,
				items: {border: false, html:'设置颜色：'},
				x: 330,
				y: 15
			}, new Ext.Button({
				handler : onChangeFrontColor,
				text : '设置前景色',
				x : 330,
				width : 100,
				y : 50
			}), new Ext.Button({
				handler: onChangeBackgroundColor,
				text : '设置背景色',
				width : 100,
				x : 330,
				y : 80
			}), new Ext.Button({
				text:'上传背景图片',
				iconCls:'icon-sys-upload',
				width : 100,
				x : 330,
				y : 110
			})]
		});
		
		var bgPanel = new Ext.Panel({
			header: false,
	        iconCls : 'icon-sys-background',
	        items : [{
	        	xtype : 'panel',
				autoScroll: true,
	        	height : 230,
	        	layout : 'fit',
				bodyStyle : 'padding:18px',
				border : true,
	        	items : view
	        }, formPanel]
		});
		
		panel.fnApply = function() {
			var node,
				nodes = view.getSelectionModel().getSelection();
			if(nodes.length === 0) {
				return false;
			}
			node = nodes[0];
			AscApp.setWallpaper(node.get('id'));
		};
		
		panel.add(bgPanel);
		panel.doLayout();

	});
</script>