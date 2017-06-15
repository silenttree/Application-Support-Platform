package com.asc.developer.designer.navigator;

import java.util.List;

public interface ILoader {
	/**
	 * 获得装载器对应节点类型标识 
	 * @return
	 */
	String getType();
	/**
	 * 返回下级节点列表
	 * @param key 父节点键值
	 * @return
	 */
	List<INode> getChildren(String appId, String key);
}
