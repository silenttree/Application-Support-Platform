package com.asc.developer.designer.navigator.node;

import com.asc.developer.config.ApplicationConnection;

public class AppConnectionNode extends BasicNode {
	public static final String TYPE = "appconn";
	
	ApplicationConnection conn;
	
	public AppConnectionNode(ApplicationConnection conn){
		this.conn = conn;
	}

	public String getAppId() {
		return conn.getAppId();
	}

	public String getType() {
		return TYPE;
	}
	
	public String getText() {
		return conn.getAppCaption();
	}

	public String getKey() {
		return "";
	}

	public boolean isLeaf() {
		return false;
	}

}
