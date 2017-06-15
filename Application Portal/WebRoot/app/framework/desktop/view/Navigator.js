// 页面下部的页脚区界面
Ext.define('Asc.framework.desktop.view.Navigator', {
	
	extend: 'Ext.container.ButtonGroup',
	
	alias: 'widget.AscDesktopNavigator',
	
	frame : false,
	
	border : false,
	
	defaults : {
		listeners : {
			mouseover : function(){
				this.showMenu(true);
			}
		}
	},
    setNavigators : function(navigators){
		Asc.common.Message.log('初始化导航菜单', this, 'log');
		var buttons = [];
		// 菜单映射
		var menus = {};
		for(var i = 0;i < navigators.length;i++){
			var record = navigators[i];
			if(record.f_disabled === 0) {
				continue;
			}
			var handler = Ext.emptyFn;
			if(Ext.isDefined(record.f_script) && record.f_script != ''){
				try{
					eval('handler=' + record.f_script);
				}catch(err){
					
				}
			}else if(Ext.isDefined(record.f_module_key)){
				handler = function(){
					var desktop = AscApp.getController('AscDesktop');
					desktop.openModule(this.appKey, this.moduleKey);
				}
			}
			if(record.f_parent_id == 0){
				// 添加根菜单
				var button = {
					//name : record.f_key,
					text : record.f_caption,
					iconCls : record.f_icon,
					tooltip : record.f_note,
					xtype : 'splitbutton',
					arrowAlign : 'bottom',
					handler : handler,
					appKey : record.f_application_key,
					moduleKey : record.f_module_key,
					//navigatorId : record.id,
					menu : []
				}
				buttons.push(button);
				menus[record.id] = button;
			}else{
				// 添加子菜单
				var menu = {
					//name : record.f_key,
					text : record.f_caption,
					iconCls : record.f_icon,
					tooltip : record.f_note,
					appKey : record.f_application_key,
					moduleKey : record.f_module_key,
					handler : handler
					//navigatorId : record.id
				}
				if(menus[record.f_parent_id]){
					if(!menus[record.f_parent_id].menu){
						menus[record.f_parent_id].menu = [];
					}
					menus[record.f_parent_id].menu.push(menu);
				}
				menus[record.id] = menu;
			}
		}
		this.add(buttons);
		this.doLayout();
    }
});