Ext.define('Asc.framework.application.controller.AppManager', {
	// 指定基类
	extend : 'Ext.app.Controller',
	// 设置别名
	alias : 'widget.AscAppManager',
	// 设置引用类
	requires : [],
	// 名称空间
	$namespace : 'Asc.framework.application',
	// 数据模型
	models : ['Application'],
	// 数据存储
	stores : ['Applications'],
	
	initedAppcnt: 0,
	
	// 初始化控制器
	init : function() {
		Asc.common.Message.log('AppManager is loaded & initialise ', this, 'log');
		// 应用数据被装载（自动装载）
		this.getStore('Applications').on('load', function(store, records, successful, eOpts){
			this.initedAppcnt = 0;
			Asc.common.Message.log('Portal applications is loaded, successful : [' + successful + '];record count : ' + records.length, this, 'log');
			var appGroupCooky = Ext.util.Cookies.get('appGroup') || 'default';
			for(var i = (records.length - 1); i >= 0; i--){
				//this.initApplication(records[i]);
				// 不属于本组的应用被忽略掉
				var group = ';' + records[i].get('f_group') + ';';
				if(group.indexOf(';' + appGroupCooky + ';') < 0) {
					store.removeAt(i);
					Asc.common.Message.log('应用[' + records[i].get('f_caption') + ']被忽略', 'log');
				} else {					
					this.initApplication(records[i]);
				}
			}
			if(records.length == 0){
				Asc.App.loadPortalData();
			}
		}, this);
	},
	// 初始化应用
	initApplication : function(model){
		var me = this;
		// 设置应用JS类名称空间装载路径
		Ext.Loader.setPath("Asc.app." + model.get('f_key').toLowerCase(), model.get('f_url') + '/app');
		// 初始化应用连接
		Ext.Ajax.request({
			url : model.get('f_url') + '/appinfo?' + Ext.data.IdGenerator.get('uuid').generate(),
			success: function(response, opts){
				Asc.common.Message.log('访问应用[' + model.get('f_key') + ']初始化信息成功！', this);
				// 装载应用Direct接口定义
				Ext.Loader.loadScript({
					url : model.get('f_url') + '/runtime/api.js',
					onLoad : function(){
						// 定义Direct函数源
						try{
							Asc.app[model.get('f_key').toLowerCase()].PROVIDER_BASE_URL = Asc.app[model.get('f_key').toLowerCase()].PROVIDER_BASE_URL = model.get('f_url') + '/direct';
							Ext.direct.Manager.addProvider(Ext.apply(Asc.app[model.get('f_key').toLowerCase()].REMOTING_API, {
								url : Asc.app[model.get('f_key').toLowerCase()].PROVIDER_BASE_URL,
								timeout: 1800000,
								enableBuffer : false,
								maxRetries : 0
							}));
						}catch(err){
							Asc.common.Message.error('装载应用[' + model.get('f_key') + ']的Direct定义文件 :[' + model.get('f_url') + '/runtime/api.js]失败', this);
						}
						// 设置状态
						model.set('f_state', '正常');
						model.set('f_is_init', true);
						me.initedAppcnt += 1;
						me.loadPortalDataIfCan();
					},
					onError : function(){
						Asc.common.Message.error('装载应用[' + model.get('f_key') + ']的Direct定义文件 :[' + model.get('f_url') + '/runtime/api.js]失败', this);
						me.initedAppcnt += 1;
						me.loadPortalDataIfCan();
					},
					scope : this
				});
			},
			failure : function(response, opts){
				Asc.common.Message.error('访问应用[' + model.get('f_key') + ']初始化信息失败！', this);
				me.initedAppcnt += 1;
				me.loadPortalDataIfCan();
			}
		});
		// 输出日志
		Asc.common.Message.log('Application 【' + model.get('f_key') + ' : ' + model.get('f_url') + '】 is initialized', this, 'log');
	},
	
	// 装载用户配置
	loadPortalDataIfCan : function() {
		if(this.getStore('Applications').getCount() === this.initedAppcnt) {
			Asc.common.Message.log('应用装载完成，开始装载用户配置');
			Asc.App.loadPortalData();
		}
	},
	
	// 根据应用标识获得应用数据对象
	getAppModel : function(key){
		return this.getStore('Applications').findRecord('f_key', key);
	},
	// 获得引擎Direct方法
	getEngineDirectFn : function(appKey, methodName){
		return eval(appKey + 'AppEngineDirect.' + methodName);
	},
	// 获得应用路径
	getAppUrl : function(appKey){
		var model = this.getAppModel(appKey);
		if(model && model != null){
			return model.get('f_url');
		}
	}
});