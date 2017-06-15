package com.asc.manager.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.manager.navigator.ILoader;
import com.asc.manager.navigator.INode;
import com.asc.manager.navigator.node.CommonTextFolderNode;

public class ApplicationWorkflowFolderLoader implements ILoader {

	@Override
	public String getType() {
		return "ApplicationWorkflowFolder";
	}

	@Override
	public List<INode> getChildren(String key) {
		List<INode> nodes = new ArrayList<INode>();
		INode node = null;
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_WORKFLOW_CONFIG, key, "流程配置", false);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_WORKFLOW_ROLE, key, "权限配置", true);
		nodes.add(node);
		return nodes;
	}

}
