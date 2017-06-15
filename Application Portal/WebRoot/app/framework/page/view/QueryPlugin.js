Ext.define('Asc.framework.page.view.QueryPlugin', {
	// 指定基类
	extend: 'Ext.AbstractPlugin',
	// 设置别名
	alias : 'widget.AscPageViewQueryPlugin',
	
	enableVagueQuery : true,
	
	enableExactQuery : false,
	
	queryInputs : [],
	
	border : false,
	
	init : function(panel){
		this.parent = panel;
        var me = this;
		var search = Ext.create('Ext.form.field.Trigger', {
			trigger1Cls : Ext.baseCSSPrefix + 'form-search-trigger',
			trigger2Cls : me.enableExactQuery ? Ext.baseCSSPrefix + 'form-trigger' : undefined,
			editable : me.enableVagueQuery,
			emptyText : '模糊查找',
			width : 150,
			listeners : {
				'specialkey' : function(f, e){
					if (e.getKey() == e.ENTER) {
		                me.doVagueQuery(this.getValue());
		            }
				},
				'render' : function(){
					Ext.tip.QuickTipManager.register({
					    target: this.el.id,
					    text: '<b>检索范围：</b>' + me.getVagueQueryTips(me.queryInputs),
					    dismissDelay: 3000
					});
				}
			},
			onTrigger1Click : function(){
				me.doVagueQuery(this.getValue());
			},
			onTrigger2Click : function(){
				me.showExactQuery();
			}
		});
		var tbar = panel.getDockedItems('[dock="top"]')[0];
		tbar.insert(0, search);
	},
	doVagueQuery : function(value){
		if(value != ''){
			this.parent.doQuery({VAGUE : value});
		}
	},
	doExactQuery : function(){
		this.parent.doQuery(this.getQueryParams());
	},
	getQueryParams : function(){
		var form = this.queryPanel.down('form').getForm();
		return form.getValues(false, true);
	},
	showExactQuery : function(){
		if(!this.queryPanel){
			var me = this;
			this.queryPanel = Ext.create('Ext.panel.Panel', {
				title : '精确查找',
				iconCls : 'icon-btn-query',
				width : 150,
				dock : 'left',
				resizable : true,
				resizeHandles : 'e',
				closable : true,
				closeAction : 'hide',
				layout : 'fit',
				items : [{
					xtype : 'form',
					bodyPadding : 5,
					autoScroll : true,
					defaults : {
						labelAlign : 'top',
						labelStyle : 'padding-bottom:3px',
						anchor: '100%',
					},
					defaultType: 'textfield',
					border : true,
					items : me.getQueryInputs(me.queryInputs)
				}]
			});
			this.parent.addDocked(this.queryPanel);
		}else if(this.queryPanel.isHidden()){
			this.queryPanel.show();
		}else{
			this.queryPanel.close();
		}
	},
	getVagueQueryTips : function(fields){
		var tips = '';
		for(n in fields){
			var f = fields[n];
			if(f.qtype == 'String'){
				tips = tips + f.fieldLabel + ';';
			}
		}
		return tips;
	},
	getQueryInputs : function(fields){
		var inputs = [];
		for(n in fields){
			var f = fields[n];
			switch(f.qtype){
			case 'String':
				inputs.push(Ext.apply({
					name : f.name,
					fieldLabel : f.fieldLabel
				}, f.editorCfg));
				break;
			case 'Number':
				inputs.push({
					fieldLabel : f.fieldLabel,
					xtype : 'fieldcontainer',
					layout: 'hbox',
	                defaults: Ext.apply({
	                    flex: 1,
						padding : '0 0 0 5',
						xtype : 'numberfield',
						anchor: '100%',
						labelWidth : 22,
						labelSeparator : '',
						hideTrigger : true
	                }, f.editorCfg),
					items : [{
						name : f.name + "_begin",
						fieldLabel : '>='
					}, {
						name : f.name + "_end",
						fieldLabel : '<='
						
					}]
				});
				break;
			case 'Dictionary':
				inputs.push(Ext.apply(f, f.editorCfg));
				break;
			case 'Date':
				inputs.push({
					fieldLabel : f.fieldLabel,
					xtype : 'fieldcontainer',
					layout: 'anchor',
					defaults: Ext.apply({
						padding : '0 0 0 5',
						xtype : 'datefield',
						anchor: '100%',
						labelWidth : 22,
						labelSeparator : '',
						format : 'Y-m-d'
					}, f.editorCfg),
					items : [{
						name : f.name + "_begin",
						fieldLabel : '从'
					}, {
						name : f.name + "_end",
						fieldLabel : '到'
						
					}]
				});
				break;
			}
		}
		inputs.push({
			xtype : 'button',
			margin : '20',
			iconCls : 'icon-btn-query',
			text : '执行检索',
			handler : function(){
				this.doExactQuery();
			},
			scope : this
		})
		return inputs;
	}
});