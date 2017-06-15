package com.asc.manager.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.manager.navigator.ILoader;
import com.asc.manager.navigator.INode;
import com.asc.manager.navigator.node.CommonTextFolderNode;

public class ServicePlatformManagerFolderLoader implements ILoader {

	@Override
	public String getType() {
		return "ServicePlatformManagerFolder";
	}

	@Override
	public List<INode> getChildren(String key) {
		List<INode> nodes = new ArrayList<INode>();
		INode node = null;
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_ESB_FLOW_INSTANCE, key, "业务流实例", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_SP_CACHE_MANAGER, key, "缓存管理", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_SP_LOG_MANAGER, key, "日志管理", true);
		nodes.add(node);
		return nodes;
	}

}
