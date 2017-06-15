// 桌面门户
Ext.define('Asc.framework.desktop.view.Portal', {
	
	extend: 'Ext.panel.Panel',
	
	alias: 'widget.AscDesktopPortal',
    // 设置引用类
	requires: ['Ext.ux.portal.PortalPanel',
	           'Ext.ux.portal.Portlet',
	           'Asc.framework.desktop.view.Shortcut'],
	
	border : false,
	
	layout : 'border',
	
    bodyStyle : 'background:lightyellow',
    
    defaults : {
    	border : false,
	    bodyStyle : 'background:transparent none'
    },
    
    portletDatas : {},
    
    // 从应用拿到的快捷按钮数据
    shortcutDatas : {},
    
    // TODO 门户布局需要改成动态
	items : [{
		// 快捷菜单
		region : 'west',
		xtype : 'AscDesktopShortcut'
	}, {
		// 桌面栏目
		region : 'center',
		autoScroll : true,
		items : [{
			xtype : 'uxPortalPanel',
	    	border : false,
		    bodyStyle : 'background:transparent none',
			items : [{
	            columnWidth:0.33
	        },{
	            columnWidth:0.33
	        },{
	            columnWidth:0.34
	        }],
	        listeners : {
	        	'drop' : {
	        		fn : function(){
	        			var portal = this.up('AscDesktopPortal');
	        			portal.updatePortletsPos();
	        		}
	        	}
	        }
		}]
	}],
	// 获得门户面板
	getPortalPanel : function(){
		return this.down('uxPortalPanel');
	},
	// 获得快捷方式面板
	getShortcutPanel : function(){
		return this.down('AscDesktopShortcut');
	},
	// 获得列数
	getColumns : function(){
		var portal = this.getPortalPanel();
		return portal.items.length;
	},
	// 获得当前最小高度的显示列
    getMinHeightCol : function(){
    	var height = 10000;
    	var index = 0;
		var portal = this.getPortalPanel();
		portal.items.each(function(column, col){
			if(column.getHeight() < height){
				height = column.getHeight();
				index = col;
			}
		}, this);
		return index;
    },
    // 获得桌面栏目定义数据
    getPortletData : function(portletKey){
    	return this.portletDatas[portletKey];
    },
    // 装载桌面栏目定义数据
    loadPortletData : function(appKey, portletId, fn, scope){
		var appManager = AscApp.getController('AscAppManager');
		var app = appManager.getAppModel(appKey);
		if(!Ext.isDefined(app)){
			Asc.common.Message.error('应用[' + appKey + ']不存在！', this);
			return;
		}
		var portletKey = appKey + '.' + portletId;
		var portletData = this.getPortletData(portletKey);
		if(!Ext.isDefined(portletData)){
			// 调用Direct方法请求模块数据
			try{				
				var getPortletFn = appManager.getEngineDirectFn(appKey, 'getPortlet');
				getPortletFn(portletId, function(result, e){
					if(result && result.success){
						// 装载模块数据
						this.portletDatas[portletKey] = result.data;
						if(Ext.isDefined(fn)){
							fn.call(scope || this);
						}
						Asc.common.Message.log('装载门户栏目[' + appKey + '.' + portletId + ']数据执行完毕！');
					}else{
						Asc.common.Message.showError('装载门户栏目[' + appKey + '.' + portletId + ']失败！');
					}
				}, this);
			} catch (e) {
				Asc.common.Message.showError('装载门户栏目[' + appKey + '.' + portletId + ']失败！');
			}
		}else{
			return portletData;
		}
    },
	// 添加桌面栏目
	addPortlet : function(portlet){
		if(!Ext.isDefined(portlet.appKey)){
			return;
		}
		// 获得门户对象
		var portal = this.getPortalPanel();
		// 获得栏目数据
		var portletData = this.loadPortletData(portlet.appKey, portlet.portletId, function(){
			this.addPortlet(portlet);
		}, this);
		if(!Ext.isDefined(portletData)){
			return;
		}
		// 根据命名规则获得栏目对象
		var portlets = Ext.ComponentQuery.query('uxPortlet[key=' + portlet.id + ']');
		if(portlets.length === 0){
			// 创建桌面栏目
			var col = portlet.col === undefined ? this.getMinHeightCol() : portlet.col;
			var portletPanel =  Ext.create('Ext.ux.portal.Portlet', {
				key : portlet.id,
				appKey : portlet.appKey,
				portletId : portlet.portletId,
				title : portletData.title || '门户栏目',
				height : portletData.height || 300,
				iconCls : portletData.iconCls || 'icon-sys-portlet',
				layout : 'fit',
				tools : [{
					type : 'refresh',
					portletId : portlet.portletId,
					handler : function(e, target, header, tool){
						this.loadPortlet(tool.portletId);
					},
					scope : this
				}]
			});
			portal.items.get(col % this.getColumns()).add(portletPanel);
			portal.doLayout();
			this.loadPortlet(portlet.id);
			return portletPanel;
		}else{
			return portlets[0];
		}
	},
	loadPortlet : function(portletId){
		// 根据命名规则获得栏目对象
		var portlets = Ext.ComponentQuery.query('uxPortlet[key=' + portletId + ']');
		if(portlets.length > 0){
			var portletPanel = portlets[0];
			// 获得门户对象
			var portal = this.getPortalPanel();
			// 获得栏目数据
			var portletData = this.loadPortletData(portletPanel.appKey, portletPanel.portletId, function(){
				this.loadPortlet(portletId);
			}, this);
			if(!Ext.isDefined(portletData)){
				return;
			}
			// 获得页面对性
			var pageManager = AscApp.getController('AscPageManager');
			var pageCmp = pageManager.getPageCmp(portletPanel.appKey, portletData.moduleId, portletData.pageId, portletData.params, {}, function(){
				this.loadPortlet(portletId);
			}, this);
			if(!Ext.isDefined(pageCmp)){
				return;
			}
			pageCmp.header = false;
			pageCmp.tbar = undefined;
			portletPanel.removeAll();
			portletPanel.add(pageCmp);
			portal.doLayout();
			if(Ext.isFunction(pageCmp.doRefresh)){
				pageCmp.doRefresh();
			}
		}
	},
	// 删除桌面栏目
	removePortlet : function(key){
		// 获得门户对象
		var portal = this.getPortalPanel();
		// 根据命名规则获得栏目对象
		var portlets = Ext.ComponentQuery.query('uxPortlet[key=' + key + ']');
		if(portlets.length > 0){
			var pc = portlets[0].findParentByType('uxPortalColumn');
			pc.remove(portlets[0]);
		}
	},
	// 清除桌面栏目
	clearPortlet : function() {
		// 获得门户对象
		var portal = this.getPortalPanel();
		// 获取所有的栏目对象
		var portlets = Ext.ComponentQuery.query('uxPortlet');
		if(portlets.length > 0){
			for(var i = portlets.length - 1; i >= 0; i--){				
				var pc = portlets[i].findParentByType('uxPortalColumn');
				if(pc){					
					pc.remove(portlets[i]);
				}
			}
		}
	},
	
	// 更新桌面栏目列数
	updateColumns : function(columns){
		var cols = columns || 3, items;
		switch(cols){
		case 2 : 
			items = [{
				columnWidth:0.5
			},{
				columnWidth:0.5
			}];
			break;
		case 4 : 
			items = [{
				columnWidth:0.25
			},{
				columnWidth:0.25
			},{
				columnWidth:0.25
			},{
				columnWidth:0.25
			}];
			break;
		default :
			items = [{
				columnWidth:0.33
			},{
				columnWidth:0.33
			},{
				columnWidth:0.34
			}];
			break;
		}
		// 获得门户面板
		var portal = this.getPortalPanel();
		portal.removeAll();
		portal.add(items);
		portal.doLayout();
	},
	// 更新门户桌面栏目位置
	updatePortletsPos : function(){
		// 获得门户对象
		var portal = this.getPortalPanel();
		// 更新桌面栏目位置
		portal.items.each(function(column, col){
			column.items.each(function(panel, row){
				var portlet = AscApp.getPortlet(panel.key);
				if(portlet){
					portlet.col  = col;
					portlet.row  = row;
				}
			}, this);
		}, this);
	},
	// 更新门户桌面栏目高度
	updatePortletHeight : function(key, h){
		var portlet = AscApp.getPortlet(key);
		if(portlet){
			portlet.height = h;
		}
	},
	
	// 添加快捷按钮
	addShortcut : function(shortcut){
		/*数据结构
		{
			id : 'Demo.sctTest',
			appKey : 'Demo',
			shortcutId : 'sctTest'
		}
		 */
		// 数据结构不全，不做操作
		if(!Ext.isDefined(shortcut.id) || !Ext.isDefined(shortcut.appKey)){
			return;
		}
		if(Ext.isDefined(this.getShortcutData(shortcut.id))) {
			this.renderShortcutToDesk(shortcut.id);
		} else {
			this.loadShortcutData(shortcut, function(){this.renderShortcutToDesk(shortcut.id);}, this);
		}
	},
	
    // 获得桌面栏目定义数据, 参数shortcutKey为用户配置中shortcut的id
    getShortcutData : function(shortcutKey){
    	return this.shortcutDatas[shortcutKey];
    },
	
	// 通过direct从对应的应用中拿快捷按钮数据放到shortCutDatas 中并回调 renderShortCutToDesk
    loadShortcutData : function(shortcut, fn, scope) {
    	try{
    		var appManager = AscApp.getController('AscAppManager');
    		var getShortcutFn = appManager.getEngineDirectFn(shortcut.appKey, 'getShortcut');
    		getShortcutFn(shortcut.shortcutId, function(result, e){
    			if(result && result.success){
    				var shortcutData = Ext.apply({}, result.data);
    				shortcutData.appKey = shortcut.appKey;
    				shortcutData.key = shortcut.id;
    				this.shortcutDatas[shortcut.id] = shortcutData;
    				console.log(shortcutData);
    				if(Ext.isDefined(fn)){
    					fn.call(scope || this);
    				}
    				Asc.common.Message.log('装载快捷按钮[' + shortcut.id + ']数据执行完毕！');
    			}else{
    				Asc.common.Message.showError('装载快捷按钮[' + shortcut.id + ']失败！');
    			}
    		}, this);
    	} catch (e) {
    		Asc.common.Message.showError('装载快捷按钮[' + shortcut.id + ']失败！');
    	}
	},
	
	// 直接添加对应的快捷按钮数据到桌面上
	renderShortcutToDesk : function(shortcutKey) {
		var shortcuts = Ext.ComponentQuery.query('button[key=' + shortcutKey + ']');
		if(shortcuts.length === 0){
			this.getShortcutPanel().addShortcut(this.getShortcutData(shortcutKey));
		}
	},
	
	// 移除快捷菜单
	removeShortcut : function(key){
		this.getShortcutPanel().removeShortcut(key);
	},
	// 清除快捷菜单
	clearShortcuts : function(){
		this.getShortcutPanel().clearShortcuts();
	}
});