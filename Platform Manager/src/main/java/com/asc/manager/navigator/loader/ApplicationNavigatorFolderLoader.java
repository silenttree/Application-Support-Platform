package com.asc.manager.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.manager.navigator.ILoader;
import com.asc.manager.navigator.INode;
import com.asc.manager.navigator.node.CommonTextFolderNode;

public class ApplicationNavigatorFolderLoader implements ILoader {

	@Override
	public String getType() {
		return "ApplicationEntranceFolder";
	}

	@Override
	public List<INode> getChildren(String key) {
		List<INode> nodes = new ArrayList<INode>();
		INode node = null;
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_NAVIGATOR_MANAGER, key, "导航菜单", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_NAVIGATOR_AUTHORITY, key, "菜单权限", true);
		nodes.add(node);
		return nodes;
	}

}
