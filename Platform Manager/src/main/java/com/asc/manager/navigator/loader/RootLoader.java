package com.asc.manager.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.manager.navigator.ILoader;
import com.asc.manager.navigator.INode;
import com.asc.manager.navigator.node.CommonTextFolderNode;


public class RootLoader implements ILoader{

	public String getType() {
		return "root";
	}
	
	public List<INode> getChildren(String key) {
		List<INode> nodes = new ArrayList<INode>();
		INode node = null;
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_REGISTER, key, "连接管理", false);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_ORGANIZATION_FOLDER, key, "组织机构", false);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_DICTIONARY_FOLDER, key, "数据字典", false);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_CONFIG, key, "门户管理", false);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_MODULE_FOLDER, key, "应用权限", false);
		nodes.add(node);
		/*node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_SERVICE_PLATFORM_CONFIG, key, "服务平台配置", false);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_MANAGER, key, "应用运行管理", false);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_SERVICE_PLATFORM_MANAGER, key, "服务平台管理", false);
		nodes.add(node);*/
		return nodes;
	}

}
