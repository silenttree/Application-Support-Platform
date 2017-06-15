package com.asc.manager.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.manager.navigator.ILoader;
import com.asc.manager.navigator.INode;
import com.asc.manager.navigator.node.CommonTextFolderNode;

public class ApplicationModuleFolder implements ILoader {

	@Override
	public String getType() {
		return "ApplicationModuleFolder";
	}

	@Override
	public List<INode> getChildren(String key) {
		List<INode> nodes = new ArrayList<INode>();
		INode node = null;
//		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_MODULE_MENU, key, "模块菜单", true);
//		nodes.add(node);
		/*node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_MODULE_ROLE_MAPPING, key, "模块角色映射", true);
		nodes.add(node);*/
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_MODULE_ROLE_AUTHORITY, key, "模块角色映射", true);
		nodes.add(node);
//		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_MODULE_DF_SETTINGS, key, "数据权限配置", true);
//		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_WORKFLOW_ROLE, key, "流程角色映射", true);
		nodes.add(node);
		return nodes;
	}

}
