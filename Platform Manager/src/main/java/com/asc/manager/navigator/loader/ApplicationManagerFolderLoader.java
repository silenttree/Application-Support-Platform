package com.asc.manager.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.manager.navigator.ILoader;
import com.asc.manager.navigator.INode;
import com.asc.manager.navigator.node.CommonTextFolderNode;

public class ApplicationManagerFolderLoader implements ILoader {

	@Override
	public String getType() {
		return "ApplicationManagerFolder";
	}

	@Override
	public List<INode> getChildren(String key) {
		List<INode> nodes = new ArrayList<INode>();
		INode node = null;
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_STATE_MANAGER, key, "运行状态", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_CACHE_MANAGER, key, "缓存管理", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_FLOW_INSTANCE, key, "流程实例", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_LOG_FOLDER, key, "日志管理", false);
		nodes.add(node);
		return nodes;
	}

}
