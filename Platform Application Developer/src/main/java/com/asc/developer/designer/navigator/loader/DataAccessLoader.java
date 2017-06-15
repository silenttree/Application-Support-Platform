package com.asc.developer.designer.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.developer.designer.navigator.ILoader;
import com.asc.developer.designer.navigator.INode;
import com.asc.developer.designer.navigator.node.CommonTextFolderNode;

public class DataAccessLoader implements ILoader {

	public String getType() {
		return CommonTextFolderNode.TYPE_DATA_ACCESS;
	}

	public List<INode> getChildren(String appId, String key) {
		List<INode> nodes = new ArrayList<INode>();
		// 返回应用节点列表
		INode dataAccess = new CommonTextFolderNode(CommonTextFolderNode.TYPE_DATASOURCES, appId, key, "数据源", false);
		nodes.add(dataAccess);
		INode modules = new CommonTextFolderNode(CommonTextFolderNode.TYPE_TABLES, appId, key, "数据表", false);
		nodes.add(modules);
		INode dictionarys = new CommonTextFolderNode(CommonTextFolderNode.TYPE_DICTIONARY, appId, key, "静态字典", false);
		nodes.add(dictionarys);
		INode workflows = new CommonTextFolderNode(CommonTextFolderNode.TYPE_QUERYS, appId, key, "数据检索", false);
		nodes.add(workflows);
		return nodes;
	}

}
