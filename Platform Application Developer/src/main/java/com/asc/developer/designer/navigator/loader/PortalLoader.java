package com.asc.developer.designer.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.developer.designer.navigator.ILoader;
import com.asc.developer.designer.navigator.INode;
import com.asc.developer.designer.navigator.node.CommonTextFolderNode;

public class PortalLoader implements ILoader {

	public String getType() {
		return CommonTextFolderNode.TYPE_PORTALS;
	}

	public List<INode> getChildren(String appId, String key) {
		List<INode> nodes = new ArrayList<INode>();
		// 返回应用节点列表
		INode portlets = new CommonTextFolderNode(CommonTextFolderNode.TYPE_PORTAL_PORTLETS, appId, key, "桌面栏目", false);
		nodes.add(portlets);
		INode documenttypes = new CommonTextFolderNode(CommonTextFolderNode.TYPE_PORTAL_DOCUMENTTYPES, appId, key, "文档类型", false);
		nodes.add(documenttypes);
		INode shortcuts = new CommonTextFolderNode(CommonTextFolderNode.TYPE_PORTAL_SHORTCUTS, appId, key, "快捷方式", false);
		nodes.add(shortcuts);
		return nodes;
	}

}
