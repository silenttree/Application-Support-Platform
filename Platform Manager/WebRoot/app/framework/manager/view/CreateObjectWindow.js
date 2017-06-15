// 创建设计对象
Ext.define('Asc.framework.manager.view.CreateObjectWindow', {
	
	extend: 'Ext.window.Window',
	
	alias: 'widget.AscManagerCreateObjectWindow',
	
	title : '创建对象窗口-请输入标识',
	
	iconCls : 'icon-sys-add',
	
    modal: true,
    
    width: 400,
    
    height:230,
    
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
			fieldLabel : '上级对象',
			name : 'parent'
		},{
			fieldLabel : '对象类型',
			name : 'type'
		},{
			xtype : 'textfield',
			fieldLabel : '对象标识',
			name : 'key',
			allowBlank : false,
			selectOnFocus:true
		}]
    }
});