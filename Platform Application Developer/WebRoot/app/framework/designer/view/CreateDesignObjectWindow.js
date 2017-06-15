// 创建设计对象
Ext.define('Asc.framework.designer.view.CreateDesignObjectWindow', {
	
	extend: 'Ext.window.Window',
	
	alias: 'widget.AscDesignerCreateDesignObjectWindow',
	
	title : '创建设计对象窗口-请输入键值',
	
	iconCls : 'icon-sys-add',
	
    modal: true,
    
    width: 400,
    
    height:250,
    
    layout: 'fit',
    
    buttonAlign:'center',

    buttons: [{
        text: '确定',
        iconCls : 'icon-sys-confirm'
    }, {
        text: '取消',
        iconCls : 'icon-sys-cancel'
    }],
    
    items : {
    	xtype : 'form',
		labelWidth: 80,
        bodyStyle:'padding:5px',
		defaults: {
			  anchor: "100%",
			  submitValue: true
		},
        defaultType: 'displayfield',
		items:[{
			fieldLabel : 'Application Id',
			name : 'appId'
		},{
			fieldLabel : 'Parent Key',
			name : 'parentKey'
		},{
			fieldLabel : 'Object Type',
			name : 'type'
		},{
			xtype : 'textfield',
			fieldLabel : 'Object Key',
			name : 'key',
			allowBlank : false,
			selectOnFocus:true
		}]
    }
});