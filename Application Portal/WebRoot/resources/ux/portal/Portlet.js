/**
 * @class Ext.ux.portal.Portlet
 * @extends Ext.panel.Panel
 * A {@link Ext.panel.Panel Panel} class that is managed by {@link Ext.app.PortalPanel}.
 */
Ext.define('Ext.ux.portal.Portlet', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.uxPortlet',
	layout: 'fit',
	anchor: '100%',
	frame: true,
	draggable: {
		moveOnDrag: false	
	},
	cls: 'x-portlet',

	listeners : {
		'afterrender' : function(){
			var id = this.getEl().id;
			var height = this.getEl().getHeight();
			var panel = this;
			this.resizeWrap = Ext.create('Ext.resizer.Resizer', {
				el : this.getEl(),
				handles : 's',
				height : height,
				minHeight: 50,
				dynamic: true,
				listeners : {
					'resize':function(r, w, h, e){
						panel.setHeight(h);
						// 设置栏目高度
						var portal = Asc.App.getAscDesktop().getAscPortal();
						if(portal){
							portal.updatePortletHeight(panel.key, h);
						}
					}
				}				
			});
		},
		'beforedestroy' : function(){
			delete this.resizeWrap;
		}
	}
});