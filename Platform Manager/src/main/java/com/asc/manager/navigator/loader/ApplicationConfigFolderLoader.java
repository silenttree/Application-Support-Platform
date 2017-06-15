package com.asc.manager.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.manager.navigator.ILoader;
import com.asc.manager.navigator.INode;
import com.asc.manager.navigator.node.CommonTextFolderNode;

public class ApplicationConfigFolderLoader implements ILoader {

	@Override
	public String getType() {
		return "ApplicationConfigFolder";
	}

	@Override
	public List<INode> getChildren(String key) {
		List<INode> nodes = new ArrayList<INode>();
		INode node = null;
//		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_NAVIGATOR, key, "导航菜单", false);
//		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_NAVIGATOR_MANAGER, key, "导航菜单", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_NAVIGATOR_AUTHORITY, key, "菜单权限", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_PORTAL_PROFILE, key, "桌面模板", true);
		nodes.add(node);
//		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_MODULE_FOLDER, key, "应用模块", false);
//		nodes.add(node);
//		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_WORKFLOW_FOLDER, key, "应用流程", false);
//		nodes.add(node);
//		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_SCHEDULE_TASK, key, "任务调度", true);
//		nodes.add(node);
//		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_DICTIONARY_FOLDER, key, "字典重载", false);
//		nodes.add(node);
//		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_LOGCONF_FOLDER, key, "日志配置", true);
//		nodes.add(node);
		return nodes;
	}

}
