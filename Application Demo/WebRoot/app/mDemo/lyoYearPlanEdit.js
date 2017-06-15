Ext.define('Asc.app.defects.mDemo.lyoYearPlanEdit', {
	// 继承自公共页面控制器类
	extend : 'Asc.framework.page.controller.AbstractPageController',
	// 名称空间
	$namespace : 'Asc.app.defects.mDemo',
	// 初始化函数
	afterInit : function(){
		this.control({
			'[pageId=mDemo.lyoYearPlanEdit]' : {
				afterrender : {
					fn : this.onPageRender,
					scope : this
				}
			},
			'[itemId=lyoiTop]' : {
				querysubmit : {
					fn : this.onQuerySubmit,
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
		// 初始化屏蔽列表操作
		center.disable();
	},
	// 查询条件设置（提交）
	onQuerySubmit : function(topPanel) {
		// 获得查询条件数据
		var queryData = topPanel.getQueryFormData();
		// 获得视图表格对象
		var center = this.getCenter();
		// 初始化屏蔽列表操作
		center.enable();
		// 刷新页面
		center.doRefresh(queryData);
	}
});