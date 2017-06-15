package com.asc.manager.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.manager.navigator.ILoader;
import com.asc.manager.navigator.INode;
import com.asc.manager.navigator.node.CommonTextFolderNode;

public class ApplicationLogFolderLoader implements ILoader {

	@Override
	public String getType() {
		return "ApplicationLogFolder";
	}

	@Override
	public List<INode> getChildren(String key) {
		List<INode> nodes = new ArrayList<INode>();
		INode node = null;
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_SYS_LOG, key, "系统日志", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_USER_LOG, key, "用户活动日志", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_MANAGER_LOG, key, "应用管理平台日志", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_CAS_LOG, key, "认证服务日志", true);
		nodes.add(node);
		return nodes;
	}
}
