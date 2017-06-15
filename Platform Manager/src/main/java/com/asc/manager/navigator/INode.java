package com.asc.manager.navigator;

import com.google.gson.JsonObject;

public interface INode {
	// 节点类型
	String getType();
	// 设计对象标识
	String getKey();
	// 节点显示
	String getText();
	// 图标
	String getIconCls();
	// 叶子节点
	boolean isLeaf();
	// 节点对象
	String getTip();
	// 输出节点对象
	JsonObject toJson();
}
