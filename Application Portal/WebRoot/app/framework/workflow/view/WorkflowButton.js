Ext.define('Asc.framework.workflow.view.WorkflowButton', {
	// 指定基类
	extend : 'Ext.Button',
	// 设置别名
	alias : 'widget.AscWorkflowButton',
	// 指定按钮类型
	type : 'flowbutton',
	// 构造函数
	constructor : function(button){
		var me = this;
		var cfg = {};
		if(typeof button == 'string'){
			me.action = button;
			// 流程按钮
			switch(button){
			case 'CompleteUserProcess':
				cfg.iconCls = 'icon-workflow-submit';
				cfg.text = '办理完毕';
				break;
			case 'ReadComplete':
				cfg.iconCls = 'icon-workflow-submit';
				cfg.text = '阅读完毕';
				break;
			case 'RequestProcess':
				cfg.iconCls = 'icon-workflow-request';
				cfg.text = '申请办理';
				break;
			case 'TakeBack':
				cfg.iconCls = 'icon-workflow-takeback';
				cfg.text = '拿回办理';
				break;
			case 'Return':
				cfg.iconCls = 'icon-workflow-return';
				cfg.text = '退回办理';
				break;
			case 'Cancel':
				cfg.iconCls = 'icon-workflow-cancel';
				cfg.text = '撤销办理';
				break;
			case 'Resume':
				cfg.iconCls = 'icon-workflow-resume';
				cfg.text = '恢复办理';
				break;
			case 'Comment':
				cfg.iconCls = 'icon-workflow-comment';
				cfg.text = '输入意见';
				break;
			case 'Forward':
				cfg.iconCls = 'icon-workflow-forward';
				cfg.text = '转移办理';
				break;
			default :
				cfg.iconCls = 'icon-workflow-' + button.toLowerCase();
				cfg.text = button;
				break;
			}
		}else{
			// 路由按钮 设置为办理完毕动作
			me.action = 'CompleteUserProcess';
			// 名称
			cfg.text = button.caption;
			// 路由
			me.routeKey = button.routeKey;
			if(button.iconCls){
				cfg.iconCls = icon;
			}else{
				// 处理图标
				switch (button.caption){
				case '提交':
					cfg.iconCls = 'icon-workflow-submit';
					break;
				case '退回':
					cfg.iconCls = 'icon-workflow-return';
					break;
				case '否决':
					cfg.iconCls = 'icon-workflow-reject';
					break;
				case '同意':
					cfg.iconCls = 'icon-workflow-agree';
					break;
				default :
					cfg.iconCls = 'icon-workflow-submit';
					break;
				}
			}
		}
        me.callParent([cfg]);
	}
});