	Ext.define('Asc.extension.editor.CdruSelector',{
		extend: 'Ext.window.Window',
		alias: 'widget.cdruselector',
		// 设置引用类
		requires : ['Asc.extension.editor.CdruSelectorPanel'],
		
	    /**
		 * @cfg {string} 单位范围表达式 空表示全局范围
		 */
		range: '',
		
		/**
		 * @cfg {string} 选择类型表达式 包含c表示要选单位 包含d表示要选部门 包含r表示要选角色 包含u表示要选用户
		 */
		selectType: 'cdru',
		
		/**
		 * @cfg {boolean} 是否单选
		 */
		singleSelect: false,
		
		/**
		 * @cfg {string} 已选中的数据表达式
		 */
		value: '',
		
		/**
		 * @cfg {function} 回调函数
		 */
		callback: Ext.emptyFn,
		
		title: '组织结构选择器',
		height: 400,
		border: false,
		width: 550,
		layout: 'fit',
		
		initComponent: function() {
			var me = this,
				selectotPanel;
			selectotPanel = Ext.create('Asc.extension.editor.CdruSelectorPanel', {
				range : me.range,
				selectType : me.selectType,
				singleSelect : me.singleSelect,
				value : me.value,
				callback : me.callback,
				closeFn : function(){
					me.close();
				}
			});
			
			me.items = [selectotPanel];
			// 调用父类的initComponent方法
			me.callParent();
		}

	});