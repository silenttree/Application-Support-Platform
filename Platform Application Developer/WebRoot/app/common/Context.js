Ext.define('Asc.common.Context', {
	// 当前激活对象
	activeObject : undefined,
	// 当前激活容器
	activeContainer : undefined,
	// 剪贴板
	clipboard : undefined,
	// 激活对象
	activateObject : function(obj){
		Asc.common.Message.log('object [' +  unescape(Ext.encode(obj).replace(/\\u/gi, '%u')) + '" is activated', this, 'log');
		this.activeObject = obj;
	},
	// 获得激活对象
	getActivateObject : function(){
		return this.activeObject;
	},
	// 激活容器
	activateContainer : function(container){
		this.activeContainer = container;
		if(container && container.getActivateObject){
			// 设置激活对象
			this.activateObject(container.getActivateObject());
		}
		if(container){
			Asc.common.Message.log('container [' + container.getId() + '" is activated', container, 'log');
		}else{
			Asc.common.Message.log('container is cleared', this, 'log');
		}
	},
	// 获得激活容器
	getActivateContainer : function(){
		return this.container;
	},
	// 清空剪切板
	clearClipboard : function(){
		this.clipboard = undefined;
	},
	// 设置剪切板（支持多个对象）
	setClipboard : function(type, keys){
		if(Ext.isDefined(type) && 
				Ext.isDefined(keys) && 
				keys instanceof Array && 
				keys.length > 0){
			var keypair = {};
			for(var i=0;i<keys.length;i++){
				keypair[keys[i]] = keys[i]
			}
			this.clipboard = {
				type : type,
				count : keys.length,
				keys : keypair
			}
		}else{
			this.clipboard = undefined;
		}
	},
	// 获得剪切板
	getClipboard : function(){
		return this.clipboard;
	},
	// 剪切版为空
	isClipboardEmpty : function(){
		return !Ext.isDefined(this.clipboard);
	},
	// 判断对象是否在剪切板中
	isObjectInClipboard : function(type, key){
		if(Ext.isDefined(this.clipboard)){
			return this.clipboard.type = type && this.clipboard[key] == key;
		}
		return false;
	}
});