Ext.define('Asc.extension.EventsEditor', {
	
	extend : 'Ext.form.field.Picker',
	
	alias : 'widget.AscEventsEditor',
	
	pickerAlign : 'tl-bl?',
	
	matchFieldWidth : true,
	
	pickerHeight : 260,
	
	pickerWidth : 600,
	
	shadow : 'side',
	
	editable : false,
	
	config : {
		//事件结构
		eventConf : {},
	    //空事件的图标类名
	  	emptyEventIconCls : 'icon-designer-emptyValue',
	  	//非空事件的图标类名
	  	notEmptyEventIconCls : 'icon-designer-noEmptyValue'
	},
	
	statics:{
		//获取事件的摘要，用来在表格中显示
		getEventsAbs : function(eventsObj){
			var rsStr = '';
			if(Ext.Object.isEmpty(eventsObj)){
				return rsStr;
			}
  			console.log(eventsObj);
  			for(var n in eventsObj){
  				if(n == 'extJs'){
					var extjsEventsObj = eventsObj[n];
					for(var extObjName in extjsEventsObj){
						var extObj = extjsEventsObj[extObjName];
						for(var extObjEvent in extObj){
							if(extObj[extObjEvent] != ''){
								rsStr = this.appendWithComma(rsStr, 'extJs.' + extObjName + '.' + extObjEvent);
							}
						}
					}
				}else if (n=='server'){
					var serverEventsObj = eventsObj[n];
					for(var objName in eventsObj){
						rsStr = this.appendWithComma(rsStr, 'server.' + objName);
					}
					//rsStr = this.appendWithComma(rsStr, 'server.' + n);
				}else if(eventsObj[n].hasOwnProperty('type') 
							&& eventsObj[n].type === 'server'
	  						&& eventsObj[n].script != ''){ 
					rsStr = this.appendWithComma(rsStr, 'server.' + n);
  				}else if(eventsObj[n].hasOwnProperty('type') 
  							&& eventsObj[n].type === 'client'
  							&& eventsObj[n].script != ''){
  					rsStr = this.appendWithComma(rsStr, 'client.' + n);
  				}
  			}
  			
  			return rsStr;
  		},
  		//把两个字符串使用逗号连接起来
  		appendWithComma: function(str,strToAppend){
  			if(!strToAppend || strToAppend === ''){
  				return str;
  			}
  			return (!str || str === '') ? strToAppend : (str + ',' + strToAppend);
  		}
	}, 
	
	/**
	 * 重写createPicker方法，返回一个panel
	 */
	createPicker : function() {
		var me = this;
		//事件名填写框
		var nameField = new Ext.form.TextField({
			width : 100,
			disabled : true,
			emptyText : '创建事件名称',
			enableKeyEvents : true,
			listeners : {
				specialkey : function(field,e){
					if(e.getKey() == e.ENTER){
						addAction.execute();
					}
				}
			}
		});
		//创建事件按钮
		var addAction = new Ext.Action({
			text : '创建',
			disabled : true,
			iconCls : 'icon-sys-add',
			handler : function(){
				var parentNode = tree.getSelectionModel().getSelection()[0];
				var nodeText = nameField.getValue();
				if(!parentNode.findChild('text',nodeText) && nodeText != ''){
					tree.appendAttributeNode(parentNode, nodeText, '', 'extJs');
				}
				parentNode.expand();
			}
		});
		
		//删除事件按钮
		var deleteAction = new Ext.Action({
			text : '删除',
			disabled : true,
			iconCls : 'icon-sys-delete',
			handler : function(){
				var node = tree.getSelectionModel().getSelection()[0];
				if(node && node.data.type === 'extJs'){					
					node.remove(true);
				}
			}
		});
	
		
		//工具栏
  		var tools = [nameField,
   					addAction,'-',
   					deleteAction
   					];
  		
  		//确定按钮
  		var okButton = new Ext.Button({
  			text : '确定',
  			iconCls : 'icon-sys-confirm',
  			handler : function(){
				me.setValue(getEventValue());
				me.collapse();
			} 
  		});
  		var getEventValue = function(){
  			var rsVal = {};
			var rootNode = tree.getRootNode();
			//添加服务器对象事件
			var serverNode = rootNode.findChild('text','server');
			if(serverNode){
				var events={};
				serverNode.eachChild(function(childNode){
//					var event = {};
//					event.type = 'server';
//					event.script = childNode.data.value;
					if(!Ext.isEmpty(childNode.data.value)){
						events[childNode.data.text] = childNode.data.value;
					}
				});
				rsVal['server']=events;
//				alert(rsVal.server.Started);
			}
			//添加客户端对象事件
			var clientNode = rootNode.findChild('text','client');
			if(clientNode){
				clientNode.eachChild(function(childNode){
					var event = {};
					event.type = 'client';
					event.script = childNode.data.value;
					if(!Ext.isEmpty(event.script)){
						rsVal[childNode.data.text] = event;
					}
				});
			}
			//添加extJs对象事件
			var extJsNode = rootNode.findChild('text','extJs');
			var extJsObjs = {};
			if(extJsNode){
				extJsNode.eachChild(function(childNode){
					var extJsObj = {};
					if(childNode){
						childNode.eachChild(function(childNode1){
							extJsObj[childNode1.data.text] = childNode1.data.value;
						}); 
					}
					if(!Ext.Object.isEmpty(extJsObj)){			
						extJsObjs[childNode.data.text] = extJsObj;
					}
				});
			}
			if(!Ext.Object.isEmpty(extJsObjs)){
				rsVal['extJs'] = extJsObjs;
			}
			return rsVal;
  		};
  		//取消按钮
  		var cencelButton = new Ext.Button({
  			text : '取消',
  			iconCls : 'icon-sys-cancel',
  			handler : function(){
  				me.collapse();
			}
  		});
  		
  		//button区
  		var buttons = ['->',
		               cencelButton,'-',
  		               okButton];
  		
  		//事件树的data model
	 	Ext.define('treeJsonModel',{
	 		extend : 'Ext.data.Model',
	 		fields: [
	 		         {name: 'value'},
	 		         {name: 'text',  type: 'string'},
	 		         {name: 'type', type : 'string'},
	 		         {name: 'funTitle', type : 'string'}
	 		         ]
	 	});
	 	
	 	//事件树的tree store
	 	var treeStore = Ext.create('Ext.data.TreeStore', {
	 		model:'treeJsonModel',
	 		root: {
	 			expanded: false,
	 			text : 'object',
	 			type : 'object',
	 			autoLoad : false,
	 			value : {},
			 	children:[]
	 		},
	 		
	 		folderSort: true
	 	});
  		
	 	//事件树
  		var tree = Ext.create('Ext.tree.Panel',{ 
 			autoScroll : true,
 			minSize : 50,
 			width: 240,
	        maxSize: 350,
			store: treeStore,
			border : false,
			region : 'west',
			rootVisible: false,
			split:true,
			weight : 2,
			
			/**
			 * 增加新节点
			 * @param parentNode 父节点
			 * @param text 节点名称
			 * @param ft 服务器事件或者客户端事件的方法头
			 * @param type 事件类型，server,client,extJs
			 * @returns
			 */
			appendAttributeNode : function(parentNode,text,ft,type){
  				var editable = true;
  				var node = parentNode.appendChild({
  					text : text,
  					funTitle : ft,
  					type : type,
  					loaded : true,
  					editable : editable,
  					leaf : !!type
  				});
  				tree.setNodeValue(node,'',true);
  				return node;
  			},
  			
  			/**
  			 * 设置某个树节点的值，当值由空变为非空，或者由非空变为空时，改变节点的图标
  			 * @param node 要设置值的节点
  			 * @param value 要设置的值
  			 * @param forceRefreshIcon 是否强行刷新图标
  			 */
  			setNodeValue: function(node,value,forceRefreshIcon){
  				var shouldChangeIcon = (!Ext.isEmpty(node.data.type)) 
  					&& ((Ext.isEmpty(node.data.value) ^ Ext.isEmpty(value)) || forceRefreshIcon);
  				node.data.value = value;
  				if(shouldChangeIcon){
  					if(Ext.isEmpty(value)){
  						node.set('iconCls',me.getEmptyEventIconCls());
  					}else{
  						node.set('iconCls',me.getNotEmptyEventIconCls());
  					}
  				}
  			}, 
  			
  			/**
  			 * 根据路径查找树节点
  			 * @param  可变长的字符串参数，没有返回根节点，否则依次为路径上各个节点的名称，如果路径不存在返回null
  			 */
  			getNodeByPath: function(){
  				var tree = this;
  				var node = tree.getRootNode();
  				for(var i = 0; i < arguments.length; i++){
  					var childNode = node.findChild('text', arguments[i]);
  					if(!childNode){
  						return null;
  					}else{
  						node = childNode;
  					}
  				}
  				return node;
  			},
  			
			
  			/**
  			 * 初始化事件树
  			 * @param obj 初始化用到的object，属性对应树的节点
  			 * @param node 要初始化到那个树节点下
  			 */
	        initTree : function(obj, node){
	        	//删除所有节点
	        	node.removeAll();
	        	var serverObjs = obj['server'];
	        	if(serverObjs){
	        		var serverNode = me.tree.appendAttributeNode(node,'server');
	        		for(var p in serverObjs){
	        			me.tree.appendAttributeNode(serverNode, p, serverObjs[p], 'server');
	        		}
	        	}
	        	
	        	var clientObjs = obj['client'];
	        	if(clientObjs){	        		
	        		var clientNode = me.tree.appendAttributeNode(node,'client');
	        		for(var p in clientObjs){
	        			me.tree.appendAttributeNode(clientNode, p, clientObjs[p], 'client');
	        		}
	        	}
	        	
	        	var extJsObjs = obj['extJs'];
	        	if(extJsObjs){
	        		var extJsNode = me.tree.appendAttributeNode(node,'extJs');
	        		for(var i = 0; i < extJsObjs.length ; i++){
	        			me.tree.appendAttributeNode(extJsNode, extJsObjs[i]);
	        		}
	        	}
	        },
  			
  			
  			/**
  			 * 设置整棵事件树上所有节点的值
  			 * @param treeValue 事件值得对象
  			 */
  			setTreeValue : function(treeValue){
  				var tree = this;
  				for(var p in treeValue){
  					if(p == 'extJs'){
  						var extJsObjs = treeValue[p];
  						for(var p2 in extJsObjs){
  							var node = tree.getNodeByPath('extJs',p2);
  							if(node){
  								var extJsObj = extJsObjs[p2];
  								for(var p3 in extJsObj){
  									var eventNode = tree.appendAttributeNode(node,p3,'','extJs');
  									if(eventNode){  										
  										tree.setNodeValue(eventNode,extJsObj[p3],true);
  									}
  								}
  							}
  						}
  					}else if(p=='server'){
  						var serverEvents = treeValue[p];
  						for(var en in serverEvents){
  							var node = tree.getNodeByPath('server',en);
  							if(node){
								console.debug(en + ":" + serverEvents[en]);
								var eventNode = tree.getNodeByPath('server',en);	
								if(eventNode){
									tree.setNodeValue(eventNode,serverEvents[en],true);
								}
  							}
  						}
  					}else if(treeValue[p].type){
  						var eventNode = tree.getNodeByPath(treeValue[p].type,p);
  						if(eventNode){  							
  							tree.setNodeValue(eventNode,treeValue[p].script,true);
  						}
  					}
  				}
  			}
  		});
  		
  		//树节点选中事件，根据选中的节点不同设置不同的操作元素的状态
  		tree.on('select',function(rowModel, record, index){
  			textField.suspendEvent('change');
  			var data = record.data;
  			me.disableAll();
  			if(record.parentNode.data.text === 'extJs'){
  				nameField.enable();
				addAction.enable();
  			}
  			
  			if(data.type === 'extJs'){
  				deleteAction.enable();
  			}
  			
  			if(data.leaf){
  				textField.setValue(data.value);
  				textField.enable();
  				if(data.type === 'server' || data.type === 'client'){
  	  				funTitle.setText(data.funTitle + '{');
  	  				funTitle.setVisible(true);
  	  				funTail.setText('}');
  	  				funTail.setVisible(true);
  				}
  			}
  			textField.resumeEvent('change');
  		});
  		
  		//server和client事件显示方法头
  		var funTitle = new Ext.form.Label({
  			minHeight : 20,
  			region : 'north',
  			weight : 1,
  			text : ''
  		});
  		
  		//显示和编辑事件内容的文本框
  		var textField = new Ext.form.field.TextArea({
  			region : 'center',
  			hideLabel : true,
  			disabled : false,
  			enableKeyEvents: true,
  			weight : 1,
  			listeners :{
  				change : function(tf, newValue, oldValue, eOpts){
  					var node = tree.getSelectionModel().getSelection()[0];
  					tree.setNodeValue(node,newValue);
  					me.setValue(getEventValue());
  				},
  				keydown : function (f, e) {
  					if (e.getKey() == e.TAB) {
	                    var el = f.inputEl.dom;
	                    if (el.setSelectionRange) {
	                        var withIns = el.value.substring(0, el.selectionStart) + '\t';
	                        var pos = withIns.length;
	                        el.value = withIns + el.value.substring(el.selectionEnd, el.value.length);
	                        el.setSelectionRange(pos, pos);
	                    }
	                    else if (document.selection) {
	                        document.selection.createRange().text = '\t';
	                    }
	                    e.stopEvent();
	     			}
	           }
  			}
  		});
  		//server和client事件显示方法尾
  		var funTail = new Ext.draw.Text({
  			overflowX : 'auto',
  			minHeight : 20,
  			weight : 1,
  			region : 'south',
  			text : ''
  		});
  		
		var panel = Ext.create('Ext.panel.Panel',{
			//显示到layer层里面（如果显示到picker的一些属性里面（bodyEl），会出现覆盖panel的部分范围，导致panel的部分范围看不到）
			renderTo : Ext.getBody(),
			width : me.pickerWidth,
			height : me.pickerHeight,
			layout : 'border',
			border : true,//有边框
			bodyBorder : false,
			floating : true,
			shadow : false,
			tbar : tools,
			bbar : buttons,
			items : [tree,funTitle,textField,funTail],
			//判断焦点的范围
		    collapseIf: function(e){
	    		if(tree.el && e.within(tree.el)){
	    			return false;
	    		}
		    	if(panel.el && e.within(panel.el)){
		    		return false;
		    	}
		    	return true;
    		}
		});
		//将所有的操作元素该隐藏的隐藏，该不可用的不可用，该清空的清空
        me.disableAll = function(){
    		funTitle.setText('');
    		funTitle.setVisible(false);
    		funTail.setText('');
    		funTail.setVisible(false);
    		textField.setValue('');
    		textField.disable();
    		nameField.disable();
    		addAction.disable();
    		deleteAction.disable();
        }; 
		me.tree = tree;
		return me.picker = panel;
	},
	/**
	 * @Overload
	 * 重写onExpand，在展开下拉列表时，需要做的事情，在这个方法里处理。
	 */
	onExpand : function() { 
		var me = this;
		me.tree.initTree(me.getEventConf(),me.tree.getRootNode());
		me.tree.setTreeValue(me.getValue());
		me.picker.setSize(me.pickerWidth,me.pickerHeight);
		me.picker.alignTo(me.triggerWrap, me.pickerAlign);
		me.disableAll();
		me.tree.collapseAll();
	},
	
	/**
	 * 根据事件的显示值，覆盖父类的方法
	 * @returns
	 */
	getRawValue: function(){
		return Asc.extension.EventsEditor.getEventsAbs(this.value);
	}, 
	
	/**
	 * 设置事件值，覆盖父类的方法
	 * @param v
	 */
	setValue : function(v){
		this.setRawValue(Asc.extension.EventsEditor.getEventsAbs(v));
		this.value = v;
	},
	
    /**
     * @private
     * 根据点击的位置判断决定是否要将picker收起来
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
     * 获取picker的值，覆盖父类的方法
     * @returns
     */
	getValue : function(){
		return this.value;
	}
    
    
});