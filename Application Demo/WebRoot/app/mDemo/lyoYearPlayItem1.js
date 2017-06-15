Ext.define('Asc.app.defects.mDemo.lyoYearPlayItem1', {
	// 继承自公共页面控制器类
	extend : 'Asc.framework.page.controller.AbstractPageController',
	// 名称空间
	$namespace : 'Asc.app.defects.mDemo',
	// 初始化函数
	afterInit : function(){
		this.control({
			'[pageId=mDemo.lyoYearPlayItem1]' : {
				afterrender : {
					fn : this.onPageRender,
					scope : this
				}
			},
			'[itemId=lyoiTop]' : {
				dataupdated : {
					fn : this.onDataUpdated,
					scope : this
				}
			},
			'[itemId=lyoiCenter]' : {
				requestedit : {
					fn : this.onRequestEdit,
					scope : this
				}
			}
		});
	},
	// 获得主页面下的Left布局对象
	getTop : function(pageCmp){
		return this.getPageItem(pageCmp || this.page, 'lyoiTop');
	},
	// 获得主页面下的Center布局对象
	getCenter : function(pageCmp){
		return this.getPageItem(pageCmp || this.page, 'lyoiCenter');
	},
	// 页面被渲染
	onPageRender : function(pageCmp){
		this.page = pageCmp;
		// 获得视图表格对象
		var center = this.getCenter(pageCmp);
		
		center.addEvents('requestedit');
	},
	// 记录更新
	onDataUpdated : function(data) {
		// 获得视图表格对象
		var center = this.getCenter();
		// 刷新页面
		center.doRefresh();
	},
	// 修改记录
	onRequestEdit : function(data) {
		// 获得视图表格对象
		var top = this.getTop();
		// 刷新页面
		top.beginEdit(data);
	}
});