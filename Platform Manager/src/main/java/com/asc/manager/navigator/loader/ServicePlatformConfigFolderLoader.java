package com.asc.manager.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.manager.navigator.ILoader;
import com.asc.manager.navigator.INode;
import com.asc.manager.navigator.node.CommonTextFolderNode;

public class ServicePlatformConfigFolderLoader implements ILoader {

	@Override
	public String getType() {
		return "ServicePlatformConfigFolder";
	}

	@Override
	public List<INode> getChildren(String key) {
		List<INode> nodes = new ArrayList<INode>();
		INode node = null;
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_ESB_INTERFACE, key, "应用接口管理", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_ESB_MESSAGE, key, "总线消息配置", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_ESB_FLOW, key, "业务流配置", false);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_SP_SCHEDULE_TASK, key, "任务调度", true);
		nodes.add(node);
		return nodes;
	}

}
