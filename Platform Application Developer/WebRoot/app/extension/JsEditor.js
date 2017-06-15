Ext.define('Asc.extension.JsEditor', {
		
	extend : 'Ext.form.field.Picker',
	
	alias : 'widget.AscJsEditor',
	
	pickerAlign : 'tl-bl?',
	
	matchFieldWidth : true,
	
	minListHeight : 200,
	
	minListWidth : 360,
	
	editable : false,
	
	clickNum : 0,
	/**
	 * 重写createPicker方法，实现显示一个panel，panle中包含一个textarea文本框和确定、取消按钮
	 */
	createPicker : function() {
		var me = this, picker,win;
		//宽度小于最小宽度的时候，list的宽度是设置的最小宽度，否则list的宽度是trigger的宽度
		var width = me.getWidth()>me.minListWidth?me.getWidth():me.minListWidth;
		picker = me.picker = Ext.create('Asc.extension.JsEditorPanel',{
			renderTo : Ext.getBody(),
			//trigger的宽度小于最小宽度的时候，picker的宽度是设置的最小宽度，否则picker的宽度是trigger的宽度
			width : width,
			//picker的高度为给定的最小高度
			height : me.minListHeight,
			floating : true,
			border : true,
			bodyBorder : false,
			shadow : false,
			resizable : true,
			onCtrlCenter : function(){
				me.showFullScreen();
			}
		});
		return picker;
	},
	showFullScreen : function(){
		var me = this;
		me.collapse();
		var p = Ext.create('Asc.extension.JsEditorPanel',{
			width : document.body.clientWidth * 0.8,
			height : document.body.clientHeight * 0.8,
			layout: 'fit',
			value: me.picker.getValue(),
			onCtrlCenter : function(){
				var value = p.getValue();
				me.setValue(value);
				win.close();
			}
		});
		var win = Ext.widget('window', {
			modal : true,
			width : document.body.clientWidth * 1,
			height : document.body.clientHeight * 1,
			layout : 'fit',
			resizable : false,
			closable : false,
			items : p
		}).show();
		p.doLayout();
		var pos = p.getCursorPosition();
		p.focusEditor();
	},
	getValue : function(){
		return this.value;
	},
	/**
	 * 重写onExpand，在展开下拉列表时，需要做的事情，在这个方法里处理。
	 */
	onExpand : function() {
		var me = this,picker = me.getPicker();
		picker.setValue(me.value);
		picker.clearSelection();
		picker.focusEditor();
		//宽度小于最小宽度的时候，list的宽度是设置的最小宽度，否则list的宽度是trigger的宽度
		var width = me.getWidth() > me.minListWidth?me.getWidth():me.minListWidth;
		//重新设置picker的宽度和高度,因为在调用expand方法时，已经重新设置了picker的宽度和高度
		picker.setSize(width,me.minListHeight);
		picker.alignTo(me.triggerWrap, me.pickerAlign);
	},
	onCollapse : function(){
		this.value = this.picker.getValue();
	}
});