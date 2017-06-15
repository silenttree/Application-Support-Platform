Ext.define('Asc.extension.editor.DisplayField', {
	// 指定基类
	extend : 'Ext.form.field.Display',
	// 设置别名
	alias : 'widget.ascdisplayfield',
	
	submitValue : false,
	/*
	fieldSubTpl: [
		'<div id="{id}" role="input" ',
		'<tpl if="fieldStyle"> style="{fieldStyle}"</tpl>', 
		' class="{fieldCls}">{value}',
		'</div>',
		'<tpl if="readOnly === false">',
			'<input type="hidden" name="{name}" value="{value}">',
		'</tpl>', 
		{
			compiled: true,
			disableFormats: true
		}
	],
	getDisplayValue: function() {
		var display = this.callParent(arguments);
		if(this.submitValue){
			var value = this.getRawValue();
			var name = this.name || this.getInputId();
			display = display + '<input type="hidden" name="' + name + '" value="' + display + '">';
		}
		return display;
	},
	*/
    getDisplayValue: function() {
        var me = this,
            value = this.getRawValue(),
            display;
        /*
        if (me.renderer) {
             display = me.renderer.call(me.scope || me, value, me);
        } else {
             display = me.htmlEncode ? Ext.util.Format.htmlEncode(value) : value;
        }
        */
        display = me.htmlEncode ? Ext.util.Format.htmlEncode(value) : value;
        return display;
    },
	valueToRaw : function(value){
		value = this.callParent(arguments);
		if(Ext.isFunction(this.renderer)){
			value = this.renderer(value, this);
		}
		return value;
	}
});