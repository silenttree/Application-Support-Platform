package com.asc.manager.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.manager.navigator.ILoader;
import com.asc.manager.navigator.INode;
import com.asc.manager.navigator.node.CommonTextFolderNode;

public class OrganizationFolderLoader implements ILoader {

	@Override
	public String getType() {
		return "OrganizationFolder";
	}

	@Override
	public List<INode> getChildren(String key) {
		List<INode> nodes = new ArrayList<INode>();
		INode node = null;
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_ORGANIZATION_MANAGER, key, "机构管理", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_ROLE_MANAGER, key, "角色管理", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_USER_MANAGER, key, "用户管理", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_ORGINAZATION_USER_MANAGER, key, "机构用户", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_ROLE_USER_MANAGER, key, "角色用户", true);
		nodes.add(node);
		return nodes;
	}

}
