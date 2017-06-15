Ext.define('Asc.extension.JsonEditor', {
		
	extend : 'Ext.form.field.Picker',
	
	alias : 'widget.AscJsonEditor',
	
	pickerAlign : 'tl-bl?',
	
	matchFieldWidth : true,
	
	minListHeight : 260,
	
	minListWidth : 550,
	
	shadow : 'side',
	
	editable : false,
	//设计对象的key
	key : '',
	//初始化参数
	initialValue : [],
	//设计对象的链接app
	appId : '',
	
	/**
	 * 重写createPicker方法，返回一个panel
	 */
	createPicker : function() {
		var me = this, picker;
	 	var textField = 
	 		new Ext.form.TextArea({
	 			region : 'center',
  				hideLabel : true,
  				anchor : '100%',
  				disabled : true
  			});
	 	Ext.define('treeJsonModel',{
	 		extend : 'Ext.data.Model',
	 		fields: [
	 		         {name : 'value'},
	 		         {name : 'text',  type : 'string'},
	 		         {name : 'type', type : 'string'},
	 		         {name : 'cfg', type : 'object'}
	 		         ]
	 	});
	 	var treeStore = Ext.create('Ext.data.TreeStore', {
	 		model:'treeJsonModel',
	 		root: {
	 			expanded : true,
	 			text : 'object',
	 			type : 'object',
	 			autoLoad : false,
	 			value : {},
	 			cfg : {},
	 			children : []
	 		},
	 		folderSort: true
	 	});
		var tree =	Ext.create('Ext.tree.Panel', {
 			autoScroll : true,
 			minSize : 50,
 			width : 240,
	        maxSize : 350,
			store : treeStore,
			border : false,
			region : 'west',
			rootVisible : true,
			hideHeaders : true,
			//修改节点的名字
			plugins: [ new Ext.grid.plugin.CellEditing({
	            clicksToEdit: 2,
	            listeners : {
	            	//编辑之前检查是否是根节点，根节点不可更名
	            	beforeedit : function(editor, e){
	            		if(e.record.data.root){
	            			e.cancel = true;
	            		}else{
	            			e.cancel = false;
	            		}
	            	},
	            	//编辑后提交，不显示红色的三角
	            	edit : function(editor, e) {
	            	   var node = e.record.parentNode;
	            	   var newValue = e.value;
	            	   var oldValue = e.originalValue;
	            	   e.record.set('text',oldValue);
	            	   var flag = findChildNode(node,newValue);
	            	   if(!flag){
	            		   e.record.set('text',newValue);
	            	   }
	            	   e.record.commit();
	            	}
	            }
	        })],
	        columns: [{
	    		xtype: 'treecolumn', 
	    		flex: 2,
	    		dataIndex: 'text',
	    		editor: {
	    			allowBlank: false
	    		}
	    	}],
			split : true,
			//节点可拖拽
			viewConfig : {
				plugins : {
					ptype : 'treeviewdragdrop',
					appendOnly : true
				},
				listeners : {
					//拖拽之前，检查重复
					beforedrop : function(node, data, overModel, dropPosition, dropHandlers){
						dropHandlers.wait = true;
						var fg = findChildNode(overModel, data.records[0].get('text'));
						if(fg){
							dropHandlers.cancelDrop();
						}else{
							dropHandlers.processDrop();
						}
					}
				}
			},
			/**
			 * 增加新节点
			 */
			appendAttributeNode : function(parentNode, name, val, type, cfg){
  				var editable = true;
  				if(parentNode.type == 'array'){
  					editable = false;
  				}
  				var node = parentNode.appendChild({
  					text : name,
  					type : type,
  					loaded : true,
  					editable : editable,
  					value : val,
  					cfg : cfg,
  					leaf : type != 'object' && type != 'array'
  				});
  				return node;
  			},
			getNodeValue : function(node){
  				if(!node.leaf){
  					return tree.getRootNode().childNodes;
  				}else{
  					var object = this.getNodeValue(node.parentNode);
  					return object[node.text];
  				}
  			},
  			/**
  			 * 刷新节点,根据记录的数据，重新加载改节点的所有子节点
  			 */
	        refreshNode : function(obj,node){
	        	me.tree.initTree(obj,node);
	        },
	        /**
	         * 初始化一棵树
	         */
	        initTree : function(obj, node){
	        	//删除所有节点,否则会在原来的树上继续添加这些节点
	        	node.removeAll();
	        	//遍历obj中的所有对象
	        	for(var i = 0; i < obj.length; i++){
	        		var childnode = me.tree.appendAttributeNode(node,obj[i].text,obj[i].value,obj[i].type,obj[i].hasOwnProperty('cfg')?obj[i].cfg:{});
	        		if(obj[i].type == 'array' || obj[i].type == 'object'){
	        			me.tree.initTree(obj[i].children, childnode);
	        		}
	        	}
	        },
	        /**
			* 根据初始值，第一层的text、value和类型，创建节点
			*/
			createNode : function(name, type, initialValue){
				for(var n in initialValue){
					if(initialValue[n]['name'] == name && initialValue[n]['type'] == type){
						var childnode = me.tree.appendAttributeNode(me.tree.getRootNode(),initialValue[n]['name'],initialValue[n]['value'],initialValue[n]['type'],initialValue[n]['cfg']);
						if(initialValue[n].hasOwnProperty('children')){
							me.tree.createTrunk(childnode,initialValue[n]['children']);
						}
					}
				}
			},
			/**
			*	创建枝干(根据初始值，创建第二层节点)
			*/
			createTrunk : function(node,children){
				for(var i = 0; i < children.length; i++){
	        		var childnode = me.tree.appendAttributeNode(node,children[i]['name'], children[i]['value'], children[i]['type'], children[i].hasOwnProperty('cfg')?children[i]['cfg']:{});
	        		if(children[i].hasOwnProperty('children')){
	        			me.tree.initTree(children[i]['children'], childnode);
	        		}
	        	}
			}
		});
		tree.on('itemclick',function(model, record, item, index, e){
			nameField.disable();
			typeField.disable();
			addAction.disable();
			deleteAction.disable();
			upAction.disable();
			downAction.disable();
			textField.enable();
			if(!record){
				textField.setValue('');
				return;
			}
			downAction.disable();
			upAction.disable();
			var plugin = tree.findPlugin('cellediting');
			if(!record.isRoot()){
				nameField.setHideTrigger(true);
				addAction.disable();
				deleteAction.enable();
				nameField.disable();
				typeField.disable();
				//renameAction.enable();
				if(record.parentNode.data.type == 'array'){
					if(!record.isFirst()){
						upAction.enable();
					}
					if(!record.isLast()){
						downAction.enable();
					}
				}
			}else{
				//根节点不可编辑
				nameField.setHideTrigger(false);
			}
			if(record.data.type == 'object' || record.data.type == 'array'){
				//下拉控件隐藏
				listgrid.setVisible(false);
				// 设置显示域的值
				var v = me.encodeJsonObject(record);
				textField.setValue(me.getObjectFormatString(false, v, 0));
				textField.el.dom.readOnly = true;
				textField.disable();
				// 设置添加子对象
				if(record.data.type == 'object'){
					nameField.enable();
				}
				typeField.enable();
				//如果可以添加，则类型显示为string类型
				typeField.setValue('string');
				addAction.enable();
			}else if(record.data.type == 'designobject'){
				//下拉控件显示，textarea不可修改
				textField.disable();
				listgrid.setVisible(true);
				listgrid.setValue(record.data.value);
				listgrid.setType(getNodeCfg(me.initialValue,record).type);
				listgrid.setScope(getNodeCfg(me.initialValue,record).scope);
				listgrid.setReturnType(getNodeCfg(me.initialValue,record).returnType);
				picker.doLayout();
				//设置显示域的值
				textField.setValue(me.getObjectFormatString(false, listgrid.getValue(), 0));
				//textField.originalValue = record.data.value;
				textField.el.dom.readOnly = false;
				//类型显示为选中节点的类型
				typeField.setValue(record.data.type);
			}else{
				//下拉控件隐藏
				listgrid.setVisible(false);
				// 设置显示域的值
				textField.setValue(record.data.value);
				textField.originalValue = record.data.value;
				textField.el.dom.readOnly = false;
				//类型显示为选中节点的类型
				typeField.setValue(record.data.type);
			}
			if(record.data.type == 'object'){
				nameField.setValue('');
			}else{
				nameField.setValue(record.data.text);
			}
 		});
		var getNodeCfg = function(value,node){
			var cfg = getRecordCfg(value,node);
			if(cfg.length == 0){
				cfg = {returnType : '',type:[], scope : 'module'};
			}
			return cfg;
		};
		var getRecordCfg = function(value, node){
			for(var n in value){
				if(value[n].name == node.data.text){
					return value[n].cfg;
				}
				if(value[n].hasOwnProperty('children')){
					getRecordCfg(value[n].children, node);
				}
			}
			return {};
		};
		//通过change事件，来控制tree节点的值
		textField.on('change',function(field,newValue,oldValue){
			var type = tree.getSelectionModel().getSelection()[0].data.type;
			//只有这三种类型的时候，节点内容才能编辑，节点的value值才会发生改变
			if(type == 'string' || type == 'number' || type == 'boolean'){
				tree.getSelectionModel().getSelection()[0].set('value',textField.getValue());
			}
			//设置文本域的真实值，也就是自动更新下拉列表的文本域的真实值，让下拉文本域的值时时都跟着tree变化
			me.setValue(me.encodeJsonObject(me.tree.getRootNode()));
		});
		//获取initialValue初始值第一层的数据
		var getStore = function(initialValue){
			var datas = [];
			for(var n in initialValue){
				var obj = {};
				if(initialValue[n].hasOwnProperty('name')){
					obj.text = initialValue[n]['name'];
				}else{
					obj.text = '';
				}
				if(initialValue[n].hasOwnProperty('type')){
					obj.type = initialValue[n]['type'];
				}else{
					obj.type = 'object';
				}
				if(initialValue[n].hasOwnProperty('value')){
					obj.value = initialValue[n]['value'];
				}else{
					obj.value = '';
				}
				obj.checked = false;
				datas.push(obj);
			}
			return datas;
		};
		var nameStore = Ext.create('Ext.data.Store',{
			fields: ['text', 'type', 'value'],
			data: getStore(me.initialValue)
		});
		//属性名称填写框
		var nameField = Ext.create('Ext.form.field.ComboBox', {
			triggerAction:'all',
			mode:'local',
			width : 150,
			value : '',
			displayField: 'text',
			//valueField: 'value',//添加此属性的话，会出现默认值是下拉列表的第一项
			editable : true,
			emptyText : '创建属性名称',
			store : nameStore,
			listeners : {
				//添加选择事件
				select : function(combo, records){
					if(!findChildNode(me.tree.getRootNode(),records[0].data.text)){
						me.tree.createNode(records[0].data.text, records[0].data.type, me.initialValue);
					}
					combo.clearValue();
				},
				//添加回车事件
				specialkey : function(field,e){
					if(e.getKey() == e.ENTER){
						//触发创建按钮事件
						addAction.execute();
					}
				}
			},
		});
		/*	
		new Ext.form.TextField({
			width : 150,
			disabled : true,
			emptyText : '创建属性名称',
			enableKeyEvents: true,//添加键盘事件，这项必须有并且为true（默认为false），否则键盘事件不可用
			listeners : {
				specialkey : function(field,e){
					if(e.getKey() == e.ENTER){
						//触发创建按钮事件
						addAction.execute();
					}
				} 
			}
		});*/
  		//选择类型
  		var typeField = new Ext.form.field.ComboBox({
			triggerAction:'all',
			disabled : true,
			mode:'local',
			width:100,
			editable:false,
			forceSelection: true,
			emptyText:'选择类型',
			value:'string',
			store:['string', 'boolean', 'number', 'object', 'array', 'designobject'],
			enableKeyEvents: true,//添加键盘事件，这项必须有并且为true（默认为false），否则键盘事件不可用
			listeners : {
				specialkey : function(field,e){
					if(e.getKey() == e.ENTER){
						//触发创建按钮事件
						addAction.execute();
					}
				} 
			}
  		});
  		//创建按钮
  		var addAction = new Ext.Action({
			text : '创建',
			iconCls : 'icon-sys-add',
			disabled : true,
			handler : function(){
				var type = typeField.getValue();
				var parentNode = me.tree.getSelectionModel().getSelection()[0];
				var name = (parentNode.data.type == 'array' ? parentNode.childNodes.length : nameField.getValue());
				if(parentNode.data.type != 'array' && name == ''){
					return;
				}
				var value;
				switch(type){
					case 'boolean':
						value = false;
						break;
					case 'string':
						value = '';
						break;
					case 'number':
						value = 0;
						break;
					case 'object':
						value = {};
						break;
					case 'designobject':
						value = {};
						break;
					case 'array':
						value = [];
						break;
					default:
						value = undefined;
						break;
				}
				//查找同级节点，如果存在相同的节点，则无法创建
				if(findChildNode(parentNode,name)){
					return false;
				}else{
					var node = tree.appendAttributeNode(parentNode, name, value, type, {});
					parentNode.expand();
					tree.getSelectionModel().select(node);
					tree.fireEvent('itemclick',me.tree, node, null, node.index, null);
					//tree.fireEvent('select',me.tree.getSelectionModel(),node,node.index);
				}
			}
  		});
  		/**
  		 * 遍历tree选中节点的所有同级子节点，查找是否有重名的问题
  		 */
  		var findChildNode = function(node,name){
			if(node.hasChildNodes() &&  node.childNodes.length>0){
				var childnodes = node.childNodes;
				//从当前节点中取出所有子节点依次遍历，查找是否有相同的text的值的节点
				for(var i=0;i<childnodes.length;i++){
					var rootnode = childnodes[i];
					if(rootnode.data.text == name){
						return true;
					}
				}
			}
			return false;
  		};
  		//删除按钮
  		var deleteAction = new Ext.Action({
			text : '删除',
			iconCls : 'icon-sys-delete',
			disabled : true,
			handler : function(){
				var node = me.tree.getSelectionModel().getSelection()[0];
				var parentNode = node.parentNode;
				var object = tree.getNodeValue(parentNode);
				var index = parentNode.indexOf(node);
				//如果是数组，把数组的当前节点以下的节点都上移一个位置，并修改对应的text属性值
				if(parentNode.data.type == 'array'){
					parentNode.removeChild(node);
					for(var i=index;i<parentNode.childNodes.length;i++){
						parentNode.childNodes[i].data.text = i + '';
					}
					//记录新节点的父节点的json对象，并重新加载父节点
					var obj = me.decodeStringJson(me.encodeJsonObject(parentNode));
					//先删除节点的所有子节点
					parentNode.removeAll();
					//重新加载该节点的所有子节点
					me.tree.refreshNode(obj,parentNode);
				}else{//否则直接删除节点
					delete object[node.text];
					parentNode.removeChild(node);
				}
				//treeStore.sort([{property:'text',direction:'ASC'}]);
				tree.getSelectionModel().select(parentNode);
				tree.fireEvent('itemclick',me.tree,parentNode,null,parentNode.index,null);
				//tree.fireEvent('select', me.tree.getSelectionModel(), parentNode, parentNode.index);
			}
  		});
  		var upAction = new Ext.Action({
			text : '上移',
			iconCls : 'icon-sys-moveup',
			disabled : true,
			handler : function(){
				var node = me.tree.getSelectionModel().getSelection()[0];
				if(node.isFirst()){
					return;
				}
				var parentNode = node.parentNode;
				var index = parentNode.indexOf(node);
				//调换上下两个节点，并把节点的text属性值也调换
				var temp = parentNode.childNodes[index];
				parentNode.childNodes[index] = parentNode.childNodes[index - 1];
				parentNode.childNodes[index - 1] = temp;
				parentNode.childNodes[index].data.text = index + '';
				parentNode.childNodes[index - 1].data.text = index - 1 + '';
				//记录新节点的父节点的json对象，并重新加载父节点
				var obj = me.decodeStringJson(me.encodeJsonObject(parentNode));
				//先删除节点的所有子节点
				parentNode.removeAll();
				//重新加载该节点的所有子节点
				me.tree.refreshNode(obj,parentNode);
				//选中当前操作的节点
				tree.getSelectionModel().select(parentNode.childNodes[index-1]);
				//触发select事件
				tree.fireEvent('itemclick',me.tree,parentNode.childNodes[index-1],null,parentNode.childNodes[index-1].index,null);
				//tree.fireEvent('select', me.tree.getSelectionModel(), parentNode.childNodes[index-1], parentNode.childNodes[index-1].index);
			}
  		});
  		var downAction = new Ext.Action({
			text : '下移',
			disabled : true,
			iconCls : 'icon-sys-movedown',
			handler : function(){
				var node = me.tree.getSelectionModel().getSelection()[0];
				if(node.isLast()){
					return;
				}
				var parentNode = node.parentNode;
				var index = parentNode.indexOf(node);
				//调换上下两个节点，并把节点的text属性值也调换
				var temp = parentNode.childNodes[index];
				parentNode.childNodes[index] = parentNode.childNodes[index + 1];
				parentNode.childNodes[index + 1] = temp;
				parentNode.childNodes[index].data.text = index + '';
				parentNode.childNodes[index + 1].data.text = index + 1 + '';
				//记录新节点的父节点的json对象
				var obj = me.decodeStringJson(me.encodeJsonObject(parentNode));
				//移除该节点的所有子节点
				parentNode.removeAll();
				//并重新加载父节点
				me.tree.refreshNode(obj,parentNode);
				//选中当前操作的节点
				tree.getSelectionModel().select(parentNode.childNodes[index+1]);
				//触发select事件
				tree.fireEvent('itemclick',me.tree,parentNode.childNodes[index+1],null,parentNode.childNodes[index+1].index,null);
				//tree.fireEvent('select', me.tree.getSelectionModel(), parentNode.childNodes[index+1], parentNode.childNodes[index+1].index);
			}
  		});
  		var tools = [nameField,
  					typeField,
  					addAction,'-',
  					deleteAction,'-',
  					upAction,'-',
  					downAction
  					];
  		var buttons = ['->',{
			text : '取消',
			iconCls : 'icon-sys-cancel',
			handler : function(){
				me.collapse();
			},
			scope : me
		},'-',{
			text : '确定',
			iconCls : 'icon-sys-confirm',
			handler : function(){
				//设置文本域的真实值
				me.setValue(me.encodeJsonObject(me.tree.getRootNode()));
				me.collapse();
			},
			scope : me
		}];
  		var listgrid = new Asc.extension.DesignObjectEditor({
  			region : 'north',
  			key : me.key,
  			appId : me.appId,
  			config : {
  				returnType : '',
  				type : ['query','datasource','table','dictionary','document','module','menu','tableform','view','urlpage','treepage','queryformpage','layout','formtablelayout','formpositionlayout','panel','flow'],
  				//type : [],
  				scope : null
  			},
  			//config : tree.getSelectionModel().getSelection()[0].data.cfg,
  			onCollapse : function(){
  				tree.getSelectionModel().getSelection()[0].data.value = this.getValue();
  				tree.doLayout(); 
  				textField.setValue(me.getObjectFormatString(false, this.getValue(), 0));
  			}
  		});
  		listgrid.setScope(listgrid.config.scope);
  		var ui = Ext.create('Ext.panel.Panel',{
  			layout : 'border',
  			region : 'center',
  			border : false,//有边框
			bodyBorder : false,
			shadow : false,
			items : [listgrid,textField]
  		});
		var panel = Ext.create('Ext.panel.Panel',{
			//显示到layer层里面（如果显示到picker的一些属性里面（bodyEl），会出现覆盖panel的部分范围，导致panel的部分范围看不到）
			renderTo : Ext.getBody(),
			width : me.minListWidth,//宽度是设置的最小宽度
			height : me.minListHeight,//高度是设置的最小高度
			layout : 'border',
			border : true,//有边框
			bodyBorder : false,
			floating : true,
			shadow : false,
			tbar : tools,
			items : [tree,ui],
			bbar : buttons,
			//判断焦点的范围
		    collapseIf:function(e){
	    		if(tree.el && e.within(tree.el)){
	    			return false;
	    		}
	    		if(panel.el && e.within(panel.el)){
	    			return false;
	    		}
	    		if(listgrid.isExpanded){
	    			return false;
	    		}
	    		if(typeField.isExpanded){
	    			return false;
	    		}
	    		if(nameField.isExpanded){
	    			return false;
	    		}
	    		return true;
		    }
		});
		me.tree = tree;
		return picker = me.picker = panel;
	},
	/**
	 * 递归树的节点变成json对象，格式是{text:value}
	 */
	encodeJsonObject : function(node){
		if(node.data.type == 'object'){//如果是对象，递归，返回对象
			var obj = {};
			for(var i=0;i<node.childNodes.length;i++){
				chNode = node.childNodes[i];
				obj[chNode.data.text] = this.encodeJsonObject(chNode);
			}
			return obj;
		}else if(node.data.type == 'array'){//如果是数组，递归，返回数组
			var arr = new Array();
			for(var i = 0;i < node.childNodes.length;i++){
				chNode = node.childNodes[i];
				arr.push(this.encodeJsonObject(chNode));
			}
			return arr;
		}else if(node.data.type == 'string' || node.data.type == 'designobject'){//其他返回基本类型
			return node.data.value;
		}else{
			return Ext.decode(node.data.value,true);
		}
	},

	/**
	 * 	json对象变成树的节点对象，格式是{text:"",value : "",type : "" ,children[{树的节点}]}
	 */
	decodeStringJson : function(jsondata){
		var obj=[];
		//遍历json对象
		for(var n in jsondata){
			var type = typeof jsondata[n];
			if(jsondata[n] instanceof Array){
				type = 'array';
			}
			if(type == 'object' && jsondata[n].hasOwnProperty('class') && jsondata[n].hasOwnProperty('data')){
				type = 'designobject';
			}
			if(jsondata.hasOwnProperty(n)){
				var item = {
						text : n,
						value : jsondata[n],
						type : type
				};
				//根据类型判断，是否有children属性
				if(type == 'array' || type == 'object'){
					item.children = this.decodeStringJson(jsondata[n]);
				}
				obj.push(item);
			}
		}
		return obj;
	},
	setValue : function(v){
		this.setRawValue(v);
		this.value = v;
	},
	/**
	 * 设置显示值
	 */
	setRawValue: function(value) {
        var me = this;
        if(typeof value == 'object'){
        	//把字符中含有Unicode码转化成中文字符
        	value = unescape(Ext.encode(value).replace(/\\u/gi, '%u'));
        }
        value = Ext.value(value, '');
        me.rawValue = value;
        //一些子类不能呈现一个inputEl
        if (me.inputEl) {
            me.inputEl.dom.value = value;
        }
        return value;
	},
	getValue : function(){
		return this.value;
	},
	/**
	 * @Overload
	 * 重写onExpand，在展开下拉列表时，需要做的事情，在这个方法里处理。
	 */
	onExpand : function() {
		var me = this,picker = me.getPicker();
		//重新设置picker的宽度和高度,因为在调用expand方法时，已经重新设置了picker的宽度和高度
		me.picker.setSize(me.minListWidth,me.minListHeight);
		me.picker.alignTo(me.triggerWrap, me.pickerAlign);
		//展开下拉列表前，根据trigger的值，对树进行初始化
		me.tree.initTree(me.decodeStringJson(me.getValue()),me.tree.getRootNode());
		//设置树初始选中的节点，为根节点
		me.tree.getSelectionModel().select(me.tree.getRootNode());
		//自动触发树的select事件
		me.tree.fireEvent('itemclick',me.tree,me.tree.getRootNode(),null,me.tree.getRootNode().index,null);
		//me.tree.fireEvent('select', me.tree.getSelectionModel(), me.tree.getRootNode(), me.tree.getRootNode().index);
	},
    /**
     * @private
     * 根据焦点的范围，判断下拉列表是否关闭
     */
    collapseIf: function(e) {
        var me = this;
        if (!me.isDestroyed && !e.within(me.bodyEl, false, true) && !e.within(me.triggerWrap, false, true) && me.picker.collapseIf(e)) {
        	me.collapse();
        }
    },
    /**
     * 自定义
     * 判断panel（即picker）焦点的范围
     */
    panelCollapseIf : function(e){
    	if(this.picker.collapseIf){
    		return this.picker.collapseIf(e);
    	}
    	return true;
    },
	/**
	 * 焦点移出picker范围的判断
	 */
    mimicBlur: function(e) {
        var me = this, picker = me.picker;
        if(!Ext.isDefined(me.picker) || (Ext.isDefined(me.picker) && !me.picker.isVisible())){
        	me.callParent(arguments);
        	return true;
        }else{
        	return false;
        }
    },
    getObjectFormatString : function(name, value, level){
    	var result = '';
    	var type = typeof value;
    	if(value instanceof Array){
    		type = 'array';
    	}
    	if(type == 'object' && value.hasOwnProperty('class') && value.hasOwnProperty('data')){
    		type = 'designobject';
    	}
    	var preTab = '';
    	for(var i=0;i<level;i++){
    		preTab = preTab + '\t';
    	}
    	result = preTab;
    	if(name){
    		result = result + name + ' : ';
    	}
    	switch(type){
    	case 'boolean':
    	case 'number':
    		result = result + value + ',\n';
    		break;
    	case 'string':
    		result = result + '"' + value + '",\n';
    		break;
    	case 'object':
    		result = result + '{\n';
    		var hasNode = false;
    		for(n in value){
        		if(value.hasOwnProperty(n)){
        			hasNode = true;
        			result = result + this.getObjectFormatString(n, value[n], level + 1);
        		}
    		}
    		if(level > 0 && hasNode){
    			result = result.substring(0,result.length -2) + '\n';
    		}
    		result = result + preTab + '},\n';
    		break;
    	case 'array':
    		result = result + '[\n';
    		var hasNode = false;
    		for(n in value){
        		if(value.hasOwnProperty(n)){
        			hasNode = true;
        			result = result + this.getObjectFormatString(false, value[n], level + 1);
        		}
    		}
    		if(level > 0 && hasNode){
    			result = result.substring(0,result.length -2) + '\n';
    		}
    		result = result + preTab + '],\n';
    		break;
    	case 'function':
    		result = result + '(' + value.toString().replace(new RegExp("\n","gm"), preTab + '\n') + '),\n';
    		break;
    	case 'designobject':
    		result = result + '{\n';
    		var hasNode = false;
    		for(n in value){
        		if(value.hasOwnProperty(n)){
        			hasNode = true;
        			result = result + this.getObjectFormatString(n, value[n], level + 1);
        		}
    		}
    		if(level > 0 && hasNode){
    			result = result.substring(0,result.length -2) + '\n';
    		}
    		result = result + preTab + '},\n';
    		break;
    		break;
    	case 'undefined':
    		result = result + value + ',\n';
    		break;
    	}
    	if(level == 0){
    		return result.substring(0,result.length -2);
    	}else{
    		return result;
    	}
    }
});