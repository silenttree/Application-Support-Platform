package com.asc.developer.designer.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.developer.designer.navigator.ILoader;
import com.asc.developer.designer.navigator.INode;
import com.asc.developer.designer.navigator.node.CommonTextFolderNode;

public class ModulePageLoader implements ILoader {

	public String getType() {
		return CommonTextFolderNode.TYPE_MODULE_PAGES;
	}

	public List<INode> getChildren(String appId, String key) {
		List<INode> nodes = new ArrayList<INode>();
		// 返回应用节点列表
		INode views = new CommonTextFolderNode(CommonTextFolderNode.TYPE_PAGE_VIEWS, appId, key, "视图页面", false);
		nodes.add(views);
		INode trees = new CommonTextFolderNode(CommonTextFolderNode.TYPE_PAGE_TREES, appId, key, "Tree页面", false);
		nodes.add(trees);
		INode forms = new CommonTextFolderNode(CommonTextFolderNode.TYPE_PAGE_FORMS, appId, key, "表单页面", false);
		nodes.add(forms);
		INode queryforms = new CommonTextFolderNode(CommonTextFolderNode.TYPE_PAGE_QUERYFORMS, appId, key, "查询表单", false);
		nodes.add(queryforms);
		INode layouts = new CommonTextFolderNode(CommonTextFolderNode.TYPE_PAGE_LAYOUTS, appId, key, "布局页面", false);
		nodes.add(layouts);
		INode https = new CommonTextFolderNode(CommonTextFolderNode.TYPE_PAGE_URLS, appId, key, "URL页面", false);
		nodes.add(https);
		
		return nodes;
	}

}
