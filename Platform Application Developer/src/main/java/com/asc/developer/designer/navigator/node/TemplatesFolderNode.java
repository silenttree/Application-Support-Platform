package com.asc.developer.designer.navigator.node;


public class TemplatesFolderNode extends BasicNode {
	public static final String TYPE = "templates";

	public String getAppId() {
		return "";
	}

	public String getType() {
		return TYPE;
	}

	public String getText() {
		return "设计模板";
	}

	public String getKey() {
		return "";
	}

	public boolean isLeaf() {
		return true;
	}

}
